package com.quickjob.quickjobFinal.quickjobHire.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobHire.adapter.ListViewAdapterThird;
import com.quickjob.quickjobFinal.quickjobHire.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobHire.config.AppController;
import com.quickjob.quickjobFinal.quickjobHire.model.Profile;

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

/**
 * Created by VinhNguyen on 7/18/2016.
 */
public class SearchProfileSecond extends AppCompatActivity {

    private ListView lv;
    private ListViewAdapterThird adapter;    private AlertDialog progressDialog;
    ProgressBar progressBar;
    int countdata, data;
    int fistdata = 10;
    int begin = 0;
    int status = 1;
    String diadiem,textkey1,kinhnghiem,nganh,tuoi1,gioitinh1;
    int beginloadmore=0;
    boolean loading;
    JSONArray mang;
    private List<Profile> celebrities = new ArrayList<Profile>();
    // private final String serverUrl = "http://192.168.0.100/android/SearchFrofile.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_xuat_tim_kiem_hire);
        lv = (ListView) findViewById(R.id.lvtimkiem);
        Intent i = getIntent();
        diadiem = i.getStringExtra("diadiem");
        textkey1 = i.getStringExtra("textkey");
        kinhnghiem = i.getStringExtra("kinhnghiem");
        nganh = i.getStringExtra("nganh");
        tuoi1 = i.getStringExtra("tuoi");
        gioitinh1 = i.getStringExtra("gioitinh");
        View footer = getLayoutInflater().inflate(R.layout.progress_bar_footer, null);
        progressBar = (ProgressBar) footer.findViewById(R.id.progressBar);
        lv.addFooterView(footer);
        adapter = new ListViewAdapterThird(SearchProfileSecond.this, android.R.layout.simple_list_item_1, celebrities,1);
        lv.setAdapter(adapter);
       // Toast.makeText(SearchProfileSecond.this,1+"",Toast.LENGTH_SHORT).show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              //  Toast.makeText(SearchProfileSecond.this,2+"",Toast.LENGTH_SHORT).show();
                    Intent is = new Intent(SearchProfileSecond.this, DetailProfileActivity.class);
                    is.putExtra("ten", celebrities.get(i).ten);
                    is.putExtra("gioitinh", celebrities.get(i).gioitinh);
                    is.putExtra("ngaysinh", celebrities.get(i).ngaysinh);
                    is.putExtra("email", celebrities.get(i).email);
                    is.putExtra("sdt", celebrities.get(i).sdt);
                    is.putExtra("kinhnghiem",celebrities.get(i).namkn);
                    is.putExtra("tencongty", celebrities.get(i).tencongty);
                    is.putExtra("chucdanh", celebrities.get(i).chucdanh);
                    is.putExtra("motacv", celebrities.get(i).motacv);
                    is.putExtra("quequan", celebrities.get(i).quequan);
                    is.putExtra("diachi", celebrities.get(i).diachi);
                    is.putExtra("mucluong", celebrities.get(i).mucluong);
                    is.putExtra("kynang", celebrities.get(i).kynang);
                    is.putExtra("ngoaingu", celebrities.get(i).ngoaingu);
                    is.putExtra("img", celebrities.get(i).img);
                    is.putExtra("mahs", celebrities.get(i).id);
                    is.putExtra("key", 0);
                    startActivity(is);
            }
        });
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {

                    if (status == 1 && !loading) {
                        loading = true;
                    //    Toast.makeText(SearchProfileSecond.this, "up data", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
//                        AsyncDataClass asyncRequestObject = new AsyncDataClass();
//                        asyncRequestObject.execute(AppConfig.URL_TIMKIEM_PROFILE, textkey1, nganh, diadiem, kinhnghiem, tuoi1, gioitinh1,beginloadmore+"");
                        getData(textkey1, nganh, diadiem, kinhnghiem, tuoi1, gioitinh1,beginloadmore+"");
                    }
                }
            }
        });

//        Toast.makeText(SearchProfileSecond.this,diadiem+textkey+kinhnghiem+nganh+tuoi+gioitinh,Toast.LENGTH_SHORT).show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

