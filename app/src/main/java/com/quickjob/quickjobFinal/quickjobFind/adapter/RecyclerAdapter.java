package com.quickjob.quickjobFinal.quickjobFind.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobFind.activity.JobDetailActivity;
import com.quickjob.quickjobFinal.quickjobFind.activity.MainActivity;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.config.AppController;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    JSONArray arritem = new JSONArray();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    Activity mContext;
    private  List<CongViec> myArray;
    String uid;
    int type;
    int mtype;
    int count=0;
    boolean wantting=false;
    String[] arr,arrgender,arrcarrer;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tencongty,tencongviec,diadiem,mucluong,ngayup,trangthai,views,txtrequest,txtrequestfist,txtrequestsecond,txtnamecarrer;
        LinearLayout linnext,linreq;RelativeLayout linit;
        ImageView saveimg,imgnext,imglogo;
        public MyViewHolder(View view) {
            super(view);
            tencongty = (TextView) view.findViewById(R.id.tenct);
            trangthai = (TextView) view.findViewById(R.id.trangthai);
            tencongviec = (TextView) view.findViewById(R.id.tencongviec);
            diadiem = (TextView) view.findViewById(R.id.diachi);
            mucluong=(TextView) view.findViewById(R.id.luong);
            ngayup=(TextView) view.findViewById(R.id.dateup1);
//            views = (TextView) view.findViewById(R.id.views);
//            lin = (LinearLayout) view.findViewById(R.id.viewCount);

            linnext= (LinearLayout) view.findViewById(R.id.linnext);
            txtrequest =(TextView) view.findViewById(R.id.txtrequest);
            saveimg  =(ImageView) view.findViewById(R.id.imgsave);
            imglogo =(ImageView) view.findViewById(R.id.thumbnail);
            txtnamecarrer = (TextView) view.findViewById(R.id.txtnamecarrer);
        }
    }

    public RecyclerAdapter(Activity context, List<CongViec> objects, int type, String uid, int mtype){
        this.mContext=context;
        this.myArray=objects;
        this.uid=uid;
        this.type=type;
        this.mtype=mtype;
        this.arr=mContext.getResources().getStringArray(R.array.mucluong);
        this.arrcarrer=mContext.getResources().getStringArray(R.array.nganhNghe);
        this.arrgender=mContext.getResources().getStringArray(R.array.sex);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customitem, parent, false);
        return new RecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CongViec job = myArray.get(position);
        holder.tencongty.setText(job.getMotacv());
      //  int status = Integer.parseInt(job.getTrangthai());
        if(Integer.parseInt(job.getStatussave())==1)
        {holder.saveimg.setImageResource(R.drawable.ic_star_rate_yellow_18dp);}else{
            holder.saveimg.setImageResource(R.drawable.ic_star_rate_black_18dp);
        }
//        if(status==0)
//        {
//            holder.trangthai.setText(R.string.st_stt_dangcho);
//        }else if(status==1){
//            holder.trangthai.setText(R.string.st_stt_dachapnhan);
//        }else if(status==2){
//            holder.trangthai.setText(R.string.st_stt_datuchoi);
//        }else if(status==4){
            holder.trangthai.setText(myArray.get(position).getDistance());
       //  }
//        if(type==0){
//            if(position<3){
//              //  holder.lin.setVisibility(View.VISIBLE);
//            }else{
//              //  holder.lin.setVisibility(View.GONE);
//            }
//            holder.trangthai.setTextSize(10);
//            holder.trangthai.setText(R.string.st_hintHanNop);
////            holder.views.setText(" "+ job.getViews()+"");
//            holder.ngayup.setText(job.getDateup());
//        }else  if(type==99){
//            String time= job.getDatecreate();
//            Long gettime=getDateInMillis(time);
//            String timeago=getTimeAgo(gettime);
//            holder.ngayup.setText(timeago);
//        }else{
//
//        }
        holder.ngayup.setText(job.getDateup());
        holder.tencongviec.setText(job.getTencongviec());
        holder.txtnamecarrer.setText(arrcarrer[Integer.parseInt(job.getNganhNghe())]);
        String[] d =job.getDiadiem().split(",");
        int maxandress=d.length;
        if(maxandress>=3) {
            holder.diadiem.setText("\u2022 "+d[maxandress - 2]);
        }else{
            holder.diadiem.setText(job.getDiadiem());

        }
        holder.mucluong.setText(arr[Integer.parseInt(job.getLuong())]);
        Glide.with(mContext).load(job.getUrl())
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy( DiskCacheStrategy.NONE )
             //  .skipMemoryCache( true )
                .into(holder.imglogo);
