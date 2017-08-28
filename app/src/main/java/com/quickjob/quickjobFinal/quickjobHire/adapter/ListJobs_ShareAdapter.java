package com.quickjob.quickjobFinal.quickjobHire.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.model.CongViec;
import com.quickjob.quickjobFinal.quickjobHire.other.CircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VinhNguyen on 5/14/2017.
 */

public class ListJobs_ShareAdapter  extends ArrayAdapter<CongViec> {
    private Context mcontext;
    private List<CongViec> myArray = new ArrayList<>();

    public ListJobs_ShareAdapter(Activity context, int layoutId, List<CongViec> objects) {
        super(context, layoutId, objects);
        this.mcontext = context;
        this.myArray = objects;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder = null;
        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.custom_listjob_share_hire, null);
        viewHolder = new ViewHolder();
        viewHolder.tencongviec =(TextView) rowView.findViewById(R.id.nameuser);
        viewHolder.time =(TextView) rowView.findViewById(R.id.time);
        viewHolder.thumbNail =(ImageView) rowView.findViewById(R.id.avata);
        viewHolder.tencongviec.setText(myArray.get(position).getTencongviec());
        viewHolder.time.setText(myArray.get(position).getDateup());
        Glide.with(mcontext.getApplicationContext()).load(myArray.get(position).getUrl())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(mcontext))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(viewHolder.thumbNail);
        return rowView;
    }


    static class ViewHolder {
        TextView time, tencongviec;
        ImageView thumbNail;
    }
}