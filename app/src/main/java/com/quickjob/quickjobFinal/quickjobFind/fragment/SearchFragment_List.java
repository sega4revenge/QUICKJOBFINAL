package com.quickjob.quickjobFinal.quickjobFind.fragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobFind.activity.GPSTracker;
import com.quickjob.quickjobFinal.quickjobFind.activity.MoreNew_Activity;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.config.AppController;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by VinhNguyen on 3/13/2017.
 */

public class SearchFragment_List extends Fragment {
    private ArrayList<Integer> category = new ArrayList<>();
    private List<CongViec> tempArrayList= new ArrayList<>();;
    private List<CongViec> secondlist = new ArrayList<>();
    private static final int PLACE_PICKER_REQUEST = 3;
    GPSTracker gps;
    String namecity="",uid="";
    int st=0;
    double lat,lng;
    TextView txtloca;
    AlertDialog dialog;boolean loadingfist=true;ProgressBar progressBar;
    LinearLayout linmart,linlocation,linbeauty,lincareworker,linchefcook,linconstruction,lincleaning,lindriver
            ,linevent,lineducation,linkitchenposter,linship,linretail,linsales,linwaiter,linwarehouse,linother;
    View view;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment_list, container, false);
        addView();
        eventclick();
        Intent s=getActivity().getIntent();
        namecity= s.getStringExtra("namecity");
        if(namecity.equals("All"))
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view_dialog = inflater2.inflate(R.layout.dialog_createlangues, null, false);
            final String[] arr = getResources().getStringArray(R.array.diadiem);
            arr[0] = getResources().getString(R.string.st_all_andress);
            ListView lv = (ListView) view_dialog.findViewById(R.id.lvnn);
            ArrayAdapter<String> adapterandress = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arr);
            lv.setAdapter(adapterandress);
            alertDialog.setTitle(getResources().getString(R.string.st_location));
            alertDialog.setView(view_dialog);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    hideview();
                    if (i > 0) {
                        txtloca.setText(arr[i]);
                        namecity = arr[i] + "";
                        tempArrayList.clear();
                        category.clear();
                        loadingfist=true;
                        progressBar.setVisibility(View.VISIBLE);
                        getData(namecity, uid);

                    } else {
                        txtloca.setText(arr[i]);
                        namecity = arr[i] + "";
                        tempArrayList.clear();
                        category.clear();
                        loadingfist=true;
                        progressBar.setVisibility(View.VISIBLE);
                        getData("", uid);
                    }
                    dialog.cancel();
                }
            });
            dialog = alertDialog.create();
            dialog.show();
        }else {
            uid = s.getStringExtra("uid");
            lat = s.getDoubleExtra("lat", 20);
            lng = s.getDoubleExtra("lng", 20);
        }
        txtloca.setText(namecity);
        getData(namecity,uid);
        return view;
    }
    private void parseJsonFeed(String result) {

        Log.d(AppController.TAG, "Search Response: " + result);

        if (result.equals("") || result == null) {
            Toast.makeText(getActivity(),R.string.st_errServer, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        try {

            JSONArray jObj3 = new JSONObject(result).getJSONArray("alljob");
            JSONArray jObj = new JSONObject(result).getJSONArray("category");
            if(jObj3.length()>0 || jObj.length()>0)
            {
                getData2(jObj3, 2);
                getDataCategory(jObj);
            }else{
                loadingfist = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.GONE);
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
    private void getDataCategory(JSONArray jObj) {

        int count=jObj.length();

        for(int i=0;i<count;i++)
        {
            try {
                category.add(jObj.getInt(i));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getStatusShowHideView(category);

    }
    public void getData2(JSONArray joc, int a){
        int countdata= joc.length();
        for (int i = 0; i < countdata; i++) {
            try {
                JSONObject ob = joc.getJSONObject(i);
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
                cv.setDatecreate(ob.getString("ngaydang"));
                cv.setStatussave(ob.getString("statussave"));
                cv.setTrangthai("4");
                tempArrayList.add(cv);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        location();
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
    private void location() {

        if (tempArrayList.size() > 0) {
            for (int i = 0; i < tempArrayList.size(); i++) {
                for(int n=i+1;n<tempArrayList.size();n++)
                {
                    double lati = Double.parseDouble(tempArrayList.get(i).getLng());
                    double lngi =Double.parseDouble(tempArrayList.get(i).getLat());
                    double latii =Double.parseDouble(tempArrayList.get(n).getLng());
                    double lngii =Double.parseDouble(tempArrayList.get(n).getLat());
                    double dis1 = getDistance(lat,lng,lati,lngi);
                    double dis2 = getDistance(lat,lng,latii,lngii);
                    if(dis1>1000)
                    {
                        dis1=dis1/1000;
                        dis1=Math.round(dis1);
                        tempArrayList.get(i).setDistance(dis1+" km");

                    }else{
                        // dis1=dis2;
                        dis1=Math.round(dis1);
                        tempArrayList.get(i).setDistance(dis1+" m");
                    }
                    if(dis2>1000)
                    {
                        dis2=dis2/1000;
                        dis2=Math.round(dis2);
                        tempArrayList.get(n).setDistance(dis2+" km");
                    }else{
                        //dis2=dis2;
                        dis2=Math.round(dis2);
                        tempArrayList.get(n).setDistance(dis2+" m");
                    }

                    if(getDistance(lat,lng,lati,lngi)>getDistance(lat,lng,latii,lngii)){
                        secondlist.add(tempArrayList.get(i));
                        tempArrayList.set(i,tempArrayList.get(n));
                        tempArrayList.set(n,secondlist.get(0));
                        secondlist.clear();
                    }
                }
            }

        }
    }

    private void eventclick() {
        linlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!loadingfist) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view_dialog = inflater.inflate(R.layout.dialog_createlangues, null, false);
                    final String[] arr = getResources().getStringArray(R.array.diadiem);
                    arr[0] = getResources().getString(R.string.st_all_andress);
                    ListView lv = (ListView) view_dialog.findViewById(R.id.lvnn);
                    ArrayAdapter<String> adapterandress = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arr);
                    lv.setAdapter(adapterandress);
                    alertDialog.setTitle(getResources().getString(R.string.st_location));
                    alertDialog.setView(view_dialog);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            hideview();
                            if (i > 0) {
                                txtloca.setText(arr[i]);
                                namecity = arr[i] + "";
                                tempArrayList.clear();
                                category.clear();
                                loadingfist=true;
                                progressBar.setVisibility(View.VISIBLE);
                                getData(namecity, uid);

                            } else {
                                txtloca.setText(arr[i]);
                                namecity = arr[i] + "";
                                tempArrayList.clear();
                                category.clear();
                                loadingfist=true;
                                progressBar.setVisibility(View.VISIBLE);
                                getData("", uid);
                            }
                            dialog.cancel();
                        }
                    });
                    dialog = alertDialog.create();
                    dialog.show();
                    //   locationPlacesIntent();
                }
            }
        });
        linmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,1);
            }
        });
        linbeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,2);
            }
        });
        lincareworker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,3);}
        }
        );
        linchefcook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,4);
            }
        });
        linconstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {IntentData(tempArrayList,7);
            }
        });
        lincleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,5);
            }
        });
        lindriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,8);
            }
        });
        linevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,6);
            }
        });
        lineducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,9);
            }
        });
        linkitchenposter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,10);
            }
        });
        linship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,11);
            }
        });
        linretail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,12);
            }
        });
        linsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,13);
            }
        });
        linwaiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,14);
            }
        });
        linwarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,15);
            }
        });
        linother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentData(tempArrayList,16);
            }
        });
    }
    private void IntentData(List<CongViec> list,int pos){
        List<CongViec> category = getObject(list,pos);
        if(category != null || category.size()>0) {
            Intent s = new Intent(getApplicationContext(), MoreNew_Activity.class);
            s.putExtra("object", (Serializable) category);
            s.putExtra("namecity",namecity);
            startActivity(s);
        }
    }
    private  List<CongViec> getObject(List<CongViec> object,int pos){
        List<CongViec> ob =new ArrayList<>();
        for(int i=0;i<object.size();i++)
        {
            if(Integer.parseInt(object.get(i).getNganhNghe())==pos)
            {
                ob.add(object.get(i));
            }
        }
        return ob;
    }
    private void locationPlacesIntent() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    private void hideview(){
        if(linmart.getVisibility()!= View.GONE){linmart.setVisibility(View.GONE);}
        if(linbeauty.getVisibility()!= View.GONE){linbeauty.setVisibility(View.GONE);}
        if(lincareworker.getVisibility()!= View.GONE){lincareworker.setVisibility(View.GONE);}
        if(linchefcook.getVisibility()!= View.GONE){linchefcook.setVisibility(View.GONE);}
        if(linconstruction.getVisibility()!= View.GONE){linconstruction.setVisibility(View.GONE);}
        if(lincleaning.getVisibility()!= View.GONE){lincleaning.setVisibility(View.GONE);}
        if(lindriver.getVisibility()!= View.GONE){lindriver.setVisibility(View.GONE);}
        if(linevent.getVisibility()!= View.GONE){linevent.setVisibility(View.GONE);}
        if(lineducation.getVisibility()!= View.GONE){lineducation.setVisibility(View.GONE);}
        if(linkitchenposter.getVisibility()!= View.GONE){linkitchenposter.setVisibility(View.GONE);}
        if(linship.getVisibility()!= View.GONE){linship.setVisibility(View.GONE);}
        if(linretail.getVisibility()!= View.GONE){linretail.setVisibility(View.GONE);}
        if(linsales.getVisibility()!= View.GONE){linsales.setVisibility(View.GONE);}
        if(linwaiter.getVisibility()!= View.GONE){linwaiter.setVisibility(View.GONE);}
        if(linwarehouse.getVisibility()!= View.GONE){linwarehouse.setVisibility(View.GONE);}
        if(linother.getVisibility()!= View.GONE){linother.setVisibility(View.GONE);}
    }
    private void getStatusShowHideView(ArrayList<Integer> category) {
        if(category.size()>0)
        {
            for(int i=0;i<category.size();i++)
            {
                switch (category.get(i))
                {
                    case 1:
                        if(linmart.getVisibility()!= View.VISIBLE){linmart.setVisibility(View.VISIBLE);}
                        break;
                    case 2:
                        if(linbeauty.getVisibility()!= View.VISIBLE){linbeauty.setVisibility(View.VISIBLE);}
                        break;
                    case 3:
                        if(lincareworker.getVisibility()!= View.VISIBLE){lincareworker.setVisibility(View.VISIBLE);}
                        break;
                    case 4:
                        if(linchefcook.getVisibility()!= View.VISIBLE){linchefcook.setVisibility(View.VISIBLE);}
                        break;
                    case 5:
                        if(lincleaning.getVisibility()!= View.VISIBLE){lincleaning.setVisibility(View.VISIBLE);}
                        break;
                    case 6:
                        if(linevent.getVisibility()!= View.VISIBLE){linevent.setVisibility(View.VISIBLE);}
                        break;
                    case 7:
                        if(linconstruction.getVisibility()!= View.VISIBLE){linconstruction.setVisibility(View.VISIBLE);}
                        break;
                    case 8:
                        if(lindriver.getVisibility()!= View.VISIBLE){lindriver.setVisibility(View.VISIBLE);}
                        break;
                    case 9:
                        if(lineducation.getVisibility()!= View.VISIBLE){lineducation.setVisibility(View.VISIBLE);}
                        break;
                    case 10:
                        if(linkitchenposter.getVisibility()!= View.VISIBLE){linkitchenposter.setVisibility(View.VISIBLE);}
                        break;
                    case 11:
                        if(linship.getVisibility()!= View.VISIBLE){linship.setVisibility(View.VISIBLE);}
                        break;
                    case 12:
                        if(linretail.getVisibility()!= View.VISIBLE){linretail.setVisibility(View.VISIBLE);}
                        break;
                    case 13:
                        if(linsales.getVisibility()!= View.VISIBLE){linsales.setVisibility(View.VISIBLE);}
                        break;
                    case 14:
                        if(linwaiter.getVisibility()!= View.VISIBLE){linwaiter.setVisibility(View.VISIBLE);}
                        break;
                    case 15:
                        if(linwarehouse.getVisibility()!= View.VISIBLE){linwarehouse.setVisibility(View.VISIBLE);}
                        break;
                    case 16:
                        if(linother.getVisibility()!= View.VISIBLE){linother.setVisibility(View.VISIBLE);}
                        break;

                }
            }
        }
        loadingfist = false;
    }


    private void addView() {
        progressBar = (ProgressBar) view.findViewById(R.id.pbLoader);
        txtloca= (TextView) view.findViewById(R.id.txtloca) ;
        linmart = (LinearLayout) view.findViewById(R.id.btbmart);
        linlocation = (LinearLayout) view.findViewById(R.id.btnlocation);
        linbeauty = (LinearLayout) view.findViewById(R.id.btnbeauty);
        lincareworker = (LinearLayout) view.findViewById(R.id.btncareworkers);
        linchefcook = (LinearLayout) view.findViewById(R.id.btnchefcook);
        linconstruction = (LinearLayout) view.findViewById(R.id.btnconstruction);
        lincleaning = (LinearLayout) view.findViewById(R.id.btncleaning);
        lindriver = (LinearLayout) view.findViewById(R.id.btndriver);
        linevent = (LinearLayout) view.findViewById(R.id.btnevents);
        lineducation = (LinearLayout) view.findViewById(R.id.btneducation);
        linkitchenposter = (LinearLayout) view.findViewById(R.id.btnkitchenporter);
        linship = (LinearLayout) view.findViewById(R.id.btbship);
        linretail = (LinearLayout) view.findViewById(R.id.btnretail);
        linsales = (LinearLayout) view.findViewById(R.id.btnsales);
        linwaiter = (LinearLayout) view.findViewById(R.id.btnwaiter);
        linwarehouse = (LinearLayout) view.findViewById(R.id.btnwarehouse);
        linother = (LinearLayout) view.findViewById(R.id.btnother);

    }



    public void search(String nganhnghe,String diadiem){
        st=0;
//        if (adapter != null) {
//            celebrities.clear();
//            adapter.notifyDataSetChanged();
//        }
//        adapter = new RecyclerAdapter(getActivity(), celebrities, 2, "", 1);
//        recyclerView.setAdapter(adapter);
//        begin=1;
//        loading = true;
//        beginloadmore=0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                if (place != null) {
                    LatLng latLng = place.getLatLng();
                    lat = latLng.latitude;
                    lng = latLng.longitude;
                    String add = (String) place.getAddress();
                    String[] arrandress = add.split(",");
                    int count = arrandress.length;
                    namecity = arrandress[count - 2];
                    if(namecity!=null || !namecity.equals(""))
                    {
                        hideview();
                        tempArrayList.clear();
                        category.clear();
                        txtloca.setText(namecity+"");
                        getData(namecity, uid);
                    }

                }
            }
        }

    }
}
