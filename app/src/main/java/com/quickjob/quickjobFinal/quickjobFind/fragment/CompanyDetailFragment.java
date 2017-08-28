package com.quickjob.quickjobFinal.quickjobFind.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.activity.JobDetailActivity;
import com.quickjob.quickjobFinal.quickjobFind.activity.MainActivity;
import com.quickjob.quickjobFinal.quickjobFind.adapter.ListJobCompanyAdapter;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;
import com.quickjob.quickjobFinal.quickjobFind.other.CircleTransform;
import com.quickjob.quickjobFinal.quickjobFind.pref.RecyclerItemClickListener;

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


public class CompanyDetailFragment extends Fragment {

    private View view;
    private ImageView logocompany;
    private TextView txtnameconpany,txtandress,txtcreer,txtinfor;
    private String namecompany="",andress="",career="",infor="",uid="",logo="",macv="";
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView image;
    AsyncDataClass asyncRequestObject;
    JSONArray mang;
    RecyclerView mRecyclerView;
    ProgressBar progressBar;
    private List<CongViec> celebrities = new ArrayList<>();
    private ListJobCompanyAdapter adapter = null;

    public CompanyDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_two, container, false);
        actionGetIntent();
        init();
        asyncRequestObject = new AsyncDataClass();
        asyncRequestObject.execute(AppConfig.URL_LISTJOBCOMPANY,macv);

        return view;
    }

    private void init() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.resjob);
        mRecyclerView.setScrollContainer(false);
        adapter = new ListJobCompanyAdapter(getActivity(), celebrities, 2, "", 3);
        mRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        logocompany= (ImageView) view.findViewById(R.id.logocompany);
        txtnameconpany = (TextView) view.findViewById(R.id.namecompany);
       // txtandress = (TextView) view.findViewById(R.id.andress);
       // txtcreer = (TextView) view.findViewById(R.id.career);
        txtinfor = (TextView) view.findViewById(R.id.information);
        txtnameconpany.setText(namecompany);
       // String[] d =andress.split(",");
       // int maxandress=d.length;
      //  txtandress.setText(d[maxandress-3]+","+d[maxandress-2]);
       // txtcreer.setText(career);


        txtinfor.setText(infor);
        Glide.with(getActivity()).load(logo)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getActivity()))
                .diskCacheStrategy( DiskCacheStrategy.ALL )
                .into(logocompany);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int i) {
                        Intent s = new Intent(getActivity(), JobDetailActivity.class);
                        s.putExtra("tencongty", celebrities.get(i).tecongty);
                        s.putExtra("tencongviec", celebrities.get(i).tencongviec);
                        s.putExtra("diadiem", celebrities.get(i).diadiem);
                        s.putExtra("mucluong", celebrities.get(i).luong);
                        s.putExtra("ngayup", celebrities.get(i).dateup);
                        s.putExtra("yeucaubangcap", celebrities.get(i).bangcap);
                        s.putExtra("dotuoi", celebrities.get(i).dotuoi);
                        s.putExtra("ngoaingu", celebrities.get(i).ngoaingu);
                        s.putExtra("gioitinh", celebrities.get(i).gioitinh);
                        s.putExtra("khac", celebrities.get(i).khac);
                        s.putExtra("motacv", celebrities.get(i).motacv);
                        s.putExtra("kn", celebrities.get(i).kn);
                        s.putExtra("macv", celebrities.get(i).macv);
                        s.putExtra("img", celebrities.get(i).url);
                        s.putExtra("sdt", celebrities.get(i).sdt);
                        s.putExtra("motact", celebrities.get(i).motact);
                        s.putExtra("statussave", celebrities.get(i).statussave);
                        s.putExtra("type", 3);
                        startActivity(s);
                    }

                    @Override public boolean onLongItemClick(View view, int position) {
                        // do whatever
                        return false;
                    }
                })
        );
    }

    private void actionGetIntent() {
        Intent i = getActivity().getIntent();
        namecompany =i.getStringExtra("tencongty");
        andress=i.getStringExtra("diadiem");
     //   career=i.getStringExtra("nganhnghe");
        infor=i.getStringExtra("motact");
        logo=i.getStringExtra("img");
        macv=i.getStringExtra("macv");
        uid= MainActivity.uid;

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
                nameValuePairs.add(new BasicNameValuePair("macv", params[1]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
                System.out.println("Returned47447 Json object " + jsonResult.toString());

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
                Toast.makeText(getActivity(),R.string.st_errServer, Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
                return;
            }
            try {
                mang = new JSONArray(result);
                if(mang.length()>0) {
                    for (int i = 0; i < mang.length(); i++) {
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
                        cv.setStatussave(ob.getString("statussave"));
                        cv.setTrangthai("3");
                        celebrities.add(cv);
                    }
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
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
