package com.quickjob.quickjobFinal.quickjobHire.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ProfileDetailActivity extends AppCompatActivity {
    ImageView imgAvata;
    private TextView txtslogan,txteducation, txtname,txtwant,txtgioitinh,txttuoi,txtmail,txtsdt,txtkn
            ,txtquequan, txtdiachi,txtkynang,txtluong,txthocvan,txtngonngu,txtkhac;
    private String uidd,img_logo,name_com,statussave,iduser,id,name,gioitinh,tuoi,mail,sdt,kn,quequan,diachi
            ,kynang,luong,hocvan,ngonngu,khac,img,mahs, macv,slogan,education,workplace;
    private Date today=new Date(System.currentTimeMillis());int key;
    private SimpleDateFormat timeFormat= new SimpleDateFormat("dd/MM/yyyy");
    private static final int REQUEST_CALL = 0;
    private String TAG = ProfileDetailActivity.class.getSimpleName().toString();
    private Button btok,btno,btsend;
    SessionManager session;Toolbar toolbar;
    HashMap<String, String> user;
    SharedPreferences.Editor edit;int statusSave;
    FloatingActionMenu fabMenu;Menu mMenu;
    private com.github.clans.fab.FloatingActionButton fabCall, fabSendMail,fabChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail_hire);
        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();
        uidd = user.get(SessionManager.KEY_ID);
        img_logo=  user.get(SessionManager.KEY_LOGO);
        name_com=  user.get(SessionManager.KEY_NAME);
        setView();
        getData();
        event();
    }

    @Override
    protected void onResume() {
        getDetailCV(uidd,mahs, AppConfig.URL_DETAIlCV);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu=menu;
        getMenuInflater().inflate(R.menu.main2,menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }else if (item.getItemId() == R.id.menuSave){
            String uid = uidd;
            if(statusSave==1) {
                getDataFromInternet("", mahs, "", uid, AppConfig.URL_UNSAVE);
            }else{
                getDataFromInternet("", mahs, "", uid, AppConfig.URL_LUUHS);

            }
        }

        return super.onOptionsItemSelected(item);

    }

    private void event() {
        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id1 = uidd.substring(0,14);
                String id2 = iduser.substring(0,14);
                Intent i = new Intent(ProfileDetailActivity.this,ChattingActivity.class);
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
        fabSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    Toast.makeText(ProfileDetailActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ProfileDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestCameraPermission();
                } else {
                    takeCall();
                }
            }
        });
        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromInternet(macv,mahs,"0","", AppConfig.URL_CHAPNHANHOS);

            }
        });
        btno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromInternet(macv,mahs,"1","",AppConfig.URL_CHAPNHANHOS);
            }
        });
    }

    private void setView() {
        toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        fabMenu.setClosedOnTouchOutside(true);
        imgAvata = (ImageView) findViewById(R.id.imglogo);
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
        btsend = (Button) findViewById(R.id.btsendmail);
        btok = (Button) findViewById(R.id.btnok);
        btno = (Button) findViewById(R.id.btnno);
        fabCall = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabCall);
        fabSendMail = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabSendMail);
        fabChat = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabChat);
        AppBarLayout htab_appbar = (AppBarLayout) findViewById(R.id.mainappbar);
        htab_appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
                    getSupportActionBar().setTitle(name);
                } else
                {
                    getSupportActionBar().setTitle("");
                }
            }
        });

    }
    private void getData() {
        Intent i = getIntent();
        key = i.getIntExtra("key", 1);
        if(key==0)
        {
            btok.setVisibility(View.GONE);
            btno.setVisibility(View.GONE);
        }else if(key==2){
            LinearLayout ds = (LinearLayout) findViewById(R.id.line);
            ds.setVisibility(View.GONE);
        }
        iduser =  i.getStringExtra("iduser");
        mahs =i.getStringExtra("mahs");
    }
    private void takeCall() {
        String phone = sdt;
        String number = phone + "";
        // Toast.makeText(ProfileDetailActivity.this, number, Toast.LENGTH_SHORT).show();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        try {
            startActivity(callIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), R.string.st_loiKXD, Toast.LENGTH_SHORT).show();
        }
    }
    private void requestCameraPermission() {
        // Camera permission has not been granted yet. Request it directly.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                REQUEST_CALL);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i(TAG, "Received response for Camera permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Camera permission has been granted, preview can be displayed

                takeCall();

            } else {
                //Permission not granted
                Toast.makeText(ProfileDetailActivity.this, R.string.st_pemissonCamera, Toast.LENGTH_SHORT).show();
            }

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
    private void parseJsonFeedCV(String result) {
        if(result.equals("") || result == null){
            Toast.makeText(ProfileDetailActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONArray detailcv = new JSONArray(result);
            for (int i = 0; i < detailcv.length(); i++) {
                JSONObject ob = detailcv.getJSONObject(i);
                txtname.setText(ob.getString("hoten"));
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
                Glide.with(ProfileDetailActivity.this).load(ob.getString("img"))
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(ProfileDetailActivity.this))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(imgAvata);
                statusSave=Integer.parseInt(ob.getString("status"));
                if(statusSave==1){
                    mMenu.getItem(0).setTitle(getResources().getString(R.string.saved_profile));
                }else{
                    mMenu.getItem(0).setTitle(getResources().getString(R.string.action_favorite));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        Glide.clear(imgAvata);
        super.onDestroy();
    }

    private void parseJsonFeed(String result) {
        if(result.equals("") || result == null){
            Toast.makeText(ProfileDetailActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
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
                    new SweetAlertDialog(ProfileDetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(ProfileDetailActivity.this.getResources().getString(R.string.saved_profile))
                            .show();
                }else if(type==3){
                    mMenu.getItem(0).setTitle(getResources().getString(R.string.action_favorite));
                    statusSave=0;
                }
                else {
                    new SweetAlertDialog(ProfileDetailActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(ProfileDetailActivity.this.getResources().getString(R.string.showmess_sus))
                            .show();
                    mMenu.getItem(0).setTitle(getResources().getString(R.string.saved_profile));
                    statusSave=1;
               }
            }else if(returnedResult==2){
                Toast.makeText(ProfileDetailActivity.this,getResources().getString(R.string.st_stt_dachapnhan), Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(ProfileDetailActivity.this,getResources().getString(R.string.st_stt_datuchoi), Toast.LENGTH_SHORT).show();
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
}
