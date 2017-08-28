package com.quickjob.quickjobFinal.quickjobHire.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobHire.adapter.ListMemberChat_Share;
import com.quickjob.quickjobFinal.quickjobHire.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobHire.config.AppController;
import com.quickjob.quickjobFinal.MyFirebaseInstanceIDService;
import com.quickjob.quickjobFinal.quickjobHire.fragment.ListChatFragment;
import com.quickjob.quickjobFinal.quickjobHire.fragment.ListJobFragment;
import com.quickjob.quickjobFinal.quickjobHire.fragment.ProfileSaveFragment;
import com.quickjob.quickjobFinal.quickjobHire.fragment.SearchProfileFragment;
import com.quickjob.quickjobFinal.quickjobHire.model.CompanyInformation;
import com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;
import com.readystatesoftware.viewbadger.BadgeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {
    Boolean exit = false;

    public ScrollView scroll;
    private static final int PLACE_PICKER_REQUEST = 3;
    double lat, lng;
    static String logopref = "";
    public static String uid, email1;
    SessionManager session;
    private static final String TAG_SEARCH = "search";
    private String[] activityTitles;
    private String emailpref, namepref, logo = "", ten = "", dc = "", mt = "", nn = "", phone = "", quymo = "", lat1 = "", lot1 = "";
    private static final String TAG = "MyFirebaseIIDService";
    SharedPreferences.Editor edit;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Toolbar mToolbar;
    EditText address,namejob,detail;
    CoordinatorLayout coordinatorLayout;
    BadgeView badge;
    FloatingActionMenu fabMenu;
    String[] arrnn, arrdd, arrsalary;
    String location = "", job = "";
    private static final int REQUEST_CALL = 0;
    List<CompanyInformation> filter = new ArrayList<>();
    private ArrayAdapter adapternn, adapterdd, adaptersalary, adapterbc;
    private com.github.clans.fab.FloatingActionButton fabCall, fabSave, fabDiamond, fablocation;
    private Toolbar toolbar;
    private Spinner nganhnghe, mucluong, diadiem, spbc;
    int status = 0;
    int pos2;
    String limitapply = "", number = "", name = "", addr = "", deta = "", difference = "",iduser;
    PagerAdapter pagerAdapter;AlertDialog mDialog;
    Locale myLocale;  List<CompanyInformation> listdata3 = new ArrayList<>();
    Firebase refe_send,rec_list_child_company;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main_hire);
        session = new SessionManager(getApplicationContext());
        if(session.getLanguage().equals("") || session.getLanguage()==null){
        }
        else
        {
            setLocale(session.getLanguage());
        }
        SharedPreferences pref = getSharedPreferences("JobHirePref", MODE_PRIVATE);
        edit = pref.edit();
        HashMap<String, String> user = session.getUserDetails();
        emailpref = user.get(SessionManager.KEY_EMAIL);
        namepref = user.get(SessionManager.KEY_NAME);
        logopref = user.get(SessionManager.KEY_LOGO);
        uid = user.get(SessionManager.KEY_ID);
        iduser = uid.substring(0,14);
        Firebase.setAndroidContext(this);
        refe_send = new Firebase("https://job-find-2fb6d.firebaseio.com/"+iduser);
        rec_list_child_company= new Firebase("https://job-find-2fb6d.firebaseio.com/"+iduser);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        MyFirebaseInstanceIDService s = new MyFirebaseInstanceIDService();
        s.sendRegistrationToServer(refreshedToken, uid);
        initToolbar();
        init();
        events();
        initViewPagerAndTabs();
        setupTabIcons();
        getData(AppConfig.URL_INFORMATION, uid, "", "", "", "", "", "", "", "", "", "", "", "", "","");
    }

