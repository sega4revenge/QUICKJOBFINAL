package com.quickjob.quickjobFinal.quickjobFind.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobFind.activity.CreateProfileActivity;
import com.quickjob.quickjobFinal.quickjobFind.activity.GPSTracker;
import com.quickjob.quickjobFinal.quickjobFind.adapter.ListViewAdapter;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.config.AppController;
import com.quickjob.quickjobFinal.quickjobFind.model.Profile;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;



public class JobAboutFragment extends Fragment {
    private static final int REQUEST_CALL = 0;
    public static final String TAG = JobAboutFragment.class.getSimpleName();
    public static String macv;
    int status = 0, luong = 0, gt = 0, hv = 0;
    private TextView report,txtdes,txtdateup,txtcarrer,txtnumber,txttencv, txtnamecompany, txtdiachi, txtluong, txtbangcap, txtmotacv, txtkn, txtdotuoi, txtgt, txtnn, txtkhac, txtdate, txtexe, txtskill,txtphucloi;
    private String reportt,carrer,id, sdt, tencv, tenct, diadiem, mucluong, ngayup, yeucaubangcap, dotuoi, ngoaingu, gioitinh, khac, motacv, kn, quymo, jobcompany, location, detail, skill,number,datecreate,phucloi;
    int type;
    private View v;Bundle bundledata;
    String[] arrnganh, arrhv, arrsalary, arrsex;
    MapView mapview_location;LatLng start,end;Double lattow,lngtwo;
    GoogleMap googleMap;String lat="",lng="";Button btncall,btnapply;AlertDialog dialog;
    private List<Profile> profile = new ArrayList<>();
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    SessionManager session;int progress ;
    SharedPreferences.Editor edit;
    public JobAboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
        session = new SessionManager(getApplicationContext());
        SharedPreferences pref = getActivity().getSharedPreferences("JobFindPref", MODE_PRIVATE);
        edit = pref.edit();
        HashMap<String, String> user = session.getUserDetails();
        String  id = user.get(SessionManager.KEY_ID);
        getData(id,"", AppConfig.URL_XUATHS);
        if(savedInstanceState!=null)
        {
            bundledata = savedInstanceState.getBundle("data");
            getDataIntent();
        }else{
            Intent i = getActivity().getIntent();
            bundledata= i.getBundleExtra("data");
            getDataIntent();
        }


    }

    private void getDataIntent() {
        tencv = bundledata.getString("tencongviec");
        tenct = bundledata.getString("tecongty");
        diadiem = bundledata.getString("diadiem");
        try {
            luong = Integer.parseInt(bundledata.getString("luong"));
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
        ngayup = bundledata.getString("ngayup");
        // yeucaubangcap = i.getString("yeucaubangcap");
        if(bundledata.getString("bangcap").equals(""))
        {
        }else{
            try {
                hv = Integer.parseInt(bundledata.getString("bangcap"));
            }catch (Exception e){
                Log.d(TAG,e.toString());
            }
        }
        dotuoi = bundledata.getString("dotuoi");
        ngoaingu = bundledata.getString("ngoaingu");
        // gioitinh = i.getString("gioitinh");
        if(bundledata.getString("gioitinh").equals(""))
        {
        }else{

            try {
                gt=Integer.parseInt(bundledata.getString("gioitinh"));
            }catch (Exception e){
                Log.d(TAG,e.toString());
            }
        }
        khac = bundledata.getString("khac");
        motacv = bundledata.getString("motacv");
        kn = bundledata.getString("kn");
        macv = bundledata.getString("macv");
        sdt = bundledata.getString("sdt");
        quymo = bundledata.getString("quymo");
        jobcompany = bundledata.getString("tecongty");
        detail = bundledata.getString("motacty");
        location = bundledata.getString("location");
        skill= bundledata.getString("kn");
        lat = bundledata.getString("lat");
        lng = bundledata.getString("lng");
        number = bundledata.getString("number");
        datecreate = bundledata.getString("datecreate");
        carrer = bundledata.getString("carrer");
        phucloi = bundledata.getString("phucloi");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Intent i = getActivity().getIntent();
        Bundle bundle= i.getBundleExtra("data");
        outState.putBundle("data",bundle);
        super.onSaveInstanceState(outState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.detail_information_job, container, false);
        mapview_location = (MapView) v.findViewById(R.id.mapView_location);
        mapview_location.onCreate(savedInstanceState);
        mapview_location.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Log.d(TAG,e.toString());
            e.printStackTrace();
        }
        arrhv = getResources().getStringArray(R.array.spHocVan);
        arrsalary = getResources().getStringArray(R.array.mucluong);
        arrsex = getResources().getStringArray(R.array.sex);
        arrnganh = getResources().getStringArray(R.array.nganhNghe);
        init();
        actionGetIntent();
        eventbutton();
        if( lng.equals("0") || lat.equals("0") || lat.equals("")|| lng.equals(""))
        {

        }else{
            Log.d(TAG,"begin");
            if(lat.startsWith("108"))
            {
                Log.d(TAG,"change");
                String langtude = lat;
                lat= lng;
                lng =langtude;
            }else{
            }
        }
        GPSTracker gps = new GPSTracker(getActivity());
        if(gps.canGetLocation()) {
            lattow = gps.getLatitude();
            lngtwo = gps.getLongitude();
            Log.d(TAG,lat+"//"+ lng);
            double dis1 = getDistance(Double.parseDouble(lat),Double.parseDouble(lng),lattow,lngtwo);
            if(dis1>1000)
            {
                dis1=dis1/1000;
                dis1=Math.round(dis1);
                txtdes.setText(dis1+" km");
            }else{
                // dis1=dis2;
                dis1=Math.round(dis1);
                txtdes.setText(dis1+" m");
            }
        }else{
        }




        mapview_location.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                googleMap.addMarker(new MarkerOptions().position(sydney).title(tencv).snippet(diadiem));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(16).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
        return v;

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
    private void eventbutton() {
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view1 = inflater.inflate(R.layout.dialog_report, null);
                Spinner spinner = (Spinner) view1.findViewById(R.id.reportspinner) ;
                final EditText detail_des = (EditText) view1.findViewById(R.id.detail_des);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        Log.e("Report", String.valueOf(parent.getItemAtPosition(position)));
                        if(position==4){
                            detail_des.setVisibility(View.VISIBLE);
                        }
                        else {
                            detail_des.setVisibility(View.GONE);
                        }
                        reportt = String.valueOf(parent.getItemAtPosition(position));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                builder.setTitle(getString(R.string.report));
                builder.setView(view1);
                builder.setPositiveButton(R.string.st_xacNhan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(getContext(),reportt + " " + detail_des.getText().toString(),Toast.LENGTH_LONG).show();
                        StringRequest strReq = new StringRequest(Request.Method.POST,
                                AppConfig.URL_REPORT, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String result) {
                                Log.d(AppController.TAG, "Reprot Response: " + result);
                                try {
                                    JSONObject a =new JSONObject(result);
                                    String aa = a.getString("error");
                                    Log.e("aaa",aa);
                                    if(aa.equals("false") ){
                                        Toast.makeText(getContext(), R.string.report_success, Toast.LENGTH_SHORT).show();
                                    }else if(aa.equals("true")){
                                        Toast.makeText(getContext(), R.string.report_existed, Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getContext(), R.string.report_failed, Toast.LENGTH_SHORT).show();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                                if (result.equals("") || result == null) {
//                                    Toast.makeText(getActivity(),R.string.st_errServer, Toast.LENGTH_SHORT).show();
//                                    //   progressBar.setVisibility(View.GONE);
//                                    return;
//                                }
//                                if (result.equals("1")) {
////            Toast.makeText(getActivity(),R.string.toast_apply, Toast.LENGTH_SHORT).show();
//
//                                }
//                                if (result.equals("0")) {
////            Toast.makeText(getActivity(),R.string.toast_alreadyapply, Toast.LENGTH_SHORT).show();
//
//                                }
                                        }
                        }, new Response.ErrorListener() {
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
                                Log.e("Report",session.getId()+macv+reportt + " " + detail_des.getText().toString());
                                params.put("id", session.getId());
                                params.put("macv", macv);
                                params.put("report", reportt + " " + detail_des.getText().toString());
                                return params;
                            }
                        };
                        // Adding request to request queue
                        strReq.setTag(this.getClass().getName());
                        VolleySingleton.getInstance(getContext()).requestQueue.add(strReq);
                    }
                });
                builder.setNegativeButton(R.string.st_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                // display dialog
                dialog.show();
            }
        });
        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestCameraPermission();
                } else {
                    takePicture();
                }
            }
        });
        btnapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view2 = inflater.inflate(R.layout.dialog_createlangues, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                String ms = getResources().getString(R.string.addProfile);
                builder.setTitle(ms);
                if(profile.size()>0) {
                    builder.setView(view2);
                    ListView lv = (ListView) view2.findViewById(R.id.lvnn);
                    ListViewAdapter adapter = new ListViewAdapter(getActivity(), android.R.layout.simple_list_item_1, profile, 1);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            getData(profile.get(i).id,macv,AppConfig.URL_NOPHS);
                            //   Toast.makeText(getActivity(),id+macv+"",Toast.LENGTH_SHORT).show();
                            //  AsyncDataClass asyncRequestObject = new AsyncDataClass();
                            //  asyncRequestObject.execute(AppConfig.URL_NOPHS, celebrities.get(i).id, macv);
                        }
                    });

                }else{
                    LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view3 = inflater2.inflate(R.layout.showmess_notsv_needcreate, null);
                    builder.setView(view3);
                    Button create = (Button) view3.findViewById(R.id.btcreate);
                    create.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(getActivity(),CreateProfileActivity.class);
                            i.putExtra("name","");
                            i.putExtra("birthdate","");
                            i.putExtra("sex","");
                            i.putExtra("email","");
                            i.putExtra("phone","");
                            i.putExtra("andress","");
                            i.putExtra("homeless","");
                            i.putExtra("logo","");
                            i.putExtra("uniqueid",id);
                            i.putExtra("status","0");
                            startActivity(i);
                            dialog.cancel();
                        }
                    });
                }
                builder.setNegativeButton(R.string.st_thoat, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.cancel();
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    private void takePicture() {
        String phone = sdt;
        String number = phone +"";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        try {
            startActivity(callIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), R.string.st_loiKXD, Toast.LENGTH_SHORT).show();
        }
    }
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},
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

                takePicture();

            } else {
                //Permission not granted
                Toast.makeText(getActivity(), R.string.st_pemissonCamera, Toast.LENGTH_SHORT).show();
            }

        }
    }





    private void init() {
        report = (TextView) v.findViewById(R.id.report);
        txtdes = (TextView)  v.findViewById(R.id.txtdes);
        txtphucloi = (TextView)  v.findViewById(R.id.txtpl);
        btncall = (Button) v.findViewById(R.id.btn_call);
        btnapply = (Button) v.findViewById(R.id.btn_apply);
        txtcarrer= (TextView)  v.findViewById(R.id.carrer);
        txtdateup= (TextView)  v.findViewById(R.id.txtdateup);
        txtnumber= (TextView)  v.findViewById(R.id.txtnumber);
        txttencv = (TextView)  v.findViewById((R.id.txtnamejob));
        txtdiachi = (TextView)  v.findViewById(R.id.txtloca);
        txtluong = (TextView) v.findViewById((R.id.txtsalary));
        // txtbangcap = (TextView) v.findViewById(R.id.txtbc);
        txtmotacv = (TextView) v.findViewById((R.id.txtdetail));
        //  txtkn = (TextView) v.findViewById((R.id.txtexe));
        txtdotuoi = (TextView) v.findViewById((R.id.txtage));
        txtgt = (TextView) v.findViewById(R.id.txtgender);
        txtnn = (TextView) v.findViewById((R.id.txtnn));
        // txtkhac = (TextView) v.findViewById((R.id.txtkhac));
        txtdate = (TextView) v.findViewById(R.id.txtlimit);
        txtskill = (TextView) v.findViewById(R.id.txtskill);
        // txtnamecompany=(TextView)  v.findViewById((R.id.txtnamecompany));
    }
    private void actionGetIntent() {
        if(kn.equals("")&&ngoaingu.equals("")&&dotuoi.equals(""))
        {
            LinearLayout lin = (LinearLayout) v.findViewById(R.id.linrequest);
            LinearLayout cv = (LinearLayout )v.findViewById(R.id.cardview);
            lin.setVisibility(View.GONE);
            cv.setVisibility(View.GONE);
            TextView txt = (TextView) v.findViewById(R.id.txtrequest);
            txt.setVisibility(View.VISIBLE);
            txt.setText(khac);
        }
        String[] d =diadiem.split(",");
        int maxandress=d.length;
        txtdiachi.setText(d[maxandress-3]+","+d[maxandress-2]);
        txtluong.setText(arrsalary[luong]);
        Log.d("ccccc",luong+"2");
        txtdate.setText(ngayup );
        txtmotacv.setText(motacv );
        txttencv.setText(tencv );
        txtdotuoi.setText(dotuoi );
        txtgt.setText(arrsex[gt] );
        Log.d("ccccc",gt+"1");
        txtnn.setText(ngoaingu );
        txtskill.setText(kn+"");
        txtnumber.setText(number+"");
        txtcarrer.setText(arrnganh[Integer.parseInt(carrer)]);
        String time= datecreate;
        Long gettime=getDateInMillis(time);
        String timeago=getTimeAgo(gettime);
        txtdateup.setText(timeago);
        txtphucloi.setText(phucloi);
     //   id = id;
    }
    private void parseJsonFeed(String result) {
        Log.d(AppController.TAG, "Login Response: " + result);

        if (result.equals("") || result == null) {
            Toast.makeText(getActivity(),R.string.st_errServer, Toast.LENGTH_SHORT).show();
            //   progressBar.setVisibility(View.GONE);
            return;
        }
        if (result.equals("1")) {
//            Toast.makeText(getActivity(),R.string.toast_apply, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(getContext().getResources().getString(R.string.toast_apply))
                    .show();
        }
        if (result.equals("0")) {
//            Toast.makeText(getActivity(),R.string.toast_alreadyapply, Toast.LENGTH_SHORT).show();
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getContext().getResources().getString(R.string.toast_alreadyapply))
                    .show();
            dialog.dismiss();
        }
        try {
            JSONArray mang = new JSONArray(result);
            if (mang.length() > 0) {
                for (int i = 0; i < mang.length(); i++) {
                    JSONObject ob = mang.getJSONObject(i);
                    profile.add(new Profile(ob.getString("nganhnghe"),
                            ob.getString("vitri"), ob.getString("mucluong"),
                            ob.getString("diadiem"), ob.getString("createdate"),
                            ob.getString("mahs"), ob.getString("hoten"),
                            ob.getString("gioitinh2"),
                            ob.getString("ngaysinh"),
                            ob.getString("email"),
                            ob.getString("sdt"),
                            ob.getString("diachi"),
                            ob.getString("quequan"),
                            ob.getString("tentruong"),
                            ob.getString("chuyennganh"),
                            ob.getString("xeploai"),
                            ob.getString("thanhtuu"),
                            ob.getString("namkn"),
                            ob.getString("tencongty"),
                            ob.getString("chucdanh"),
                            ob.getString("mota"),
                            ob.getString("ngoaingu"),
                            ob.getString("kynang"),
                            ob.getString("tencv"), id,
                            ob.getString("img")));
                }
            }else{
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getData(final String id, final String macv, final String url) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                parseJsonFeed(result);
            }
        }, new Response.ErrorListener() {
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
        VolleySingleton.getInstance(getActivity()).requestQueue.add(strReq);
    }
    public String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }
        long now =System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }
        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return getActivity().getResources().getString(R.string.justnow);
        } else if (diff < 2 * MINUTE_MILLIS) {
            return getActivity().getResources().getString(R.string.anminute);
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS +" "+ getActivity().getResources().getString(R.string.minutesago);
        } else if (diff < 90 * MINUTE_MILLIS) {
            return getActivity().getResources().getString(R.string.anhour);
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS +" "+ getActivity().getResources().getString(R.string.hourago);
        } else if (diff < 48 * HOUR_MILLIS) {
            return getActivity().getResources().getString(R.string.yesterday);
        } else {
            return diff / DAY_MILLIS +" "+getActivity().getResources().getString(R.string.daysago);
        }
    }
    public static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}