package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.other.CircleTransform;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;

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
import java.util.HashMap;
import java.util.List;

public class InfoActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvAge, tvPhone, tvLocation;
    private ImageView imgProf;
    private Toolbar toolbar;
    private SessionManager session;
    AsyncDataClass asyncRequestObject;
    private String emailpref, namepref, logo="",uid,ten="",gt="",ns="",em="",qq="",dc="",p="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        session = new SessionManager(getApplicationContext());

        uid=session.getId();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvLocation = (TextView) findViewById(R.id.tvLocation);

        tvName.setText("");
        tvEmail.setText("");
        tvAge.setText("");
        tvPhone.setText("");
        tvLocation.setText("");

        imgProf = (ImageView) findViewById(R.id.img_profile);



    }

    @Override
    public void onResume() {
         asyncRequestObject = new AsyncDataClass();
        asyncRequestObject.execute(AppConfig.URL_INFORMATION,uid);
        super.onResume();
    }

    private void loadData() {


        tvName.setText(ten);
        tvEmail.setText(em);
        if(logo == null || logo.equals("")) {
            Glide.with(this).load(R.drawable.user_icon)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy( DiskCacheStrategy.NONE )
                    .skipMemoryCache( true )
                    .into(imgProf);
        }else {
            Glide.with(this).load(logo)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy( DiskCacheStrategy.NONE )
                    .skipMemoryCache( true )
                    .into(imgProf);
        }


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
                nameValuePairs.add(new BasicNameValuePair("id", params[1]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
                System.out.println("Returned3 Json object " + jsonResult.toString());

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
            System.out.println("Resulted4 Value: " + result);
            if (result.equals("") || result == null) {
                Toast.makeText(InfoActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONArray mang = new JSONArray(result);
                if(mang.length()>0) {
                    JSONObject obs = mang.getJSONObject(0);
                     ten = obs.getString("hoten");
                     gt = obs.getString("gioitinh2");
                     ns = obs.getString("ngaysinh");
                     em = obs.getString("email");
                     dc = obs.getString("quequan");
                     dc = obs.getString("diachi");
                     p = obs.getString("sdt");
                     logo = obs.getString("img");
                    tvName.setText(ten);
                    tvEmail.setText(em);
                    tvAge.setText(ns);
                    tvPhone.setText(p);
                    tvLocation.setText(dc);
                    if(logo.equals("")) {
                        Glide.with(InfoActivity.this).load(R.drawable.user_icon)
                                .crossFade()
                                .thumbnail(0.5f)
                                .bitmapTransform(new CircleTransform(InfoActivity.this))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgProf);
                    }else {
                        Glide.with(InfoActivity.this).load(logo)
                                .crossFade()
                                .thumbnail(0.5f)
                                .bitmapTransform(new CircleTransform(InfoActivity.this))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgProf);
                    }
                }else{
                    HashMap<String, String> user = session.getUserDetails();
                    em = user.get(SessionManager.KEY_EMAIL);
                    ten= user.get(SessionManager.KEY_NAME);
                    System.out.println(ten);
                    logo = session.getProfilePic();
                    loadData();
                }
            } catch (JSONException e) {
                e.printStackTrace();
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

    @Override
    public void onBackPressed() {
//        Intent s= new Intent(getApplicationContext(), SettingActivity.class);
//        s.putExtra("logo",logo);
//        startActivity(s);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);
        } if (id == R.id.edit){
            Intent s =new Intent(getApplicationContext(), EditInfoActivity.class);

            s.putExtra("ten",ten);
            s.putExtra("mail",em);
            s.putExtra("ngaysinh",ns);
            s.putExtra("phone",p);
            s.putExtra("quequan",dc);
            s.putExtra("logo",logo);
            startActivity(s);
            overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);

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
