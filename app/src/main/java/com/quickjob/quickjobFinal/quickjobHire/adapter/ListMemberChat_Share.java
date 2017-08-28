package com.quickjob.quickjobFinal.quickjobHire.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.activity.MainActivity;
import com.quickjob.quickjobFinal.quickjobHire.model.CompanyInformation;
import com.quickjob.quickjobFinal.quickjobHire.other.CircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VinhNguyen on 5/14/2017.
 */

public class ListMemberChat_Share  extends ArrayAdapter<CompanyInformation> {
    private Context mcontext;
    private List<CompanyInformation> myArray = new ArrayList<>();

    public ListMemberChat_Share(Context context, int layoutId, List<CompanyInformation> objects) {
        super(context, layoutId, objects);
        this.mcontext = context;
        this.myArray = objects;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder = null;
        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.custom_select_member_chat_share_hire, null);
        viewHolder = new ViewHolder();
        viewHolder.tencongviec =(TextView) rowView.findViewById(R.id.nameuser);
        viewHolder.checkbox =(CheckBox) rowView.findViewById(R.id.checkbox);
        viewHolder.thumbNail =(ImageView) rowView.findViewById(R.id.avata);
        viewHolder.lin = (LinearLayout) rowView.findViewById(R.id.usersend);
        viewHolder.tencongviec.setText(myArray.get(position).getName());
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalViewHolder.checkbox.isChecked()){
                    finalViewHolder.checkbox.setChecked(false);
                    ((MainActivity) mcontext).Filter(myArray.get(position),0);
                }else{
                    finalViewHolder.checkbox.setChecked(true);
                    ((MainActivity) mcontext).Filter(myArray.get(position),1);
                }
            }
        });
        Glide.with(mcontext.getApplicationContext()).load(myArray.get(position).getAvata())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(mcontext))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(viewHolder.thumbNail);
        return rowView;
    }


    static class ViewHolder {
        TextView  tencongviec;CheckBox checkbox;LinearLayout lin;
        ImageView thumbNail;
    }
}