package com.quickjob.quickjobFinal.quickjobHire.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.activity.DetailProfileActivity;
import com.quickjob.quickjobFinal.quickjobHire.other.CircleTransform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by VinhNguyen on 3/31/2017.
 */

public class Profile_Apply_Adapter extends RecyclerView.Adapter<Profile_Apply_Adapter.MyViewHolder> {


    Date today=new Date(System.currentTimeMillis());
    SimpleDateFormat timeFormat= new SimpleDateFormat("dd/MM/yyyy");
    Activity mcontext;
    int type;
    JSONArray myArray =new JSONArray();
    String[] salary;
    String update,macv;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbNail;
        public MyViewHolder(View view) {
            super(view);
            thumbNail = (ImageView) view.findViewById(R.id.img_avata);

        }
    }

    public Profile_Apply_Adapter(Activity context, JSONArray objects ,String macv){
        this.mcontext=context;
        this.myArray=objects;
        this.macv = macv;
    }

    @Override
    public Profile_Apply_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_apply_adapter_layout_hire, parent, false);
        return new Profile_Apply_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Profile_Apply_Adapter.MyViewHolder holder, int position) {
        try {
            final JSONObject ob =myArray.getJSONObject(position);
            Glide.with(mcontext).load(ob.getString("img"))
                    .crossFade()
                    .override(80,80)
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(mcontext))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.thumbNail);
            holder.thumbNail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent is = new Intent(mcontext, DetailProfileActivity.class);
                    try {
                        is.putExtra("iduser", ob.getString("iduser"));
                        is.putExtra("ten", ob.getString("hoten"));
                        is.putExtra("gioitinh", ob.getString("gioitinh2"));
                        is.putExtra("ngaysinh", ob.getString("ngaysinh"));
                        is.putExtra("email", ob.getString("email"));
                        is.putExtra("sdt", ob.getString("sdt"));
                        is.putExtra("kinhnghiem",ob.getString("namkn"));
                        is.putExtra("tencongty", ob.getString("tencongty"));
                        is.putExtra("chucdanh", ob.getString("chucdanh"));
                        is.putExtra("motacv", ob.getString("mota"));
                        is.putExtra("quequan", ob.getString("quequan"));
                        is.putExtra("diachi", ob.getString("diachi"));
                        is.putExtra("mucluong", ob.getString("mucluong"));
                        is.putExtra("kynang", ob.getString("kynang"));
                        is.putExtra("ngoaingu", ob.getString("ngoaingu"));
                        is.putExtra("img", ob.getString("img"));
                        is.putExtra("mahs", ob.getString("mahs"));
                        is.putExtra("macv", macv);
                        is.putExtra("education", ob.getString("education"));
                        is.putExtra("slogan", ob.getString("slogan"));
                        is.putExtra("workplace", ob.getString("diadiem"));
                        is.putExtra("key", 1);

                        mcontext.startActivity(is);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public int getItemCount() {
        return myArray.length();
    }
}