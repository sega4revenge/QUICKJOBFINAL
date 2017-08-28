package com.quickjob.quickjobFinal.quickjobHire.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobHire.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobHire.config.AppController;
import com.quickjob.quickjobFinal.quickjobHire.other.CircleTransform;
import com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailProfileActivity extends AppCompatActivity {

    private TextView txtslogan,txteducation, txtname,txtwant,txtgioitinh,txttuoi,txtmail,txtsdt,txtkn,txtquequan,txtdiachi,txtkynang,txtluong,txthocvan,txtngonngu,txtkhac;
    private ImageView logo;
    private Button btok,btno,btsave,btsaved,btcall,btsms,btsend;
    private String uidd,img_logo,name_com,statussave,iduser,id,name,gioitinh,tuoi,mail,sdt,kn,quequan,diachi,kynang,luong,hocvan,ngonngu,khac,img,mahs, macv,slogan,education,workplace;
    private Date today=new Date(System.currentTimeMillis());int key;
    private SimpleDateFormat timeFormat= new SimpleDateFormat("dd/MM/yyyy");
    private static final int REQUEST_CALL = 0;
    public static final String TAG = "DetailProfileActivity";
    SessionManager session;
    HashMap<String, String> user;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_profile_hire);
        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();
        uidd = user.get(SessionManager.KEY_ID);
        img_logo=  user.get(SessionManager.KEY_LOGO);
        name_com=  user.get(SessionManager.KEY_NAME);
        addView();
        getData();
        getDetailCV(uidd,mahs, AppConfig.URL_DETAIlCV);