public void setNumMessagerTab(int num)
{
    View v = tabLayout.getTabAt(2).getCustomView();
    TextView num_txt = (TextView) v.findViewById(R.id.txt_num);
    Log.d(TAG,num+"");
    if(num>0) {
        num_txt.setVisibility(View.VISIBLE);
        num_txt.setText(num+"");
    }else{
        num_txt.setVisibility(View.GONE);
        badge.hide();
    }
}
    private void hideFab() {
        if (fabMenu.getVisibility() == View.VISIBLE) {
            fabMenu.close(true);
            fabMenu.setVisibility(View.GONE);
        }
    }

    private void showFab() {
        if (fabMenu.getVisibility() == View.GONE) {
            fabMenu.close(true);
            fabMenu.setVisibility(View.VISIBLE);
        }
    }

    private void setToolbarTitle(int pos) {
        getSupportActionBar().setTitle(activityTitles[pos]);
    }

    private void backtop() {
        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appBarLayout);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedFling(coordinatorLayout, appbar, null, 0, -1000, true);
    }

    private void setupTabIcons() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewTab = inflater.inflate(R.layout.type_tab_chat_hire, null);
        tabLayout.getTabAt(0).setIcon(R.drawable.menu_search_select);
        tabLayout.getTabAt(1).setIcon(R.drawable.menu_saveprofile);
        tabLayout.getTabAt(2).setCustomView(viewTab);
        tabLayout.getTabAt(3).setIcon(R.drawable.menu_manager_job);
        tabLayout.getTabAt(4).setIcon(R.drawable.menu_account);
        Intent i = getIntent();
        if(!i.getBooleanExtra("setting",true)){
            tabLayout.getTabAt(0).setIcon(R.drawable.menu_search_select);
            tabLayout.getTabAt(4).setIcon(R.drawable.menu_account_select);
        }else{
            tabLayout.getTabAt(4).setIcon(R.drawable.menu_account);
        }
        View v = tabLayout.getTabAt(2).getCustomView();
        badge = new BadgeView(MainActivity.this, v);
     //   badge.decrement(100);
    }

    private void initToolbar() {


        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.st_timkiem));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    private void initViewPagerAndTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(5);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new SearchProfileFragment(), "");
        pagerAdapter.addFragment(new ProfileSaveFragment(), "");
        pagerAdapter.addFragment(new ListChatFragment(), "");
        pagerAdapter.addFragment(new ListJobFragment(), "");
        pagerAdapter.addFragment(new SettingActivity(), "");
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        Intent i = getIntent();
        if(!i.getBooleanExtra("noti",true))
        {
            viewPager.setCurrentItem(3);
//            hideFab();
//            i.putExtra("notification",false);
        }
        else if(!i.getBooleanExtra("chat",true)){
            viewPager.setCurrentItem(2);
        }else if(!i.getBooleanExtra("setting",true)){
            hideFab();
            viewPager.setCurrentItem(4);
//            i.putExtra("jobapplication",false);
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tabCall = tabLayout.getTabAt(position);
                switch (position) {
                    case 0:
                        setToolbarTitle(0);
                        showFab();
                        changTabChat(1);
                        tabLayout.getTabAt(4).setIcon(R.drawable.menu_account);
                        tabCall.setIcon(R.drawable.icon_menus);
                        break;
                    case 1:
                        backtop();
                        hideFab();
                        changTabChat(1);
                        tabLayout.getTabAt(4).setIcon(R.drawable.menu_account);
                        setToolbarTitle(1);
                        tabCall.setIcon(R.drawable.icons_menu2);
                        TabLayout.Tab tabCall2 = tabLayout.getTabAt(0);
                        tabCall2.setIcon(R.drawable.icon_menus);
                        break;
                    case 2:
                        backtop();
                        hideFab();
                        changTabChat(0);
                        tabLayout.getTabAt(4).setIcon(R.drawable.menu_account);
                        setToolbarTitle(2);
                        tabCall.setIcon(R.drawable.icons_menus_chat);
                        TabLayout.Tab tabCall5 = tabLayout.getTabAt(0);
                        tabCall5.setIcon(R.drawable.icon_menus);
                        break;
                    case 3:
                        setToolbarTitle(3);
                        showFab();
                        changTabChat(1);
                        tabLayout.getTabAt(4).setIcon(R.drawable.menu_account);
                        tabCall.setIcon(R.drawable.icon_menu3);
                        TabLayout.Tab tabCall3 = tabLayout.getTabAt(0);
                        tabCall3.setIcon(R.drawable.icon_menus);
                        break;
                    case 4:
                        setToolbarTitle(4);
                        hideFab();
                        changTabChat(1);
                        tabCall.setIcon(R.drawable.icon_menu4);
                        TabLayout.Tab tabCall4 = tabLayout.getTabAt(0);
                        tabCall4.setIcon(R.drawable.icon_menus);
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
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
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

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
    @Override
    public void onBackPressed() {

           super.onBackPressed();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void events() {

        fabCall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            edit("","","","","","","","","","0","");
            }
        });
        fabDiamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(MainActivity.this)
                        .setTitleText(getResources().getString(R.string.fab_premium))
                        .setContentText(getResources().getString(R.string.register_adv))
                        .setCancelText(getResources().getString(R.string.nav_menu_6))
                        .setConfirmText(getResources().getString(R.string.st_goidienCV))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                                            REQUEST_CALL);
                                } else {
                                    String phone = "01645065117";
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:" + phone));
                                    try {
                                        startActivity(callIntent);
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(getApplicationContext(), R.string.st_loiKXD, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .show();
            }
        });
        fabSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Create_Job_Activity.class);
                i.putExtra("tenct", ten);
                i.putExtra("diachi", dc);
                i.putExtra("mota", mt);
                i.putExtra("logo", logo);
                startActivity(i);
            }
        });

    }

    //
