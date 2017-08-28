package com.quickjob.quickjobFinal.quickjobFind.fragment;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quickjob.quickjobFinal.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

/**
 * Created by VinhNguyen on 12/29/2016.
 */

public class JobManager extends Fragment  {
    View view;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    BottomBar bottomBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.jobmanager,container,false);

         bottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
        updatestatusfragment();
        return view;
    }
    public void updatestatusfragment(){
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_jobsave) {
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    JobSaveFragment fragment = new JobSaveFragment();
                    fragmentTransaction.replace(R.id.content, fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                }else if(tabId == R.id.tab_jobapply){
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    RecruitmentFragment fragment2 = new RecruitmentFragment();
                    fragmentTransaction.replace(R.id.content, fragment2);
                    fragmentTransaction.commitAllowingStateLoss();
                }else{
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    NotificationFragment fragment2 = new NotificationFragment();
                    fragmentTransaction.replace(R.id.content, fragment2);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            }
        });
        if(!getActivity().getIntent().getBooleanExtra("notification",true)){
            bottomBar.selectTabAtPosition(2);
        }else if(!getActivity().getIntent().getBooleanExtra("jobapplication",true)){
            bottomBar.selectTabAtPosition(1);

        }
    }
    public void updatelistview(){
        updatestatusfragment();
    }
}
