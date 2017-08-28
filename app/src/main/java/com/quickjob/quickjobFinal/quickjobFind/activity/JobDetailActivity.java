package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobFind.adapter.ListViewAdapter;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.config.AppController;
import com.quickjob.quickjobFinal.quickjobFind.fragment.CompanyDetailFragment;
import com.quickjob.quickjobFinal.quickjobFind.fragment.JobAboutFragment;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;
import com.quickjob.quickjobFinal.quickjobFind.model.Profile;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;
import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobDetailActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private NetworkImageView logo;
    private List<Profile> celebrities = new ArrayList<>();
    private List<CongViec> detailjob = new ArrayList<>();
    private static final int REQUEST_CALL = 0;
    private ListView lv;
    private ListViewAdapter adapter;
    public static final String TAG =JobDetailActivity.class.getSimpleName();
    public String macv="";
    int status = 0,luong=0,hv=0,gt=0;
    private String avata,name,namecom,email,id="", sdt, tencv, tenct, diadiem, mucluong, ngayup, yeucaubangcap, dotuoi, ngoaingu, gioitinh, khac, motacv, kn, img,quymo,jobcompany,location,detail,matd;
    private CollapsingToolbarLayout collapsingToolbar;
    int type;
    private int mPreviousVisibleItem;
    private FloatingActionButton fabCall, fabSave,fablocation;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    boolean statussave=false;
    AlertDialog dialog;String[] arrsalary,arrbc;
    Boolean getstt=false;
    private Menu menu;ImageView logocompany;FloatingActionButton fabMenu;
    double lat,lng;boolean isAttached;Drawable upArrow;
    JSONArray arritem = new JSONArray();SessionManager session;String[] parts=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.detail_job_layout);
        status = 1;
        actionGetIntent();

        AppBarLayout htab_appbar = (AppBarLayout) findViewById(R.id.htab_appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fabMenu = (FloatingActionButton) findViewById(R.id.fabMenu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        upArrow = getResources().getDrawable(R.drawable.ic_arrow_white);

        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        htab_appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
                   toolbar.setBackgroundColor(getResources().getColor(R.color.actionbar));
                } else
                {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.blur));
                }
            }
        });
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String iduser = id.substring(0,14);
                String iduser2 = detailjob.get(0).email.substring(0,14);
                Intent i = new Intent(JobDetailActivity.this,ChattingActivity.class);
                i.putExtra("idsend",iduser);
                i.putExtra("idrec",iduser2);
                i.putExtra("idcompany",detailjob.get(0).email);
                i.putExtra("name",name);
                i.putExtra("logocompany",detailjob.get(0).url);
                i.putExtra("avata",avata);
                i.putExtra("namecompany",detailjob.get(0).tecongty);
                startActivity(i);
            }
        });

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached=false;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttached=true;
    }

    private void events() {
/*
        fabCall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                if (ActivityCompat.checkSelfPermission(JobDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestCameraPermission();
                } else {
                    takePicture();
                }
            }
        });
        fablocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+diadiem);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
   */

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new JobAboutFragment(), getResources().getString(R.string.st_about_job));
        adapter.addFragment(new CompanyDetailFragment(), getResources().getString(R.string.st_about_company));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void actionGetIntent() {
        arrsalary= getResources().getStringArray(R.array.mucluong);
        arrbc= getResources().getStringArray(R.array.speducation);
        session = new SessionManager(getApplicationContext());
        Intent i = getIntent();
        String productId="";
        Uri data = i.getData();
        if (data == null) {
            macv=i.getStringExtra("macv");
            HashMap<String, String> user = session.getUserDetails();
            id = user.get(SessionManager.KEY_ID);
            email = user.get(SessionManager.KEY_EMAIL);
            name = user.get(SessionManager.KEY_NAME);
            avata= user.get(SessionManager.KEY_LOGO);
            getDataJob(macv, id);
        }
        else {
                    if (session.isLoggedIn()) {
                        String jobid = data.getQueryParameter("jobid");
                        jobid=jobid.replace("/","");
                        HashMap<String, String> user = session.getUserDetails();
                        id = user.get(SessionManager.KEY_ID);
                        email = user.get(SessionManager.KEY_EMAIL);
                        name= user.get(SessionManager.KEY_NAME);
                        avata= user.get(SessionManager.KEY_LOGO);
                        try {
                            macv = jobid;
                        }catch (Exception  e)
                        {
                            Toast.makeText(this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
                        }
                         getDataJob(macv, id);
                    }
                    else {
                        startActivity(i);
                        finish();
                    }

           }
        }
    private void setDataJob(){
        logocompany = (ImageView) findViewById(R.id.logocompany);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        Intent i = getIntent();
        Bundle bundle =i.getExtras();
        bundle.putString("tencongviec",detailjob.get(0).tencongviec);
        bundle.putString("tecongty",detailjob.get(0).tecongty);
        bundle.putString("diadiem",detailjob.get(0).diadiem);
        bundle.putString("luong",detailjob.get(0).luong);
        bundle.putString("ngayup",detailjob.get(0).dateup);
        bundle.putString("bangcap",detailjob.get(0).bangcap);
        bundle.putString("dotuoi",detailjob.get(0).dotuoi);
        bundle.putString("ngoaingu",detailjob.get(0).ngoaingu);
        bundle.putString("gioitinh",detailjob.get(0).gioitinh);
        bundle.putString("khac",detailjob.get(0).khac);
        bundle.putString("motacv",detailjob.get(0).motacv);
        bundle.putString("kn",detailjob.get(0).kn);
        bundle.putString("sdt",detailjob.get(0).sdt);
        bundle.putString("macv",detailjob.get(0).macv);
        bundle.putString("quymo",detailjob.get(0).quymo);
        bundle.putString("jobcompany",detailjob.get(0).tecongty);
        bundle.putString("detail",detailjob.get(0).motact);
        bundle.putString("location",detailjob.get(0).diachict);
        bundle.putString("lat",detailjob.get(0).lat);
        bundle.putString("lng",detailjob.get(0).lng);
        bundle.putString("datecreate",detailjob.get(0).datecreate);
        bundle.putString("number",detailjob.get(0).soluong);
        bundle.putString("carrer",detailjob.get(0).nganhNghe);
        bundle.putString("phucloi",detailjob.get(0).phucloi);
        bundle.putString("email",detailjob.get(0).email);
        i.putExtra("data",bundle);


        if(!isAttached) {
            if(detailjob.get(0).url.equals(""))
            {
            }else {
                Glide.with(getApplicationContext()).load(detailjob.get(0).url)
                       // .override(200, 200)
                        .crossFade()
                        //.thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(logocompany);
            }
        }
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        getstt=true;
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
        // Camera permission has not been granted yet. Request it directly.
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
                Toast.makeText(JobDetailActivity.this, R.string.st_pemissonCamera, Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Glide.clear(logocompany);
        }catch (Exception e)
        {
            Log.i(TAG,"Loading Wrong.");
        }
        
    }



    @Override
    protected void onStart() {
        super.onStart();
        /*Intent s= getIntent();
        matd= s.getStringExtra("matd");
        if(matd==null || matd.equals("")) {
        }else{
            getData(matd,"", AppConfig.URL_VIEW);
        }*/
    }
    private void parseJsonFeed(String result) {
        Log.d(AppController.TAG, "Login Response: " + result);

        if (result.equals("") || result == null) {
            Toast.makeText(JobDetailActivity.this,R.string.st_errServer, Toast.LENGTH_SHORT).show();
            //   progressBar.setVisibility(View.GONE);
            return;
        }
        if (result.equals("3")) {
            Toast.makeText(JobDetailActivity.this,R.string.toast_save, Toast.LENGTH_SHORT).show();
            Intent a = new Intent();
            a.setAction("appendChatScreenMsg");
            a.putExtra("reload", true);
            sendBroadcast(a);
        }
        if (result.equals("2")) {
            Toast.makeText(JobDetailActivity.this,R.string.toast_alreadysaved, Toast.LENGTH_SHORT).show();
        }
    }
    private void getData(final String id, final String macv, final String url) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
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

                params.put("id", id);
                params.put("macv", macv);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(JobDetailActivity.this).requestQueue.add(strReq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
      //  if(type==1 || type==3)
      //  {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_detail_job, menu);
            if(!statussave) {
                menu.findItem(R.id.action_save).setIcon(R.drawable.ic_save_white);
            }else{ menu.findItem(R.id.action_save).setIcon(R.drawable.ic_action_saved);}
       // }

        return true;
    }








    private void saveJobAction() {
        statussave=true;
        menu.findItem(R.id.action_save).setIcon(R.drawable.ic_action_saved);
        getData(id,macv, AppConfig.URL_LUUCV);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ids = item.getItemId();

        if (ids == android.R.id.home) {

            super.onBackPressed();
        }

        if ((ids == R.id.action_save)){
            if(getstt) {
                if (!statussave) {
                    saveJobAction();
                } else {
                    try {
                        arritem.put(0, macv);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show();
                    }
                    DeleteSaveJob(id, arritem + "");
                    statussave = false;
                    menu.findItem(R.id.action_save).setIcon(R.drawable.ic_save_white);
                }
            }

        }
        if ((ids == R.id.action_share)){
            ShareAction();

         //   Firebase.setAndroidContext(this);
          //  Firebase root = new Firebase("https://job-find-2fb6d.firebaseio.com/"+iduser);

        }
        return super.onOptionsItemSelected(item);
    }

    private void ShareAction() {
        String message="";
        if(detailjob.get(0).ngoaingu.equals("") || detailjob.get(0).kn.equals("") || detailjob.get(0).bangcap.equals("") || detailjob.get(0).dotuoi.equals(""))
        {
             message = "Tên công việc : "+detailjob.get(0).tencongviec+"\n Địa điểm: "+detailjob.get(0).diadiem+"\n Mức lương : "+arrsalary[Integer.parseInt(detailjob.get(0).getLuong())]+"\n Mô tả công việc: "+detailjob.get(0).motacv+" \n Yêu cầu: \n - Khác: "+detailjob.get(0).khac+"\n Hạn nộp hồ sơ :"+detailjob.get(0).dateup+"\n Tham khảo thêm tại: http://quickjob.ga/link.php?jobid="+detailjob.get(0).macv;

        }else{
             message = "Tên công việc : "+detailjob.get(0).tencongviec+"\n Địa điểm: "+detailjob.get(0).diadiem+"\n Mức lương : "+arrsalary[Integer.parseInt(detailjob.get(0).getLuong())]+"\n Mô tả công việc: "+detailjob.get(0).motacv+" \n Yêu cầu: \n - Bằng cấp:  "+arrbc[Integer.parseInt(detailjob.get(0).bangcap)]+"\n - Độ tuổi "+detailjob.get(0).dotuoi+ "\n - Kỹ năng: "+detailjob.get(0).kn+"\n - Ngoại ngữ:  "+detailjob.get(0).ngoaingu+"\n - Khác: "+detailjob.get(0).khac+"\n Hạn nộp hồ sơ :"+detailjob.get(0).dateup+"\n Tham khảo thêm tại: http://quickjob.ga/link.php?jobid="+detailjob.get(0).macv ;
        }
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));
    }
    private void DeleteSaveJob(final String id, final String macv) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/


        // Tag used to cancel the request
        final String serverUrl = "http://quickjob.ga/xoacongviec.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                serverUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                parseJsonFeed(result);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppController.TAG, "Login Error: "+ error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("id", id);
                params.put("listitem", macv);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(JobDetailActivity.this).requestQueue.add(strReq);
    }
    private void parseJsonFeedJob(String result) {
        Log.d(AppController.TAG, "Login Response: " + result);
        if (result.equals("") || result == null) {
            Toast.makeText(JobDetailActivity.this,R.string.st_errServer, Toast.LENGTH_SHORT).show();
            //   progressBar.setVisibility(View.GONE);
            return;
        }
        try {
            JSONArray mang = new JSONArray(result);
            JSONObject ob = mang.getJSONObject(0);
            CongViec cv = new CongViec();
            cv.setMacv(ob.getString("macv"));
            cv.setTencongviec(ob.getString("tencv"));
            cv.setTecongty(ob.getString("tenct"));
            cv.setLuong(ob.getString("mucluong"));
            cv.setDiadiem(ob.getString("diadiem"));
            cv.setDateup(ob.getString("hannophoso"));
            cv.setNganhNghe(ob.getString("nganhnghe"));
            cv.setBangcap(ob.getString("bangcap"));
            cv.setMotacv(ob.getString("motacv"));
            cv.setDotuoi(ob.getString("dotuoi"));
            cv.setNgoaingu(ob.getString("ngoaingu"));
            cv.setGioitinh(ob.getString("gioitinh"));
            cv.setKhac(ob.getString("khac"));
            cv.setKn(ob.getString("kynang"));
            cv.setUrl(ob.getString("img"));
            cv.setSdt(ob.getString("phone"));
            cv.setLat(ob.getString("lat"));
            cv.setLng(ob.getString("long"));
            cv.setStatussave(ob.getString("statussave"));
            cv.setSoluong(ob.getString("number"));
            cv.setDatecreate(ob.getString("dateup"));
            cv.setNganhNghe(ob.getString("nganhnghe"));
            cv.setPhucloi(ob.getString("phucloi"));
            cv.setEmail(ob.getString("idcompany"));
            detailjob.add(cv);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Internet!!", Toast.LENGTH_SHORT).show();
        }
        // getSupportActionBar().setTitle(detailjob.get(0).tencongviec);
        setDataJob();
        if(detailjob.size()>0) {
            if(detailjob.get(0).statussave.equals("0"))
            {statussave =false;
            }else{
                menu.findItem(R.id.action_save).setIcon(R.drawable.ic_action_saved);
                statussave =true;
            }

        }

    }
    private void getDataJob(final String macv,final  String uid) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_JOBDETAIL_SHARE, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                parseJsonFeedJob(result);
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
                params.put("macv", macv);
                params.put("uid", uid);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(JobDetailActivity.this).requestQueue.add(strReq);
    }
}