package com.quickjob.quickjobFinal.quickjobHire.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobHire.adapter.CongViecAdapter;
import com.quickjob.quickjobFinal.quickjobHire.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobHire.config.AppController;
import com.quickjob.quickjobFinal.quickjobHire.model.CongViec;
import com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListJobFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListJobFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View rootView;
    private LinearLayout lin2;int hoso;
    private ListView lv;
    private CongViecAdapter adapter;
    private ArrayList<CongViec> celebrities = new ArrayList<CongViec>();
    private String id;
    private TextView ss;int status=0;
    private AlertDialog progressDialog;
    private String dc="",logo1="",nn="",mt="",qm="",tenct="",pl="";
    SessionManager session;
    SharedPreferences.Editor edit;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    JSONArray mang;
    ProgressBar progressBar;
    String[] arrsalary;
    public ListJobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListJobFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListJobFragment newInstance(String param1, String param2) {
        ListJobFragment fragment = new ListJobFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.list_job_layout_hire, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pbLoader);
        ss = (TextView) rootView.findViewById(R.id.lin);
        lin2 = (LinearLayout) rootView.findViewById(R.id.lin2);
        arrsalary = getResources().getStringArray(R.array.mucluong);
        session = new SessionManager(getActivity());
        SharedPreferences pref=getActivity().getSharedPreferences("JobFindPref", MODE_PRIVATE);
        edit=pref.edit();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        id=user.get(SessionManager.KEY_ID);
        lv =(ListView) rootView.findViewById(R.id.listhoso);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                try {
//                    JSONObject obs = mang.getJSONObject(i);
//                    Intent s = new Intent(getActivity(), Edit_Job_activity.class);
//                    s.putExtra("macv", obs.getString("macv"));
//                    s.putExtra("matd", obs.getString("matd"));
//                    s.putExtra("tencongty", obs.getString("tenct"));
//                    s.putExtra("diachi", obs.getString("diachi"));
//                 //   s.putExtra("nganhnghe", obs.getString("nganhnghe"));
//                 //   s.putExtra("quymo", obs.getString("quymo"));
//                    s.putExtra("motact", obs.getString("motact"));
//                    s.putExtra("nganhNghe", obs.getString("nn"));
//                    s.putExtra("chucdanh", obs.getString("chucdanh"));
//                    s.putExtra("soluong", obs.getString("soluong"));
//                    s.putExtra("phucloi", obs.getString("phucloi"));
//                    s.putExtra("tencongviec", obs.getString("tencv"));
//                    s.putExtra("diadiem", obs.getString("diadiem"));
//                    s.putExtra("mucluong",obs.getString("mucluong"));
//                    s.putExtra("ngayup", obs.getString("hannophoso"));
//                    s.putExtra("yeucaubangcap", obs.getString("bangcap"));
//                    s.putExtra("dotuoi", obs.getString("dotuoi"));
//                    s.putExtra("ngoaingu", obs.getString("ngoaingu"));
//                    s.putExtra("gioitinh", obs.getString("gioitinh"));
//                    s.putExtra("khac", obs.getString("khac"));
//                    s.putExtra("motacv", obs.getString("motacv"));
//                    s.putExtra("kn", obs.getString("kynang"));
//                    s.putExtra("img", obs.getString("img"));
//                    startActivity(s);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        return rootView;
    }
    @Override
    public void onResume() {
        if(adapter!= null)
        {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
//        asyncRequestObject = new AsyncDataClass();
//        asyncRequestObject.execute(AppConfig.URL_XUATHS1, id);
        getData(id);
        super.onResume();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
        mListener = null;
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
    private void parseJsonFeed(String result) {
        System.out.println("Resulted Value: " + result);
        if(result.equals("") || result == null){
            Toast.makeText(getActivity(), R.string.st_errServer, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        try {
            mang = new JSONArray(result);
            if(mang.length()>0) {
                status=1;
                JSONObject obs = mang.getJSONObject(0);
                dc = obs.getString("diachi");
                nn= obs.getString("nganhnghe");
                mt = obs.getString("motact");
                qm = obs.getString("quymo");
                logo1 = obs.getString("img");
                tenct =obs.getString("tenct");
                int stt=0;
                stt=obs.getInt("status");
                if(stt==0) {
                    for (int i = 0; i < mang.length(); i++) {
                        JSONObject ob = mang.getJSONObject(i);
                        JSONArray profile = ob.getJSONArray("hoso");
                        CongViec job_profile_apply =new CongViec();
                        if(profile.length()>0)
                        {
                            job_profile_apply.setProfile_apply(profile);
                        }
                        job_profile_apply.setMacv(ob.getString("macv"));//
                        job_profile_apply.setTencongviec(ob.getString("tencv"));//
                        job_profile_apply.setQuymo(ob.getString("quymo"));//
                        job_profile_apply.setTecongty(ob.getString("tenct"));//
                        job_profile_apply.setDiachict(ob.getString("diadiem"));//
                        job_profile_apply.setNganhNghe(ob.getString("nganhNghe"));//
                        job_profile_apply.setNgannghe(ob.getString("nganhnghe"));
                        job_profile_apply.setMotact(ob.getString("motact"));
                        job_profile_apply.setLuong(ob.getString("mucluong"));//
                        job_profile_apply.setDiadiem(ob.getString("diadiem"));
                        job_profile_apply.setDateup(ob.getString("hannophoso"));//
                        job_profile_apply.setSoluong(ob.getString("soluong"));
                        job_profile_apply.setMotacv(ob.getString("motacv"));//
                        job_profile_apply.setBangcap(ob.getString("bangcap"));//
                        job_profile_apply.setNgoaingu(ob.getString("ngoaingu"));//
                        job_profile_apply.setDotuoi(ob.getString("dotuoi"));//
                        job_profile_apply.setGioitinh(ob.getString("gioitinh"));//
                        job_profile_apply.setKhac(ob.getString("khac"));//
                        job_profile_apply.setKn(ob.getString("kynang"));//
                        job_profile_apply.setUrl(ob.getString("img"));//
                        job_profile_apply.setPhucloi(ob.getString("phucloi"));//
                        celebrities.add(job_profile_apply);
                     //  celebrities.add(new CongViec(ob.getString("macv"), ob.getString("tencv"), ob.getString("tenct"), obs.getString("diachi"), obs.getString("nganhnghe"), obs.getString("motact"), obs.getString("quymo"), obs.getString("nn"), obs.getString("chucdanh"), obs.getString("soluong"), ob.getString("mucluong"), ob.getString("diadiem"), ob.getString("hannophoso"), ob.getString("motacv"), ob.getString("bangcap"), ob.getString("dotuoi"), ob.getString("ngoaingu"), ob.getString("gioitinh"), ob.getString("khac"), ob.getString("kynang"), ob.getString("img"), ob.getString("phucloi"), "5", ""));
                    }
                    adapter = new CongViecAdapter(getActivity(), android.R.layout.simple_list_item_1, celebrities, 5, "", 3);
                    lv.setAdapter(adapter);
                    ss.setVisibility(View.GONE);
                    lin2.setVisibility(View.VISIBLE);
                }else{
                    ss.setVisibility(View.VISIBLE);
                }
            }else {
                ss.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.GONE);
    }

    private void getData(final String id) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/
        // Tag used to cancel the request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_XUATHS1, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                parseJsonFeed(result);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("id", id);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(getContext()).requestQueue.add(strReq);
    }
//    private class AsyncDataClass extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            HttpParams httpParameters = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
//            HttpConnectionParams.setSoTimeout(httpParameters, 5000);
//
//            HttpClient httpClient = new DefaultHttpClient(httpParameters);
//            HttpPost httpPost = new HttpPost(params[0]);
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//
//            String jsonResult = "";
//            try {
//                nameValuePairs.add(new BasicNameValuePair("id", params[1]));
//                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
//
//                HttpResponse response = httpClient.execute(httpPost);
//                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
//                System.out.println("Returned Json object " + jsonResult.toString());
//
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return jsonResult;
//        }
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            System.out.println("Resulted Value: " + result);
//            if(result.equals("") || result == null){
//                Toast.makeText(getActivity(), R.string.st_errServer, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
//                return;
//            }
//            try {
//                 mang = new JSONArray(result);
//                if(mang.length()>0) {
//                    status=1;
//                    JSONObject obs = mang.getJSONObject(0);
//                    dc = obs.getString("diachi");
//                    nn= obs.getString("nganhnghe");
//                    mt = obs.getString("motact");
//                    qm = obs.getString("quymo");
//                    logo1 = obs.getString("img");
//                    tenct =obs.getString("tenct");
//                    int stt=0;
//                    stt=obs.getInt("status");
//                    if(stt==0) {
//                        for (int i = 0; i < mang.length(); i++) {
//                            JSONObject ob = mang.getJSONObject(i);
//                            celebrities.add(new CongViec(ob.getString("macv"), ob.getString("tencv"), ob.getString("tenct"), obs.getString("diachi"), obs.getString("nganhnghe"), obs.getString("motact"), obs.getString("quymo"), obs.getString("nn"), obs.getString("chucdanh"), obs.getString("soluong"), ob.getString("mucluong"), ob.getString("diadiem"), ob.getString("hannophoso"), ob.getString("motacv"), ob.getString("bangcap"), ob.getString("dotuoi"), ob.getString("ngoaingu"), ob.getString("gioitinh"), ob.getString("khac"), ob.getString("kynang"), ob.getString("img"), ob.getString("phucloi"), "5", ""));
//                        }
//                        adapter = new CongViecAdapter(getActivity(), android.R.layout.simple_list_item_1, celebrities, 5, "", 3);
//                        lv.setAdapter(adapter);
//                        ss.setVisibility(View.GONE);
//                        lin2.setVisibility(View.VISIBLE);
//                    }else{
//                        ss.setVisibility(View.VISIBLE);
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            progressBar.setVisibility(View.GONE);
//        }
//    }
    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            while ((rLine = br.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answer;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        if ( asyncRequestObject!= null) {
//            if (!asyncRequestObject.isCancelled()) {
//                asyncRequestObject.cancel(true);
//            }
//        }
    }
    public void updateFragmentListView(){
        if(adapter != null){
          onResume();
        }
    }
//    private void createCV() {
//        Intent i = new Intent(getActivity(), CreateJobActivity.class);
//        i.putExtra("uniqueid", id);
//        i.putExtra("tenct", tenct);
//        i.putExtra("quymo", qm);
//        i.putExtra("diachi", dc);
//        i.putExtra("nganhnghe", nn);
//        i.putExtra("mota", mt);
//        i.putExtra("logo", logo1);
//        i.putExtra("status", status);
//        startActivity(i);
//    }
}
