package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobFind.adapter.FirebaseChatAdapter;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.config.AppController;
import com.quickjob.quickjobFinal.quickjobFind.model.CompanyInformation;
import com.quickjob.quickjobFinal.quickjobFind.model.Messager_Chat;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;
import com.facebook.FacebookSdk;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChattingActivity extends AppCompatActivity {
    private static final String TAG = ChattingActivity.class.getSimpleName();
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    Firebase refe_send,refe_rec,refe_rec_child,refe_send_child;
    String user;
    String idsend,idrec,name,logo,namecompany,idcompany="",avata,newchat,uid,timenewss;RecyclerView rec;
    EditText ed;ProgressBar progressbar;
    TextView txt_sharejob,txt_writing;
    ImageButton btn_send;LinearLayoutManager linearLayoutManager_company;int notseen,firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;
    List<Messager_Chat> listdata =new ArrayList<>();boolean loadmore=true;int CountDown=10,downstep=2,maxCount=0,statusInBox=0;
    FirebaseChatAdapter adapter;SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_chatting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        session = new SessionManager(getApplicationContext());
        Intent i =getIntent();
        idsend = session.getId().substring(0,14);
        idrec = i.getStringExtra("idrec");
        name = i.getStringExtra("name");
        logo = i.getStringExtra("logocompany");
        avata = i.getStringExtra("avata");
        idcompany= i.getStringExtra("idcompany");
        namecompany= i.getStringExtra("namecompany");
        LayoutInflater inflator = LayoutInflater.from(this);
        getSupportActionBar().setTitle(namecompany);
        Firebase.setAndroidContext(this);
        refe_send = new Firebase("https://job-find-2fb6d.firebaseio.com/"+idsend);
        refe_rec = new Firebase("https://job-find-2fb6d.firebaseio.com/"+idrec);
        refe_send_child = new Firebase("https://job-find-2fb6d.firebaseio.com/"+idsend+"/"+idrec);
        refe_rec_child = new Firebase("https://job-find-2fb6d.firebaseio.com/"+idrec+"/"+idsend);
        Addview();
        getStatusInBoxUser();
        changeStatusInBox(1);
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(i2==0)
                {
                    changeStatusInBox(1);
                }else {
                    changeStatusInBox(2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat desiredFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = desiredFormat.format(new Date());
                String mess = ed.getText().toString();
                if(mess.equals("") || mess==null) {
                }else{
                    Map<String, String> mapcompany = new HashMap<String, String>();
                    mapcompany.put("namecompany", namecompany);
                    mapcompany.put("logo", logo);
                    mapcompany.put("newchat", mess);
                    mapcompany.put("uid", idrec);
                    mapcompany.put("notseen", 0+"");
                    mapcompany.put("timenew", currentDateandTime);
                    mapcompany.put("statusInbox", statusInBox+"");
                    Map<String, String> mapuser = new HashMap<String, String>();
                    mapuser.put("name", name);
                    mapuser.put("avata", avata);
                    mapuser.put("newchat", mess);
                    mapuser.put("timenew", currentDateandTime);
                    mapuser.put("uid", idsend);
                    if(statusInBox==0) {
                        mapuser.put("notseen", (notseen+1)+"");
                    }else{
                        mapuser.put("notseen", 0+"");
                    }
                    mapuser.put("statusInbox", 1+"");
                    refe_send_child.child("UserReceive").setValue(mapcompany);
                    refe_rec_child.child("UserReceive").setValue(mapuser);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("messager", mess);
                    map.put("name", name);
                    map.put("avata", avata);
                    map.put("uid", uid);
                    map.put("time", currentDateandTime);
                    refe_send_child.child("Messager").push().setValue(map);
                    refe_rec_child.child("Messager").push().setValue(map);
                    ed.setText("");
                    if(statusInBox==0)
                    {
                        Log.d(TAG,avata);
                        Log.d(TAG,name);
                        Log.d(TAG,mess);
                        Log.d(TAG,idcompany);
                        Log.d(TAG, idrec);
                        SendNotification(avata,name,mess,idcompany+"",idsend);
                    }else{
                        Log.d(TAG,"Online");
                    }
                }
            }
        });
        refe_rec_child.child("UserReceive").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Messager_Chat message = dataSnapshot.getValue(Messager_Chat.class);
                    notseen = Integer.parseInt(message.getNotseen());
                }else {
                    notseen=0;
                    Log.d(TAG,"Not Data");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        refe_send_child.child("Messager").limitToLast(20).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int getcountdown = downstep*CountDown;
                Log.i(TAG,"LoadMore:"+getcountdown+"22222222222");
                refe_send_child.child("Messager").limitToLast(getcountdown).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(listdata.size()>0)
                            listdata.clear();
                        for(DataSnapshot pos:dataSnapshot.getChildren())
                        {
                            Messager_Chat mess = pos.getValue(Messager_Chat.class);
                            listdata.add(mess);
                        }
                        changeStatusHideShowTime();
                        try {
                            rec.scrollToPosition(adapter.getItemCount() - 1);
                        }catch (Exception e)
                        {  Log.d(TAG,e.toString());
                        }
                       // }
                        maxCount=listdata.size();
                        loadmore=false;
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        rec.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG,dx+"");
                Log.d(TAG,dy+"");
                if(dy<0)
                {
                    visibleItemCount = linearLayoutManager_company.getChildCount();
                    totalItemCount = linearLayoutManager_company.getItemCount();
                    pastVisiblesItems = linearLayoutManager_company.findFirstVisibleItemPosition();
                    Log.i(TAG+":visibleItemCount",visibleItemCount+"");
                    Log.i(TAG+":totalItemCount",totalItemCount+"");
                    Log.i(TAG+":pastVisiblesItems",pastVisiblesItems+"");
                    if (!loadmore)
                    {

                        if (pastVisiblesItems==0)
                        {
                            loadmore=true;
                            progressbar.setVisibility(View.VISIBLE);
                            new CountDownTimer(1000, 1000) {
                                @Override
                                public void onTick(long l) {
                                }

                                @Override
                                public void onFinish() {
                                    Log.i(TAG,"LoadMore");
                                    downstep=downstep+2;
                                    progressbar.setVisibility(View.GONE);
                                    int getcountdown = downstep*CountDown;
                                    Log.i(TAG,"LoadMore:"+getcountdown);
                                    refe_send_child.child("Messager").limitToLast(getcountdown).addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(listdata.size()>0)
                                                listdata.clear();
                                            for(DataSnapshot pos:dataSnapshot.getChildren())
                                            {
                                                Messager_Chat mess = pos.getValue(Messager_Chat.class);
                                                listdata.add(mess);
                                            }

                                            if(maxCount!=listdata.size()) {
                                                maxCount=listdata.size();
                                                rec.scrollToPosition(maxCount-totalItemCount+visibleItemCount-1);
                                                downstep = downstep + 2;
                                                loadmore = false;
                                            }
                                            changeStatusHideShowTime();
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {
                                        }
                                    });
                                }
                            }.start();

                        }
                    }

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        changeStatusInBox(0);
        super.onDestroy();
    }
    public void changeStatusHideShowTime(){
        for(int i=1;i<listdata.size();i++){
            Messager_Chat jobs = listdata.get(i);
            Messager_Chat jobscc = listdata.get(i - 1);
            if (jobs.getUid().equals(uid)) {

                if(jobscc.getUid().equals(uid)) {
                    listdata.get(i - 1).setIsview(true);
                }else{
                    listdata.get(i - 1).setIsview(false);
                }

            } else {
                if(jobscc.getUid().equals(uid)){
                    listdata.get(i - 1).setIsview(false);
                }else {
                    listdata.get(i - 1).setIsview(true);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
    private void changeStatusInBox(final int i) {
        refe_rec_child.child("UserReceive").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    SimpleDateFormat desiredFormat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime = desiredFormat.format(new Date());
                    Messager_Chat message = dataSnapshot.getValue(Messager_Chat.class);
                    newchat = message.getNewchat();
                    notseen = Integer.parseInt(message.getNotseen());
                    Log.d(TAG,newchat+"");
                    Map<String, String> mapuser = new HashMap<String, String>();
                    mapuser.put("name", name);
                    mapuser.put("avata", avata);
                    mapuser.put("newchat", newchat);
                    mapuser.put("notseen", notseen+"");
                    mapuser.put("timenew", timenewss);
                    mapuser.put("uid", idsend);
                    mapuser.put("statusInbox", i + "");
                    refe_rec_child.child("UserReceive").setValue(mapuser);
                    refe_send_child.child("UserReceive").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                SimpleDateFormat desiredFormat = new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime = desiredFormat.format(new Date());
                                Map<String, String> mapcompany = new HashMap<String, String>();
                                mapcompany.put("namecompany", namecompany);
                                mapcompany.put("logo", logo);
                                mapcompany.put("newchat", newchat+"");
                                mapcompany.put("uid", idrec);
                                mapcompany.put("notseen", 0 + "");
                                mapcompany.put("timenew", timenewss);
                                mapcompany.put("statusInbox", statusInBox + "");
                                refe_send_child.child("UserReceive").setValue(mapcompany);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else {
                    Log.d(TAG,"Not Data");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void SendNotification(final String avata, final String name, final String messager, final String idcompany, final String idsend) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHAT_NOTI, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                parseJsonFeed(result);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppController.TAG, "Login Error: " + error.getMessage());
             //   Toast.makeText(getApplicationContext(),
              //          error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("avata", avata);
                params.put("name", name);
                params.put("messager", messager);
                params.put("idcompany", idcompany);
                params.put("idsend",idsend);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(ChattingActivity.this).requestQueue.add(strReq);
    }
    private void parseJsonFeed(String result) {
        System.out.println("Resulted Value: " + result);
        if(result.equals("") || result == null){
            Toast.makeText(ChattingActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
            return;
        }
//        try {
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
    private void getStatusInBoxUser() {
        refe_send_child.child("UserReceive").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    CompanyInformation comin = new CompanyInformation();
                    comin = dataSnapshot.getValue(CompanyInformation.class);
                    statusInBox = Integer.parseInt(comin.getStatusInbox());
                    timenewss = comin.getTimenew();
                    if(statusInBox==2)
                    {
                        if(txt_writing.getVisibility()==View.GONE) {
                            txt_writing.setVisibility(View.VISIBLE);
                        }
                    }else{
                        if(txt_writing.getVisibility()==View.VISIBLE) {
                            txt_writing.setVisibility(View.GONE);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void Addview() {
//        txt_sharejob = (TextView) findViewById(R.id.txt_sharejob);
        txt_writing = (TextView) findViewById(R.id.txt_writing);
        btn_send = (ImageButton) findViewById(R.id.btn_send);
        ed =(EditText) findViewById(R.id.rep_chat);
        rec = (RecyclerView)findViewById(R.id.rec_chat);
        rec.setScrollContainer(false);
        rec.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });
        progressbar=(ProgressBar) findViewById(R.id.progressBar);
        adapter = new FirebaseChatAdapter(this,listdata,rec);
        rec.setAdapter(adapter);
        linearLayoutManager_company = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rec.setLayoutManager(linearLayoutManager_company);
        rec.setItemAnimator(new DefaultItemAnimator());
        SharedPreferences pref = getSharedPreferences("JobFindPref", MODE_PRIVATE);
        HashMap<String, String> user = session.getUserDetails();
        uid = user.get(SessionManager.KEY_ID);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ids = item.getItemId();
        if (ids == android.R.id.home) {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            changeStatusInBox(0);
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        changeStatusInBox(0);
        super.onBackPressed();
    }
}
