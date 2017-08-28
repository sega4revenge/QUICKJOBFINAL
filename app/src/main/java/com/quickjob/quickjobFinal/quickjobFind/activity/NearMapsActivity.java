package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.config.AppController;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NearMapsActivity extends FragmentActivity {
    Marker mPerth;
    private Marker mSydney, mLocationPicker;
    private List<CongViec> celebrities = new ArrayList<>();
    private GoogleMap mMap;
    double lat, lng;
    SessionManager session;
    String uid, namecity;
    double radiusInMeters = 10000;
    boolean isFirst = false;
    Circle circle;
    int number;
    ArrayList<Marker> listJob = new ArrayList<Marker>();
    LatLng me;
    Button btlayout,btdetail;
    int idlayout = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        Intent i = getIntent();
        lat = i.getDoubleExtra("lat", 20);
        lng = i.getDoubleExtra("lng", 20);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        btlayout = (Button)findViewById(R.id.btlayout);
        btdetail = (Button)findViewById(R.id.btdetailjob);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            namecity = addresses.get(0).getAdminArea();
        }
        else {
            namecity = "";
        }
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        uid = user.get(SessionManager.KEY_ID);
        Toast.makeText(this, namecity + "", Toast.LENGTH_SHORT).show();
        getData(lat + "", lng + "", uid);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


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
        VolleySingleton.getInstance(this).requestQueue.add(strReq);
    }

    private void parseJsonFeed(String result) {
        System.out.println("Resulted Value: " + result);
        if (result.equals("") || result == null) {
            Toast.makeText(NearMapsActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
            //                progressDialog.dismiss();
            return;
        }
        try {
            celebrities.clear();
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
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(new OnMapReadyCallback() {
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
                                                            if(idlayout>=4){
                                                                idlayout = 1;
                                                                mMap.setMapType(idlayout);
                                                            }
                                                            else
                                                            {
                                                                idlayout++;
                                                                mMap.setMapType(idlayout);
                                                            }

                                                        }
                                                    }
                        );
                        System.out.println(celebrities.size());
                        me = new LatLng(lat, lng);
                        mSydney = mMap.addMarker(new MarkerOptions()
                                                         .position(me)
                                                         .icon(BitmapDescriptorFactory.fromResource(R.drawable.me))
                                                         .title("Me"));
                        mSydney.setTag(19931995);
                        if (!isFirst) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14.0f));
                            isFirst = true;
                            CircleOptions circleOptions = new CircleOptions().center(me).radius(radiusInMeters).fillColor(Color.argb(100, 78, 200, 156)).strokeColor(Color.argb(100, 78, 200, 156)).strokeWidth(8);
                            circle = googleMap.addCircle(circleOptions);
                        }
                        for (int i = 0; i < celebrities.size(); i++) {
                            LatLng PERTH = new LatLng(Double.parseDouble(celebrities.get(i).getLng()), Double.parseDouble(celebrities.get(i).getLat()));
                            listJob.add(mMap.addMarker(new MarkerOptions()
                                                               .position(PERTH)
                                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder))
                                                               .snippet(celebrities.get(i).getLuong())
                                                               .snippet("Company:" + celebrities.get(i).getTecongty())
                                                               .title(celebrities.get(i).getTencongviec())));
                            listJob.get(i).setTag(i);
                        }
                        btdetail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(number!=19931995) {
                                    Intent s = new Intent(NearMapsActivity.this, JobDetailActivity.class);
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
                                VolleySingleton.getInstance(NearMapsActivity.this).requestQueue.cancelAll(new RequestQueue.RequestFilter() {
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
                                circle.remove();
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
                        if (ActivityCompat.checkSelfPermission(NearMapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NearMapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




}
