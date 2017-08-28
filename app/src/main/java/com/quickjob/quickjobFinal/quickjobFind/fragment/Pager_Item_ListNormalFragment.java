package com.quickjob.quickjobFinal.quickjobFind.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.adapter.ListJobCompanyAdapter;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VinhNguyen on 3/2/2017.
 */

public class Pager_Item_ListNormalFragment  extends Fragment {
    RecyclerView listgoldmember;
    View view;
    ListJobCompanyAdapter adaper;
    List<CongViec> data = new ArrayList<>();
    List<CongViec> getdata = new ArrayList<>();
    int position=0;
    int itemcount=4;
    public Pager_Item_ListNormalFragment() {

    }
    public Pager_Item_ListNormalFragment(List<CongViec> getdata, int position) {
        this.getdata=getdata;
        this.position=position;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_pager__item_fragment, container, false);
        listgoldmember = (RecyclerView) view.findViewById(R.id.listgoldmember);
        listgoldmember.setScrollContainer(false);
        adaper = new ListJobCompanyAdapter(getActivity(), data, 3, "", 3);
        listgoldmember.setAdapter(adaper);
        LinearLayoutManager linearLayoutManager_gm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listgoldmember.setLayoutManager(linearLayoutManager_gm);
        listgoldmember.setItemAnimator(new DefaultItemAnimator());
        int nextitem=position*itemcount;
        int finshitem=itemcount+nextitem;
        if(getdata.size()>0) {
            if(finshitem<getdata.size()) {
                for (int i = nextitem; i < finshitem; i++) {
                    data.add(getdata.get(i));
                }

            }else{
                for (int i = nextitem; i < getdata.size(); i++) {
                    data.add(getdata.get(i));
                }

            }
        }else{
            if(adaper!=null)
            {
                data.clear();
                adaper.notifyDataSetChanged();
            }
        }
        return view;
    }
}

