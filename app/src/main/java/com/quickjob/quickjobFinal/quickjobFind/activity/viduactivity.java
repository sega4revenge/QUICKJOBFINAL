package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.quickjob.quickjobFinal.R;


public class viduactivity extends AppCompatActivity {
    RelativeLayout rela;
    Thread th;
    int st=0;
    CountDownTimer cdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viduactivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rela = (RelativeLayout) findViewById(R.id.liner);
        TopView();


         cdt= new CountDownTimer(22000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // do something after 1s
            }

            @Override
            public void onFinish() {
                        if (st == 0) {
                          //  Toast.makeText(viduactivity.this, "1", Toast.LENGTH_SHORT).show();
                            TopView();
                          cdt.start();
                        } else {
                         //   Toast.makeText(viduactivity.this, "2", Toast.LENGTH_SHORT).show();
                            DownView();
                        cdt.start();
                        }
            }

        }.start();
    }
    public void DownView(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            rela.animate()
                    .translationY(0)
                    .translationYBy(-1000)
                    .setDuration(25000);
        }
        st=0;
    }
    public void TopView(){
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            rela.animate()
                    .translationY(0)
                    .translationYBy(1000)
                    .setDuration(25000);
        }
        st=1;
    }
}
