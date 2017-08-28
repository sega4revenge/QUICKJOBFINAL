package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.adapter.CongViecAdapter;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
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
import java.util.List;

import dmax.dialog.SpotsDialog;

public class XuatTimKiemActivity extends AppCompatActivity {

    private ListView lv ;
    private CongViecAdapter adapter = null;
    private List<CongViec> celebrities = new ArrayList<CongViec>();
    private ProgressBar pbLoader;
    private CoordinatorLayout coordinatorLayout;
    private String diadiem, textkey;
    JSONArray mang;
    int countdata, data;
    int fistdata = 10;
    int begin = 0;
    int status = 0;
    boolean loading;
    ProgressBar progressBar;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View footer = getLayoutInflater().inflate(R.layout.progress_bar_footer, null);
        progressBar = (ProgressBar) footer.findViewById(R.id.progressBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv = (ListView) findViewById(R.id.lvtimkiem);
        lv.addFooterView(footer);
        adapter = new CongViecAdapter(XuatTimKiemActivity.this, android.R.layout.simple_list_item_1, celebrities, 2, "", 0);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    JSONObject ob =mang.getJSONObject(i);
                    Intent s = new Intent(XuatTimKiemActivity.this, JobDetailActivity.class);
                    s.putExtra("tencongty", ob.getString("tenct"));
                    s.putExtra("tencongviec",ob.getString("tencv"));
                    s.putExtra("diadiem", ob.getString("diadiem"));
                    s.putExtra("mucluong", ob.getString("mucluong"));
                    s.putExtra("ngayup", ob.getString("hannophoso"));
                    s.putExtra("yeucaubangcap", ob.getString("bangcap"));
                    s.putExtra("dotuoi", ob.getString("dotuoi"));
                    s.putExtra("ngoaingu", ob.getString("ngoaingu"));
                    s.putExtra("gioitinh", ob.getString("gioitinh"));
                    s.putExtra("khac", ob.getString("khac"));
                    s.putExtra("motacv", ob.getString("motacv"));
                    s.putExtra("kn", ob.getString("kynang"));
                    s.putExtra("macv", ob.getString("macv"));
                    s.putExtra("img", ob.getString("img"));
                    s.putExtra("sdt", ob.getString("sdtct"));
                    s.putExtra("motact", ob.getString("motact"));
                    s.putExtra("type",0);
                    startActivity(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
//                    MyToast.showToast(MainActivity.this,"listview ended");
                    if (status == 0 && !loading) {
                        loadFist(); // =>chay dau tien
                    } else if (status == 1 && !loading) {
                        //  loadMore();
                        Toast.makeText(XuatTimKiemActivity.this, "up data", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                        new LoadMoreDataTask().execute();
                    }
                }
            }
        });




    }

    private void loadFist() {
        Intent i = getIntent();
        diadiem = i.getStringExtra("diadiem");
        textkey = i.getStringExtra("textkey");
        progressDialog = new SpotsDialog(XuatTimKiemActivity.this, R.style.Custom);
        progressDialog.show();
        loading = true;
        AsyncDataClass asyncRequestObject = new AsyncDataClass();
        asyncRequestObject.execute(AppConfig.URL_XUAT_TIMKIEM, diadiem, textkey);
//        Toast.makeText(XuatTimKiemActivity.this, "load fist", Toast.LENGTH_SHORT).show();
    }
    private class LoadMoreDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            loading = true;
            if (isCancelled()) {
                return null;
            }
            // Simulates a background task
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            try {
                if (countdata > 10) {
                    for (int i = begin; i < fistdata; i++) {
                        JSONObject ob = mang.getJSONObject(i);
                        CongViec cv = new CongViec();
                        cv.setMacv(ob.getString("macv"));
                        cv.setTencongviec(ob.getString("tencv"));
                        cv.setTecongty(ob.getString("tenct"));
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
                        cv.setTrangthai("3");
                        celebrities.add(cv);
                    }
                } else {
                    for (int i = begin; i < data; i++) {
                        JSONObject ob = mang.getJSONObject(i);
                        CongViec cv = new CongViec();
                        cv.setMacv(ob.getString("macv"));
                        cv.setTencongviec(ob.getString("tencv"));
                        cv.setTecongty(ob.getString("tenct"));
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
                        cv.setTrangthai("3");
                        celebrities.add(cv);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.notifyDataSetChanged();
            if (countdata > 10) {
                countdata = countdata - 10;
                begin = begin + 10;
            }else{
                status = 3;
            }
            loading = false;
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled() {
            // Notify the loading more operation has finished
        }
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
                nameValuePairs.add(new BasicNameValuePair("textkey", params[1]));
                nameValuePairs.add(new BasicNameValuePair("diadiem", params[2]));
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
            //    pbLoader.setVisibility(View.GONE);
    //             Toast.makeText(XuatTimKiemActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
    //                pbLoader.setVisibility(View.GONE);

                //error();


                return;
            }
            try {
                mang = new JSONArray(result);
                countdata = mang.length();
                data = countdata;
                if (countdata > fistdata) {
                    for (int i = begin; i < fistdata; i++) {
                        JSONObject ob = mang.getJSONObject(i);
                        CongViec cv = new CongViec();
                        cv.setMacv(ob.getString("macv"));
                        cv.setTencongviec(ob.getString("tencv"));
                        cv.setTecongty(ob.getString("tenct"));
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
                        cv.setTrangthai("3");
                        celebrities.add(cv);
                    }
                    adapter.notifyDataSetChanged();
                    countdata = countdata - 10;
                    fistdata = fistdata + 10;
                    begin = begin + 10;
                    status = 1;
                } else {
                    for (int i = begin; i < countdata; i++) {
                        JSONObject ob = mang.getJSONObject(i);
                        CongViec cv = new CongViec();
                        cv.setMacv(ob.getString("macv"));
                        cv.setTencongviec(ob.getString("tencv"));
                        cv.setTecongty(ob.getString("tenct"));
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
                        cv.setTrangthai("3");
                        celebrities.add(cv);
                    }
                    adapter.notifyDataSetChanged();
                    status = 3;
                }
                loading = false;
                lv.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

  /*  private void error() {

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, R.string.internet_error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.internet_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pbLoader.setVisibility(View.VISIBLE);
                        AsyncDataClass asyncRequestObject = new AsyncDataClass();
                        asyncRequestObject.execute(AppConfig.URL_XUAT_TIMKIEM, diadiem, textkey);
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    } */

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


}



