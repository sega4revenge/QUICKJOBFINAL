package com.quickjob.quickjobFinal.quickjobHire.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.activity.ChattingActivity;
import com.quickjob.quickjobFinal.quickjobHire.model.CompanyInformation;
import com.quickjob.quickjobFinal.quickjobHire.other.CircleTransform;
import com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager;
import com.firebase.client.Firebase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;




/**
 * Created by VinhNguyen on 4/11/2017.
 */

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.MyViewHolder> {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    Activity mContext;String uid;String iduser;
    private List<CompanyInformation> myArray;SessionManager session;
    String avata="",name="";
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView avata,avatarec;View divide;
        TextView name,time,mess,txt_num, namerec,timerec,messrec;LinearLayout lin,lin2;
        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.nameuser);
            time = (TextView) view.findViewById(R.id.time);
            mess = (TextView) view.findViewById(R.id.messager);
            avata= (ImageView) view.findViewById(R.id.avata);
            lin= (LinearLayout) view.findViewById(R.id.usersend);
            txt_num= (TextView) view.findViewById(R.id.txt_num);
            divide= view.findViewById(R.id.divide);

        }
    }

    public ListChatAdapter(Activity context, List<CompanyInformation> objects){
        this.mContext=context;
        this.myArray=objects;
        session = new SessionManager(mContext);
        HashMap<String, String> user = session.getUserDetails();
        uid = user.get(SessionManager.KEY_ID);
        avata= user.get(SessionManager.KEY_LOGO);
        name  = user.get(SessionManager.KEY_NAME);
        iduser = uid.substring(0,14);

    }
    @Override
    public ListChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_listchat_hire, parent, false);
        return new ListChatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListChatAdapter.MyViewHolder holder, int position) {
        Glide.clear(holder.avata);
        final CompanyInformation job = myArray.get(position);
            holder.name.setText(job.getName());
            holder.time.setText("");
            holder.mess.setText(job.getNewchat());
        if(Integer.parseInt(job.getNotseen())>0)
        {
            holder.txt_num.setVisibility(View.VISIBLE);
            holder.txt_num.setText(job.getNotseen());
        }else{
            holder.txt_num.setVisibility(View.GONE);
        }
        if(job.getAvata()==null || job.getAvata().equals("")) {
            holder.avata.setImageResource(R.drawable.logo_hire);
        }else{
            Glide.with(mContext.getApplicationContext()).load(job.getAvata())
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.avata);
        }
        Long gettime=getDateInMillis(job.getTimenew());
        String timeago=getTimeAgo(gettime);
        holder.time.setText(timeago+"");

        holder.lin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(mContext.getResources().getString(R.string.st_xoaChat))
                        .setCancelText(mContext.getString(R.string.st_xacNhanCancel))
                        .setConfirmText(mContext.getString(R.string.st_xacNhanOK))
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Firebase  rec_data = new Firebase("https://job-find-2fb6d.firebaseio.com/"+iduser+"/"+job.getUid());
                                rec_data.removeValue();
                                sDialog.dismiss();
                            }
                        }).show();

                return false;
            }
        });
        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext,ChattingActivity.class);
                i.putExtra("idsend",iduser);
                i.putExtra("idrec",job.getUid());
                i.putExtra("name",name);
                i.putExtra("logocompany",job.getAvata());
                i.putExtra("avata",avata);
                i.putExtra("namecompany",job.getName());
                i.putExtra("iduser",job.getUid());
                mContext.startActivity(i);
            }
        });
        if(position==0)
        {
            if(holder.divide.getVisibility()!=View.GONE)
                holder.divide.setVisibility(View.GONE);

        }else{
            if(holder.divide.getVisibility()!=View.VISIBLE)
                holder.divide.setVisibility(View.VISIBLE);
        }

    }
    public String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = System.currentTimeMillis();
//        if (time > now || time <= 0) {
//            return null;
//        }
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
    @Override
    public int getItemCount() {
        return myArray.size();
    }

}

