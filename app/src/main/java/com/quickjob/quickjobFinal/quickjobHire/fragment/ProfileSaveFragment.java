package com.quickjob.quickjobFinal.quickjobHire.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.activity.MainActivity;
import com.quickjob.quickjobFinal.quickjobHire.activity.ProfileDetailActivity;
import com.quickjob.quickjobFinal.quickjobHire.adapter.RecyclerAdapter;
import com.quickjob.quickjobFinal.quickjobHire.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobHire.model.Profile;
import com.quickjob.quickjobFinal.quickjobHire.pref.RecyclerItemClickListener;

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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileSaveFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileSaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileSaveFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LinearLayout lnjr;
    private View view;
    private String uid;
    private RecyclerAdapter adapter;
    private List<Profile> celebrities = new ArrayList<Profile>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    JSONArray mang;
    int sm =0;
    TextView texterror;
    ProgressBar progressBar;
    private RecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;
     AsyncDataClass asyncRequestObject;;
    public ProfileSaveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileSaveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileSaveFragment newInstance(String param1, String param2) {
        ProfileSaveFragment fragment = new ProfileSaveFragment();
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
        view = inflater.inflate(R.layout.save_profile_hire,container,false);
        lnjr = (LinearLayout) view.findViewById(R.id.linjr);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) view.findViewById(R.id.pbLoader);
        final Intent i = getActivity().getIntent();
        uid= MainActivity.uid;
        if (adapter != null) {
            celebrities.clear();
            adapter.notifyDataSetChanged();
        }
        texterror = (TextView) view.findViewById(R.id.texterror);
        adapter = new RecyclerAdapter(getActivity(), celebrities, 1);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int i) {
                        if(sm==0) {
                            try {
                                JSONObject ob = mang.getJSONObject(i);
                                Intent is = new Intent(getActivity(), ProfileDetailActivity.class);
                                is.putExtra("iduser", ob.getString("iduser"));
                                is.putExtra("mahs", ob.getString("mahs"));
                                is.putExtra("key", 2);
                                startActivity(is);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override public void onLongItemClick(View view, final int position) {
                        sm=1;

                        if(mang.length()>0)
                        {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(R.string.st_xoaHS);
                            builder.setPositiveButton(R.string.st_xacNhanOK, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    try {
                                        JSONObject ob =mang.getJSONObject(position);
                                        String mahs= ob.getString("mahs");
                                        String serverUrl = "http://quickjob.gq/xoahosodaluu.php";
                                        AsyncDataClass asyncRequestObject = new AsyncDataClass();
                                        asyncRequestObject.execute(serverUrl,mahs, uid,"1");
                                        sm=0;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            builder.setNegativeButton(R.string.st_xacNhanCancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    sm=0;
                                }
                            });
                            builder.show();
                        }
                    }
                })
        );

        return view;
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

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            celebrities.clear();
            adapter.notifyDataSetChanged();
        }
        asyncRequestObject = new AsyncDataClass();
        asyncRequestObject.execute(AppConfig.URL_XUATHS_DALUU, uid,"","0");

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

                    sm=1;
                    if(params[3].equals("0")) {
                        nameValuePairs.add(new BasicNameValuePair("uid", params[1]));
                        sm=0;
                    }else if(params[3].equals("1"))
                    {
                        nameValuePairs.add(new BasicNameValuePair("mahs", params[1]));
                        nameValuePairs.add(new BasicNameValuePair("id", params[2]));
                }
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
            if (result.equals("") || result == null) {
                Toast.makeText(getActivity(), R.string.st_errServer, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (sm == 0) {
                try {
                    mang = new JSONArray(result);
                    if (mang.length() > 0) {

                        recyclerView.setVisibility(View.VISIBLE);
                        texterror.setVisibility(View.GONE);
                        for (int i = 0; i < mang.length(); i++) {
                            JSONObject ob = mang.getJSONObject(i);
                            Profile profile =new Profile();
                            profile.setNganhnghe(ob.getString("nganhnghe"));
                            profile.setVitri(ob.getString("vitri"));
                            profile.setMucluong(ob.getString("mucluong"));
                            profile.setDiadiem(ob.getString("diadiem"));
                            profile.setNgaydang(ob.getString("createdate"));
                            profile.setId(ob.getString("mahs"));
                            profile.setTen(ob.getString("hoten"));
                            profile.setGioitinh( ob.getString("gioitinh2"));
                            profile.setNgaysinh(ob.getString("ngaysinh"));
                            profile.setEmail(ob.getString("email"));
                            profile.setSdt(ob.getString("sdt"));
                            profile.setDiachi(ob.getString("diachi"));
                            profile.setQuequan(ob.getString("quequan"));
                            profile.setTentruong(ob.getString("tentruong"));
                            profile.setChuyennganh(ob.getString("chuyennganh"));
                            profile.setXeploai(ob.getString("xeploai"));
                            profile.setThanhtuu( ob.getString("thanhtuu"));
                            profile.setNamkn(ob.getString("namkn"));
                            profile.setTencongty(ob.getString("tencongty"));
                            profile.setChucdanh(ob.getString("chucdanh"));
                            profile.setMotacv(ob.getString("mota"));
                            profile.setNgoaingu(ob.getString("ngoaingu"));
                            profile.setKynang(ob.getString("kynang"));
                            profile.setTencv(ob.getString("tencv"));
                            profile.setImg(ob.getString("img"));
                            profile.setIduser(ob.getString("iduser"));
                            celebrities.add(profile);
                           // celebrities.add(new Profile(, , ,, , ), ,, , , , ,, , ob.getString("chuyennganh"), ob.getString("xeploai"), ob.getString("thanhtuu"), ob.getString("namkn"), ob.getString("tencongty"), ob.getString("chucdanh"), ob.getString("mota"), ob.getString("ngoaingu"), ob.getString("kynang"), ob.getString("tencv"), "", ob.getString("img")));

                        }
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        lnjr.setVisibility(View.GONE);
                    } else {
                        texterror.setVisibility(View.VISIBLE);
                        lnjr.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    JSONObject resultObject = new JSONObject(result);
                    if (resultObject.getString("success").equals("1")) {
                        Toast.makeText(getActivity(), resultObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (adapter != null) {
                            celebrities.clear();
                            adapter.notifyDataSetChanged();
                        }
                        AsyncDataClass asyncRequestObject = new AsyncDataClass();
                        asyncRequestObject.execute(AppConfig.URL_XUATHS_DALUU, uid, "", "0");
                    } else {
                        Toast.makeText(getActivity(), resultObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