//        if(job.getKn()==null || job.getKn().equals("") && job.getNgoaingu()==null || job.getNgoaingu().equals("") && job.getGioitinh()==null || job.getGioitinh().equals(""))
//        {}else {
//            holder.txtrequest.setText("\u2022   "+arrgender[Integer.parseInt(job.getGioitinh())]+" ,"+job.getDotuoi()+" age");
//            holder.txtrequestfist.setText("\u2022   "+job.getKn()+" ,"+job.getNgoaingu());
//            holder.txtrequestsecond.setText("\u2022   "+job.getKhac());
//            holder.linreq.setVisibility(View.VISIBLE);
//        }
        holder.saveimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!wantting) {
                    wantting=true;
                    if (Integer.parseInt(job.getStatussave()) == 0) {
                        SaveJob(MainActivity.uid,job.getMacv() );

                        holder.saveimg.setImageResource(R.drawable.ic_star_rate_yellow_18dp);
                        job.setStatussave("1");
                    } else {
                        try {
                            arritem.put(1, job.getMacv());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        DeleteSaveJob(MainActivity.uid, arritem.toString());

                        holder.saveimg.setImageResource(R.drawable.ic_star_rate_black_18dp);
                        job.setStatussave("0");

                    }
                }
            }
        });
        holder.imglogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextinfomation(job);
            }
        });
        holder.linnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextinfomation(job);
            }
        });
    }
    private void nextinfomation(CongViec job){
        Intent s = new Intent(mContext, JobDetailActivity.class);
        s.putExtra("tencongty", job.getTecongty());
        s.putExtra("tencongviec", job.getTencongviec());
        s.putExtra("diadiem", job.getDiadiem());
        s.putExtra("mucluong", job.getLuong());
        s.putExtra("ngayup", job.getDateup());
        s.putExtra("yeucaubangcap", job.getBangcap());
        s.putExtra("dotuoi", job.getDotuoi());
        s.putExtra("ngoaingu", job.getNgoaingu());
        s.putExtra("gioitinh", job.getGioitinh());
        s.putExtra("khac", job.getKhac());
        s.putExtra("motacv", job.getMotacv());
        s.putExtra("kn", job.getKn());
        s.putExtra("macv", job.getMacv());
        s.putExtra("img", job.getUrl());
        s.putExtra("sdt", job.getSdt());
        s.putExtra("motact", job.getMotact());
        s.putExtra("matd", job.getMatd());
        s.putExtra("statussave", job.getStatussave());
        s.putExtra("email", job.getEmail());
        s.putExtra("type", 3);
        mContext.startActivity(s);
    }
    private void SaveJob(final String id, final String macv) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/


        // Tag used to cancel the request

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LUUCV, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Intent a = new Intent();
                a.setAction("appendChatScreenMsg");
                a.putExtra("reload", true);
                mContext.sendBroadcast(a);
                parseJsonFeed(result);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("id", id);
                params.put("macv", macv);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(mContext).requestQueue.add(strReq);
    }
    private void DeleteSaveJob(final String id, final String macv) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/


        // Tag used to cancel the request
        String serverUrl = "http://45.32.225.156:299/xoacongviec.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                serverUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Intent a = new Intent();
                a.setAction("appendChatScreenMsg");
                a.putExtra("reload", true);
                mContext.sendBroadcast(a);
                parseJsonFeed(result);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("id", id);
                params.put("listitem", macv);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(mContext).requestQueue.add(strReq);
    }
    private void parseJsonFeed(String result) {

        Log.d(AppController.TAG, "Sattus Response: " + result);
        if (result.equals("") || result == null) {
            Toast.makeText(mContext,R.string.st_errServer, Toast.LENGTH_SHORT).show();
            return;
        }
            if (result.equals("3")) {
                Toast.makeText(mContext,R.string.toast_save, Toast.LENGTH_SHORT).show();

            }
            if (result.equals("2")) {
                Toast.makeText(mContext,R.string.toast_alreadysaved, Toast.LENGTH_SHORT).show();
            }
        if (result.equals("1")) {
            Toast.makeText(mContext,R.string.toast_apply, Toast.LENGTH_SHORT).show();

        }
        if (result.equals("0")) {
            Toast.makeText(mContext,R.string.toast_alreadyapply, Toast.LENGTH_SHORT).show();

        }
        wantting=false;
    }
    @Override
    public int getItemCount() {
        return myArray.size();
    }
   /* public String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }
        long now =System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }
        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return mContext.getResources().getString(R.string.justnow);
        } else if (diff < 2 * MINUTE_MILLIS) {
            return mContext.getResources().getString(R.string.anminute);
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS +" "+ mContext.getResources().getString(R.string.minutesago);
        } else if (diff < 90 * MINUTE_MILLIS) {
            return mContext.getResources().getString(R.string.anhour);
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS +" "+ mContext.getResources().getString(R.string.hourago);
        } else if (diff < 48 * HOUR_MILLIS) {
            return mContext.getResources().getString(R.string.yesterday);
        } else {
            return diff / DAY_MILLIS +" "+mContext.getResources().getString(R.string.daysago);
        }
    }
    public static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    } */
}
