package com.quickjob.quickjobFinal.quickjobFind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.model.Profile;
import com.quickjob.quickjobFinal.quickjobFind.other.CircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VinhNguyen on 7/8/2016.
 */
public class ListViewAdapter extends ArrayAdapter<Profile>
{
    Context mcontext;
    int status;
   // String macv = JobDetailActivity.macv;
//    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    List<Profile> celebrities = new ArrayList<>();
    List<Profile> myArray=new ArrayList<>();
    String[] arrnganh,arrhv,arrsalary,arrsex;
    public ListViewAdapter(Context context, int layoutId, List<Profile> objects, int status){
        super(context, layoutId, objects);
        this.mcontext=context;
        this.myArray=new ArrayList<Profile>(objects);
        this.status=status;
        arrhv= mcontext.getResources().getStringArray(R.array.spHocVan);
        arrsalary= mcontext.getResources().getStringArray(R.array.mucluong);
        arrsex= mcontext.getResources().getStringArray(R.array.sex);
        arrnganh= mcontext.getResources().getStringArray(R.array.nganhNghe);


    }



    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder=null;

        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.customitems,null);
        viewHolder = new ViewHolder();
        viewHolder.txt1 = (TextView) rowView.findViewById(R.id.txt1);
        viewHolder.txt2 = (TextView) rowView.findViewById(R.id.txt2);
        viewHolder.numprofile = (TextView) rowView.findViewById(R.id.numprofile);
        viewHolder.txt3 = (TextView) rowView.findViewById(R.id.txt3);
        viewHolder.txt4 = (TextView) rowView.findViewById(R.id.txt4);
        viewHolder.txt5 = (TextView) rowView.findViewById(R.id.txt5);
        viewHolder.lin = (LinearLayout) rowView.findViewById(R.id.liner);
        viewHolder.thumbNail = (ImageView) rowView.findViewById(R.id.thumbnail);

        Glide.with(mcontext).load(myArray.get(position).img)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(mcontext))
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .skipMemoryCache( true )
                .into(viewHolder.thumbNail);

        viewHolder.txt1.setText(arrnganh[Integer.parseInt(myArray.get(position).nganhnghe)]);
        viewHolder.txt2.setText(myArray.get(position).vitri);
        viewHolder.txt3.setText(arrsalary[Integer.parseInt(myArray.get(position).mucluong)]);
        viewHolder.numprofile.setText("Profile: "+(position+1));
        viewHolder.txt4.setText(myArray.get(position).diadiem);
        viewHolder.txt5.setText(mcontext.getResources().getString(R.string.ngaydang)+": "+myArray.get(position).ngaydang);
//        viewHolder.lin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(status==0) {
//                    Intent i = new Intent(mcontext, EditProfileActivity.class);
//                    i.putExtra("key", 1);
//                    i.putExtra("mahs", myArray.get(position).id);
//                    i.putExtra("nn", myArray.get(position).nganhnghe);
//                    i.putExtra("vitri", myArray.get(position).vitri);
//                    i.putExtra("mucluong", myArray.get(position).mucluong);
//                    i.putExtra("diadiem", myArray.get(position).diadiem);
//                    i.putExtra("ngaydang", myArray.get(position).ngaydang);
//                    i.putExtra("ten", myArray.get(position).ten);
//                    i.putExtra("quequan", myArray.get(position).quequan);
//                    i.putExtra("sdt", myArray.get(position).sdt);
//                    i.putExtra("gioitinh", myArray.get(position).gioitinh);
//                    i.putExtra("email", myArray.get(position).email);
//                    i.putExtra("diachi", myArray.get(position).diachi);
//                    i.putExtra("ngaysinh", myArray.get(position).ngaysinh);
//                    i.putExtra("tentruong", myArray.get(position).tentruong);
//                    i.putExtra("chuyennganh", myArray.get(position).chuyennganh);
//                    i.putExtra("xeploai", myArray.get(position).xeploai);
//                    i.putExtra("thanhtuu", myArray.get(position).thanhtuu);
//                    i.putExtra("namkn", myArray.get(position).namkn);
//                    i.putExtra("tencongty", myArray.get(position).tencongty);
//                    i.putExtra("chucdanh", myArray.get(position).chucdanh);
//                    i.putExtra("mota", myArray.get(position).motacv);
//                    i.putExtra("ngoaingu", myArray.get(position).ngoaingu);
//                    i.putExtra("kynang", myArray.get(position).kynang);
//                    i.putExtra("tencv", myArray.get(position).tencv);
//                    i.putExtra("uid", myArray.get(position).uid);
//                    i.putExtra("logo", myArray.get(position).img);
//                    mcontext.startActivity(i);
//
//                }else{
//                    AsyncDataClass asyncRequestObject = new AsyncDataClass();
//                    asyncRequestObject.execute(AppConfig.URL_NOPHS, myArray.get(position).id, macv);
//                }
//
//            }
//        });
        return rowView;
    }


    static class ViewHolder{
        TextView txt1,txt2,txt3,txt4,txt5,numprofile;
        LinearLayout lin; ImageView thumbNail;
    }




}