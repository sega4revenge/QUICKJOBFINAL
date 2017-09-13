package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.MyFirebaseInstanceIDService;
import com.quickjob.quickjobFinal.quickjobFind.fragment.JobManager;
import com.quickjob.quickjobFinal.quickjobFind.fragment.ListChatFragment;
import com.quickjob.quickjobFinal.quickjobFind.fragment.ProfileFragment;
import com.quickjob.quickjobFinal.quickjobFind.fragment.SearchFragment;
import com.quickjob.quickjobFinal.quickjobFind.pref.PrefManager;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.readystatesoftware.viewbadger.BadgeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager.KEY_LOGO;


public class MainActivity extends AppCompatActivity   {
    private static final String TAG = MainActivity.class.getSimpleName();
    private String emailpref="", namepref="",birthdate="",sexef,phone="",andress="",homeless="", logo = "",slogan="";
    static String logopref="";
    public static String uid, email1;
    int status=0;
    SessionManager session;
        CustomerAsyncTask asyncRequestObject;
    private String[] activityTitles;
    public  double latitude;
    PagerAdapter pagerAdapter;
    private  double longitude;
    SharedPreferences.Editor edit;
    GPSTracker gps;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Toolbar mToolbar;
    CoordinatorLayout coordinatorLayout;
    //  if(num>0) {
    BadgeView badge;
    FloatingActionButton fabMenu;
    private PrefManager prefManager;
    boolean nummess=false;
    boolean doubleBackToExitPressedOnce = false;
    boolean fisttab=false;
    CoordinatorLayout.Behavior fistbehavior;
    Locale myLocale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  Fabric.with(this, new Crashlytics());
          setContentView(R.layout.activity_main);
            session = new SessionManager(getApplicationContext());
               if(session.getLanguage().equals("") || session.getLanguage()==null){}else{
                   setLocale(session.getLanguage());
               }
            if (!session.isLoggedIn()) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                if(CheckNetwork()) {
               // setContentView(R.layout.activity_main);
                getMyData();
                fabMenu = (FloatingActionButton) findViewById(R.id.fabMenu);
                changeFab();
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                if(refreshedToken!=null) {
                    if(refreshedToken.equals(""))
                    {

                    }else{

                        MyFirebaseInstanceIDService s = new MyFirebaseInstanceIDService();
                        s.sendRegistrationToServer(refreshedToken, uid);
                    }
                }
                initToolbar();
                initViewPagerAndTabs();
                    setupTabIcons();
                }else{
                 setContentView(R.layout.errorinternet_layout);
                 Toast.makeText(MainActivity.this,"Kết nối wifi và thử lại.", Toast.LENGTH_SHORT).show();
                 TextView tryagain = (TextView) findViewById(R.id.tryagain);
                    tryagain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(getIntent());
                        }
                    });
                }
            }

      //  }
    }

    private boolean CheckNetwork() {
        if(isNetworkConnected() || isInternetAvailable())
        {return true;}else{return false;}
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("https://www.google.com.vn");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }
    private void changeFab() {
        if (fabMenu.getVisibility() == View.GONE) {
            fabMenu.setVisibility(View.VISIBLE);
        }
        fabMenu.setImageResource(R.drawable.ic_action_search);
        fabMenu.setColorNormal(getResources().getColor(R.color.actionbar));
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkConnected()) {
                //    tabLayout.getTabAt(0).setCustomView(R.layout.item_new_job );
                    String namecity = getlocation();
                    if(namecity!=null) {
                        if (namecity.equals("")) {
                            Intent i = new Intent(MainActivity.this, SearchJobActivity.class);
                            i.putExtra("namecity", "All");
                            i.putExtra("uid", uid);
                            i.putExtra("lat", latitude);
                            i.putExtra("lng", longitude);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MainActivity.this, SearchJobActivity.class);
                            i.putExtra("namecity", namecity);
                            i.putExtra("uid", uid);
                            i.putExtra("lat", latitude);
                            i.putExtra("lng", longitude);
                            startActivity(i);
                        }
                    }else{
                        Intent i = new Intent(MainActivity.this, SearchJobActivity.class);
                        i.putExtra("namecity", "All");
                        i.putExtra("uid", uid);
                        i.putExtra("lat", latitude);
                        i.putExtra("lng", longitude);
                        startActivity(i);
                    }
                }
            }
        });
    }
    public void setNumMessagerTab(int num)
    {
        Log.d(TAG,num+"");
            if(num>0) {
                badge.setText(num + "");
                badge.show();
            }else{
                badge.hide();
            }
    }
    private void hideFab() {
        if (fabMenu.getVisibility() == View.VISIBLE) {
           fabMenu.setVisibility(View.GONE);
        }
    }
    private void showFab() {
        if (fabMenu.getVisibility() == View.GONE) {
            fabMenu.setVisibility(View.VISIBLE);
        }
        fabMenu.setImageResource(R.drawable.fab_add);
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,CreateProfileActivity.class);
                i.putExtra("name",namepref);
                i.putExtra("birthdate",birthdate);
                i.putExtra("sex",sexef);
                i.putExtra("email",emailpref);
                i.putExtra("phone",phone);
                i.putExtra("andress",andress);
                i.putExtra("homeless",homeless);
                i.putExtra("logo",logo);
                i.putExtra("uniqueid",uid);
                i.putExtra("status",status);
                i.putExtra("slogan",slogan);
                startActivity(i);
            }
        });
    }

 public void getMyData(){
     session = new SessionManager(getApplicationContext());
     SharedPreferences pref = getSharedPreferences("JobFindPref", MODE_PRIVATE);
     edit = pref.edit();
     HashMap<String, String> user = session.getUserDetails();
     emailpref = user.get(SessionManager.KEY_EMAIL);
     namepref = user.get(SessionManager.KEY_NAME);
     logopref = user.get(KEY_LOGO);
     uid = user.get(SessionManager.KEY_ID);

 }
    private void setToolbarTitle(int pos) {
        getSupportActionBar().setTitle(activityTitles[pos]);
    }
    private void backtop() {
        //set new Behavior
        fisttab=true;
        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appBarLayout);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
        try{
            AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
            behavior.onNestedFling(coordinatorLayout, appbar, null, 0, -1000, true);
        }catch (Exception e){
            Log.d(TAG,"Error Notification");
        }

        fistbehavior=  params.getBehavior();
        params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        appbar.requestLayout();
    }
    private void backFist() {
        //setBehavior old fist, tab have scroll hide action
        fisttab=false;
        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appBarLayout);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
        params.setBehavior(fistbehavior);
        appbar.requestLayout();
    }

    private void setupTabIcons() {
        //Custom View Tab Chat
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewTab = inflater.inflate(R.layout.type_tab_chat, null);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_library_books_white_24dp);
        tabLayout.getTabAt(2).setCustomView(viewTab);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_assignment_ind_white_24dp);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_setting_true);
        Intent i = getIntent();
        if(!i.getBooleanExtra("setting",true)){
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_white_24dp);
            tabLayout.getTabAt(4).setIcon(R.drawable.ic_action_setting);
        }
        if(!i.getBooleanExtra("apply",true)){
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_white_24dp);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_library_books_black_24dp);
        }
        else if(!i.getBooleanExtra("chat",true)) {
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_white_24dp);
            ImageView viewimgselect=(ImageView) viewTab.findViewById(R.id.imgselect);
            ImageView viewimgunselect=(ImageView) viewTab.findViewById(R.id.imgunselect);
            viewimgselect.setVisibility(View.VISIBLE);
            viewimgunselect.setVisibility(View.GONE);
        }
        View v = tabLayout.getTabAt(2).getCustomView();
        badge = new BadgeView(MainActivity.this, v);
    }

    private void initToolbar() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        setSupportActionBar(mToolbar);
        setToolbarTitle(0);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void initViewPagerAndTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(5);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new SearchFragment(),"");
        pagerAdapter.addFragment(new JobManager(),"");
        pagerAdapter.addFragment(new ListChatFragment(), "");
        pagerAdapter.addFragment(new ProfileFragment(), "");
        pagerAdapter.addFragment(new SettingActivity(), "");
        viewPager.setAdapter(pagerAdapter);
        Intent i = getIntent();
        if(!i.getBooleanExtra("save",true))
        {
            viewPager.setCurrentItem(1);
            backtop();
            hideFab();
            i.putExtra("notification",false);
        }
        else if(!i.getBooleanExtra("apply",true)){
            viewPager.setCurrentItem(1);
            backtop();
            hideFab();
            i.putExtra("jobapplication",false);
        }
        else if(!i.getBooleanExtra("chat",true)){
            viewPager.setCurrentItem(2);
            backtop();
            hideFab();
        }else if(!i.getBooleanExtra("setting",true)){
            viewPager.setCurrentItem(4);
            backtop();
            hideFab();
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                    TabLayout.Tab tabCall = tabLayout.getTabAt(position);
                switch (position) {
                    case 0:
                        backFist();
                        changTabChat(1);
                        setToolbarTitle(0);
                        changeFab();
                        // showFab();
                        tabCall.setIcon(R.drawable.icon_menus);
                        tabLayout.getTabAt(4).setIcon(R.drawable.ic_setting_true);
                        break;
                    case 1:
                        if(!fisttab)
                        { backtop();}
                        changTabChat(1);
                        fabMenu.setVisibility(View.GONE);
                        setToolbarTitle(1);
                        hideFab();
                        tabLayout.getTabAt(4).setIcon(R.drawable.ic_setting_true);
                        tabCall.setIcon(R.drawable.icons_menu2);
                        TabLayout.Tab tabCall3 = tabLayout.getTabAt(0);
                        tabCall3.setIcon(R.drawable.icon_menus);

                        break;
                    case 2:
                        if(!fisttab)
                        { backtop();}
                        changTabChat(0);
                        setToolbarTitle(2);
                        hideFab();
                        tabLayout.getTabAt(4).setIcon(R.drawable.ic_setting_true);
                        TabLayout.Tab tabCall5 = tabLayout.getTabAt(0);
                        tabCall5.setIcon(R.drawable.icon_menus);
                        break;
                    case 3:
                        if(!fisttab)
                        { backtop();}
                        changTabChat(1);
                        setToolbarTitle(3);
                        showFab();
                        tabLayout.getTabAt(4).setIcon(R.drawable.ic_setting_true);
                        tabCall.setIcon(R.drawable.icon_menu3);
                        TabLayout.Tab tabCall4 = tabLayout.getTabAt(0);
                        tabCall4.setIcon(R.drawable.icon_menus);
                        break;
                    case 4:
                        if(!fisttab)
                        { backtop();}
                        changTabChat(1);
                        setToolbarTitle(4);
                        hideFab();
                        tabCall.setIcon(R.drawable.icon_menu4);
                        TabLayout.Tab tabCall6 = tabLayout.getTabAt(0);
                        tabCall6.setIcon(R.drawable.icon_menus);
                        break;
                    default:
                        tabCall.setIcon(R.drawable.ic_home_white_24dp);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);

    }
    private void changTabChat(int type){
        View tabView = tabLayout.getTabAt(2).getCustomView();
        ImageView viewimgselect=(ImageView) tabView.findViewById(R.id.imgselect);
        ImageView viewimgunselect=(ImageView) tabView.findViewById(R.id.imgunselect);
        if(type!=0)
        {
            viewimgselect.setVisibility(View.GONE);
            viewimgunselect.setVisibility(View.VISIBLE);
        }else{

            viewimgselect.setVisibility(View.VISIBLE);
            viewimgunselect.setVisibility(View.GONE);
        }
    }
    static class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     // getMenuInflater().inflate(R.menu.main, menu);
      return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(CheckNetwork()) {
            getMyData();
            asyncRequestObject = new CustomerAsyncTask();
            asyncRequestObject.execute(AppConfig.URL_TakeLogo, uid);
        }
    }
        public class CustomerAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(new File(getCacheDir(), "okdata"), cacheSize);
            CacheControl cacheControl = new CacheControl.Builder().maxAge(2, TimeUnit.HOURS).build();

            OkHttpClient client = new OkHttpClient.Builder()

                    .cache(cache)
                    .build();
            RequestBody body;

                body = new FormBody.Builder()
                        .add("uid", params[1])
                        .build();




            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(params[0])
                    .cacheControl(cacheControl)
                    .post(body)
                    .build();
            Response forceCacheResponse = null;
            String responsestring = "";
            try {
                forceCacheResponse = client.newCall(request).execute();

                responsestring = forceCacheResponse.body().string();

                if(forceCacheResponse.isSuccessful()){


                }

                else{


                }


            } catch (Exception e) {

                e.printStackTrace();


            }
            return responsestring;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
           /* System.out.println("Resulted Value: " + result);*/
            if(result.equals("")){
                Toast.makeText( MainActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
               JSONObject obs = new JSONObject(result);
                int stt = obs.getInt("success");
                if(stt==1)
                {
                    logo=obs.getString("avata");
                    emailpref=obs.getString("email");
                    namepref=obs.getString("name");
                    birthdate=obs.getString("birthdate");
                    phone=obs.getString("phone");
                    andress=obs.getString("andress");
                    sexef=obs.getString("sex");
                    homeless=obs.getString("homeless");
                    slogan=obs.getString("slogan");
                    logopref=logo;
                    edit.putString("logo", logo);
                    edit.putString("email", emailpref);
                    edit.commit();
                    status=1;
                    Log.d("AAA",emailpref+"");
                    session.createLoginSession(namepref,emailpref,birthdate,sexef,phone,andress,homeless,logo,slogan,uid);
                    Log.e("BLA BLA",session.getName()+session.getEmail());
                }else{
                    logo="";
                    status=0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

    private String getlocation(){
        String namecity="";
            gps = new GPSTracker(MainActivity.this);
            if(gps.canGetLocation()){
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                namecity= getAddress(latitude,longitude);
//                }

            }else{
            }
        return namecity;
        }
    public void showSettingsAlert(){
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.st_err_gps));

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.st_err_gps_messeger));

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public String getAddress(double lat ,double lng) {
        String address;
        try {
            Geocoder geocoder;
            List<Address> addresses;
            Locale.setDefault(new Locale("vi_VN"));
            geocoder = new Geocoder(this,Locale.getDefault());
            if (lat != 0 || lng != 0) {
                addresses = geocoder.getFromLocation(lat , lng, 1);
                Log.i("getAddress",addresses+" - ");
                address = addresses.get(0).getAdminArea();
                if(address==null || address.equals(""))
                {
                    int  add= addresses.get(0).getMaxAddressLineIndex();
                    address =addresses.get(0).getAddressLine(add -1) ;
                }
                Log.i("getAddress",address+"");
                if(address.equals("Da Nang"))
                    address = "Đà Nẵng";
                return address;
            } else {
                Log.i(TAG, "latitude and longitude are null");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
//        Intent refresh = new Intent(getApplication(), MainActivity.class);
//        refresh.putExtra("setting", false);
//        startActivity(refresh);
    }
}
