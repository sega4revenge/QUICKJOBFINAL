package com.quickjob.quickjobFinal.quickjobFind.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.activity.JobDetailActivity;
import com.quickjob.quickjobFinal.AppController;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by VinhNguyen on 2/25/2017.
 */

public class ListJobCompanyAdapter extends RecyclerView.Adapter<ListJobCompanyAdapter.MyViewHolder> {

    JSONArray arritem = new JSONArray();

    Activity mContext;
    private List<CongViec> myArray;
    String uid;
    int type;
    int mtype;
    boolean wantting=false;
    String[] arr,arrgender,arrcarrer;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namecompany,tencongty,tencongviec,diadiem,mucluong,ngayup,trangthai,des,views,txtrequest,txtrequestfist,txtrequestsecond,txtnamecarrer;
        LinearLayout linreq,linwait,linone;RelativeLayout linit;
        LinearLayout linnext;
        ImageView saveimg,imgnext,imglogo;
        public MyViewHolder(View view) {
            super(view);
            tencongty = (TextView) view.findViewById(R.id.tenct);
            trangthai = (TextView) view.findViewById(R.id.txtstatus);
            tencongviec = (TextView) view.findViewById(R.id.tencongviec);
            diadiem = (TextView) view.findViewById(R.id.diachi);
            mucluong=(TextView) view.findViewById(R.id.luong);
            namecompany = (TextView) view.findViewById(R.id.txt_namecompany);
            ngayup=(TextView) view.findViewById(R.id.dateup1);
            linnext= (LinearLayout) view.findViewById(R.id.linnext);
            txtrequest =(TextView) view.findViewById(R.id.txtrequest);
            saveimg  =(ImageView) view.findViewById(R.id.imgsave);
            imglogo =(ImageView) view.findViewById(R.id.thumbnail);
            txtnamecarrer = (TextView) view.findViewById(R.id.txtnamecarrer);
            des= (TextView) view.findViewById(R.id.txtdes);
            linwait=(LinearLayout) view.findViewById(R.id.linwatting);
            linone=(LinearLayout) view.findViewById(R.id.linone);
        }
    }

    public ListJobCompanyAdapter(Activity context, List<CongViec> objects, int type, String uid, int mtype){
        this.mContext=context;
        this.myArray=objects;
        this.uid=uid;
        this.type=type;
        this.mtype=mtype;
        this.arr= AppController.context.getResources().getStringArray(R.array.mucluong);
        this.arrcarrer=AppController.context.getResources().getStringArray(R.array.nganhNghe);
        this.arrgender=AppController.context.getResources().getStringArray(R.array.sex);
    }

    @Override
    public ListJobCompanyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customitem_listjobcompany, parent, false);
        return new ListJobCompanyAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListJobCompanyAdapter.MyViewHolder holder, int position) {
        final CongViec job = myArray.get(position);
        holder.tencongty.setText(job.motacv);
        holder.tencongviec.setText(job.getTencongviec());
        holder.txtnamecarrer.setText(arrcarrer[Integer.parseInt(job.getNganhNghe())]);
        holder.ngayup.setText(job.getDateup());
        holder.namecompany.setText(job.getTecongty());
        String[] d =job.getDiadiem().split(",");
        int maxandress=d.length;
        if(maxandress>=3) {
            holder.diadiem.setText(d[maxandress - 2]);
        }else{
            holder.diadiem.setText(job.getDiadiem());
        }
        if(job.getUrl()== null || job.getUrl().equals(""))
        {
         //  holder.imglogo.setImageResource(R.drawable.logo2);
        }else {
            Glide.with(mContext).load(job.getUrl())
                    .override(150, 150)
                    .crossFade()
                    //  .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.imglogo);
        }
        holder.mucluong.setText(arr[Integer.parseInt(job.getLuong())]);
        holder.linnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextinfomation(job);
            }
        });
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
    @Override
    public int getItemCount() {
        return myArray.size();
    }

}
