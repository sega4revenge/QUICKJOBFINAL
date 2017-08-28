package com.quickjob.quickjobFinal.quickjobHire.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.quickjob.quickjobFinal.quickjobHire.adapter.ListViewAdapterSecond;
import com.quickjob.quickjobFinal.quickjobHire.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobHire.config.AppController;
import com.quickjob.quickjobFinal.quickjobHire.model.CongViec;
import com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager;

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

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobManageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JobManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobManageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View rootView;
    private LinearLayout lin,lin2,lintitle;
    private ListView lv;
    private ListViewAdapterSecond adapter;
    private ArrayList<CongViec> celebrities = new ArrayList<CongViec>();
    private String uid;
    private AlertDialog progressDialog;
    SessionManager session;
    SharedPreferences.Editor edit;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AsyncDataClass asyncRequestObject;
    private OnFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    public JobManageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobManageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JobManageFragment newInstance(String param1, String param2) {
        JobManageFragment fragment = new JobManageFragment();
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
        rootView = inflater.inflate(R.layout.fragment_jopmanager_hire, container, false);
        //=========
        session = new SessionManager(getActivity());
        SharedPreferences pref=getActivity().getSharedPreferences("JobFindPref", MODE_PRIVATE);
        edit=pref.edit();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        uid=user.get(SessionManager.KEY_ID);

        //=========
        lintitle = (LinearLayout) rootView.findViewById(R.id.lintitle) ;
        progressBar = (ProgressBar) rootView.findViewById(R.id.pbLoader);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        adapter = new ListViewAdapterSecond(getActivity(), celebrities);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        lin = (LinearLayout) rootView.findViewById(R.id.lin);
        lin2 = (LinearLayout) rootView.findViewById(R.id.lin2);
        //===========
        Button createhoso = (Button) rootView.findViewById(R.id.create);

        TextView txt = (TextView) rootView.findViewById(R.id.txt);

        createhoso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), CrceateJobActivity.class);
//                i.putExtra("status",0);
//                startActivity(i);
            }
        });

        return rootView;
    }
    @Override
    public void onResume() {
        if(adapter!= null)
        {
            celebrities.clear();
            adapter.notifyDataSetChanged();
        }
//        asyncRequestObject = new AsyncDataClass();
//        asyncRequestObject.execute(AppControllernfig.URL_XUATCV, uid);
        getData(uid);
        super.onResume();
    }
    private void parseJsonFeed(String result) {
        System.out.println("Resulted Value: " + result);
        if(result.equals("") || result == null){
            Toast.makeText(getActivity(), R.string.st_errServer, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        try {
            JSONArray mang = new JSONArray(result);
            if (mang.length() > 0) {
                lin.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);
                for (int i = 0; i < mang.length(); i++) {
                    JSONObject ob = mang.getJSONObject(i);
                    CongViec congviec = new CongViec();
                    congviec.setTencongviec(ob.getString("tencongviec"));
                    congviec.setChucdanh(ob.getString("chucdanh"));
                    congviec.setNganhnghe(ob.getString("nganhnghe"));
                    congviec.setSohoso(ob.getInt("sohoso"));
                    congviec.setSoluong(ob.getString("soluong"));
                    congviec.setMahs(ob.getString("mahs"));
                    congviec.setMacv(ob.getString("macv"));
                    celebrities.add(congviec);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                lintitle.setVisibility(View.VISIBLE);
            }else{
                lin2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                lintitle.setVisibility(View.GONE);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void getData(final String id) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/
        // Tag used to cancel the request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_XUATCV, new com.android.volley.Response.Listener<String>() {
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
                nameValuePairs.add(new BasicNameValuePair("id", params[1]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

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
            System.out.println("Resulted Value: " + result);
            if(result.equals("") || result == null){
                Toast.makeText(getActivity(), R.string.st_errServer, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            try {
                JSONArray mang = new JSONArray(result);
                if (mang.length() > 0) {
                    lin.setVisibility(View.VISIBLE);
                    lin2.setVisibility(View.GONE);
                    for (int i = 0; i < mang.length(); i++) {
                        JSONObject ob = mang.getJSONObject(i);
                        CongViec congviec = new CongViec();
                        congviec.setTencongviec(ob.getString("tencongviec"));
                        congviec.setChucdanh(ob.getString("chucdanh"));
                        congviec.setNganhnghe(ob.getString("nganhnghe"));
                        congviec.setSohoso(ob.getInt("sohoso"));
                        congviec.setSoluong(ob.getString("soluong"));
                        congviec.setMahs(ob.getString("mahs"));
                        congviec.setMacv(ob.getString("macv"));
                        celebrities.add(congviec);
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    lintitle.setVisibility(View.VISIBLE);
                }else{
                    lin2.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    lintitle.setVisibility(View.GONE);
                }
            }catch(JSONException e){
                e.printStackTrace();
            }

        }
    }
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
     * See the Android Training lesson <a href=v
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
        if ( asyncRequestObject!= null) {
            if (!asyncRequestObject.isCancelled()) {
                asyncRequestObject.cancel(true);
            }
        }
    }
}