//    public void getLocationFromAddress(Context context,String strAddress) {
//        Geocoder coder = new Geocoder(context);
//        if (strAddress != null && !strAddress.isEmpty()) {
//            try {
//                List<Address> addressList = coder.getFromLocationName(strAddress, 5);
//                if (addressList != null && addressList.size() > 0) {
//                    lat = addressList.get(0).getLatitude();
//                    lng = addressList.get(0).getLongitude();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } // end catch
//        } // end if
//
//    }
    @Override
    protected void onResume() {
        super.onResume();
        getData(AppConfig.URL_INFORMATION, uid, "", "", "", "", "", "", "", "", "", "", "", "", "","");
    }

    private void parseJsonFeed(String result) {
        System.out.println("Resulted Value: " + result);
        if (result.equals("") || result == null) {
            Toast.makeText(MainActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
            return;
        }
        if (status == 0) {
            try {
                JSONArray mang = new JSONArray(result);
                if (mang.length() > 0) {
                    JSONObject obs = mang.getJSONObject(0);
                    ten = obs.getString("tencongty");
                    dc = obs.getString("diachi");
                    mt = obs.getString("mota");
                    phone = obs.getString("sdt");
                    quymo = obs.getString("quymo");
                    nn = obs.getString("nganhnghe");
                    logo = obs.getString("img");
                    edit.putString("logo", logo);
                    edit.putString("email", ten);
                    edit.commit();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if (result.equals("1")) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.st_create_success) + "", Toast.LENGTH_SHORT).show();
                if (viewPager.getCurrentItem() != 3) {
                    pagerAdapter.getItem(3).onResume();
                    viewPager.setCurrentItem(3);
                } else {
                    pagerAdapter.getItem(3).onResume();
                }
            }else{
                if (viewPager.getCurrentItem() != 3) {
                    viewPager.setCurrentItem(3);
                } else {
                    pagerAdapter.getItem(3).onResume();
                }
            }
            status = 0;
        }
    }

    private void getData(final String url, final String id, final String namejob, final String tencongty
            , final String diadiem, final String motacongviec, final String nganhnghe, final String mucluong, final String latitude
            , final String longitude, final String numberapply, final String limitapply, final String bangcap, final String yeucaukhac, final String status1,final String macv) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/
        // Tag used to cancel the request
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
                        getResources().getString(R.string.st_errServer), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                if (status != 0) {
                    params.put("namejob", namejob);
                    params.put("tencongty", tencongty);
                    params.put("diadiem", diadiem);
                    params.put("motacongviec", motacongviec);
                    params.put("nganhnghe", nganhnghe);
                    params.put("mucluong", mucluong);
                    params.put("latitude", latitude);
                    params.put("longitude", longitude);
                    params.put("numberapply", numberapply);
                    params.put("limitapply", limitapply);
                    params.put("bangcap", bangcap);
                    params.put("yeucaukhac", yeucaukhac);
                    params.put("status", status1);
                    params.put("macv",macv);
                }
                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(this).requestQueue.add(strReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void locationPlacesIntent() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void init() {

        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        fabDiamond = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabDiamond);
        fabCall = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabCall);
        fabSave = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabSave);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fabMenu.setClosedOnTouchOutside(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(MainActivity.this, data);
                if (place != null) {
                    LatLng latLng = place.getLatLng();
                    lot1 = String.valueOf(latLng.latitude);
                    lat1 = String.valueOf(latLng.longitude);
                    String add = (String) place.getAddress();
//                    Toast.makeText(this, add+"1"+lot+"2"+lng, Toast.LENGTH_SHORT).show();
                    address.setText(add);
                }
            }

        }
    }
    public void  Filter(CompanyInformation com,int type)
    {
        if(type==0)
        {
            filter.remove(com);
        }else{
            filter.add(com);
        }
    }
    public  void shareQJ(final String mess){
        if(filter.size()>0)
        {
            filter.clear();
        }
        if(listdata3.size()>0)
        {
            listdata3.clear();
        }
        final AlertDialog.Builder buildershare_QJ = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater infla = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView2 = infla.inflate(R.layout.content_xuat_tim_kiem_hire,null);
        buildershare_QJ.setTitle(R.string.st_chiaseCV);
        buildershare_QJ.setView(mView2);
        final ProgressBar progressBar =(ProgressBar) mView2.findViewById(R.id.pbLoader);
        progressBar.setVisibility(View.VISIBLE);
        final ListView lv = (ListView) mView2.findViewById(R.id.lvtimkiem);
        final ListMemberChat_Share adapter2 = new ListMemberChat_Share(MainActivity.this, android.R.layout.simple_list_item_1, listdata3);
        lv.setAdapter(adapter2);
        final TextView tx =(TextView) mView2.findViewById(R.id.txt_noti);
        final LinearLayout line = (LinearLayout) mView2.findViewById(R.id.createjob);
        refe_send.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        CompanyInformation mess = messageSnapshot.child("UserReceive").getValue(CompanyInformation.class);
                        listdata3.add(mess);
                        if(listdata3.size()>0)
                        {
                            progressBar.setVisibility(View.GONE);
                            tx.setVisibility(View.GONE);
                        }else{
                            progressBar.setVisibility(View.GONE);
                            tx.setVisibility(View.VISIBLE);
                            tx.setText(getResources().getString(R.string.no_data));
                        }
                        adapter2.notifyDataSetChanged();
                    }
                }else{
                    lv.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    tx.setText(R.string.nodata);
                    line.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        buildershare_QJ.setNegativeButton(MainActivity.this.getResources().getString(R.string.st_chiaseCV), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SimpleDateFormat desiredFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = desiredFormat.format(new Date());
                for(int x =0 ; x<filter.size(); x++)
                {
                    Firebase re_send = new Firebase("https://job-find-2fb6d.firebaseio.com/"+iduser+"/"+filter.get(x).getUid());
                    Firebase re_rec = new Firebase("https://job-find-2fb6d.firebaseio.com/"+filter.get(x).getUid()+"/"+iduser);
                    Map<String, String> mapcompany = new HashMap<String, String>();
                    mapcompany.put("namecompany",namepref);
                    mapcompany.put("logo", logo);
                    mapcompany.put("newchat", mess);
                    mapcompany.put("notseen", Integer.parseInt(filter.get(x).getNotseen())+1+"");
                    mapcompany.put("uid", iduser);
                    mapcompany.put("timenew", currentDateandTime);
                    mapcompany.put("statusInbox", 0+"");
                    Map<String, String> mapuser = new HashMap<String, String>();
                    mapuser.put("name", filter.get(x).getName());
                    mapuser.put("avata", filter.get(x).getAvata());
                    mapuser.put("newchat", mess);
                    mapuser.put("timenew", currentDateandTime);
                    mapuser.put("uid", filter.get(x).getUid());
                    mapuser.put("notseen", 0+"");
                    mapuser.put("statusInbox", filter.get(x).getStatusInbox()+"");
                    re_rec.child("UserReceive").setValue(mapcompany);
                    re_send.child("UserReceive").setValue(mapuser);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("messager", mess);
                    map.put("name", namepref);
                    map.put("avata", logo);
                    map.put("uid", uid);
                    map.put("time", currentDateandTime);
                    re_rec.child("Messager").push().setValue(map);
                    re_send.child("Messager").push().setValue(map);
                    if(Integer.parseInt(filter.get(x).getStatusInbox())==0)
                    {
                        SendNotification(logo,namepref,mess,filter.get(x).getUid(),iduser);
                    }else{
                        Log.d(TAG,"Status: Online");
                    }
                }
            }
        });
        buildershare_QJ.create().show();
      //  mDialog.cancel();
    }
    private void SendNotification(final String avata, final String name, final String messager, final String idcompany, final String idsend) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHAT_NOTI, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Log.d(TAG,result+"");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                  Toast.makeText(getApplicationContext(),
                          getResources().getString(R.string.st_errServer), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("avata", avata);
                params.put("name", name);
                params.put("messager", messager);
                params.put("idcompany", idcompany);
                params.put("idsend", idsend);
                return params;
            }
        };
