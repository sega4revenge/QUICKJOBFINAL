package com.quickjob.quickjobFinal.quickjobFind.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.activity.MoreNew_Activity;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VinhNguyen on 2/24/2017.
 */

public class categoryadapter extends  RecyclerView.Adapter<categoryadapter.MyViewHolder> {

private ArrayList<Integer>  joblist;
        Activity mContext;String[] arr;
    private List<CongViec> myArray;
public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView  category;


    public MyViewHolder(View view) {
        super(view);
        category = (TextView) view.findViewById(R.id.txtcategory);
    }
}

    public categoryadapter(ArrayList<Integer> joblist, Activity context,List<CongViec> object){
        this.joblist = joblist;
        this.mContext=context;
        this.myArray=object;
        this.arr=mContext.getResources().getStringArray(R.array.nganhNghe);
    }

    @Override
    public categoryadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customtextview_category, parent, false);

        return new categoryadapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(categoryadapter.MyViewHolder holder, final int position) {

        holder.category.setText(arr[joblist.get(position)]+"");
        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                     List<CongViec> category = getObject(myArray,joblist.get(position));
                        Intent s = new Intent(mContext, MoreNew_Activity.class);
                        s.putExtra("object", (Serializable) category);
                        mContext.startActivity(s);
            }
        });
    }

    private  List<CongViec> getObject(List<CongViec> object,int pos){
        List<CongViec> ob =new ArrayList<>();
        for(int i=0;i<object.size();i++)
        {
            if(Integer.parseInt(object.get(i).getNganhNghe())==pos)
            {
                ob.add(object.get(i));
            }
        }
        return ob;
    }
    @Override
    public int getItemCount() {
        return joblist.size();
    }
}
