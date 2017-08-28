package com.quickjob.quickjobFinal.quickjobFind.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.activity.MainActivity;
import com.quickjob.quickjobFinal.quickjobFind.adapter.ListChatAdapter;
import com.quickjob.quickjobFinal.quickjobFind.model.CompanyInformation;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by VinhNguyen on 4/11/2017.
 */

public class ListChatFragment extends Fragment {
    ListChatAdapter adapter;String arrname="";
    View view;
    RecyclerView rec_list_chat;String nameuser,iduser;
    List<CompanyInformation> listdata3 = new ArrayList<>();
    DataSnapshot mdataSnapshot;
    LinearLayout linnotfind;ProgressBar pbLoader;int CountNumMess=0;int numMessRow=0;
    Firebase rec_list_data,rec_list_child,rec_list_child_company;SessionManager session;String uid;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_chat_layout, container, false);
        session = new SessionManager(getActivity());
        SharedPreferences pref = getActivity().getSharedPreferences("JobFindPref", MODE_PRIVATE);
        HashMap<String, String> user = session.getUserDetails();
        uid = user.get(SessionManager.KEY_ID);
        nameuser= user.get(SessionManager.KEY_NAME);
        iduser = uid.substring(0,14);
        Firebase.setAndroidContext(getActivity());
        rec_list_data = new Firebase("https://job-find-2fb6d.firebaseio.com/"+iduser);
        rec_list_child= new Firebase("https://job-find-2fb6d.firebaseio.com/"+iduser);
        rec_list_child_company= new Firebase("https://job-find-2fb6d.firebaseio.com/"+iduser);
        rec_list_chat = (RecyclerView )view.findViewById(R.id.rec_listchat);
        pbLoader = (ProgressBar ) view.findViewById(R.id.pbLoader);
        linnotfind= (LinearLayout ) view.findViewById(R.id.linnotfind);
        rec_list_chat.setScrollContainer(false);
        adapter = new ListChatAdapter(getActivity(),listdata3);
        rec_list_chat.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager_company = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager_company.setStackFromEnd(true);
        linearLayoutManager_company.setReverseLayout(true);
        rec_list_chat.setLayoutManager(linearLayoutManager_company);
        rec_list_chat.setItemAnimator(new DefaultItemAnimator());
        rec_list_data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if (listdata3.size() > 0) {
                        listdata3.clear();
                        adapter.notifyDataSetChanged();
                        CountNumMess=0;
                    }
                    if(dataSnapshot.exists()) {
                        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                            CompanyInformation mess = messageSnapshot.child("UserReceive").getValue(CompanyInformation.class);
                            CountNumMess = CountNumMess + Integer.parseInt(mess.getNotseen());
                            listdata3.add(mess);
                            if (listdata3.size() > 0) {
                                linnotfind.setVisibility(View.GONE);
                                rec_list_chat.setVisibility(View.VISIBLE);
                            } else {
                                rec_list_chat.setVisibility(View.GONE);
                                linnotfind.setVisibility(View.VISIBLE);
                            }
                            try {
                                ((MainActivity) getActivity()).setNumMessagerTab(CountNumMess);
                            } catch (Exception e) {
                                Log.d("ListChat", e + "");
                            }
                            adapter.notifyDataSetChanged();
                        }
                        if (listdata3.size() >= 2) {
                            arrangement();
                        }
                    }else{
                        rec_list_chat.setVisibility(View.GONE);
                        linnotfind.setVisibility(View.VISIBLE);
                        try {
                            ((MainActivity) getActivity()).setNumMessagerTab(0);
                        } catch (Exception e) {
                            Log.d("ListChat", e + "");
                        }
                    }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        return view;
    }

    private void arrangement() {
        List<CompanyInformation> secondlist = new ArrayList<>();
        for(int i=0;i<listdata3.size();i++){
            for(int x=i+1;x<listdata3.size();x++){
                long fist = getDateInMillis(listdata3.get(i).getTimenew());
                long second = getDateInMillis(listdata3.get(x).getTimenew());
                if(fist>second){
                    secondlist.add(listdata3.get(i));
                    listdata3.set(i,listdata3.get(x));
                    listdata3.set(x,secondlist.get(0));
                    secondlist.clear();
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}