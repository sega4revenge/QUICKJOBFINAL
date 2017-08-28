package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.adapter.ListJobCompanyAdapter;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

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

/**
 * Created by VinhNguyen on 3/21/2017.
 */

public class MoreNormal_Activity extends AppCompatActivity {
    JSONArray mang;
    private ListJobCompanyAdapter adapter = null;
    AsyncDataClass asyncRequestObject;
     int countdata,beginloadmore=0,st=1;
    private ProgressBar progressBar;String uid="",namecity="";
    CircleProgressBar progressBar2;
    private List<CongViec> celebrities = new ArrayList<>();
    List<CongViec> tempArrayList;
    SwipeRefreshLayout mSwipeRefreshLayout;ProgressDialog dialog;
    RecyclerView recyclerView;SessionManager session;   LinearLayoutManager mLayoutManager;
    int firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems; private boolean loading2= true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_morehot);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.customtextview_actionbar, null);
        TextView customtv=((TextView)v.findViewById(R.id.txtcategory));
        customtv.setText(getResources().getString(R.string.st_more));
        customtv.setTextSize(12);
        getSupportActionBar().setCustomView(v);
        st = 0;
        session = new SessionManager(getApplicationContext());
        SharedPreferences pref = getSharedPreferences("JobFindPref", MODE_PRIVATE);
        HashMap<String, String> user = session.getUserDetails();
        uid = user.get(SessionManager.KEY_ID);
        addView();
        Intent i =getIntent();
        namecity = i.getStringExtra("namecity");
        //getSupportActionBar().setTitle(getResources().getString(R.string.st_morePer)+" "+namecity);
        asyncRequestObject = new AsyncDataClass();
        asyncRequestObject.execute(AppConfig.URL_MORENEW,beginloadmore+"",namecity,uid);

    }

    private void addView() {

        progressBar = (ProgressBar) findViewById(R.id.pbLoader);
        progressBar2 = (CircleProgressBar) findViewById(R.id.pbLoader2);
        progressBar2.setVisibility(View.GONE);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new ListJobCompanyAdapter(MoreNormal_Activity.this, celebrities, 3, "", 3);
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MoreNormal_Activity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {

                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading2)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
//                            dialog = ProgressDialog.show(MoreNormal_Activity.this, "",
//                                    "Loading. Please wait...", true);
//                            dialog.show();
                            progressBar2.setVisibility(View.VISIBLE);
                            loading2 = false;
                            Log.v("...", "Last Item Wow !");
                            asyncRequestObject = new AsyncDataClass();
                            asyncRequestObject.execute(AppConfig.URL_MORENEW,beginloadmore+"",namecity,uid);

                        }
                    }
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }

        });
    }
    private void refreshContent() {
        beginloadmore=0;
        if(celebrities!= null)
        {
            celebrities.clear();
            adapter.notifyDataSetChanged();
        }
        asyncRequestObject = new AsyncDataClass();
        asyncRequestObject.execute(AppConfig.URL_MORENEW,beginloadmore+"",namecity,uid);
        mSwipeRefreshLayout.setRefreshing(false);
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
                nameValuePairs.add(new BasicNameValuePair("page", params[1]));
                nameValuePairs.add(new BasicNameValuePair("location", params[2]));
                nameValuePairs.add(new BasicNameValuePair("uid", params[3]));
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
            System.out.println("Resulted Value: " + result);
            if (result.equals("") || result == null) {
                Toast.makeText(MoreNormal_Activity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
                return;
            }
            try {
                mang = new JSONArray(result);
                countdata = mang.length();
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
                        cv.setTrangthai("3");
                        celebrities.add(cv);
                    }
                    adapter.notifyDataSetChanged();
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(dialog!=null && dialog.isShowing()) {
                dialog.dismiss();
            }
            progressBar2.setVisibility(View.GONE);
            loading2= true;
            beginloadmore = beginloadmore + 1;
            progressBar.setVisibility(View.GONE);

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
    public void intentdetail(List<CongViec> list,int pos){
        Intent s = new Intent(MoreNormal_Activity.this, JobDetailActivity.class);
        s.putExtra("tencongty", list.get(pos).tecongty);
        s.putExtra("tencongviec", list.get(pos).tencongviec);
        s.putExtra("diadiem", list.get(pos).diadiem);
        s.putExtra("mucluong", list.get(pos).luong);
        s.putExtra("ngayup", list.get(pos).dateup);
        s.putExtra("yeucaubangcap", list.get(pos).bangcap);
        s.putExtra("dotuoi", list.get(pos).dotuoi);
        s.putExtra("ngoaingu", list.get(pos).ngoaingu);
        s.putExtra("gioitinh", list.get(pos).gioitinh);
        s.putExtra("khac", list.get(pos).khac);
        s.putExtra("motacv", list.get(pos).motacv);
        s.putExtra("kn", list.get(pos).kn);
        s.putExtra("macv", list.get(pos).macv);
        s.putExtra("img", list.get(pos).url);
        s.putExtra("sdt", list.get(pos).sdt);
        s.putExtra("motact", list.get(pos).motact);
        s.putExtra("matd", list.get(pos).matd);
        s.putExtra("type", 3);
        startActivity(s);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MoreNormal_Activity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                st=1;
                int textlength = newText.length();
                tempArrayList = new ArrayList<CongViec>();
                for(CongViec c: celebrities){
                    if (textlength <= c.tencongviec.length()) {
                        if (c.tencongviec.toLowerCase().contains(newText.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }else{
                            //celebrities.remove(c);
                        }
                        //adapter.notifyDataSetChanged();
                        adapter = new ListJobCompanyAdapter(MoreNormal_Activity.this, tempArrayList, 3, "", 3);
                        recyclerView.setAdapter(adapter);
                    }
                }

                return false;
            }
        });
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
