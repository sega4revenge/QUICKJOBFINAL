package com.quickjob.quickjobFinal.quickjobHire.adapter;

import android.app.Activity;
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
import com.quickjob.quickjobFinal.quickjobHire.model.Profile;
import com.quickjob.quickjobFinal.quickjobHire.other.CircleTransform;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {


    Date today=new Date(System.currentTimeMillis());
    SimpleDateFormat timeFormat= new SimpleDateFormat("dd/MM/yyyy");
    Activity mcontext;
    int type;
    List<Profile> myArray;
    String[] salary;
    String update;
public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView tencongty,tencongviec,diadiem,mucluong,ngayup,exe;
    LinearLayout lin;
    ImageView thumbNail;


    public MyViewHolder(View view) {
        super(view);
        lin = (LinearLayout) view.findViewById(R.id.liner);
        tencongty = (TextView) view.findViewById(R.id.txt1);
        tencongviec = (TextView) view.findViewById(R.id.txt2);
        diadiem = (TextView) view.findViewById(R.id.txt3);
        mucluong=(TextView) view.findViewById(R.id.txt4);
        ngayup=(TextView) view.findViewById(R.id.txt5);
        thumbNail = (ImageView) view.findViewById(R.id.thumbnail);
        exe=(TextView) view.findViewById(R.id.txt7);
    }
}

    public RecyclerAdapter(Activity context, List<Profile> objects, int type ){
        this.mcontext=context;
        this.myArray=objects;
        this.type=type;
        this.salary = mcontext.getResources().getStringArray(R.array.mucluong);
        update = mcontext.getResources().getString(R.string.st_upadate);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customitems_hire, parent, false);
        return new RecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Profile job = myArray.get(position);
        holder.tencongty.setText(job.getTencv());
        holder.tencongviec.setText(job.getTen());
        holder.diadiem.setText(salary[Integer.parseInt(job.getMucluong())]);
        holder.mucluong.setText(job.getDiadiem());
        holder.ngayup.setText("( "+update+" "+job.getNgaydang()+" )");
        holder.exe.setText(job.getNamkn());
        Glide.with(mcontext).load(job.getImg())
                .crossFade()
                .override(80,80)
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(mcontext))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbNail);
    }
    @Override
    public int getItemCount() {
        return myArray.size();
    }
}