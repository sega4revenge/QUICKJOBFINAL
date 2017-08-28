package com.quickjob.quickjobFinal.quickjobFind.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobFind.activity.GPSTracker;
import com.quickjob.quickjobFinal.quickjobFind.activity.MoreNormal_Activity;
import com.quickjob.quickjobFinal.quickjobFind.activity.MorePremium_Acitivity;
import com.quickjob.quickjobFinal.quickjobFind.adapter.InformationCompanyAdapter;
import com.quickjob.quickjobFinal.quickjobFind.adapter.ListJobCompanyAdapter;
import com.quickjob.quickjobFinal.quickjobFind.adapter.categoryadapter;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.config.AppController;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private static final String TAG = SearchFragment.class.getSimpleName();
    private List<CongViec> celebrities = new ArrayList<>();
    private List<CongViec> NearNor = new ArrayList<>();
    private List<CongViec> celebrities3 = new ArrayList<>();
    private List<CongViec> celebrities4 = new ArrayList<>();
    private List<CongViec> secondlist = new ArrayList<>();
    private static final int PLACE_PICKER_REQUEST = 3;
    int countdata;
    int begin = 0;
    int status = 0;
    boolean loading = true;
    int have = 0;
    private ListJobCompanyAdapter adapter = null, adapter_hot = null, adapter_near = null;
    private categoryadapter adapter_category;
    private InformationCompanyAdapter adapter_company = null;
    private String namecity;
    RelativeLayout recontent;
    private View view;
    private ProgressBar progressBar;
    LinearLayout linmore, linhot, linnear, linnotfind, linlocation;
    String uid = "";
    SessionManager session;
    private double latitude, longitude;
    JSONArray category;
    TextView txt, txt2, txt3, txt4, txtcategory, txtchangelocation, txterror, txtnumpager, txtnumpagerNormal;
    private RecyclerView recyclerView, recyclerView_company, recyclerView_hot, recyclerView_near, recyclerView_category;
    ArrayList<Integer> listcategory = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    ScreenSlidePagerAdapter mPagerAdapter;
    SlidePagerAdapter mPagerNormalAdapger;
    AlertDialog mdialog;
    ViewPager listgoldmember, listnormal;
    Activity mcontext;GPSTracker gps;boolean getOnGPS =true;
    ImageView morePer, moreNor,imgerror;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getContext());
        view = inflater.inflate(R.layout.fragment_search, container, false);
        begin = 0;
        mcontext = getActivity();
        session = new SessionManager(getApplicationContext());
        SharedPreferences pref = getActivity().getSharedPreferences("JobFindPref", MODE_PRIVATE);
        HashMap<String, String> user = session.getUserDetails();
        uid = user.get(SessionManager.KEY_ID);
        addView();
        eventmore();
        return view;
    }

    private void eventmore() {
        morePer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MorePremium_Acitivity.class);
                i.putExtra("namecity", namecity);
                startActivity(i);
            }
        });
        moreNor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MoreNormal_Activity.class);
                i.putExtra("namecity", namecity);
                startActivity(i);
            }
        });
        txtchangelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationPlacesIntent();
            }
        });
    }

    private void locationPlacesIntent() {

        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void addView() {
        imgerror = (ImageView) view.findViewById(R.id.error);
        imgerror.setImageResource(R.drawable.sad);
        morePer = (ImageView) view.findViewById(R.id.moreone);
        moreNor = (ImageView) view.findViewById(R.id.moretwo);
        txtnumpagerNormal = (TextView) view.findViewById(R.id.numpager2);
        txtnumpager = (TextView) view.findViewById(R.id.numpager);
        //  linlocation= (LinearLayout) view.findViewById(R.id.linloca);
        linnotfind = (LinearLayout) view.findViewById(R.id.linnotfind);
        recontent = (RelativeLayout) view.findViewById(R.id.rela_content);
        txtcategory = (TextView) view.findViewById(R.id.txtlocation);
        txt = (TextView) view.findViewById(R.id.txt);
        txtchangelocation = (TextView) view.findViewById(R.id.txtchangelocation);
        txterror = (TextView) view.findViewById(R.id.txterror);
        progressBar = (ProgressBar) view.findViewById(R.id.pbLoader);
        NestedScrollView s = (NestedScrollView) view.findViewById(R.id.nested);
        s.setSmoothScrollingEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);

        recyclerView_company = (RecyclerView) view.findViewById(R.id.newcompany);
        recyclerView_company.setScrollContainer(false);
        adapter_company = new InformationCompanyAdapter(celebrities4, getActivity());
        recyclerView_company.setAdapter(adapter_company);
        LinearLayoutManager linearLayoutManager_company = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_company.setLayoutManager(linearLayoutManager_company);
        recyclerView_company.setItemAnimator(new DefaultItemAnimator());
        listnormal = (ViewPager) view.findViewById(R.id.normallist);
        mPagerNormalAdapger = new SlidePagerAdapter(getActivity().getSupportFragmentManager());
        listnormal.setAdapter(mPagerNormalAdapger);
        listnormal.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int count = NearNor.size();
                if ((NearNor.size() % 4) > 0) {
                    if (NearNor.size() > 4) {
                        count = (NearNor.size() / 4) + 1;
                    } else {
                        count = 1;
                    }
                } else if (NearNor.size() < 4) {
                    count = 1;
                } else {
                    count = (NearNor.size() / 4);
                }
                txtnumpagerNormal.setText(position + 1 + "/" + count);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        listgoldmember = (ViewPager) view.findViewById(R.id.goldmember);
        mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
        listgoldmember.setAdapter(mPagerAdapter);
        listgoldmember.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int count = celebrities3.size();
                if ((celebrities3.size() % 4) > 0) {
                    if (celebrities3.size() > 4) {
                        count = (celebrities3.size() / 4) + 1;
                    } else {
                        count = 1;
                    }
                } else if (celebrities3.size() < 4) {
                    count = 1;
                } else {
                    count = (celebrities3.size() / 4);
                }
                txtnumpager.setText(position + 1 + "/" + count);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }

        });

    }

    private class SlidePagerAdapter extends FragmentStatePagerAdapter {
        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new Pager_Item_ListNormalFragment(NearNor, position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            return super.instantiateItem(container, position);

        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public int getCount() {
            int count = NearNor.size();
            if ((NearNor.size() % 4) > 0) {
                if (NearNor.size() > 4) {
                    count = (NearNor.size() / 4) + 1;
                } else {
                    count = 1;
                }
            } else if (NearNor.size() < 4) {
                count = 1;
            } else {
                count = (NearNor.size() / 4);
            }
            return count;
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new Pager_ItemFragment(celebrities3, position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            return super.instantiateItem(container, position);

        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public int getCount() {
            int count = celebrities3.size();
            if ((celebrities3.size() % 4) > 0) {
                if (celebrities3.size() > 4) {
                    count = (celebrities3.size() / 4) + 1;
                } else {
                    count = 1;
                }
            } else if (celebrities3.size() < 4) {
                count = 1;
            } else {
                count = (celebrities3.size() / 4);
            }
            return count;
        }
    }


    public void hideText() {
        recontent.setVisibility(View.GONE);
    }

    public void showText() {
        recontent.setVisibility(View.VISIBLE);

    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.st_err_gps));
        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.st_err_gps_messeger));
        // On pressing Settings button
        alertDialog.setPositiveButton(getResources().getString(R.string.st_turnonGPS), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton(getResources().getString(R.string.st_selectLocation), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setCancelable(false);
                LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view_dialog = inflater2.inflate(R.layout.dialog_createlangues, null, false);
                final String[] arr = getResources().getStringArray(R.array.select_setting_information);
                ListView lv = (ListView) view_dialog.findViewById(R.id.lvnn);
                ArrayAdapter<String> adapterandress = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arr);
                lv.setAdapter(adapterandress);
                alertDialog.setTitle(getResources().getString(R.string.st_location));
                alertDialog.setView(view_dialog);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            namecity=arr[i];
                            getData(namecity, uid);
                            txtcategory.setText(namecity);
                        mdialog.dismiss();
                    }
                });
                mdialog=alertDialog.create();
                mdialog.show();
                dialog.cancel();
