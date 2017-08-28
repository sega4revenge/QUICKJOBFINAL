package com.quickjob.quickjobFinal.quickjobFind.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.activity.JobDetailActivity;
import com.quickjob.quickjobFinal.quickjobFind.activity.MainActivity;
import com.quickjob.quickjobFinal.AppController;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by VinhNguyen on 3/2/2017.
 */

public class JobManager_RecyclerAdapter extends RecyclerView.Adapter<JobManager_RecyclerAdapter.MyViewHolder> {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    Activity mContext;
    private List<CongViec> myArray;
    private List<CongViec> myArray2;
    String uid;
    int type;
    int mtype;
    int count=0,item=0;
    JSONArray arritem = new JSONArray();
    LinearLayout lin,linjr;
    TSnackbar snackbar;
    int[] array;
    int[] array2;
    int stt=0;
    RecyclerView rec;
    String[] arr,arrgender,arrcarrer;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tencongty,tencongviec,diadiem,mucluong,ngayup,trangthai,des,views,txtrequest,txtrequestfist,txtrequestsecond,txtnamecarrer;
        LinearLayout linreq,linwait,linone;
        LinearLayout linnext;CheckBox mCheckBox;
        ImageView saveimg,imgnext,imglogo;
        public MyViewHolder(View view) {
            super(view);
            tencongty = (TextView) view.findViewById(R.id.tenct);
            trangthai = (TextView) view.findViewById(R.id.txtstatus);
            tencongviec = (TextView) view.findViewById(R.id.tencongviec);
            diadiem = (TextView) view.findViewById(R.id.diachi);
            mucluong=(TextView) view.findViewById(R.id.luong);
            ngayup=(TextView) view.findViewById(R.id.dateup1);
            linnext= (LinearLayout) view.findViewById(R.id.linnext);
            txtrequest =(TextView) view.findViewById(R.id.txtrequest);
            saveimg  =(ImageView) view.findViewById(R.id.imgsave);
            imglogo =(ImageView) view.findViewById(R.id.thumbnail);
            txtnamecarrer = (TextView) view.findViewById(R.id.txtnamecarrer);
            des= (TextView) view.findViewById(R.id.txtdes);
            linwait=(LinearLayout) view.findViewById(R.id.linwatting);
            linone=(LinearLayout) view.findViewById(R.id.linone);
            mCheckBox=(CheckBox) view.findViewById(R.id.checkBox);
        }
    }

    public JobManager_RecyclerAdapter(Activity context, List<CongViec> objects, int type, String uid, int mtype,RecyclerView rec){
        this.mContext=context;
        this.myArray=objects;
        this.uid=uid;
        this.type=type;
        this.mtype=mtype;
        this.arr= AppController.context.getResources().getStringArray(R.array.mucluong);
        this.arrcarrer=AppController.context.getResources().getStringArray(R.array.nganhNghe);
        this.arrgender=AppController.context.getResources().getStringArray(R.array.sex);
        this.array=new int[myArray.size()];
        this.array2=new int[myArray.size()];
        this.rec = rec;
        this.count=0;
        this.item=0;
        this.arritem=new JSONArray();
        for(int i=0;i<myArray.size();i++){
            array[i]=0;
            array2[i]=0;
        }

        this.myArray2=new ArrayList<CongViec>();

    }

    @Override
    public JobManager_RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customitem_listjobcompany, parent, false);
        return new JobManager_RecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final JobManager_RecyclerAdapter.MyViewHolder holder, final int position) {
        holder.linnext.setBackgroundResource(R.color.white);
        final CongViec job = myArray.get(position);
        holder.tencongty.setText(job.motacv);
        holder.tencongviec.setText(job.getTencongviec());
        holder.txtnamecarrer.setText(arrcarrer[Integer.parseInt(job.getNganhNghe())]);
        holder.ngayup.setText(job.getDateup());
        String[] d =job.getDiadiem().split(",");
        int maxandress=d.length;
//        if(job.statusDelete){
//            if(holder.mCheckBox.getVisibility()!=View.VISIBLE)
//            holder.mCheckBox.setVisibility(View.VISIBLE);
//        }else{
//            if(holder.mCheckBox.getVisibility()!=View.GONE)
//                holder.mCheckBox.setVisibility(View.GONE);
//        }
        if(maxandress>=3) {
            holder.diadiem.setText(d[maxandress - 2]);
        }else{
            holder.diadiem.setText(job.getDiadiem());

        }

        Glide.with(mContext).load(job.getUrl())
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                //  .skipMemoryCache( true )
                .into(holder.imglogo);
        holder.mucluong.setText(arr[Integer.parseInt(job.getLuong())]);
        if(mtype==1 || mtype == 2)
        {
            holder.linone.setVisibility(View.VISIBLE);
        }
        if(mtype==2){
            int status = Integer.parseInt(job.getTrangthai());
            holder.linwait.setVisibility(View.VISIBLE);
            if(status==0)
            {
                holder.trangthai.setText(R.string.st_stt_dangcho);
            }else if(status==1){
                holder.trangthai.setText(R.string.st_stt_dachapnhan);
                holder.linwait.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
            }else if(status==2){
                holder.trangthai.setText(R.string.st_stt_datuchoi);
                holder.linwait.setBackgroundColor(mContext.getResources().getColor(R.color.slide_3));
            }
        }
        holder.linnext.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    for(int n=0;n<myArray.size();n++){
                        myArray.get(n).setStatusDelete(true);
                        notifyItemChanged(n);
                    }

                    if(count<1)
                    {
                        count=1;
                        item=1;
                        array[position]=1;
                        array2[position]=position;
                       // holder.mCheckBox.setChecked(true);
                        try {
                            if(mtype==1 || mtype==3){
                                arritem.put(position, myArray.get(position).getMacv());
                            }else if(mtype==2)
                            {
                                arritem.put(position, myArray.get(position).getIdungtuyen());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       // holder.linnext.setBackgroundResource(R.color.blur);
                        int Maxheight =Integer.parseInt(getScreenResolution(mContext));
                        int heightpadding = ((Maxheight-128)*9)/100;
                        rec.setPadding(0,heightpadding,0,0);
                        snackbar = TSnackbar.make(holder.linnext, mContext.getResources().getString(R.string.selecte)+" "+item+" "+mContext.getResources().getString(R.string.of)+" "+myArray.size()+" "+mContext.getResources().getString(R.string.tab_ProfileManger)+" ", TSnackbar.LENGTH_INDEFINITE);
                        snackbar.setActionTextColor(Color.WHITE);
                        snackbar.setCallback(new TSnackbar.Callback() {
                            @Override
                            public void onDismissed(TSnackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                rec.setPadding(0,0,0,0);
                                for(int i=0;i<myArray.size();i++){
                                    for(int i3=0;i3<array.length;i3++)
                                    {
                                        if(array[i3]==1) {
                                            notifyItemChanged(i3);
                                        }

                                    }
                                    //notifyDataSetChanged();
                                    count=0;
                                    item=0;
                                    array=new int[myArray.size()];
                                    array2=new int[myArray.size()];
                                    for(int i2=0;i2<myArray.size();i2++){
                                        array[i2]=0;
                                        array2[i2]=0;
                                    }
                                    arritem=new JSONArray();
                                }
                            }
                        });

                            snackbar.setAction(R.string.st_btnXoa, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    holder.linnext.setBackgroundResource(R.color.white);
                                    for (int i = 0; i < myArray.size(); i++) {
                                        if (array[i] != 1) {
                                            myArray2.add(myArray.get(i));
                                        }
                                    }
                                    myArray.clear();
                                    myArray.addAll(myArray2);
                                    myArray2.clear();
                                    //   Toast.makeText(mContext, myArray.size()+"//2", Toast.LENGTH_SHORT).show();
                                    if (myArray.size() == 0) {
                                        View lin = mContext.findViewById(R.id.linjr);
                                        lin.setVisibility(View.VISIBLE);
                                    }
                                    notifyDataSetChanged();
                                    if (myArray.size() == 0) {
                                        //      linjr.setVisibility(View.VISIBLE);
                                    }
                                    count = 0;
                                    item = 0;
                                    // holder.linnext.setPadding(0,0,0,0);
                                    String id = MainActivity.uid + "";
                                    array = new int[myArray.size()];
                                    array2 = new int[myArray.size()];
                                    for (int i = 0; i < myArray.size(); i++) {
                                        array[i] = 0;
                                        array2[i] = 0;
                                    }
                                    String serverUrl = "";
                                    if (mtype == 1) {
                                        serverUrl = "http://quickjob.gq/xoacongviec.php";

                                    } else if (mtype == 2) {
                                        serverUrl = "http://quickjob.gq/xoacongviecdaungtuyen.php";
                                    } else {
                                        serverUrl = "http://quickjob.gq/removenotification.php";
                                    }
                                    AsyncDataClass asyncRequestObject = new AsyncDataClass();
                                    asyncRequestObject.execute(serverUrl, arritem.toString(), id);
                                }
                            });

                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundResource(R.color.colorPrimary);
                        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                      //  Button bt = (Button) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_action);
                        textView.setTextColor(Color.WHITE);

                      //  snackbarView.cancelDragAndDrop();
                        snackbar.show();

                    }

                    return false;
                }
            });
        holder.linnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(count==0)
            {
                nextinfomation(job);
            }else {
                 if(array[position]==0)
                        {

                            array[position] = 1;
                            array2[position]=position;
                            item=item+1;
                            snackbar.setText(mContext.getResources().getString(R.string.selecte)+" "+item+" "+mContext.getResources().getString(R.string.of)+" "+myArray.size()+" "+mContext.getResources().getString(R.string.job));
                            snackbar.setAction(R.string.st_btnXoa, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    holder.linnext.setBackgroundResource(R.color.white);
                                    for (int i = 0; i < myArray.size(); i++) {
                                        if (array[i] != 1) {
                                            myArray2.add(myArray.get(i));
                                        }
                                    }
                                    myArray.clear();
                                    myArray.addAll(myArray2);
                                    myArray2.clear();
                                    //   Toast.makeText(mContext, myArray.size()+"//2", Toast.LENGTH_SHORT).show();
                                    if (myArray.size() == 0) {
                                        View lin = mContext.findViewById(R.id.linjr);
                                        lin.setVisibility(View.VISIBLE);
                                    }
                                    notifyDataSetChanged();
                                    if (myArray.size() == 0) {
                                        //      linjr.setVisibility(View.VISIBLE);
                                    }
                                    count = 0;
                                    item = 0;
                                    // holder.linnext.setPadding(0,0,0,0);
                                    String id = MainActivity.uid + "";
                                    array = new int[myArray.size()];
                                    array2 = new int[myArray.size()];
                                    for (int i = 0; i < myArray.size(); i++) {
                                        array[i] = 0;
                                        array2[i] = 0;
                                    }
                                    String serverUrl = "";
                                    if (mtype == 1) {
                                        serverUrl = "http://quickjob.gq/xoacongviec.php";

                                    } else if (mtype == 2) {
                                        serverUrl = "http://quickjob.gq/xoacongviecdaungtuyen.php";
                                    } else {
                                        serverUrl = "http://quickjob.gq/removenotification.php";
                                    }
                                    AsyncDataClass asyncRequestObject = new AsyncDataClass();
                                    asyncRequestObject.execute(serverUrl, arritem.toString(), id);
                                }
                            });
                            try {
                                if(mtype==1 || mtype==3){
                                    arritem.put(position, myArray.get(position).getMacv());
                                }else if(mtype==2)
                                {
                                    arritem.put(position, myArray.get(position).getIdungtuyen());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            holder.linnext.setBackgroundResource(R.color.blur);

                        }else{
                           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                arritem.remove(position);
                                item=item-1;
                                array[position]=0;
                                array2[position]=0;
                                snackbar.setText(mContext.getResources().getString(R.string.selecte)+" "+item+" "+mContext.getResources().getString(R.string.of)+" "+myArray.size()+" "+mContext.getResources().getString(R.string.job));
                               if(item==0)
                               {
                                   snackbar.setAction(R.string.st_done, new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           rec.setPadding(0,0,0,0);
                                           snackbar.dismiss();
                                           count=0;
                                           item=0;
                                           array=new int[myArray.size()];
                                           array2=new int[myArray.size()];
                                           for(int i2=0;i2<myArray.size();i2++){
                                               array[i2]=0;
                                               array2[i2]=0;
                                           }
                                           arritem=new JSONArray();
                                       }
                                   });
                               }
                           }else{
                               item=item-1;
                               array[position]=0;
                               array2[position]=0;
                               snackbar.setText(mContext.getResources().getString(R.string.selecte)+" "+item+" "+mContext.getResources().getString(R.string.of)+" "+myArray.size()+" "+mContext.getResources().getString(R.string.job));
                               if(item==0)
                               {
                                   snackbar.setAction(R.string.st_done, new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           rec.setPadding(0,0,0,0);
                                           snackbar.dismiss();
                                           count=0;
                                           item=0;
                                           array=new int[myArray.size()];
                                           array2=new int[myArray.size()];
                                           for(int i2=0;i2<myArray.size();i2++){
                                               array[i2]=0;
                                               array2[i2]=0;
                                           }
                                           arritem=new JSONArray();
                                       }
                                   });
                               }
                               try {
                                    arritem.put(position,"");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            array[position] = 0;
                            holder.linnext.setBackgroundResource(R.color.white);
                        }
                }
            }
        });

//
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

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
        s.putExtra("type", 3);
        mContext.startActivity(s);
    }
    @Override
    public int getItemCount() {
        return myArray.size();
    }
    public String getTimeAgo(long time) {
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
    }

    private class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(params[0]);

            String jsonResult = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("listitem", params[1]));
                nameValuePairs.add(new BasicNameValuePair("id", params[2]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
                System.out.println("Returned Json object " + jsonResult.toString());

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("Resulted Value: " + result);
            if(result.equals("") || result == null){
                return;
            }

            try {
                JSONObject resultObject = new JSONObject(result);
                if (resultObject.getString("success").equals("1")) {
//                    Toast.makeText(mContext, R.string.toast_delete, Toast.LENGTH_SHORT).show();
                    new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(mContext.getResources().getString(R.string.toast_delete))
                            .show();
                    arritem=new JSONArray();

//                    String tag=mContext.pagerAdapter.getFragmentTag(0);
//                    SearchFragment f = (SearchFragment) getSupportFragmentManager().findFragmentByTag(tag);
//                    f.onResume();
                    //   adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(mContext, resultObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            stt=0;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return answer;
        }
    }
    private static String getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return height+"";
    }


}