//    private void loadFist() {
//        progressDialog = new SpotsDialog(SearchProfileSecond.this, R.style.Custom);
//        progressDialog.show();
//        loading = true;
//        //  AsyncDataClass asyncRequestObject = new AsyncDataClass();
//        //  asyncRequestObject.execute(AppConfig.URL_TIMKIEM_PROFILE, textkey, nganh, diadiem, kinhnghiem, tuoi, gioitinh);
//        Toast.makeText(SearchProfileSecond.this, "load fist", Toast.LENGTH_SHORT).show();
//
//    }
//    private class LoadMoreDataTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            loading = true;
//            if (isCancelled()) {
//                return null;
//            }
//            // Simulates a background task
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//            }
//            try {
//                if (countdata >= 10) {
//                    for (int i = begin; i < fistdata; i++) {
//                        JSONObject ob = mang.getJSONObject(i);
//                        celebrities.add(new Profile(ob.getString("nganhnghe"), ob.getString("vitri"), ob.getString("mucluong"), ob.getString("diadiem"), ob.getString("createdate"), ob.getString("mahs"), ob.getString("hoten"), ob.getString("gioitinh2"), ob.getString("ngaysinh"), ob.getString("email"), ob.getString("sdt"), ob.getString("diachi"), ob.getString("quequan"), ob.getString("tentruong"), ob.getString("chuyennganh"), ob.getString("xeploai"), ob.getString("thanhtuu"), ob.getString("namkn"), ob.getString("tencongty"), ob.getString("chucdanh"), ob.getString("mota"), ob.getString("ngoaingu"), ob.getString("kynang"), ob.getString("tencv"), "1", ob.getString("img")));
//                    }
//                }else{
//                    for (int i = begin; i < data; i++){
//                        JSONObject ob = mang.getJSONObject(i);
//                        celebrities.add(new Profile(ob.getString("nganhnghe"), ob.getString("vitri"), ob.getString("mucluong"), ob.getString("diadiem"), ob.getString("createdate"), ob.getString("mahs"), ob.getString("hoten"), ob.getString("gioitinh2"), ob.getString("ngaysinh"), ob.getString("email"), ob.getString("sdt"), ob.getString("diachi"), ob.getString("quequan"), ob.getString("tentruong"), ob.getString("chuyennganh"), ob.getString("xeploai"), ob.getString("thanhtuu"), ob.getString("namkn"), ob.getString("tencongty"), ob.getString("chucdanh"), ob.getString("mota"), ob.getString("ngoaingu"), ob.getString("kynang"), ob.getString("tencv"), "1", ob.getString("img")));
//                    }
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            loading = false;
//            progressDialog.dismiss();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            adapter.notifyDataSetChanged();
//            if (countdata > 10) {
//                countdata = countdata - 10;
//                begin = begin + 10;
//            }else{
//                status = 3;
//            }
//            loading = false;
//            progressBar.setVisibility(View.GONE);
//            super.onPostExecute(result);
//        }
//
//        @Override
//        protected void onCancelled() {
//            // Notify the loading more operation has finished
//        }
//    }
private void parseJsonFeed(String result) {
    System.out.println("Resulted Value: " + result);
    if (result.equals("") || result == null) {
        Toast.makeText(SearchProfileSecond.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        return;
    }
    try {
        mang = new JSONArray(result);
        countdata = mang.length();
        if (mang.length() > 0) {
            lv.setVisibility(View.VISIBLE);
            for (int i = 0; i < countdata; i++) {
                JSONObject ob = mang.getJSONObject(i);
                celebrities.add(new Profile(ob.getString("nganhnghe"), ob.getString("vitri"), ob.getString("mucluong"), ob.getString("diadiem"), ob.getString("createdate"), ob.getString("mahs"), ob.getString("hoten"), ob.getString("gioitinh2"), ob.getString("ngaysinh"), ob.getString("email"), ob.getString("sdt"), ob.getString("diachi"), ob.getString("quequan"), ob.getString("tentruong"), ob.getString("chuyennganh"), ob.getString("xeploai"), ob.getString("thanhtuu"), ob.getString("namkn"), ob.getString("tencongty"), ob.getString("chucdanh"), ob.getString("mota"), ob.getString("ngoaingu"), ob.getString("kynang"), ob.getString("tencv"), "1", ob.getString("img")));
            }
            adapter.notifyDataSetChanged();
        }else{
            status=0;
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
    loading = false;
    beginloadmore=beginloadmore+1;
    progressBar.setVisibility(View.GONE);
}

    private void getData(final String textkey, final String nganh, final String diadiem, final String kinhnghiem
            , final String tuoi, final String gioitinh, final String page) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/
        // Tag used to cancel the request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_TIMKIEM_PROFILE, new com.android.volley.Response.Listener<String>() {
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
                params.put("textkey", textkey);
                params.put("nganh", nganh);
                params.put("diadiem", diadiem);
                params.put("kinhnghiem", kinhnghiem);
                params.put("tuoi", tuoi);
                params.put("gioitinh", gioitinh);
                params.put("page", page);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(this).requestQueue.add(strReq);
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
                nameValuePairs.add(new BasicNameValuePair("nganh", params[2]));
                nameValuePairs.add(new BasicNameValuePair("diadiem", params[3]));
                nameValuePairs.add(new BasicNameValuePair("kinhnghiem", params[4]));
                nameValuePairs.add(new BasicNameValuePair("tuoi", params[5]));
                nameValuePairs.add(new BasicNameValuePair("gioitinh", params[6]));
                nameValuePairs.add(new BasicNameValuePair("page", params[7]));
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
                Toast.makeText(SearchProfileSecond.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            try {
                mang = new JSONArray(result);
                countdata = mang.length();
                if (mang.length() > 0) {
                    lv.setVisibility(View.VISIBLE);
                    for (int i = 0; i < countdata; i++) {
                        JSONObject ob = mang.getJSONObject(i);
                        celebrities.add(new Profile(ob.getString("nganhnghe"), ob.getString("vitri"), ob.getString("mucluong"), ob.getString("diadiem"), ob.getString("createdate"), ob.getString("mahs"), ob.getString("hoten"), ob.getString("gioitinh2"), ob.getString("ngaysinh"), ob.getString("email"), ob.getString("sdt"), ob.getString("diachi"), ob.getString("quequan"), ob.getString("tentruong"), ob.getString("chuyennganh"), ob.getString("xeploai"), ob.getString("thanhtuu"), ob.getString("namkn"), ob.getString("tencongty"), ob.getString("chucdanh"), ob.getString("mota"), ob.getString("ngoaingu"), ob.getString("kynang"), ob.getString("tencv"), "1", ob.getString("img")));
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    status=0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loading = false;
            beginloadmore=beginloadmore+1;
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
            // TODO Auto-generated catch block+
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


