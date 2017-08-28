package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.adapter.ListJobCompanyAdapter;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;
import com.quickjob.quickjobFinal.quickjobFind.other.CircleTransform;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Information_Company extends AppCompatActivity {
   private ImageView logocompany;
   private TextView txtnameconpany,txtandress,txtcreer,txtinfor;
    private String namecompany="",andress="",career="",infor="",uid="",logo="",macv="";
//    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView image;
    AsyncDataClass asyncRequestObject;
    JSONArray mang;
    RecyclerView mRecyclerView;
    private List<CongViec> celebrities = new ArrayList<>();
    private ListJobCompanyAdapter adapter = null;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information__company);
        image = (ImageView) findViewById(R.id.image);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.dot_light_screen2));

        getData();
        addView();


    }

    private void getData() {
        Intent i = getIntent();
        namecompany =i.getStringExtra("nameconpany");
        andress=i.getStringExtra("andress");
        career=i.getStringExtra("career");
        infor=i.getStringExtra("infor");
        logo=i.getStringExtra("logo");
        macv=i.getStringExtra("macv");
        uid=MainActivity.uid;
        getSupportActionBar().setTitle(namecompany);
//        collapsingToolbarLayout.setTitle(namecompany);
    }
    private void addView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.resjob);
        mRecyclerView.setScrollContainer(false);
        adapter = new ListJobCompanyAdapter(Information_Company.this, celebrities, 3, "",3);
        mRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Information_Company.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        logocompany= (ImageView) findViewById(R.id.logocompany);
        txtnameconpany = (TextView) findViewById(R.id.namecompany);
        txtandress = (TextView) findViewById(R.id.andress);
        txtcreer = (TextView) findViewById(R.id.career);
        txtinfor = (TextView) findViewById(R.id.information);
        txtnameconpany.setText(namecompany);
        txtandress.setText(andress);
        txtcreer.setText(career);
        txtinfor.setText(infor);
        Glide.with(Information_Company.this).load(logo)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(Information_Company.this))
                .diskCacheStrategy( DiskCacheStrategy.ALL )
                .into(logocompany);
        Glide.with(Information_Company.this).load(logo)
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy( DiskCacheStrategy.ALL )
                .into(image);
      /*  mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(Information_Company.this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int i) {
                        Intent s = new Intent(Information_Company.this, JobDetailActivity.class);
                        s.putExtra("tencongty", celebrities.get(i).tecongty);
                        s.putExtra("tencongviec", celebrities.get(i).tencongviec);
                        s.putExtra("diadiem", celebrities.get(i).diadiem);
                        s.putExtra("mucluong", celebrities.get(i).luong);
                        s.putExtra("ngayup", celebrities.get(i).dateup);
                        s.putExtra("yeucaubangcap", celebrities.get(i).bangcap);
                        s.putExtra("dotuoi", celebrities.get(i).dotuoi);
                        s.putExtra("ngoaingu", celebrities.get(i).ngoaingu);
                        s.putExtra("gioitinh", celebrities.get(i).gioitinh);
                        s.putExtra("khac", celebrities.get(i).khac);
                        s.putExtra("motacv", celebrities.get(i).motacv);
                        s.putExtra("kn", celebrities.get(i).kn);
                        s.putExtra("macv", celebrities.get(i).macv);
                        s.putExtra("img", celebrities.get(i).url);
                        s.putExtra("sdt", celebrities.get(i).sdt);
                        s.putExtra("motact", celebrities.get(i).motact);
                        s.putExtra("type", 3);
                        startActivity(s);

                    }

                    @Override public boolean onLongItemClick(View view, int position) {
                        // do whatever
                        return false;
                    }
                })
        );*/
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
                nameValuePairs.add(new BasicNameValuePair("uid", params[2]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

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
            if (result.equals("") || result == null) {
                Toast.makeText(Information_Company.this,R.string.st_errServer, Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
                return;
            }
            try {
                mang = new JSONArray(result);
                if(mang.length()>0) {
                    for (int i = 0; i < mang.length(); i++) {
                        JSONObject ob = mang.getJSONObject(i);
                        CongViec cv = new CongViec();
                        cv.setMacv(ob.getString("macv"));
                        cv.setTencongviec(ob.getString("tencv"));
                        cv.setTecongty(ob.getString("tenct"));
                        cv.setDiachict(ob.getString("diadiem"));
                        cv.setQuymo(ob.getString("quymo"));
                        cv.setNganhNghe(ob.getString("nganhnghe"));
                        cv.setMotact(ob.getString("motact"));
                        cv.setLuong(ob.getString("mucluong"));
                        cv.setDiadiem(ob.getString("diadiem"));
                        cv.setDateup(ob.getString("hannophoso"));
                        cv.setMotacv(ob.getString("motacv"));
                        cv.setBangcap(ob.getString("bangcap"));
                        cv.setNgoaingu(ob.getString("ngoaingu"));
                        cv.setDotuoi(ob.getString("dotuoi"));
                        cv.setGioitinh(ob.getString("gioitinh"));
                        cv.setKhac(ob.getString("khac"));
                        cv.setKn(ob.getString("kynang"));
                        cv.setUrl(ob.getString("img"));
                        cv.setSdt(ob.getString("sdt"));
                        cv.setStatussave(ob.getString("statussave"));
                        cv.setTrangthai("3");
                        celebrities.add(cv);
                    }
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!= null){
            celebrities.clear();
            adapter.notifyDataSetChanged();
        }
        asyncRequestObject = new AsyncDataClass();
        asyncRequestObject.execute(AppConfig.URL_PRINTJOB,macv,uid);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if ( asyncRequestObject!= null) {
            if (!asyncRequestObject.isCancelled()) {
                asyncRequestObject.cancel(true);
            }
        }
    }
}
