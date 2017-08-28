package com.quickjob.quickjobFinal.quickjobHire.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobHire.activity.Edit_Job_activity;
import com.quickjob.quickjobFinal.quickjobHire.activity.MainActivity;
import com.quickjob.quickjobFinal.quickjobHire.activity.RecruitmentListActivity;
import com.quickjob.quickjobFinal.quickjobHire.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobHire.config.AppController;
import com.quickjob.quickjobFinal.quickjobHire.model.CompanyInformation;
import com.quickjob.quickjobFinal.quickjobHire.model.CongViec;
import com.quickjob.quickjobFinal.quickjobHire.other.CircleTransform;
import com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager;
import com.firebase.client.Firebase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by SONTHO on 15/08/2016.
 */
public class CongViecAdapter extends ArrayAdapter<CongViec>
{
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Activity mcontext;
    List<CongViec> myArray;
    String uid;
    int type;
    String serverUrl = "", iduser;
    String sa="";
    int mtype;AlertDialog dialog,mDialog;Firebase refe_send,rec_list_child_company;
    String[] arrsalary,arrbc;SessionManager session;
    List<CompanyInformation> listdata3 = new ArrayList<>();
    List<CompanyInformation> filter = new ArrayList<>();HashMap<String, String> user;
    //  ImageLoader imageLoader = AppController.getInstance().getImageLoader();
  String message="";
    public CongViecAdapter(Activity context, int layoutId, List<CongViec> objects, int type, String uid, int mtype){
        super(context, layoutId, objects);
        this.mcontext=context;
        this.myArray=objects;
        this.type=type;
        this.mtype=mtype;
        arrsalary=mcontext.getResources().getStringArray(R.array.mucluong);
        arrbc= mcontext.getResources().getStringArray(R.array.speducation);
        this.session = new SessionManager(context);
        SharedPreferences pref = context.getSharedPreferences("JobFindPref", MODE_PRIVATE);
        user = session.getUserDetails();
        uid = user.get(SessionManager.KEY_ID);
        iduser = uid.substring(0,14);
        refe_send = new Firebase("https://quickjob-hire.firebaseio.com/"+iduser);
        rec_list_child_company= new Firebase("https://quickjob-hire.firebaseio.com/"+iduser);
    }



    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder=null;
        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.customitem_hire,null);
        viewHolder = new ViewHolder();
        viewHolder.card_content = (CardView) rowView.findViewById(R.id.card_content);
        viewHolder.noti= (TextView) rowView.findViewById(R.id.noti);
        viewHolder.lin = (LinearLayout) rowView.findViewById(R.id.linit);
        viewHolder.trangthai = (TextView) rowView.findViewById(R.id.trangthai);
        viewHolder.tencongviec = (TextView) rowView.findViewById(R.id.tencongviec);
        viewHolder.diadiem = (TextView) rowView.findViewById(R.id.diachi);
        viewHolder.mucluong=(TextView) rowView.findViewById(R.id.luong);
        viewHolder.ngayup=(TextView) rowView.findViewById(R.id.dateup1);
        viewHolder.thumbNail = (ImageView) rowView.findViewById(R.id.thumbnail);
        viewHolder.btn_menu = (LinearLayout) rowView.findViewById(R.id.btn_menu);
        viewHolder.tencongviec.setText(myArray.get(position).tencongviec);
        String[] d =myArray.get(position).diadiem.split(", ");
        int maxandress=d.length;
        if(maxandress>=3) {
            viewHolder.diadiem.setText(d[maxandress - 2]+"");
        }else{
            viewHolder.diadiem.setText(myArray.get(position).diadiem+"");
        }
        viewHolder.mucluong.setText(arrsalary[Integer.parseInt(myArray.get(position).luong)]);
        viewHolder.ngayup.setText(myArray.get(position).dateup);

    Glide.with(mcontext).load(myArray.get(position).url)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(mcontext))
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .skipMemoryCache( true )
                .into(viewHolder.thumbNail);

        viewHolder.recyclerView_profile = (RecyclerView) rowView.findViewById(R.id.recyclerview_profile);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext, LinearLayoutManager.HORIZONTAL, false);
        viewHolder.recyclerView_profile .setLayoutManager(linearLayoutManager);
        viewHolder.recyclerView_profile .setItemAnimator(new DefaultItemAnimator());
       if( myArray.get(position).profile_apply != null )
        {
            viewHolder.noti.setText(mcontext.getResources().getString(R.string.st_apply)+" "+myArray.get(position).profile_apply.length());
            JSONArray profile_apply = myArray.get(position).profile_apply;
            Profile_Apply_Adapter adapter =new Profile_Apply_Adapter(mcontext,profile_apply,myArray.get(position).macv);
            viewHolder.recyclerView_profile.setAdapter(adapter);

        }else{}
        viewHolder.btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                LayoutInflater inf = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mView = inf.inflate(R.layout.dialog_createlangues,null);
                builder.setView(mView);
                ListView selecte =(ListView) mView.findViewById(R.id.lvnn);
                String[] arr = mcontext.getResources().getStringArray(R.array.menu_select);
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mcontext,android.R.layout.simple_list_item_1,arr);
                selecte.setAdapter(adapter);
                selecte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i)
                        {
                            case  0:
                                if(myArray.get(position).bangcap.equals("") || myArray.get(position).bangcap == null && myArray.get(position).gioitinh.equals("") || myArray.get(position).gioitinh == null){
//                                    Log.e("AAAAAAA",myArray.get(position).tencongviec+","+myArray.get(position).nganhNghe+","+myArray.get(position).dateup+","+myArray.get(position).luong+","+myArray.get(position).soluong+","+myArray.get(position).diadiem+","+myArray.get(position).motacv+","+myArray.get(position).khac);
                                    ((MainActivity)mcontext).edit("",myArray.get(position).tencongviec,myArray.get(position).nganhNghe,myArray.get(position).dateup,myArray.get(position).luong,myArray.get(position).soluong,myArray.get(position).diadiem,myArray.get(position).motacv,myArray.get(position).khac,"1",myArray.get(position).macv);
                                    dialog.cancel();

                                }
                                else
                                {
                                    IntentData(position);
                                    dialog.cancel();
                                }

                                break;
                            case 1:
                                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(getContext().getResources().getString(R.string.confirm_del))
                                        .setContentText(getContext().getResources().getString(R.string.st_xacNhanXoaCV))
                                        .setCancelText(getContext().getResources().getString(R.string.st_xacNhanCancel))
                                        .setConfirmText(getContext().getResources().getString(R.string.st_xacNhanOK))
                                        .showCancelButton(true)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                dialog.cancel();
                                                sDialog.cancel();
                                            }
                                        })
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                String unique_id =user.get(SessionManager.KEY_ID);
                                                getDataFromInternet(AppConfig.URL_DeleteJOB,myArray.get(position).macv+"",unique_id+"");
                                                List<CongViec> objectremove = myArray;
                                                myArray.remove(objectremove.get(position));
                                                dialog.cancel();
                                                sDialog.cancel();
                                            }
                                        })
                                        .show();

                                break;
                            case 2:
                                if(myArray.get(position).ngoaingu.equals("") || myArray.get(position).kn.equals("") || myArray.get(position).bangcap.equals("") || myArray.get(position).dotuoi.equals(""))
                                {
                                    message = "Tên công việc : "+myArray.get(position).tencongviec+"\n Địa điểm: "+myArray.get(position).diadiem+"\n Mức lương : "+arrsalary[Integer.parseInt(myArray.get(position).getLuong())]+"\n Mô tả công việc: "+myArray.get(position).motacv+" \n Yêu cầu: \n - Khác: "+myArray.get(position).khac+"\n Hạn nộp hồ sơ :"+myArray.get(position).dateup+"\n Tham khảo thêm tại: http://quickjob.gq/link.php?jobid="+myArray.get(position).macv;

                                }else{
                                    message = "Tên công việc : "+myArray.get(position).tencongviec+"\n Địa điểm: "+myArray.get(position).diadiem+"\n Mức lương : "+arrsalary[Integer.parseInt(myArray.get(position).getLuong())]+"\n Mô tả công việc: "+myArray.get(position).motacv+" \n Yêu cầu: \n - Bằng cấp:  "+arrbc[Integer.parseInt(myArray.get(position).bangcap)]+"\n - Độ tuổi "+myArray.get(position).dotuoi+ "\n - Kỹ năng: "+myArray.get(position).kn+"\n - Ngoại ngữ:  "+myArray.get(position).ngoaingu+"\n - Khác: "+myArray.get(position).khac+"\n Hạn nộp hồ sơ :"+myArray.get(position).dateup+"\n Tham khảo thêm tại: http://quickjob.gq/link.php?jobid="+myArray.get(position).macv ;
                                }
                                final AlertDialog.Builder buildershare = new AlertDialog.Builder(mcontext);
                                LayoutInflater inf = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View mView = inf.inflate(R.layout.custom_sharejobs_menu_hire,null);
                                buildershare.setView(mView);
                                buildershare.setTitle(R.string.st_chiaseCV);
                                LinearLayout linQJ = (LinearLayout) mView.findViewById(R.id.lin_shareQJ);
                                LinearLayout linDif = (LinearLayout) mView.findViewById(R.id.lin_sharedifference);
                                linQJ.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ((MainActivity) mcontext).shareQJ(message);
                                        mDialog.cancel();
                                    }
                                });
                                linDif.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                      ShareJob(position,message);
                                      mDialog.cancel();
                                    }
                                });
                                buildershare.setNegativeButton(mcontext.getResources().getString(R.string.st_thoat), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                mDialog = buildershare.create();
                                mDialog.show();

                                dialog.cancel();
                                break;
                            default:
                        }

                    }
                });
                dialog=builder.show();
            }
        });
        viewHolder.card_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mcontext,RecruitmentListActivity.class);
                i.putExtra("macv",myArray.get(position).macv);
                i.putExtra("tencv", myArray.get(position).tencongviec);
                mcontext.startActivity(i);
            }
        });
        /*viewHolder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(mcontext, EditJobActivity.class);
                    i.putExtra("tencongty", myArray.get(position).tecongty);
                    i.putExtra("diachi", myArray.get(position).diachict);
                    i.putExtra("nganhnghe", myArray.get(position).nganhnghe);
                    i.putExtra("quymo", myArray.get(position).quymo);
                    i.putExtra("motact", myArray.get(position).motact);
                    i.putExtra("nganhNghe", myArray.get(position).nganhNghe);
                    i.putExtra("chucdanh", myArray.get(position).chucdanh);
                    i.putExtra("soluong", myArray.get(position).soluong);
                    i.putExtra("phucloi", myArray.get(position).idungtuyen);
                    i.putExtra("tencongviec", myArray.get(position).tencongviec);
                    i.putExtra("diadiem", myArray.get(position).diadiem);
                    i.putExtra("mucluong", myArray.get(position).luong);
                    i.putExtra("ngayup", myArray.get(position).dateup);
                    i.putExtra("yeucaubangcap", myArray.get(position).bangcap);
                    i.putExtra("dotuoi", myArray.get(position).dotuoi);
                    i.putExtra("ngoaingu", myArray.get(position).ngoaingu);
                    i.putExtra("gioitinh", myArray.get(position).gioitinh);
                    i.putExtra("khac", myArray.get(position).khac);
                    i.putExtra("motacv", myArray.get(position).motacv);
                    i.putExtra("kn", myArray.get(position).kn);
                    i.putExtra("img", myArray.get(position).url);
                    mcontext.startActivity(i);
            }
        }); */

        return rowView;
    }

    private void ShareJob(int position ,String message) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        mcontext.startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));
    }

    private void getDataFromInternet(final String url,final String macv,final String id) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/
        // Tag used to cancel the request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                parseJsonFeed(result);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(mcontext,
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
        VolleySingleton.getInstance(mcontext).requestQueue.add(strReq);
    }
    private void parseJsonFeed(String result) {
        System.out.println("Resulted Value: " + result);
        if (result.equals("") || result == null) {
            Toast.makeText(mcontext, R.string.st_errServer, Toast.LENGTH_SHORT).show();
            return;
        }
        int jsonResult = returnParsedJsonObject(result);
        if (jsonResult == 0) {
            //    Toast.makeText(EditJobActivity.this, R.string.st_errNamePass, Toast.LENGTH_SHORT).show();
            return;
        }
        if (jsonResult==1) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(getContext().getResources().getString(R.string.st_xacXoaCV))
                    .show();
//            Toast.makeText(mcontext, R.string.st_xacXoaCV, Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
          //  mcontext.finish();
        }
    }
    private int returnParsedJsonObject(String result) {

        JSONObject resultObject = null;
        int returnedResult = 0;
        try {
            resultObject = new JSONObject(result);
            returnedResult = resultObject.getInt("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }
    private void IntentData(int position){
        Intent i = new Intent(mcontext, Edit_Job_activity.class);
        if(myArray.get(position).dotuoi.equals(""))
        {
            i.putExtra("dotuoi", "");
        }else{
            i.putExtra("dotuoi", myArray.get(position).dotuoi);
        }
        if(myArray.get(position).bangcap.equals(""))
        {
            i.putExtra("yeucaubangcap", "0");
        }else{
            i.putExtra("yeucaubangcap", myArray.get(position).bangcap);
        }
        if(myArray.get(position).luong.equals(""))
        {
            i.putExtra("mucluong", "0");
        }else{
            i.putExtra("mucluong", myArray.get(position).luong);
        }
        if( myArray.get(position).gioitinh.equals(""))
        {
            i.putExtra("gioitinh", "2");
        }else{
            i.putExtra("gioitinh",myArray.get(position).gioitinh);
        }
        i.putExtra("tencongty", myArray.get(position).tecongty);
        i.putExtra("diachi", myArray.get(position).diachict);
        i.putExtra("nganhnghe", myArray.get(position).nganhnghe);
        i.putExtra("quymo", myArray.get(position).quymo);
        i.putExtra("motact", myArray.get(position).motact);
        i.putExtra("nganhNghe", myArray.get(position).nganhNghe);
        i.putExtra("chucdanh", myArray.get(position).chucdanh);
        i.putExtra("soluong", myArray.get(position).soluong);
        i.putExtra("phucloi", myArray.get(position).idungtuyen);
        i.putExtra("tencongviec", myArray.get(position).tencongviec);
        i.putExtra("diadiem", myArray.get(position).diadiem);

        i.putExtra("ngayup", myArray.get(position).dateup);
        i.putExtra("ngoaingu", myArray.get(position).ngoaingu);

        i.putExtra("phucloi", myArray.get(position).phucloi);
        i.putExtra("khac", myArray.get(position).khac);
        i.putExtra("motacv", myArray.get(position).motacv);
        i.putExtra("kn", myArray.get(position).kn);
        i.putExtra("img", myArray.get(position).url);
        i.putExtra("macv", myArray.get(position).macv);

        mcontext.startActivity(i);
    }
    static class ViewHolder{
        TextView tencongty,tencongviec,diadiem,mucluong,ngayup,trangthai,noti;
        LinearLayout lin;
        RecyclerView recyclerView_profile;LinearLayout btn_menu;
        ImageView thumbNail;
        CardView card_content;
    }

}
