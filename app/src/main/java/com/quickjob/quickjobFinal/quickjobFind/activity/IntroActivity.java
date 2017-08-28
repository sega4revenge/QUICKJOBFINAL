package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.pref.PrefManager;

import java.util.ArrayList;
import java.util.List;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;


public class IntroActivity extends AppCompatActivity {
    private static final int REQUEST_GPS = 1;
    private static final int REQUEST_CALL = 0;
    private static final int REQUEST_CODE = 1234;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen(0);
        } else {
            launchHomeScreen(1);
        }

    }
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(IntroActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                REQUEST_CALL);

    }
    public void launchHomeScreen(int statusIntro) {
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putExtra("statusIntro",statusIntro);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);
        if(checkAndRequestPermissions()) {
        }
    }
    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_CALL);
            return false;
        }
        return true;
    }
    private void requestGPSPermission() {
        // Camera permission has not been granted yet. Request it directly.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && IntroActivity.this.checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && IntroActivity.this.checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_GPS);
        }
    }
    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem(R.string.slide_1_title_search, R.string.slide_1_african_story_books_subtitle,
                R.color.slide_1, R.drawable.find_slide_1_front, R.drawable.find_slide_1_background);

        TutorialItem tutorialItem2 = new TutorialItem(R.string.slide_2_tile_saving_time, R.string.slide_2_volunteer_professionals_subtitle,
                R.color.slide_2, R.drawable.find_slide_2_background, R.drawable.find_slide_2_front);

        TutorialItem tutorialItem3 = new TutorialItem(R.string.slide_3_title_notification, R.string.slide_3_download_and_go_subtitle,
                R.color.slide_3, R.drawable.find_slide_3_background, R.drawable.find_slide_3_front);

        TutorialItem tutorialItem4 = new TutorialItem(R.string.slide_4_title_start, R.string.slide_4_different_languages_subtitle,R.string.slide_4_title_start, R.string.slide_4_different_languages_subtitle,
                R.color.fb1, R.drawable.findjob, R.drawable.recruitment_icon);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            prefManager.setFirstTimeLaunch(true);
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            finish();
        }else{
            prefManager.setFirstTimeLaunch(true);
            finish();
        }
    }


}