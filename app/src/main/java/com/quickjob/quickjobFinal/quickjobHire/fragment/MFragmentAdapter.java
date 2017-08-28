package com.quickjob.quickjobFinal.quickjobHire.fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MFragmentAdapter extends FragmentStatePagerAdapter

    {
        public String[] mTab;
        public MFragmentAdapter(FragmentManager fm, String[] mTab){
        super(fm);
        this.mTab=mTab;

    }
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return  new FragmentFistTab();
            case 1:
                return new FragmentSecondTab();
            default:
                return null;
        }

    }

        @Override
        public int getCount()
        {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        return this.mTab[position];
    }
}
