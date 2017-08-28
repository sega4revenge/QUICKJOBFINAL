package com.quickjob.quickjobFinal.quickjobHire.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.fragment.MFragmentAdapter;


public class RecruitmentListActivity extends AppCompatActivity {

    private TabLayout mTabLayour;
    private ViewPager viewPager;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitment_list_hire);
        mTabLayour =(TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new MFragmentAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.arr)));
        mTabLayour.setupWithViewPager(viewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        title = intent.getStringExtra("tencv");
        getSupportActionBar().setTitle(title);

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