//        addData();

        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromInternet(macv,mahs,"0","",AppConfig.URL_CHAPNHANHOS);
                // AsyncDataClass asyncRequestObject = new AsyncDataClass();
                //  asyncRequestObject.execute(AppConfig.URL_CHAPNHANHOS, mahs, macv, "0");
                //    Toast.makeText(DetailProfileActivity.this, R.string.st_hsDaChon, Toast.LENGTH_SHORT).show();

            }
        });
        btno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromInternet(macv,mahs,"1","",AppConfig.URL_CHAPNHANHOS);
            }
        });
        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = id;
                getDataFromInternet("",mahs,"",uid,AppConfig.URL_LUUHS);

            }
        });
        btsaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = id;
                getDataFromInternet("",mahs,"",uid,AppConfig.URL_UNSAVE);
            }
        });
        btcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(DetailProfileActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestCameraPermission();
                } else {
                    takePicture();
                }
            }
        });
        btsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id1 = id.substring(0,14);
                String id2 = iduser.substring(0,14);
                Intent i = new Intent(DetailProfileActivity.this,ChattingActivity.class);
                i.putExtra("idsend",id1);
                i.putExtra("idrec",id2);
                i.putExtra("name",name_com);
                i.putExtra("avata",img_logo);
                i.putExtra("logocompany",img);
                i.putExtra("namecompany",name);
                i.putExtra("iduser",iduser);
                startActivity(i);
            }
        });
        btsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TO = mail;
                SharedPreferences pref=getSharedPreferences("JobFindPref", MODE_PRIVATE);
                edit=pref.edit();
                HashMap<String, String> user = session.getUserDetails();
                String email = user.get(SessionManager.KEY_EMAIL);
                email=email;
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DetailProfileActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void getData() {
        Intent i = getIntent();
        key = i.getIntExtra("key", 1);
        if(key==0)
        {
            btok.setVisibility(View.GONE);
            btno.setVisibility(View.GONE);
            btsave.setVisibility(View.VISIBLE);
        }else if(key==2){
            LinearLayout ds = (LinearLayout) findViewById(R.id.line);
            ds.setVisibility(View.GONE);
        }
        iduser =  i.getStringExtra("iduser");
        mahs =i.getStringExtra("mahs");
    }

    private void takePicture() {
        String phone = sdt;
        String number = phone + "";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        try {
            startActivity(callIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), R.string.st_loiKXD, Toast.LENGTH_SHORT).show();
        }
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                REQUEST_CALL);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            Log.i(TAG, "Received response for Camera permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture();
            } else {
                //Permission not granted
                Toast.makeText(DetailProfileActivity.this, R.string.st_pemissonCamera, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void parseJsonFeedCV(String result) {
        if(result.equals("") || result == null){
            Toast.makeText(DetailProfileActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONArray detailcv = new JSONArray(result);
            for (int i = 0; i < detailcv.length(); i++) {
                JSONObject ob = detailcv.getJSONObject(i);
                txtname.setText("Hi! I am "+ob.getString("hoten")+".");
                String[] gender = getResources().getStringArray(R.array.sex);
                String[] luong2 = getResources().getStringArray(R.array.mucluong);
                String[] arreducation = getResources().getStringArray(R.array.speducation);
                if(Integer.parseInt(ob.getString("gioitinh2"))==1)
                {

                    txtgioitinh.setText(gender[0]);
                }else{
                    txtgioitinh.setText(gender[1]);
                }

                String s=timeFormat.format(today.getTime());
                String ss = ob.getString("ngaysinh")+"";
                String[] dd =ss.split("/");
                String[] d =s.split("/");
                int nam1 = Integer.parseInt(d[2]);
                int nam2 = Integer.parseInt(dd[0]);
                int tuois =nam1-nam2;
                txtslogan.setText(ob.getString("slogan"));
                txthocvan.setText(arreducation[Integer.parseInt(ob.getString("education"))]);
                txttuoi.setText(tuois+"");
                txtmail.setText(ob.getString("email")+"");
                txtsdt.setText(ob.getString("sdt")+"");
                txtkn.setText(ob.getString("namkn")+"");
                //  txtquequan.setText(quequan+"");
                txtdiachi.setText(ob.getString("diachi")+"");
                txtkynang.setText(ob.getString("kynang")+"");
                txtluong.setText(luong2[Integer.parseInt(ob.getString("mucluong"))]);
                txtngonngu.setText(ob.getString("ngoaingu")+"");
                txtwant.setText(ob.getString("diadiem")+"");
                img = ob.getString("img");
                name = ob.getString("hoten");
                sdt = ob.getString("sdt");
                mail = ob.getString("email");
                Glide.with(DetailProfileActivity.this).load(ob.getString("img"))
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(DetailProfileActivity.this))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(logo);
                if(ob.getString("status").equals("1")){
                //    btsave.setVisibility(View.GONE);
                //    btsaved.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void parseJsonFeed(String result) {
        if(result.equals("") || result == null){
            Toast.makeText(DetailProfileActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject resultObject = null;
        int returnedResult = 0;

        try {
            resultObject = new JSONObject(result);
            returnedResult = resultObject.getInt("success");
            if(returnedResult==1)
            {
                int type =resultObject.getInt("type");
                if(type==0){
                    new SweetAlertDialog(DetailProfileActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(DetailProfileActivity.this.getResources().getString(R.string.saved_profile))
                            .show();
              }else if(type==3){
                    btsave.setVisibility(View.VISIBLE);
                    btsaved.setVisibility(View.GONE);
                }
                else {
                    new SweetAlertDialog(DetailProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(DetailProfileActivity.this.getResources().getString(R.string.showmess_sus))
                            .show();
                    btsave.setVisibility(View.GONE);
                    btsaved.setVisibility(View.VISIBLE);
//                    Toast.makeText(DetailProfileActivity.this, getResources().getString(R.string.showmess_sus), Toast.LENGTH_SHORT).show();
                }
            }else if(returnedResult==2){
                Toast.makeText(DetailProfileActivity.this,getResources().getString(R.string.st_stt_dachapnhan), Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(DetailProfileActivity.this,getResources().getString(R.string.st_stt_datuchoi), Toast.LENGTH_SHORT).show();
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void getDetailCV(final String uidd, final String mahs,final String url) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/
        // Tag used to cancel the request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Log.d(TAG,"result:"+result);
                parseJsonFeedCV(result);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("mahs", mahs);
               params.put("uidd",uidd);

                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(this).requestQueue.add(strReq);
    }
    private void getDataFromInternet(final String macv, final String mahs,final String status ,final String uid,final String url) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/
        // Tag used to cancel the request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Log.d(TAG,"result:"+result);
                parseJsonFeed(result);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("mahs", mahs);
                if(key==0)
                {
                    params.put("id", uid);
                }else {
                    params.put("macv", macv);
                    params.put("status", status);
                }

                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(this).requestQueue.add(strReq);
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
                if(key==0)
                {
                    nameValuePairs.add(new BasicNameValuePair("id", params[2]));
                }else {
                    nameValuePairs.add(new BasicNameValuePair("macv", params[2]));
                    nameValuePairs.add(new BasicNameValuePair("status", params[3]));
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
                Toast.makeText(DetailProfileActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject resultObject = null;
            int returnedResult = 0;

            try {
                resultObject = new JSONObject(result);
                returnedResult = resultObject.getInt("success");
                if(returnedResult==1)
                {
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(DetailProfileActivity.this,getResources().getString(R.string.showmess_sus), Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);

    }


    private void addData() {
        txtname.setText("Hi! I am "+name+".");
        String[] gender = getResources().getStringArray(R.array.sex);
        String[] luong2 = getResources().getStringArray(R.array.mucluong);
        String[] arreducation = getResources().getStringArray(R.array.speducation);
        if(Integer.parseInt(gioitinh)==1)
        {

            txtgioitinh.setText(gender[1]);
        }else{
            txtgioitinh.setText(gender[0]);
        }

        String s=timeFormat.format(today.getTime());
        String ss = tuoi+"";
        String[] dd =ss.split("/");
        String[] d =s.split("/");
        int nam1 = Integer.parseInt(d[2]);
        int nam2 = Integer.parseInt(dd[0]);
        int tuois =nam1-nam2;
        txtslogan.setText(slogan);
        txthocvan.setText(arreducation[Integer.parseInt(education)]);
        txttuoi.setText(tuois+"");
        txtmail.setText(mail+"");
        txtsdt.setText(sdt+"");
        txtkn.setText(kn+"");
        //  txtquequan.setText(quequan+"");
        txtdiachi.setText(diachi+"");
        txtkynang.setText(kynang+"");
        txtluong.setText(luong2[Integer.parseInt(luong)]);
        txtngonngu.setText(ngonngu+"");
        txtwant.setText(workplace+"");
//        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        //   logo.setImageUrl(img, imageLoader);
        Glide.with(DetailProfileActivity.this).load(img)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(DetailProfileActivity.this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(logo);
        if(statussave.equals("1")){
            btsave.setVisibility(View.GONE);
            btsaved.setVisibility(View.VISIBLE);
//            btsave.setText(R.string.saved_profile);
//            btsave.setBackgroundColor(getResources().getColor(R.color.gray));
//            btsave.setFocusableInTouchMode(false);
        }
    }

    @Override
    protected void onDestroy() {
        Glide.clear(logo);
        super.onDestroy();
    }

    private void addView() {
        txtwant= (TextView) findViewById(R.id.txtwant);
        txtslogan = (TextView) findViewById(R.id.txtslogan);
        txtname = (TextView) findViewById(R.id.txtname);
        txtgioitinh = (TextView) findViewById(R.id.txtgioitinh);
        txttuoi = (TextView) findViewById(R.id.txtdotuoi);
        txtmail = (TextView) findViewById(R.id.txtemail);
        txtsdt = (TextView) findViewById(R.id.txtphone);
        txtkn = (TextView) findViewById(R.id.txtnamkn);
        //txtquequan = (TextView) findViewById(R.id.txtQueQuan);
        txtdiachi = (TextView) findViewById(R.id.txtaddress);
        txtkynang = (TextView) findViewById(R.id.txtkn);
        txtluong = (TextView) findViewById(R.id.txtluong);
        txtwant=(TextView) findViewById(R.id.txtwant);
        txthocvan = (TextView) findViewById(R.id.txthocvan);
        txtngonngu = (TextView) findViewById(R.id.txtnn);
        //   txtkhac = (TextView) findViewById(R.id.txtThongTinBoSung);
        logo = (ImageView) findViewById(R.id.imglogo);
        btcall = (Button) findViewById(R.id.btcall);
        btsms = (Button) findViewById(R.id.btsms);
        btsend = (Button) findViewById(R.id.btsendmail);
        btok = (Button) findViewById(R.id.btnok);
        btno = (Button) findViewById(R.id.btnno);
        btsave = (Button) findViewById(R.id.btnsave);
        btsaved = (Button) findViewById(R.id.btnsaved);

        txtslogan.setMovementMethod(new ScrollingMovementMethod());
        txtslogan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        session = new SessionManager(getApplicationContext());
        SharedPreferences pref = getSharedPreferences("JobFindPref", MODE_PRIVATE);
        edit = pref.edit();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        id = user.get(SessionManager.KEY_ID);
        img_logo=  user.get(SessionManager.KEY_LOGO);
        name_com=  user.get(SessionManager.KEY_NAME);
    }


}
