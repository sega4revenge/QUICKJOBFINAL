package com.quickjob.quickjobFinal.quickjobHire.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.fragment.CompanyDetailFragment;
import com.quickjob.quickjobFinal.quickjobHire.fragment.JobAboutFragment;

import java.util.ArrayList;
import java.util.List;

public class JobDetailActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private NetworkImageView logo;
    private ListView lv;


    private String motact, number,   chucdanh, img,matd,diachi,nganhnghe,nganhNghe,soluong,phucloi,tuoi;
    String hannop="",yeucaubc="";
    private static final int REQUEST_CALL = 0;
    public static final String TAG = "JobDetailActivity";
    public static String macv;
    int status = 0,luong=0,gt=0,hv=0;
    private TextView txttencv, txtdiachi, txtluong,txtbangcap, txtmotacv, txtkn, txtdotuoi, txtgt, txtnn, txtkhac, txtdate,txtskill,txtnamecompany;

    private String skill,id, sdt, tencv, tenct, diadiem, mucluong, ngayup, yeucaubangcap, dotuoi, ngoaingu, gioitinh, khac, motacv, kn,quymo,jobcompany,location,detail;
    private View v;
    String[] arrnganh,arrhv,arrsalary,arrsex;

    private CollapsingToolbarLayout collapsingToolbar;
    int type;
    private int mPreviousVisibleItem;

    private com.github.clans.fab.FloatingActionButton fabMenu;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_j_hire);
//        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.floatbutton);
        status = 1;
        arrsalary = getResources().getStringArray(R.array.mucluong);
        arrhv = getResources().getStringArray(R.array.spHocVan);
        arrsex= getResources().getStringArray(R.array.sex);
        actionGetIntent();
        init();
        setData();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(JobDetailActivity.this, Edit_Job_activity.class);
                s.putExtra("macv", macv);
                s.putExtra("matd", matd);
                s.putExtra("tencongty", tenct);
                s.putExtra("diachi",diachi);
                s.putExtra("nganhnghe", nganhnghe);
               // s.putExtra("quymo", quymo);
                s.putExtra("motact", motact);
                s.putExtra("nganhNghe",nganhNghe);
               // s.putExtra("chucdanh", chucdanh);
                s.putExtra("soluong", soluong);
                s.putExtra("phucloi",phucloi);
                s.putExtra("tencongviec", tencv);
                s.putExtra("diadiem",diadiem);
                s.putExtra("mucluong",mucluong);
                s.putExtra("ngayup", ngayup);
                s.putExtra("yeucaubangcap",yeucaubc);
                s.putExtra("dotuoi", tuoi);
                s.putExtra("ngoaingu", ngoaingu);
                s.putExtra("gioitinh", gioitinh);
                s.putExtra("khac", khac);
                s.putExtra("motacv", motacv);
                s.putExtra("kn", kn);
                s.putExtra("img", img);
                startActivityForResult(s,333);
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(resultCode==333){
            tenct=data.getStringExtra("tencongty");
            diachi=data.getStringExtra("diachi");
            nganhnghe=data.getStringExtra("nganhnghe");
            quymo=data.getStringExtra("quymo");
            motact=data.getStringExtra("motact");
            chucdanh=data.getStringExtra("chucdanh");
            soluong=data.getStringExtra("soluong");
            phucloi=data.getStringExtra("phucloi");
            tencv=data.getStringExtra("tencongviec");
            diadiem=data.getStringExtra("diadiem");
            mucluong=data.getStringExtra("mucluong");
            ngayup=data.getStringExtra("ngayup");
            tuoi=data.getStringExtra("dotuoi");
            ngoaingu=data.getStringExtra("ngoaingu");
            gioitinh=data.getStringExtra("gioitinh");
            khac=data.getStringExtra("khac");
            motacv=data.getStringExtra("motacv");
            kn=data.getStringExtra("kn");
            img=data.getStringExtra("img");
            if(data.getStringExtra("yeucaubangcap").equals(""))
            {
                hv=0;
            }else{
                hv=Integer.parseInt(data.getStringExtra("yeucaubangcap"));
            }
            if(data.getStringExtra("nganhNghe").equals(""))
            {
                nganhNghe="0";
            }else{
                nganhNghe=data.getStringExtra("nganhNghe");
            }
            if(data.getStringExtra("gioitinh").equals(""))
            {
                gt=2;
            }else{
                gt=Integer.parseInt(data.getStringExtra("gioitinh"));
            }
                setData();
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new JobAboutFragment(), getResources().getString(R.string.st_about_job));
        adapter.addFragment(new CompanyDetailFragment(), getResources().getString(R.string.st_about_company));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setData() {
        id = MainActivity.uid;
        if(kn.equals("")&&ngoaingu.equals("")&&tuoi.equals(""))
        {
            LinearLayout lin = (LinearLayout) findViewById(R.id.linrequest);
            CardView cv = (CardView)findViewById(R.id.cardview);
            lin.setVisibility(View.GONE);
            cv.setVisibility(View.GONE);
            TextView txt = (TextView) findViewById(R.id.txtrequest);
            txt.setVisibility(View.VISIBLE);
            txt.setText(khac);
        }
        txtdiachi.setText(diadiem);
        txtluong.setText(mucluong);
        txtdate.setText(ngayup + "");
        txtmotacv.setText(motacv + "");
        txttencv.setText(tencv + "");
     //   txtbangcap.setText(arrhv[hv] + "");
        txtkn.setText(kn + "");
        txtdotuoi.setText(tuoi + "");
        txtgt.setText(arrsex[gt] + "");
        txtnn.setText(ngoaingu + "");
     //   txtkhac.setText(khac + "");
        txtskill.setText(kn+"");
        txtnamecompany.setText(tenct+"");
        id = MainActivity.uid;
    }

    private void init() {

        txttencv = (TextView)  findViewById((R.id.txtnamejob));
        txtdiachi = (TextView)  findViewById(R.id.txtloca);
        txtluong = (TextView) findViewById((R.id.txtsalary));
        // txtbangcap = (TextView) findViewById(R.id.txtbc);
        txtmotacv = (TextView) findViewById((R.id.txtdetail));
        txtkn = (TextView) findViewById((R.id.txtexe));
        txtdotuoi = (TextView) findViewById((R.id.txtage));
        txtgt = (TextView) findViewById(R.id.txtgender);
        txtnn = (TextView) findViewById((R.id.txtnn));
        // txtkhac = (TextView) findViewById((R.id.txtkhac));
        txtdate = (TextView) findViewById(R.id.txtlimit);
        txtskill = (TextView) findViewById(R.id.txtskill);
        txtnamecompany=(TextView)  findViewById(R.id.txtnamecompany);
        fabMenu = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabMenu);

