package com.quickjob.quickjobFinal.quickjobFind.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SONTHO on 15/08/2016.
 */
public class CongViecAdapter extends ArrayAdapter<CongViec>
{
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Activity mcontext;
    List<CongViec> myArray;
    String uid;
    int type;
    String serverUrl = "";
    String sa="";
    int mtype;
//    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String[] arr;
    public CongViecAdapter(Activity context, int layoutId, List<CongViec> objects, int type, String uid, int mtype){
        super(context, layoutId, objects);
        this.mcontext=context;
        this.myArray=objects;
        this.uid=uid;
        this.type=type;
        this.mtype=mtype;
        this.arr=mcontext.getResources().getStringArray(R.array.mucluong);
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder=null;

        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.customitem,null);
        viewHolder = new ViewHolder();
        viewHolder.lin = (RelativeLayout) rowView.findViewById(R.id.linit);
        viewHolder.tencongty = (TextView) rowView.findViewById(R.id.tenct);
        viewHolder.trangthai = (TextView) rowView.findViewById(R.id.trangthai);
        viewHolder.tencongviec = (TextView) rowView.findViewById(R.id.tencongviec);
        viewHolder.diadiem = (TextView) rowView.findViewById(R.id.diachi);
        viewHolder.mucluong=(TextView) rowView.findViewById(R.id.luong);
        viewHolder.ngayup=(TextView) rowView.findViewById(R.id.dateup1);
     //   viewHolder.thumbNail = (ImageView) rowView.findViewById(R.id.thumbnail);
        viewHolder.tencongty.setText(myArray.get(position).tecongty+"");
        int status = Integer.parseInt(myArray.get(position).trangthai);
        if(status==0)
        {
            viewHolder.trangthai.setText(R.string.st_stt_dangcho);
        }else if(status==1){
            viewHolder.trangthai.setText(R.string.st_stt_dachapnhan);
        }else if(status==2){
            viewHolder.trangthai.setText(R.string.st_stt_datuchoi);
        }else if(status==4){
            viewHolder.trangthai.setText(myArray.get(position).getDistance());
        }
        viewHolder.tencongty.setText(myArray.get(position).tecongty);
        viewHolder.tencongviec.setText(myArray.get(position).tencongviec);
        viewHolder.diadiem.setText(myArray.get(position).diadiem);
        viewHolder.mucluong.setText(arr[Integer.parseInt(myArray.get(position).luong)]);
        viewHolder.ngayup.setText(myArray.get(position).dateup);
//        Glide.with(mcontext).load(myArray.get(position).url)
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(mcontext))
//                .diskCacheStrategy( DiskCacheStrategy.ALL )
//             //  .skipMemoryCache( true )
//                .into(viewHolder.thumbNail);
//        viewHolder.lin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(type<3) {
//                    Intent i = new Intent(mcontext, JobDetailActivity.class);
//                    i.putExtra("tencongty", myArray.get(position).tecongty);
//                    i.putExtra("tencongviec", myArray.get(position).tencongviec);
//                    i.putExtra("diadiem", myArray.get(position).diadiem);
//                    i.putExtra("mucluong", myArray.get(position).luong);
//                    i.putExtra("ngayup", myArray.get(position).dateup);
//                    i.putExtra("yeucaubangcap", myArray.get(position).bangcap);
//                    i.putExtra("dotuoi", myArray.get(position).dotuoi);
//                    i.putExtra("ngoaingu", myArray.get(position).ngoaingu);
//                    i.putExtra("gioitinh", myArray.get(position).gioitinh);
//                    i.putExtra("khac", myArray.get(position).khac);
//                    i.putExtra("motacv", myArray.get(position).motacv);
//                    i.putExtra("kn", myArray.get(position).kn);
//                    i.putExtra("macv", myArray.get(position).macv);
//                    i.putExtra("img", myArray.get(position).url);
//                    i.putExtra("sdt", myArray.get(position).sdt);
//                    i.putExtra("quymo", myArray.get(position).quymo);
//                    i.putExtra("nganhnghe", myArray.get(position).nganhNghe);
//                    i.putExtra("diachi", myArray.get(position).diachict);
//                    i.putExtra("motact", myArray.get(position).motact);
//
//                    if(mtype==0)
//                    {
//                        i.putExtra("type",0);
//                    }else{
//                        i.putExtra("type",1);
//                    }
//                    mcontext.startActivity(i);
//                }
//            }
//        });


        return rowView;
    }
    static class ViewHolder{
        TextView tencongty,tencongviec,diadiem,mucluong,ngayup,trangthai;
        RelativeLayout lin;
        ImageView thumbNail;
    }

    private class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(params[0]);

            String jsonResult = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("macv", params[1]));
                nameValuePairs.add(new BasicNameValuePair("id", params[2]));
                if(type==1)
                {
                    nameValuePairs.add(new BasicNameValuePair("idungtuyen", params[3]));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
                System.out.println("Returned Json object " + jsonResult.toString());

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("Resulted Value: " + result);
            if(result.equals("") || result == null){
                Toast.makeText(mcontext, R.string.st_errServer, Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getString("success").equals("1"))
                {
                    Toast.makeText(mcontext,resultObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mcontext,resultObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                }
//                if(type==0)
//                {
//                    fragmentManager = mcontext.getFragmentManager();
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    JopSave main = new JopSave();
//                    fragmentTransaction.replace(R.id.container, main);
//                    fragmentTransaction.commit();
//                    //    Toast.makeText(mcontext,"reload",Toast.LENGTH_SHORT).show();
//                }else
//                {
//                    fragmentManager = mcontext.getFragmentManager();
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    SearchFragment main = new SearchFragment();
//                    fragmentTransaction.replace(R.id.container, main);
//                    fragmentTransaction.commit();
//                    //   Toast.makeText(mcontext,"reload",Toast.LENGTH_SHORT).show();
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            while ((rLine = br.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answer;
    }
}
