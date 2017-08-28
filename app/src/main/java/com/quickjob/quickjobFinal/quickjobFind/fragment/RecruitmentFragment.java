package com.quickjob.quickjobFinal.quickjobFind.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecruitmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecruitmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecruitmentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout lnjr, pLoad;
    private RecyclerView recyclerView;
    private View view;
    private String uid;
    private AlertDialog progressDialog;
    private JobManager_RecyclerAdapter adapter = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<CongViec> celebrities = new ArrayList<CongViec>();
    JSONArray mang;
    boolean stt=false;
    boolean sttfist=false;
    private OnFragmentInteractionListener mListener;

    public RecruitmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecruitmentFragment newInstance(String param1, String param2) {
        RecruitmentFragment fragment = new RecruitmentFragment();
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
        view = inflater.inflate(R.layout.fragment_job_recruitment, container, false);
        lnjr = (LinearLayout) view.findViewById(R.id.linjr);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
     //   mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        pLoad = (LinearLayout) view.findViewById(R.id.load);
        uid = MainActivity.uid;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
     /*   recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int i) {
//                        if(!stt) {
//                            try {
//                                JSONObject ob = mang.getJSONObject(i);
//                                Intent s = new Intent(getActivity(), JobDetailActivity.class);
//                                s.putExtra("tencongty", ob.getString("tenct"));
//                                s.putExtra("tencongviec", ob.getString("tencv"));
//                                s.putExtra("diadiem", ob.getString("diadiem"));
//                                s.putExtra("mucluong", ob.getString("mucluong"));
//                                s.putExtra("ngayup", ob.getString("hannophoso"));
//                                s.putExtra("yeucaubangcap", ob.getString("bangcap"));
//                                s.putExtra("dotuoi", ob.getString("dotuoi"));
//                                s.putExtra("ngoaingu", ob.getString("ngoaingu"));
//                                s.putExtra("gioitinh", ob.getString("gioitinh"));
//                                s.putExtra("khac", ob.getString("khac"));
//                                s.putExtra("motacv", ob.getString("motacv"));
//                                s.putExtra("kn", ob.getString("kynang"));
//                                s.putExtra("macv", ob.getString("macv"));
//                                s.putExtra("img", ob.getString("img"));
//                                s.putExtra("sdt", ob.getString("sdt"));
//                                s.putExtra("motact", ob.getString("motact"));
//                                s.putExtra("statussave", ob.getString("statussave"));
//                                s.putExtra("type", 3);
//                                startActivity(s);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                     //   }
                    }

                    @Override public boolean onLongItemClick(View view, final int i) {
                        stt=true;
                        if(mang.length()>0)
                        {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(R.string.st_xacNhanXoaCV);
                            builder.setCancelable(false);
                            builder.setPositiveButton(R.string.st_xacNhanOK, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sttfist=true;
                                    try {
                                        JSONObject ob =mang.getJSONObject(i);
                                        String idungtuyen= ob.getString("idungtuyen");
                                        String serverUrl = "http://quickjob.ga/xoacongviecdaungtuyen.php";
                                        AsyncDataClass asyncRequestObject = new AsyncDataClass();
                                        asyncRequestObject.execute(serverUrl,idungtuyen);
                                        stt=false;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            builder.setNegativeButton(R.string.st_xacNhanCancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    stt=false;
                                }
                            });
                            builder.show();
                        }
                        return false;
                    }
                })
        );*/


//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshContent();
//            }
//
//        });
        return view;
    }

    private void refreshContent() {
        if(celebrities!= null)
        {
            celebrities.clear();
            adapter.notifyDataSetChanged();
        }
//        mSwipeRefreshLayout.setRefreshing(false);
        AsyncDataClass asyncRequestObject = new AsyncDataClass();
        asyncRequestObject.execute(AppConfig.URL_XUATCV_DANGUT, uid);
    }

    private class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(params[0]);

            String jsonResult = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                if(!sttfist) {
                    nameValuePairs.add(new BasicNameValuePair("uid", params[1]));
                }else{
                    nameValuePairs.add(new BasicNameValuePair("idungtuyen", params[1]));
                    sttfist=true;
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
                System.out.println("Returned Json object " + jsonResult.toString());

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


//            progressDialog.dismiss();

        }
    }

    private static StringBuilder inputStreamToString(InputStream is) {
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
    public void onResume() {
        if(adapter!= null)
        {
            celebrities.clear();
            adapter.notifyDataSetChanged();
        }
       getData(uid);
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
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void getData(final String uid) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                                                 AppConfig.URL_XUATCV_DANGUT, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                celebrities.clear();
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

                if(!sttfist) {
                    params.put("uid", uid);
                }else{
                    params.put("idtrungtuyen", uid);

                    sttfist=true;
                }

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
        if(!sttfist) {
            try {
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
                        cv.setNganhNghe(ob.getString("nganhnghe"));
                        cv.setBangcap(ob.getString("bangcap"));
                        cv.setMotacv(ob.getString("motacv"));
                        cv.setDotuoi(ob.getString("dotuoi"));
                        cv.setNgoaingu(ob.getString("ngoaingu"));
                        cv.setGioitinh(ob.getString("gioitinh"));
                        cv.setKhac(ob.getString("khac"));
                        cv.setKn(ob.getString("kynang"));
                        cv.setUrl(ob.getString("img"));
                        cv.setIdungtuyen(ob.getString("idungtuyen"));
                        cv.setTrangthai(ob.getString("trangthai"));
                        cv.setStatussave(ob.getString("statussave"));
                        celebrities.add(cv);
                    }
                    adapter = new JobManager_RecyclerAdapter(getActivity(), celebrities,2,uid, 2,recyclerView);
                    recyclerView.setAdapter(adapter);
                    pLoad.setVisibility(View.GONE);
                } else {
                    lnjr.setVisibility(View.VISIBLE);
                    pLoad.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                JSONObject resultObject = new JSONObject(result);
                if (resultObject.getString("success").equals("1")) {
                    Toast.makeText(getActivity(), R.string.toast_delete, Toast.LENGTH_SHORT).show();
                    refreshContent();
                    sttfist=false;
                } else {
                    Toast.makeText(getActivity(), resultObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