//        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(MainActivity.this).requestQueue.add(strReq);
    }
    public void edit(String tenct, String tencv, String nganhnghe, String hannop, String mucluong, String soluong, String diachi, String mota, String yeucau, final String type, final String macv){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view1 = inflater.inflate(R.layout.createjob_steptwo_hire, null);
        final ScrollView scroll = (ScrollView)view1.findViewById(R.id.scrollnormal);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });
        final EditText num = (EditText) view1.findViewById(R.id.ednum);
        final EditText limit = (EditText) view1.findViewById(R.id.limit);
        final EditText khac = (EditText) view1.findViewById(R.id.yeucaukhac);
        limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog fromDatePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        Locale.setDefault(new Locale("vi_VN"));
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        limit.setText(dateFormatter.format(newDate.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();
            }
        });
        final EditText namecompany = (EditText) view1.findViewById(R.id.tenct);
        namecompany.setText(ten);
        address = (EditText) view1.findViewById(R.id.eddc);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationPlacesIntent();
            }
        });

        arrnn = getResources().getStringArray(R.array.nganhNghe);
        arrdd = getResources().getStringArray(R.array.diadiem);
        arrsalary = getResources().getStringArray(R.array.mucluong);
        adapternn = new ArrayAdapter<String>(MainActivity.this, R.layout.customspniner_hire, arrnn);
        adapternn.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        adaptersalary = new ArrayAdapter<String>(MainActivity.this, R.layout.customspniner_hire, arrsalary);
        adaptersalary.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        final Spinner spnganh = (Spinner) view1.findViewById(R.id.nganhnghe);
        spnganh.setAdapter(adapternn);
        final Spinner salary = (Spinner) view1.findViewById(R.id.mucluong);
        salary.setAdapter(adaptersalary);
        spbc = (Spinner) view1.findViewById(R.id.spbc);
        adapterbc = new ArrayAdapter<String>(MainActivity.this, R.layout.customspniner_hire, getResources().getStringArray(R.array.spChucDanh));
        adapterbc.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spbc.setAdapter(adapterbc);
        builder.setTitle(getString(R.string.fabcreatefast));
        builder.setView(view1);
        String positiveText = getString(android.R.string.ok);
        namejob = (EditText) view1.findViewById(R.id.tencv);
        //   EditText address = (EditText) view1.findViewById(eddc);
        detail = (EditText) view1.findViewById(R.id.motacv);
        namejob.setText(tencv);
        if((nganhnghe.equals("") || nganhnghe == null) && (mucluong.equals("") || mucluong == null)){

        }
        else {
            spnganh.setSelection(Integer.parseInt(nganhnghe));
            salary.setSelection(Integer.parseInt(mucluong));
        }
        limit.setText(hannop);
        num.setText(soluong);
        address.setText(diachi);
        detail.setText(mota);
        khac.setText(yeucau);
        String negativeText = getString(android.R.string.cancel);

        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

         mDialog = builder.create();
        mDialog.show();
        mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int pos = spnganh.getSelectedItemPosition();
                pos2 = salary.getSelectedItemPosition();
                int pos3 = spbc.getSelectedItemPosition();
    
                limitapply = limit.getText() + "";
                number = num.getText() + "";
                name = namecompany.getText() + "";
                job = namejob.getText() + "";
                addr = address.getText() + "";
                deta = detail.getText() + "";
                difference = khac.getText() + "";
                if ( job == null || job.equals("") || addr == null || addr.equals("") || number == null || number.equals("") || limitapply == null || limitapply.equals("")) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.st_err_taocv) + "", Toast.LENGTH_SHORT).show();
                } else {
                    status = 1;
                    if(type == "0"){
                        getData(AppConfig.URL_CREATEJOB, uid, job, name, addr, deta, pos + "", pos2 + "", lat1 + "", lot1 + "", number, limitapply, pos3 + "", difference, "0","");
                        mDialog.dismiss();
                    }
                    else{
                        getData(AppConfig.URL_UPDATEJOBPARTTIME, uid, job, name, addr, deta, pos + "", pos2 + "", lat1 + "", lot1 + "", number, limitapply, pos3 + "", difference, "0",macv);
                        mDialog.dismiss();
                    }
                }
            }
        });

    }
    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

}