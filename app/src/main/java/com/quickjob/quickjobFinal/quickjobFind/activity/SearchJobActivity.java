package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.fragment.MapNearFragment;
import com.quickjob.quickjobFinal.quickjobFind.fragment.SearchFragment_List;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;

import java.util.ArrayList;
import java.util.List;

public class SearchJobActivity extends AppCompatActivity {
    private ArrayList<Integer> category = new ArrayList<>();
    private List<CongViec> tempArrayList = new ArrayList<>();
    Menu menu;
    private List<CongViec> secondlist = new ArrayList<>();
    private static final int PLACE_PICKER_REQUEST = 3;
    GPSTracker gps;
    String namecity = "", uid = "";
    int st = 0;
    double lat, lng;
    boolean wait=false;
    boolean status = false,gpsstatus=false;
    CountDownTimer count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);
        Intent s = getIntent();
        namecity = s.getStringExtra("namecity");
        namecity= s.getStringExtra("namecity");
        if(namecity.equals("All"))
        {
            uid = s.getStringExtra("uid");
        }else {
            uid = s.getStringExtra("uid");
            lat = s.getDoubleExtra("lat", 20);
            lng = s.getDoubleExtra("lng", 20);
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!status) {
            SearchFragment_List lm_Fragment = new SearchFragment_List();
            fragmentTransaction.replace(android.R.id.content, lm_Fragment);
        } else {
            MapNearFragment pm_Fragment = new MapNearFragment();
            fragmentTransaction.replace(android.R.id.content, pm_Fragment);
        }
        fragmentTransaction.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ids = item.getItemId();

        if (ids == android.R.id.home) {
            super.onBackPressed();
        }else if(ids == R.id.action_search)
        {
            if(!wait) {
                wait=true;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (!status) {
                    gps = new GPSTracker(getApplicationContext());
                    if(gps.canGetLocation()){
                        MapNearFragment pm_Fragment = new MapNearFragment();
                        fragmentTransaction.replace(android.R.id.content, pm_Fragment);
                        menu.getItem(0).setIcon(R.drawable.ic_view_list_white_24dp);
                        status = true;
                    }else{
                        Toast.makeText(this,getResources().getString(R.string.st_err_gps_messeger), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    SearchFragment_List lm_Fragment = new SearchFragment_List();
                    fragmentTransaction.replace(android.R.id.content, lm_Fragment);
                    status = false;
                    menu.getItem(0).setIcon(R.drawable.map_loca);
                }
                fragmentTransaction.commit();
                 count = new CountDownTimer(3000,1000) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        wait=false;
                    }
                }.start();

            }
        }
        return super.onOptionsItemSelected(item);
    }
}

