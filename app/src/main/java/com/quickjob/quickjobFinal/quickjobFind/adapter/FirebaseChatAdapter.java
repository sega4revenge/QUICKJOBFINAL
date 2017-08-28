package com.quickjob.quickjobFinal.quickjobFind.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.model.Messager_Chat;
import com.quickjob.quickjobFinal.quickjobFind.other.CircleTransform;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by VinhNguyen on 4/11/2017.
 */

public class FirebaseChatAdapter extends RecyclerView.Adapter<FirebaseChatAdapter.MyViewHolder> {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    Activity mContext;String uid;int countItem=0;
    private List<Messager_Chat> myArray;SessionManager session;
    RecyclerView rec;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView avata,avatarec;
        TextView name;
        TextView time;
        TextView mess;
        TextView namerec,txt_datetime;
        TextView timerec;int type;
        TextView messrec;LinearLayout lin,lin2;View mView =null;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameuser);
            time = (TextView) itemView.findViewById(R.id.time);
            mess = (TextView) itemView.findViewById(R.id.messager);
            avata= (ImageView) itemView.findViewById(R.id.avata);
            lin= (LinearLayout) itemView.findViewById(R.id.usersend);
            lin2= (LinearLayout) itemView.findViewById(R.id.userrec);
            // namerec = (TextView) itemView.findViewById(R.id.nameuser_rec);
            timerec = (TextView) itemView.findViewById(R.id.time_rec);
            messrec = (TextView) itemView.findViewById(R.id.messager_rec);
            avatarec= (ImageView) itemView.findViewById(R.id.avata_rec);
            txt_datetime= (TextView) itemView.findViewById(R.id.txt_datetime);
        }
    }
    public FirebaseChatAdapter(Activity context, List<Messager_Chat> objects, RecyclerView rec){
        this.mContext=context;
        this.myArray=objects;
        this.rec =rec;
        session = new SessionManager(mContext);
        SharedPreferences pref = mContext.getSharedPreferences("JobFindPref", MODE_PRIVATE);
        HashMap<String, String> user = session.getUserDetails();
        uid = user.get(SessionManager.KEY_ID);

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customitem_chat, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Messager_Chat job = myArray.get(position);
        Glide.clear(holder.avata);
        if(position==0)
        {
            String toDate = getDate(job.getTime());
            holder.txt_datetime.setVisibility(View.VISIBLE);
            holder.txt_datetime.setText(toDate);
        }else {
        Log.d("FireBase",job.getMessager());
            try {
                String toDate = getDate(job.getTime());
                String fistDate = getDate(myArray.get(position - 1).getTime());
                if (toDate.equals(fistDate)) {
                    if (holder.txt_datetime.getVisibility() != View.GONE) {
                        holder.txt_datetime.setVisibility(View.GONE);
                    }
                } else {
                    if (holder.txt_datetime.getVisibility() != View.VISIBLE) {
                        holder.txt_datetime.setVisibility(View.VISIBLE);
                    }
                    holder.txt_datetime.setText(getDate(job.getTime()));
                }
            } catch (Exception e) {
                Log.d("FireBase", e + "");
            }
        }

        if(job.getUid().equals(uid)) {
            if(holder.lin.getVisibility()== View.GONE)
            {
                holder.lin.setVisibility(View.VISIBLE);
            }
            if(holder.lin2.getVisibility()== View.VISIBLE)
            {
                holder.lin2.setVisibility(View.GONE);
            }
            holder.name.setText(job.getName());
            holder.mess.setText(job.getMessager());
            if(job.getAvata().equals("") || job.getAvata()==null)
            {
                holder.avata.setImageResource(R.drawable.logo2);
            }else {
                Glide.with(getApplicationContext()).load(job.getAvata())
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(mContext))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.avata);
            }

            if(!job.isview())
            {
                if(holder.time.getVisibility()==View.GONE)
                {
                    holder.time.setVisibility(View.VISIBLE);
                }
                String gettime=getDateInMillis(job.getTime());
                holder.time.setText(gettime+"");
            }else{
                if(holder.time.getVisibility()!=View.GONE)
                {
                    holder.time.setVisibility(View.GONE);
                }
            }
//            if(position>0) {
//                Messager_Chat jobscc = myArray.get(position - 1);
//                if (jobscc.getUid().equals(uid)) {
//                    myArray.get(position - 1).isview = true;
//                } else {
//                }
//            }
       }else{
            if(holder.lin2.getVisibility()== View.GONE)
            {
                holder.lin2.setVisibility(View.VISIBLE);
            }
            if(holder.lin.getVisibility()== View.VISIBLE)
            {
                holder.lin.setVisibility(View.GONE);
            }
            if(position>0) {
                Messager_Chat jobscc = myArray.get(position - 1);
                if (jobscc.getUid().equals(uid)) {
                    holder.avatarec.setVisibility(View.VISIBLE);
                    if (job.getAvata().equals("") || job.getAvata() == null) {
                        holder.avatarec.setImageResource(R.drawable.logo2);
                    } else {
                        Glide.with(mContext.getApplicationContext()).load(job.getAvata())
                                .crossFade()
                                .thumbnail(0.5f)
                                .bitmapTransform(new CircleTransform(mContext))
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(holder.avatarec);
                    }
                } else {
                    holder.avatarec.setVisibility(View.GONE);
//                    if(position>0){
//                        myArray.get(position-1).isview =true;
//                    }
                }
            }else{
                holder.avatarec.setVisibility(View.VISIBLE);
                if (job.getAvata().equals("") || job.getAvata() == null) {
                    holder.avatarec.setImageResource(R.drawable.logo2);
                } else {
                    Glide.with(mContext.getApplicationContext()).load(job.getAvata())
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(mContext))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(holder.avatarec);
                }
            }

            if(!job.isview)
            {
                if(holder.timerec.getVisibility()==View.GONE)
                {
                    holder.timerec.setVisibility(View.VISIBLE);
                }
                String gettime=getDateInMillis(job.getTime());
                holder.timerec.setText(gettime+"");
            }else{
                if(holder.timerec.getVisibility()!=View.GONE)
                {
                    holder.timerec.setVisibility(View.GONE);
                }
            }
            holder.messrec.setText(job.getMessager());
        }

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
    public String getDate(String date){
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String dateInMillis = "";
        try {
            Date date2 = desiredFormat.parse(date);
            DateFormat time = new SimpleDateFormat("dd-MM-yyyy");
            return time.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        String dateInMillis = "";
        try {
            Date date = desiredFormat.parse(srcDate);
            DateFormat time = new SimpleDateFormat("hh:mm");
            // dateInMillis = date.getTime();
            return time.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    @Override
    public int getItemCount() {
        return myArray.size();
    }


}
