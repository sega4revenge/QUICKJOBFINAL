package com.quickjob.quickjobFinal.quickjobFind.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobFind.activity.GPSTracker;
import com.quickjob.quickjobFinal.quickjobFind.activity.JobDetailActivity;
import com.quickjob.quickjobFinal.quickjobFind.adapter.ListJobCompanyAdapter;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.config.AppController;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by VinhNguyen on 3/10/2017.
 */

public class MapNearFragment extends Fragment {
    View view;
    Double lat, lng;
    String namecity, uid, textstring;
    SessionManager session;
    private GoogleMap mMap, googleMap;
    Marker mPerth;
    private Marker mSydney, mLocationPicker;
    private List<CongViec> celebrities = new ArrayList<>();
    double radiusInMeters = 10000;
    boolean isFirst = false;
    Circle circle;
    int number;
    ArrayList<Marker> listJob = new ArrayList<Marker>();
    LatLng me;
    MapView mapview_location;
    RecyclerView rec;
    ListJobCompanyAdapter adapter;
    int idlayout = 1;
    View bottomsheet;
    TextView txttitle, txtnotfind;
    GPSTracker gps;
    BottomSheetBehavior bottomsheetLay;
    Button btlayout, btdetail;
    private static final String TAG = MapNearFragment.class.getSimpleName();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FacebookSdk.sdkInitialize(getActivity());
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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textstring = getResources().getString(R.string.st_findjob);
        if (CheckNetwork()) {
          getloca();
        }
    }
    private  void getloca(){
        namecity= getlocation();
        Toast.makeText(getActivity(), namecity+"", Toast.LENGTH_SHORT).show();
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        uid = user.get(SessionManager.KEY_ID);
        if(lat== null || lat==0 || lng==null || lng==0)
        {}else {
            getData(lat + "", lng + "", uid + "");
        }
    }
    private String getlocation(){
        String namecity="";
        gps = new GPSTracker(getActivity());
        if(gps.canGetLocation()){
            lat = gps.getLatitude();
            lng = gps.getLongitude();
            if(lat== null || lat==0 || lng==null || lng==0)
            {
                new CountDownTimer(5000,5000 ) {
                    @Override
                    public void onTick(long l) {
                    }
                    @Override
                    public void onFinish() {
                        getloca();
                    }
                }.start();
            }else {
                namecity = getAddress(lat, lng);
            }
        }else{
            gps.showSettingsAlert();
        }
        return namecity;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mapnear_fragment, container, false);
        mapview_location = (MapView) view.findViewById(R.id.mapView_location);
        mapview_location.onCreate(savedInstanceState);
        mapview_location.onResume();
        txttitle = (TextView) view.findViewById(R.id.bottomSheetHeading);
        bottomsheet = view.findViewById(R.id.bottomSheetLayout);
        bottomsheetLay = BottomSheetBehavior.from(bottomsheet);
        txtnotfind = (TextView) view.findViewById(R.id.txtnotfind);
        btlayout = (Button) view.findViewById(R.id.btlayout);
        btdetail = (Button) view.findViewById(R.id.btdetailjob);
        rec = (RecyclerView) view.findViewById(R.id.recycler_view);
        rec.setScrollContainer(false);
        adapter = new ListJobCompanyAdapter(getActivity(), celebrities, 3, "", 3);
        rec.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager_gm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rec.setLayoutManager(linearLayoutManager_gm);
        rec.setItemAnimator(new DefaultItemAnimator());
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        bottomsheetLay.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        //    if (showFAB)
                        //      fab.startAnimation(shrinkAnimation);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        //      showFAB = true;
                        //     fab.setVisibility(View.VISIBLE);
                        //     fab.startAnimation(growAnimation);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        //    showFAB = false;
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }

        });
        txttitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomsheetLay.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomsheetLay.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomsheetLay.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PlaceAutocompleteFragment autocompleteFragment;
        if(Integer.valueOf(android.os.Build.VERSION.SDK)>=21) {
             autocompleteFragment = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment1);
        }else{
             autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment1);
        }
        EditText ed = ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input));
        View v = ((View) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_fragment1));
        v.setBackgroundColor(getResources().getColor(R.color.white));
        ed.setBackgroundColor(getResources().getColor(R.color.white));
        ed.setTextSize(14);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("AAAAA", "Place: " + place.getName());

                String placeDetailsStr = place.getName() + "\n"
                        + place.getId() + "\n"
                        + place.getLatLng().toString() + "\n"
                        + place.getAddress() + "\n"
                        + place.getAttributions();

                if (place.getName() != null || !place.getName().equals("")) {
                    LatLng latlng = place.getLatLng();
                    Double latt = latlng.latitude;
                    Double lngg = latlng.longitude;
                    if (mLocationPicker != null) {
                        mLocationPicker.remove();
                    }
                    for (int i = 0; i < listJob.size(); i++) {

                        listJob.get(i).remove();
                    }
                    listJob.clear();
                    if(circle!= null) {
                        circle.remove();
                    }
                    mLocationPicker = mMap.addMarker(new MarkerOptions()
                            .position(place.getLatLng())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                            .title("Location"));
                    mLocationPicker.setTag(19931995);
                    getData(latt + "", lngg + "", uid);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, lngg), 11.0f));
                    CircleOptions circleOptions = new CircleOptions().center(place.getLatLng()).radius(radiusInMeters).fillColor(Color.argb(100, 78, 200, 156)).strokeColor(Color.argb(100, 78, 200, 156)).strokeWidth(8);
                    circle = mMap.addCircle(circleOptions);

                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("AAAAA", "An error occurred: " + status);
            }
        });
    }

    private void getData(final String lat, final String lng, final String uid) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/


        // Tag used to cancel the request

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MAPSNEAR, new com.android.volley.Response.Listener<String>() {
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
                params.put("lat", lat + "");
                params.put("lot", lng + "");
                params.put("uid", uid);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(getActivity()).requestQueue.add(strReq);
    }

    private void parseJsonFeed(String result) {
        System.out.println("Resulted Value: " + result);
        if (result.equals("") || result == null) {
            Toast.makeText(getActivity(), R.string.st_errServer, Toast.LENGTH_SHORT).show();
            //                progressDialog.dismiss();
            return;
        }
        try {
            celebrities.clear();
            txtnotfind.setVisibility(View.GONE);
            JSONArray mang = new JSONArray(result);
            int countdata = mang.length();
            if (countdata > 0) {
                for (int i = 0; i < countdata; i++) {
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
                    cv.setMatd(ob.getString("matd"));
                    cv.setViews(ob.getString("views"));
                    cv.setLat(ob.getString("lat"));
                    cv.setLng(ob.getString("long"));
                    cv.setStatussave(ob.getString("statussave"));
                    cv.setTrangthai("3");
                    celebrities.add(cv);

                }
                txttitle.setText(textstring + " (" + celebrities.size() + ")");
                adapter.notifyDataSetChanged();
                if (bottomsheetLay.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomsheetLay.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                mapview_location.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap = googleMap;
                        if (mPerth != null) {
                            mPerth.remove();
                        }
                        btlayout.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            System.out.println(idlayout);
                                                            if (idlayout >= 4) {
                                                                idlayout = 1;
                                                                mMap.setMapType(idlayout);
                                                            } else {
                                                                idlayout++;
                                                                mMap.setMapType(idlayout);
                                                            }

                                                        }
                                                    }
                        );

                        me = new LatLng(lat, lng);
                        mSydney = mMap.addMarker(new MarkerOptions()
                                .position(me)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation))
                                .title("Me"));
                        mSydney.setTag(19931995);
                        if (!isFirst) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14.0f));
                            isFirst = true;
                            CircleOptions circleOptions = new CircleOptions().center(me).radius(radiusInMeters).fillColor(Color.argb(100, 78, 200, 156)).strokeColor(Color.argb(100, 78, 200, 156)).strokeWidth(8);
                            circle = googleMap.addCircle(circleOptions);
                        }
                        for (int i = 0; i < celebrities.size(); i++) {
                            LatLng PERTH = new LatLng(Double.parseDouble(celebrities.get(i).getLat()), Double.parseDouble(celebrities.get(i).getLng()));
                            listJob.add(mMap.addMarker(new MarkerOptions()
                                    .position(PERTH)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location2))
                                    .snippet(celebrities.get(i).getLuong())
                                    .snippet(celebrities.get(i).getTecongty())
                                    .title(celebrities.get(i).getTencongviec())));
                            dropPinEffect(listJob.get(i));
                            listJob.get(i).setTag(i);
                        }
                        btdetail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (number != 19931995) {
                                    Intent s = new Intent(getActivity(), JobDetailActivity.class);
                                    s.putExtra("tencongty", celebrities.get(number).tecongty);
                                    s.putExtra("tencongviec", celebrities.get(number).tencongviec);
                                    s.putExtra("diadiem", celebrities.get(number).diadiem);
                                    s.putExtra("mucluong", celebrities.get(number).luong);
                                    s.putExtra("ngayup", celebrities.get(number).dateup);
                                    s.putExtra("yeucaubangcap", celebrities.get(number).bangcap);
                                    s.putExtra("dotuoi", celebrities.get(number).dotuoi);
                                    s.putExtra("ngoaingu", celebrities.get(number).ngoaingu);
                                    s.putExtra("gioitinh", celebrities.get(number).gioitinh);
                                    s.putExtra("khac", celebrities.get(number).khac);
                                    s.putExtra("motacv", celebrities.get(number).motacv);
                                    s.putExtra("kn", celebrities.get(number).kn);
                                    s.putExtra("macv", celebrities.get(number).macv);
                                    s.putExtra("img", celebrities.get(number).url);
                                    s.putExtra("sdt", celebrities.get(number).sdt);
                                    s.putExtra("motact", celebrities.get(number).motact);
                                    s.putExtra("matd", celebrities.get(number).matd);
                                    s.putExtra("statussave", celebrities.get(number).statussave);
                                    s.putExtra("type", 3);
                                    startActivity(s);
                                }
                            }
                        });
                        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                VolleySingleton.getInstance(getActivity()).requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                                    @Override
                                    public boolean apply(Request<?> request) {
                                        return true;
                                    }
                                });
                                if (mLocationPicker != null) {
                                    mLocationPicker.remove();
                                }
                                for (int i = 0; i < listJob.size(); i++) {

                                    listJob.get(i).remove();
                                }
                                listJob.clear();
                                if(circle!= null) {
                                    circle.remove();
                                }
                                btdetail.setVisibility(View.GONE);
                                mLocationPicker = mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                                        .title("Location"));
                                mLocationPicker.setTag(19931995);
                                getData(latLng.latitude + "", latLng.longitude + "", uid);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 11.0f));
                                CircleOptions circleOptions = new CircleOptions().center(latLng).radius(radiusInMeters).fillColor(Color.argb(100, 78, 200, 156)).strokeColor(Color.argb(100, 78, 200, 156)).strokeWidth(8);
                                circle = mMap.addCircle(circleOptions);

                            }
                        });


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
                        mMap.setMyLocationEnabled(true);
                        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener()
                        {
                            @Override
                            public boolean onMyLocationButtonClick()
                            {

                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14.0f));
                                return true;
                            }
                        });
                        // Set a listener for marker click.
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                number = (Integer)marker.getTag();
                                btdetail.setVisibility(View.VISIBLE);
                                return false;
                            }
                        });

                    }
                });
            }else{
                mapview_location.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap = googleMap;
                        if (mPerth != null) {
                            mPerth.remove();
                        }
                        mSydney = mMap.addMarker(new MarkerOptions()
                                .position(me)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation))
                                .title("Me"));
                        mSydney.setTag(19931995);
                        if (!isFirst) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14.0f));
                            isFirst = true;
                            CircleOptions circleOptions = new CircleOptions().center(me).radius(radiusInMeters).fillColor(Color.argb(100, 78, 200, 156)).strokeColor(Color.argb(100, 78, 200, 156)).strokeWidth(8);
                            circle = googleMap.addCircle(circleOptions);
                        }
                        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                VolleySingleton.getInstance(getActivity()).requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                                    @Override
                                    public boolean apply(Request<?> request) {
                                        return true;
                                    }
                                });
                                if (mLocationPicker != null) {
                                    mLocationPicker.remove();
                                }
                                for (int i = 0; i < listJob.size(); i++) {

                                    listJob.get(i).remove();
                                }
                                listJob.clear();
                                if(circle!= null) {
                                    circle.remove();
                                }
                                btdetail.setVisibility(View.GONE);
                                mLocationPicker = mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                                        .title("Location"));
                                mLocationPicker.setTag(19931995);
                                getData(latLng.latitude + "", latLng.longitude + "", uid);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 11.0f));
                                CircleOptions circleOptions = new CircleOptions().center(latLng).radius(radiusInMeters).fillColor(Color.argb(100, 78, 200, 156)).strokeColor(Color.argb(100, 78, 200, 156)).strokeWidth(8);
                                circle = mMap.addCircle(circleOptions);

                            }
                        });


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
                        mMap.setMyLocationEnabled(true);
                        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener()
                        {
                            @Override
                            public boolean onMyLocationButtonClick()
                            {

                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14.0f));
                                return true;
                            }
                        });
                    }
                });
                if(bottomsheetLay.getState()==BottomSheetBehavior.STATE_HIDDEN)
                {
                    bottomsheetLay.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                me = new LatLng(lat, lng);

                celebrities.clear();
                adapter.notifyDataSetChanged();
                txtnotfind.setText(getResources().getString(R.string.errorfindjob)+" here");
                txtnotfind.setVisibility(View.VISIBLE);
                txttitle.setText(getResources().getString(R.string.st_findjob)+" (0)");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void dropPinEffect(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post again 15ms later.
                    handler.postDelayed(this, 15);
                } else {
                    marker.showInfoWindow();

                }
            }
        });
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
}
