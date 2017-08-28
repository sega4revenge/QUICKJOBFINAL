package com.quickjob.quickjobFinal.quickjobHire.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quickjob.quickjobFinal.R;

/**
 * Created by VinhNguyen on 1/4/2017.
 */

public class ProfileManager extends Fragment {
    View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentTabHost mTabHost;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profilemanager_hire, container, false);
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ProfileSaveFragment fragment = new ProfileSaveFragment();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.commit();
//        BottomBar bottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
//        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelected(@IdRes int tabId) {
//                if (tabId == R.id.tab_profilesave) {
//                    fragmentManager = getFragmentManager();
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    ProfileSaveFragment fragment = new ProfileSaveFragment();
//                    fragmentTransaction.replace(R.id.content, fragment);
//                    fragmentTransaction.commit();
//                } else if (tabId == R.id.tab_jobmanager) {
//                    fragmentManager = getFragmentManager();
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    JobManageFragment fragment2 = new JobManageFragment();
//                    fragmentTransaction.replace(R.id.content, fragment2);
//                    fragmentTransaction.commit();
//                }
//            }
//        });

        return view;
    }
}