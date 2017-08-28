package com.quickjob.quickjobFinal.quickjobFind.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobFind.activity.MainActivity;
import com.quickjob.quickjobFinal.quickjobFind.adapter.JobManager_RecyclerAdapter;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.config.AppController;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobSaveFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JobSaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobSaveFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters



    private LinearLayout lnjr;
    private RecyclerView recyclerView;
    private View view;
    private String uid;
    private LinearLayout progressBar;
//    private AlertDialog progressDialog;
    private JobManager_RecyclerAdapter adapter = null;
    private List<CongViec> celebrities = new ArrayList<CongViec>();
    private JSONArray mang;
    boolean stt =false;
    private OnFragmentInteractionListener mListener;
    int sm=0;
    RelativeLayout coordinatorLayout;
    int[]  pos;
    public JobSaveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JobSaveFragment newInstance(String param1, String param2) {
        JobSaveFragment fragment = new JobSaveFragment();
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
        view = inflater.inflate(R.layout.fragment_job_saves,container,false);

        coordinatorLayout = (RelativeLayout) view.findViewById(R.id.coor);
        lnjr = (LinearLayout) view.findViewById(R.id.linjr);
        progressBar = (LinearLayout) view.findViewById(R.id.load);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        final Intent i = getActivity().getIntent();
        uid= MainActivity.uid;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
       // Cache cache = AppController.getInstance().getRequestQueue().getCache();
      //  Cache.Entry entry = cache.get(AppConfig.URL_XUATCV_DALUU);
     /*     if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                parseJsonFeed(data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }*/
        getData(uid);


        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
      //  getActivity().registerReceiver(this.appendChatScreenMsgReceiver, new IntentFilter("appendChatScreenMsg"));
        if(lnjr.getVisibility()==View.VISIBLE)
        {
            lnjr.setVisibility(View.GONE);
        }
        if(adapter!= null)
        {
            celebrities.clear();
            adapter.notifyDataSetChanged();
        }
      getData(uid);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {
//        if(appendChatScreenMsgReceiver!=null)
//            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(appendChatScreenMsgReceiver);
        super.onDestroy();


    }
    BroadcastReceiver appendChatScreenMsgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            if (b != null) {
                if (b.getBoolean("reload")) {
                    if(lnjr.getVisibility()==View.VISIBLE)
                    {
                        lnjr.setVisibility(View.GONE);
                    }
                    celebrities.clear();

                    VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().remove(AppConfig.URL_XUATCV_DALUU);
                    getData(uid);
                }
            }
        }
    };
    private void getData(final String uid) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                                                 AppConfig.URL_XUATCV_DALUU, new com.android.volley.Response.Listener<String>() {
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
        if(result.equals("") || result == null){
            //                Toast.makeText(getActivity(), R.string.st_errServer, Toast.LENGTH_SHORT).show();
            //                progressDialog.dismiss();
            progressBar.setVisibility(View.GONE);
            return;
        }
        try {
            celebrities.clear();
            mang = new JSONArray(result);
            if (mang.length() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                for (int i = 0; i < mang.length(); i++) {
                    JSONObject ob = mang.getJSONObject(i);
                    CongViec cv = new CongViec();
                    cv.setMacv(ob.getString("macv"));
                    cv.setTencongviec(ob.getString("tencv"));
                    cv.setTecongty(ob.getString("tenct"));
                    cv.setLuong(ob.getString("mucluong"));
                    cv.setDiadiem(ob.getString("diadiem"));
                    cv.setDateup(ob.getString("hannophoso"));
                    cv.setBangcap(ob.getString("bangcap"));
                    cv.setMotacv(ob.getString("motacv"));
                    cv.setDotuoi(ob.getString("dotuoi"));
                    cv.setNgoaingu(ob.getString("ngoaingu"));
                    cv.setNganhNghe(ob.getString("nganhnghe"));
                    cv.setGioitinh(ob.getString("gioitinh"));
                    cv.setKhac(ob.getString("khac"));
                    cv.setKn(ob.getString("kynang"));
                    cv.setUrl(ob.getString("img"));
                    cv.setSdt(ob.getString("phone"));
                    cv.setMotact(ob.getString("motact"));
                    cv.setStatussave(ob.getString("statussave"));
                    cv.setTrangthai("3");
                    celebrities.add(cv);
                }
                adapter = new JobManager_RecyclerAdapter(getActivity(), celebrities, 2, "", 1,recyclerView);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            } else {
                lnjr.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
