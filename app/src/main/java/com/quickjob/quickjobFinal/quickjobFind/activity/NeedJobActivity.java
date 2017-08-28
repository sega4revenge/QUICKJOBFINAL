package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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

public class NeedJobActivity extends AppCompatActivity {
    AsyncDataClass asyncRequestObject;
    JSONArray mang;
    private List<CongViec> celebrities = new ArrayList<>();
    private List<CongViec> secondlist = new ArrayList<>();
    LocationManager locationManager;
    LocationListener locationlistener;
    EditText search;
    double lat, lng;
    GPSTracker gps;
    ProgressBar progressBar;
    ListView lv;
    double latitude=0;
    double longitude=0;
    int i;
    private CongViecAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_job);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);
     //  setSupportActionBar(toolbar);
        addView();
        Intent i = getIntent();
        latitude=i.getDoubleExtra("lat",20);
        longitude=i.getDoubleExtra("lng",20);
        if(latitude==0 || longitude==0){
            gps = new GPSTracker(this);
            if(gps.canGetLocation()){
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            }else{
                gps.showSettingsAlert();
            }
        }
        asyncRequestObject = new AsyncDataClass();
        asyncRequestObject.execute(AppConfig.URL_TakeLocations);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    private void addView() {

        progressBar = (ProgressBar) findViewById(R.id.pbLoader);
        View footer = getLayoutInflater().inflate(R.layout.progress_bar_footer, null);
        progressBar = (ProgressBar) footer.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        lv = (ListView) findViewById(R.id.lvtimkiem);
        lv.addFooterView(footer);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent s = new Intent(NeedJobActivity.this, JobDetailActivity.class);
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
                s.putExtra("type", 3);
                startActivity(s);
            }
        });
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
                Toast.makeText(NeedJobActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int textlength = newText.length();
                List<CongViec> tempArrayList = new ArrayList<CongViec>();
                for(CongViec c: celebrities){
                    if (textlength <= c.tencongviec.length()) {
                        if (c.tencongviec.toLowerCase().contains(newText.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }else{
                            // celebrities.remove(c);
                        }
                        // adapter.notifyDataSetChanged();
                        adapter = new CongViecAdapter(NeedJobActivity.this, android.R.layout.simple_list_item_1, tempArrayList, 2, "", 1);
                        lv.setAdapter(adapter);
                    }
                }

                return false;
            }
        });
        return true;

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

    private class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(params[0]);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            String jsonResult = "";
            try {
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
                Toast.makeText( NeedJobActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
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
                        cv.setTrangthai("4");
                        cv.setLat(ob.getString("lat"));
                        cv.setLng(ob.getString("lng"));
                        celebrities.add(cv);
                    }
                }else{
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            location();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void location() {
        if(celebrities.size()>0) {
            for (i = 0; i < celebrities.size();i++) {
                for(int n=i+1;n<celebrities.size();n++)
                {
                    double lati = Double.parseDouble(celebrities.get(i).getLat());
                    double lngi =Double.parseDouble(celebrities.get(i).getLng());
                    double latii =Double.parseDouble(celebrities.get(n).getLat());
                    double lngii =Double.parseDouble(celebrities.get(n).getLng());
                    double dis1 = getDistance(latitude,longitude,lati,lngi);
                    double dis2 = getDistance(latitude,longitude,latii,lngii);
                       if(dis1>1000)
                       {
                           dis1=dis1/1000;
                           dis1=Math.round(dis1);
                           celebrities.get(i).setDistance(dis1+" KM");

                       }else{
                           dis1=Math.round(dis1);
                           celebrities.get(i).setDistance(dis1+" M");
                       }
                    if(dis2>1000)
                    {
                        dis2=dis2/1000;
                        dis2=Math.round(dis2);
                        celebrities.get(n).setDistance(dis2+" KM");
                    }else{
                        dis2=Math.round(dis2);
                        celebrities.get(n).setDistance(dis2+" M");
                    }


                    if(getDistance(latitude,longitude,lati,lngi)>getDistance(latitude,longitude,latii,lngii)){
                        secondlist.add(celebrities.get(i));
                        celebrities.set(i,celebrities.get(n));
                        celebrities.set(n,secondlist.get(0));
                        secondlist.clear();
                    }
                }
            }
            adapter = new CongViecAdapter(NeedJobActivity.this, android.R.layout.simple_list_item_1, celebrities, 2, "", 1);
            lv.setAdapter(adapter);
        }else{
        }
    }
   public float distanceBetween(double startLatitude,double startLongitude, double endLatitude,double endLongitude,float[] results){
     float ss;
       ss=results[0];
        return ss;
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
