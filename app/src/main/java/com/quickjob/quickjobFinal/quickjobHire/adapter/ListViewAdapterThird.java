package com.quickjob.quickjobFinal.quickjobHire.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.model.Profile;
import com.quickjob.quickjobFinal.quickjobHire.other.CircleTransform;

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
 * Created by VinhNguyen on 7/16/2016.
 */
public class ListViewAdapterThird extends ArrayAdapter<Profile>
{

    Context mcontext;
    int type;
    List<Profile> myArray;
    String[] salary;
    String update;
//    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ListViewAdapterThird(Context context, int layoutId, List<Profile> objects, int type){
        super(context, layoutId, objects);
        this.mcontext=context;
        this.myArray=objects;
        this.type=type;
        this.salary = mcontext.getResources().getStringArray(R.array.mucluong);
        update = mcontext.getResources().getString(R.string.st_upadate);

    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder=null;

        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.customitems_hire,null);
        Profile job = myArray.get(position);
        viewHolder = new ViewHolder();
        viewHolder.lin = (LinearLayout) rowView.findViewById(R.id.liner);
        viewHolder.tencongty = (TextView) rowView.findViewById(R.id.txt1);
        viewHolder.tencongviec = (TextView) rowView.findViewById(R.id.txt2);
        viewHolder.diadiem = (TextView) rowView.findViewById(R.id.txt3);
        viewHolder.mucluong=(TextView) rowView.findViewById(R.id.txt4);
        viewHolder.exe=(TextView) rowView.findViewById(R.id.txt7);
        viewHolder.ngayup=(TextView) rowView.findViewById(R.id.txt5);
        viewHolder.thumbNail = (ImageView) rowView.findViewById(R.id.thumbnail);
        viewHolder.ngayup=(TextView) rowView.findViewById(R.id.txt5);
        viewHolder.tencongty.setText(job.getTencv());
        viewHolder.tencongviec.setText(job.getTen());
        viewHolder.diadiem.setText(salary[Integer.parseInt(job.getMucluong())]);
        viewHolder.mucluong.setText(job.getDiadiem());
        viewHolder.ngayup.setText("( "+update+": "+job.getNgaydang()+" )");
        viewHolder.exe.setText(job.getNamkn());
        Glide.with(mcontext).load(job.getImg())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(mcontext))
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .skipMemoryCache( true )
                .into(viewHolder.thumbNail);
            /*viewHolder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mcontext, DetailProfileActivity.class);
                i.putExtra("ten", myArray.get(position).ten);
                i.putExtra("gioitinh", myArray.get(position).gioitinh);
                i.putExtra("ngaysinh", myArray.get(position).ngaysinh);
                i.putExtra("email", myArray.get(position).email);
                i.putExtra("sdt", myArray.get(position).sdt);
                i.putExtra("kinhnghiem", myArray.get(position).namkn);
                i.putExtra("tencongty", myArray.get(position).tencongty);
                i.putExtra("chucdanh", myArray.get(position).chucdanh);
                i.putExtra("motacv", myArray.get(position).motacv);
                i.putExtra("quequan", myArray.get(position).quequan);
                i.putExtra("diachi", myArray.get(position).diachi);
                i.putExtra("mucluong", myArray.get(position).mucluong);
                i.putExtra("kynang", myArray.get(position).kynang);
                i.putExtra("ngoaingu", myArray.get(position).ngoaingu);
                i.putExtra("img", myArray.get(position).img);
                i.putExtra("mahs", myArray.get(position).id);
                if(type==0)
                { i.putExtra("key", 1);
                }
                else if(type==1){
                    i.putExtra("key", 0);
                }else{
                    i.putExtra("key", 2);
                }

                mcontext.startActivity(i);

            }
        }); */


        return rowView;
    }
    static class ViewHolder{
        TextView tencongty,tencongviec,diadiem,mucluong,ngayup,exe;
        LinearLayout lin;
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

              nameValuePairs.add(new BasicNameValuePair("mahs", params[1]));
              nameValuePairs.add(new BasicNameValuePair("id", params[2]));

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