//        logo = (NetworkImageView) findViewById(R.id.backdrop);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
    }

    private void actionGetIntent() {
        Intent i =getIntent();
        macv=i.getStringExtra("macv");
        matd=i.getStringExtra("matd");
        tenct=i.getStringExtra("tencongty");
        diachi=i.getStringExtra("diachi");
        nganhnghe=i.getStringExtra("nganhnghe");
        quymo=i.getStringExtra("quymo");
        motact=i.getStringExtra("motact");
        chucdanh=i.getStringExtra("chucdanh");
        soluong=i.getStringExtra("soluong");
        phucloi=i.getStringExtra("phucloi");
        tencv=i.getStringExtra("tencongviec");
        diadiem=i.getStringExtra("diadiem");
        mucluong=i.getStringExtra("mucluong");
        ngayup=i.getStringExtra("ngayup");
        tuoi=i.getStringExtra("dotuoi");
        ngoaingu=i.getStringExtra("ngoaingu");
        gioitinh=i.getStringExtra("gioitinh");
        khac=i.getStringExtra("khac");
        motacv=i.getStringExtra("motacv");
        kn=i.getStringExtra("kn");
        img=i.getStringExtra("img");
        if(i.getStringExtra("yeucaubangcap").equals(""))
        {
            hv=0;
        }else{
            hv=Integer.parseInt(i.getStringExtra("yeucaubangcap"));
        }
        if(i.getStringExtra("nganhNghe").equals(""))
        {
            nganhNghe="0";
        }else{
            nganhNghe=i.getStringExtra("nganhNghe");
        }
        if(i.getStringExtra("gioitinh").equals(""))
        {
            gt=0;
        }else{
            gt=Integer.parseInt(i.getStringExtra("gioitinh"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(type==1 || type==3)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_detail_job, menu);
        }
        return true;
    }










    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {

            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