//                linnotfind.setVisibility(View.VISIBLE);
//                txterror.setText(getResources().getString(R.string.st_err_gps_messeger));
//                imgerror.setImageResource(R.drawable.gps_off);
//                progressBar.setVisibility(View.GONE);
//                txtchangelocation.setVisibility(View.GONE);
//                mSwipeRefreshLayout.setRefreshing(false);

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    private void getlocation() {
        if (namecity == null || namecity.equals("")) {
            GPSTracker tracker = new GPSTracker(getActivity());
            if (!tracker.canGetLocation()) {
                getOnGPS = false;
                showSettingsAlert();
            } else {
                latitude = tracker.getLatitude();
                longitude = tracker.getLongitude();
                if(latitude==0 || longitude==0)
                {
                    new CountDownTimer(5000,5000 ) {
                        @Override
                        public void onTick(long l) {
                        }
                        @Override
                        public void onFinish() {
                            getlocation();
                        }
                    }.start();
                }else{
                    namecity=getAddress(latitude,longitude);
                    Log.d("aaaaa",namecity+"");
                    if(namecity.equals(" Da Nang"))
                    {   namecity = "Đà Nẵng";}
                    if(namecity.equals(" Ho Chi Minh"))
                    {   namecity = "Hồ Chí Minh";}
                    if(namecity.equals(" Hanoi"))
                    {   namecity = "Hà Nội";}
                    getData(namecity, uid);
                    txtcategory.setText(namecity);
                }

            }
            } else {
            Log.d("aaaaa",namecity+"2");
            if(namecity.equals(" Da Nang"))
            {   namecity = "Đà Nẵng";

            }
            if(namecity.equals(" Ho Chi Minh"))
            {   namecity = "Hồ Chí Minh";

            }
            if(namecity.equals(" Hanoi"))
            {   namecity = "Hà Nội";

            }
            getData(namecity, uid);
        Log.d("aaaaa",namecity+"3");
    }
    }

    public String getAddress(double lat ,double lng) {
        String address;
        try {
            Geocoder geocoder;
            List<Address> addresses;
            Locale.setDefault(new Locale("vi_VN"));
            geocoder = new Geocoder(getActivity(),Locale.getDefault());
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


    private void refreshContent() {
        if(CheckNetwork()) {
            if (mPagerAdapter != null ) {
                celebrities3.clear();
                mPagerAdapter.notifyDataSetChanged();
            }
            if ( mPagerNormalAdapger != null) {
                NearNor.clear();
                mPagerNormalAdapger.notifyDataSetChanged();
            }
            if (adapter_company != null) {
                celebrities4.clear();
                adapter_company.notifyDataSetChanged();
            }
            hideText();
            linnotfind.setVisibility(View.GONE);
            loading = false;
            begin = 0;
          //  VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().remove(AppConfig.URL_XUATCV_NEW_HOT);
            getlocation();
        }else{
            Toast.makeText(getActivity(), "Mạng yếu, vui lòng kiểm tra lại Internet!!", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean CheckNetwork() {
        if(isNetworkConnected() || isInternetAvailable())
        {return true;}else{return false;}
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error Internet", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
    public double getDistance(double my_lat,double my_long, double to_lat,double to_long) {

        double pk = (float) (180.f/Math.PI);

        double a1 = my_lat / pk;
        double a2 = my_long / pk;
        double b1 = to_lat / pk;
        double b2 = to_long / pk;

        double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
        double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
        double t3 = Math.sin(a1)*Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000*tt;
    }
    public double getDistance2(double my_lat,double my_long, double to_lat,double to_long) {
        double distance = 0;
        Location locationA = new Location("A");
        locationA.setLatitude(my_lat);
        locationA.setLongitude(my_long);
        Location locationB = new Location("B");
        locationB.setLatitude(to_lat);
        locationB.setLongitude(to_long);
        distance = locationA.distanceTo(locationB);

        return distance;
    }
    private void location() {
        if (celebrities3.size() > 0) {


            int count=celebrities3.size();
            if((celebrities3.size()%4)>0 && celebrities3.size()>4)
            {
                if(celebrities3.size()>4) {
                    count = (celebrities3.size() / 4) + 1;
                }else{
                    count=1;
                }
            }else if(celebrities3.size()<4){
                count=1;
            }else{
                count = (celebrities3.size() / 4);
            }
            txtnumpager.setText("1/"+count);
            linnotfind.setVisibility(View.GONE);
            mPagerAdapter.notifyDataSetChanged();

        }
    }
    private void parseJsonFeed(String result) {
//        celebrities.clear();
        NearNor.clear();
        celebrities3.clear();
        celebrities4.clear();
        Log.d(AppController.TAG, "Login Response: " + result);

        if (result.equals("") || result == null) {
            Toast.makeText(getActivity(),R.string.st_errServer, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        try {
            JSONArray jObj4 = new JSONObject(result).getJSONArray("company");
            JSONArray jObj3 = new JSONObject(result).getJSONArray("nearVip");
            JSONArray jObj2 = new JSONObject(result).getJSONArray("nearNor");

         //    JSONArray jObj = new JSONObject(result).getJSONArray("category");
            if(jObj3.length()>0 ||jObj2.length()>0 )
            {
                getData3(jObj4);
                getData2(jObj3, 2);
                getData1(jObj2, 2);
                showText();
            }else{
                linnotfind.setVisibility(View.VISIBLE);
                txterror.setText(getResources().getString(R.string.errorfindjob)+" "+namecity);

            }
            mSwipeRefreshLayout.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.GONE);
    }



    @Override
    public void onResume() {
        super.onResume();
       getlocation();
    }

    private void getData(final String city, final String uid) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/
        // Tag used to cancel the request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_XUATCV_NEW_HOT, new com.android.volley.Response.Listener<String>() {
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

                params.put("location", city);
                params.put("uid", uid);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(getActivity()).requestQueue.add(strReq);
    }
  /*  public class CustomerAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {


            OkHttpClient client = new OkHttpClient.Builder()

                    .build();
            RequestBody body;

            body = new FormBody.Builder()
                    .add("location", params[1])

                    .build();




            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(params[0])
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

        }
    }*/


    private void getDataCategory(JSONArray jObj) {

        int count=jObj.length();

        for(int i=0;i<count;i++)
        {
            try {
                listcategory.add(jObj.getInt(i));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
      //  adapter_category.notifyDataSetChanged();

    }
    public void getData1(JSONArray joc,int a){
        countdata= joc.length();
        for (int i = 0; i < countdata; i++) {
            try {
                JSONObject ob = joc.getJSONObject(i);
                CongViec cv = new CongViec();
                cv.setEmail(ob.getString("idcompany"));
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
                cv.setMatd(ob.getString("matd"));
                cv.setViews(ob.getString("views"));
                cv.setLat(ob.getString("lat"));
                cv.setLng(ob.getString("long"));
                cv.setDatecreate(ob.getString("ngaydang"));
                cv.setStatussave(ob.getString("statussave"));
                cv.setTrangthai("4");
                NearNor.add(cv);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(countdata>0) {
            linnotfind.setVisibility(View.GONE);
            mPagerNormalAdapger.notifyDataSetChanged();
        }else{
           // txtnumpagerNormal.setText("0/" + NearNor.size() / 4);
        }
        int count=NearNor.size();
        if((NearNor.size()%4)>0)
        {
            if(NearNor.size()>4) {
                count = (NearNor.size() / 4) + 1;
            }else{
                count=1;
            }
        }else if(NearNor.size()<4){
            count=1;
        }else{
            count = (NearNor.size() / 4);
        }
        txtnumpagerNormal.setText("1/" + count);

    }
    public void getData2(JSONArray joc,int a){
        countdata= joc.length();
        for (int i = 0; i < countdata; i++) {
            try {
                JSONObject ob = joc.getJSONObject(i);
                CongViec cv = new CongViec();
                cv.setEmail(ob.getString("idcompany"));
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
                cv.setMatd(ob.getString("matd"));
                cv.setViews(ob.getString("views"));
                cv.setLat(ob.getString("lat"));
                cv.setLng(ob.getString("long"));
                cv.setDatecreate(ob.getString("ngaydang"));
                cv.setStatussave(ob.getString("statussave"));
                cv.setTrangthai("4");
                celebrities3.add(cv);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        location();
        have=1;
        status=1;

    }



    private void getData3(JSONArray jObj4) {
        countdata= jObj4.length();
        for (int i = 0; i < countdata; i++) {
            try {
                JSONObject ob = jObj4.getJSONObject(i);
                CongViec cv = new CongViec();
                cv.setMacv(ob.getString("uid"));
                cv.setTecongty(ob.getString("namecompany"));
                cv.setDiachict(ob.getString("diachi"));
                cv.setQuymo(ob.getString("quymo"));
                cv.setNganhNghe(ob.getString("nganhnghe"));
                cv.setMotact(ob.getString("mota"));
                cv.setUrl(ob.getString("img"));
                celebrities4.add(cv);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter_company.notifyDataSetChanged();

    }

//    private void addcommpany() {
//        Toast.makeText(getActivity(), celebrities.size()+"", Toast.LENGTH_SHORT).show();
//        for (int i =0;i<celebrities.size();i++)
//        {
//            for(int n=i+1;n<=celebrities.size();n++)
//            {
//                if(celebrities.get(i).tecongty.equals(celebrities.get(n).tecongty))
//                {
//
//                }else{
//                    celebrities2.add(celebrities.get(i));
//                }
//            }
//        }
//        Toast.makeText(getActivity(), celebrities2.size()+"", Toast.LENGTH_SHORT).show();
//        adapter_company.notifyDataSetChanged();
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                if (place != null) {
                    LatLng latLng = place.getLatLng();
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                    String add = (String) place.getAddress();
                    if(add.contains(",")) {
                        String[] arrandress = add.split(",");
                        int count = arrandress.length;
                        namecity = arrandress[count - 2];
                        txtcategory.setText(namecity);
                    }
                    else {
                        txtcategory.setText(add);
                    }
                    refreshContent();
                }
            }
        }
    }
}