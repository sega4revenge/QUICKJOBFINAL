package com.quickjob.quickjobFinal.quickjobHire.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.activity.RecruitmentListActivity;
import com.quickjob.quickjobFinal.quickjobHire.model.CongViec;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by VinhNguyen on 7/13/2016.
 */
public class ListViewAdapterSecond  extends RecyclerView.Adapter<ListViewAdapterSecond.MyViewHolder> {

    Date today=new Date(System.currentTimeMillis());
    SimpleDateFormat timeFormat= new SimpleDateFormat("dd/MM/yyyy");
    Activity mcontext;
    int type;
    List<CongViec> myArray;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt1,txt2,txt3,txt4,txt5;
        LinearLayout lin;
        public MyViewHolder(View view) {
            super(view);
            txt1 = (TextView) view.findViewById(R.id.txt1);
            txt5 = (TextView) view.findViewById(R.id.txt5);
            lin = (LinearLayout) view.findViewById(R.id.liner);
        }
    }

    public ListViewAdapterSecond(Activity context, List<CongViec> objects ) {
        this.mcontext = context;
        this.myArray = objects;
    }
    @Override
    public ListViewAdapterSecond.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlistview_job_hire, parent, false);
        return new ListViewAdapterSecond.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewAdapterSecond.MyViewHolder holder, final int position) {
        holder.txt1.setText(myArray.get(position).getTencongviec());
        holder.txt5.setText(myArray.get(position).getSohoso()+"");
        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mcontext,RecruitmentListActivity.class);
                i.putExtra("macv",myArray.get(position).macv);
                i.putExtra("tencv", myArray.get(position).tencongviec);
                mcontext.startActivity(i);
            }
        });

    }
    @Override
    public int getItemCount() {
        return myArray.size();
    }
}

/*extends ArrayAdapter<CongViec>
{  FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Activity mcontext;
    List<CongViec> myArray;
    public ListViewAdapterSecond(Activity context, int layoutId, List<CongViec> objects){
        super(context, layoutId, objects);
        this.mcontext=context;
        this.myArray=objects;

    }



    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder=null;

        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.itemlistview_job,null);
        viewHolder = new ViewHolder();

        viewHolder.txt1 = (TextView) rowView.findViewById(R.id.txt1);
        viewHolder.txt5 = (TextView) rowView.findViewById(R.id.txt5);
        viewHolder.lin = (RelativeLayout) rowView.findViewById(R.id.liner);
        viewHolder.txt1.setText(myArray.get(position).getTencongviec());
        viewHolder.txt5.setText(myArray.get(position).getSohoso()+"");
        viewHolder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mcontext,RecruitmentListActivity.class);
                i.putExtra("macv",myArray.get(position).macv);
                i.putExtra("tencv", myArray.get(position).tencongviec);
                mcontext.startActivity(i);
            }
        });
        return rowView;
    }
    static class ViewHolder{
        TextView txt1,txt2,txt3,txt4,txt5;
        RelativeLayout lin;
    }
  /*  public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        myArray.clear();
        if (charText.length() == 0) {
            myArray.addAll(stock);
        } else {
            for (secondactivity cs : stock) {
                if (cs.getName().contains(charText)) {
                    myArray.add(cs);
                }
            }
        }
        notifyDataSetChanged(); */

