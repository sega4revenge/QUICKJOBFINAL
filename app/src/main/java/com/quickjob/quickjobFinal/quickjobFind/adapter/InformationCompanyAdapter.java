package com.quickjob.quickjobFinal.quickjobFind.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.activity.Information_Company;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;
import com.quickjob.quickjobFinal.quickjobFind.other.RoundedCornersTransformation;

import java.util.List;



/**
 * Created by VinhNguyen on 1/3/2017.
 */

public class InformationCompanyAdapter  extends RecyclerView.Adapter<InformationCompanyAdapter.MyViewHolder> {

private List<CongViec> joblist;
    Activity mContext;
    public static int sCorner = 5;
    public static int sMargin = 2;
public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView  nameconpany,infor;
    public ImageView logocompany;
    public RelativeLayout relacontent;
    public MyViewHolder(View view) {
        super(view);
        nameconpany = (TextView) view.findViewById(R.id.txtnamecompany);
        logocompany = (ImageView) view.findViewById(R.id.logocompany);
        relacontent = (RelativeLayout) view.findViewById(R.id.rela_content);
      //  infor= (TextView) view.findViewById(R.id.txtinfor);
    }
}
    public InformationCompanyAdapter(List<CongViec> joblist,Activity context ){
        this.joblist = joblist;
        this.mContext=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_item_inforcompany, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CongViec job = joblist.get(position);
        String upperString = job.getTecongty().substring(0,1).toUpperCase() + job.getTecongty().substring(1);
        holder.nameconpany.setText(upperString);
       // holder.infor.setText(joblist.get(position).motact);
        if(joblist.get(position).url.equals("null"))
        {
            Glide.with(mContext).load(R.drawable.logo2)
                    .centerCrop()
                    .bitmapTransform(new RoundedCornersTransformation( mContext,sCorner, sMargin))
                    .into(holder.logocompany);
        }else{
            Glide.with(mContext).load(joblist.get(position).url)
                    .centerCrop()
                    .bitmapTransform(new RoundedCornersTransformation( mContext,sCorner, sMargin))
                    .into(holder.logocompany);

        }
        holder.relacontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, Information_Company.class);
                i.putExtra("nameconpany",job.getTecongty());
                i.putExtra("andress",job.getDiadiem());
                i.putExtra("career",job.getNganhNghe());
                i.putExtra("infor",job.getMotact());
                i.putExtra("logo",job.getUrl());
                i.putExtra("macv",job.getMacv());
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return joblist.size();
    }
}

