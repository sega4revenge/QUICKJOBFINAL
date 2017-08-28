package com.quickjob.quickjobFinal.quickjobHire.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
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
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobHire.adapter.FirebaseChatAdapter;
import com.quickjob.quickjobFinal.quickjobHire.adapter.ListJobs_ShareAdapter;
import com.quickjob.quickjobFinal.quickjobHire.model.Messager_Chat;
import com.quickjob.quickjobFinal.quickjobHire.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobHire.config.AppController;
import com.quickjob.quickjobFinal.quickjobHire.model.CompanyInformation;
import com.quickjob.quickjobFinal.quickjobHire.model.CongViec;
import com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    boolean singleBackToExitPressedOnce = true;

    Firebase refe_send,refe_rec,refe_rec_child,refe_send_child;
    String user,newchat,timenewss; LinearLayoutManager linearLayoutManager_company;
    String idsend,idrec,name,logo,namecompany,avata,uid,iduser;RecyclerView rec;
    private ArrayList<CongViec> celebrities = new ArrayList<>();
    ListJobs_ShareAdapter adapter2;
    LinearLayout createjob;
    ListView lv;ProgressBar progressBar;private int lastVisiblePosition = 0;
    private String dc="",logo1="",nn="",mt="",qm="",tenct="",pl="";
    EditText ed;ImageButton btn_send;int notseen=0,firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;
    List<Messager_Chat> listdata =new ArrayList<>();ProgressBar progressbar;TextView txt_sharejob,txt_noti;JSONArray mang;
    AlertDialog dialog; String[] arrsalary,arrbc;TextView txt_writing;
    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = null;
    FirebaseChatAdapter adapter;SessionManager session;boolean loadmore=true;int CountDown=10,downstep=2,maxCount=0, statusInBox=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_hire);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Intent i =getIntent();
        idsend = user.get(SessionManager.KEY_ID).substring(0,14);
        idrec = i.getStringExtra("idrec");
        name = i.getStringExtra("name");
        logo = i.getStringExtra("logocompany");
        avata = i.getStringExtra("avata");
        namecompany= i.getStringExtra("namecompany");
        iduser= idrec;
        getSupportActionBar().setTitle(namecompany);
        Firebase.setAndroidContext(this);
        refe_rec = new Firebase("https://job-find-2fb6d.firebaseio.com/"+idrec);
        refe_send = new Firebase("https://job-find-2fb6d.firebaseio.com/"+idsend);
        refe_rec_child = new Firebase("https://job-find-2fb6d.firebaseio.com/"+idrec+"/"+idsend);
        refe_send_child = new Firebase("https://job-find-2fb6d.firebaseio.com/"+idsend+"/"+idrec);
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
        txt_sharejob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view2 = inflater.inflate(R.layout.content_xuat_tim_kiem_hire, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ChattingActivity.this);
                builder.setView(view2);
                builder.setTitle(getResources().getString(R.string.selectjob_share));
                lv = (ListView) view2.findViewById(R.id.lvtimkiem);
                progressBar =(ProgressBar) view2.findViewById(R.id.pbLoader);
                progressBar.setVisibility(View.VISIBLE);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(celebrities.size()>0) {
                            String messager="";
                            if(celebrities.get(i).ngoaingu.equals("") || celebrities.get(i).kn.equals("") || celebrities.get(i).bangcap.equals("") || celebrities.get(i).dotuoi.equals(""))
                            {
                                messager = "Tên công việc : "+celebrities.get(i).tencongviec+"\n Địa điểm: "+celebrities.get(i).diadiem+"\n Mức lương : "+arrsalary[Integer.parseInt(celebrities.get(i).getLuong())]+"\n Mô tả công việc: "+celebrities.get(i).motacv+" \n Yêu cầu: \n - Khác: "+celebrities.get(i).khac+"\n Hạn nộp hồ sơ :"+celebrities.get(i).dateup+"\n Tham khảo thêm tại: \n http://quickjob.gq/link.php?jobid="+celebrities.get(i).macv;

                            }else{
                                messager = "Tên công việc : "+celebrities.get(i).tencongviec+"\n Địa điểm: "+celebrities.get(i).diadiem+"\n Mức lương : "+arrsalary[Integer.parseInt(celebrities.get(i).getLuong())]+"\n Mô tả công việc: "+celebrities.get(i).motacv+" \n Yêu cầu: \n - Bằng cấp:  "+arrbc[Integer.parseInt(celebrities.get(i).bangcap)]+"\n - Độ tuổi "+celebrities.get(i).dotuoi+ "\n - Kỹ năng: "+celebrities.get(i).kn+"\n - Ngoại ngữ:  "+celebrities.get(i).ngoaingu+"\n - Khác: "+celebrities.get(i).khac+"\n Hạn nộp hồ sơ :"+celebrities.get(i).dateup+"\n Tham khảo thêm tại: \n http://quickjob.gq/link.php?jobid="+celebrities.get(i).macv ;
                            }
                            sendMessager(messager);
                            dialog.cancel();
                        }
                    }
                });

                txt_noti= (TextView) view2.findViewById(R.id.txt_noti);
                createjob = (LinearLayout) view2.findViewById(R.id.createjob);
                if(adapter2!= null)
                {
                    adapter2.clear();
                    adapter2.notifyDataSetChanged();
                }
                getData(uid);
                dialog =builder.create();
                dialog.show();
            }
        });
        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_sharejob.getVisibility()!=View.GONE) {
                    txt_sharejob.setVisibility(View.GONE);
                    btn_send.setVisibility(View.VISIBLE);
                    if(adapter!=null) {
                        rec.scrollToPosition(adapter.getItemCount() - 1);
                    }

                }else{
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    txt_sharejob.setVisibility(View.VISIBLE);
                    btn_send.setVisibility(View.GONE);
                }

            }
        });
//        ed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    txt_sharejob.setVisibility(View.GONE);
//                    btn_send.setVisibility(View.VISIBLE);
//                    if(adapter!=null){
//                        rec.scrollToPosition(adapter.getItemCount()-1);
//                    }
//                }
//            }
//        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messager =ed.getText().toString();
                sendMessager(messager);
            }
        });
        refe_rec_child.child("UserReceive").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    CompanyInformation message = dataSnapshot.getValue(CompanyInformation.class);
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
                        maxCount=listdata.size();
                        loadmore=false;
                        Log.d("sssss",adapter.getItemCount()+"");
                        try {
                            rec.scrollToPosition(adapter.getItemCount() - 1);
                        }catch (Exception e)
                        {  Log.d(TAG,e.toString());
                        }

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
                    lastVisiblePosition = linearLayoutManager_company.findLastVisibleItemPosition();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard on", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            txt_sharejob.setVisibility(View.VISIBLE);
            Toast.makeText(this, "keyboard off", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        changeStatusInBox(0);
        super.onDestroy();
    }
    private void sendMessager(String mess){
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = desiredFormat.format(new Date());
        if(mess.equals("") || mess==null) {
        }else{
            Map<String, String> mapcompany = new HashMap<String, String>();
            mapcompany.put("namecompany",name);
            mapcompany.put("logo", avata);
            mapcompany.put("newchat", mess);
            if(statusInBox==0)
            {
                mapcompany.put("notseen", (notseen+1)+"");
            }else{
                mapcompany.put("notseen", 0+"");
            }
            mapcompany.put("uid", idsend);
            mapcompany.put("timenew", currentDateandTime);
            mapcompany.put("statusInbox", 1+"");
            Map<String, String> mapuser = new HashMap<String, String>();
            mapuser.put("name", namecompany);
            mapuser.put("avata", logo);
            mapuser.put("newchat", mess);
            mapuser.put("timenew", currentDateandTime);
            mapuser.put("uid", idrec);
            mapuser.put("notseen", 0+"");
            mapuser.put("statusInbox", statusInBox+"");
            refe_rec_child.child("UserReceive").setValue(mapcompany);
            refe_send_child.child("UserReceive").setValue(mapuser);
            Map<String, String> map = new HashMap<String, String>();
            map.put("messager", mess);
            map.put("name", name);
            map.put("avata", avata);
            map.put("uid", uid);
            map.put("time", currentDateandTime);
            refe_rec_child.child("Messager").push().setValue(map);
            refe_send_child.child("Messager").push().setValue(map);
            ed.setText("");
            if(statusInBox==0)
            {
                Log.d(TAG,avata);
                Log.d(TAG,name);
                Log.d(TAG,mess);
                Log.d(TAG,iduser);
                SendNotification(avata,name,mess,iduser);
            }else{
                Log.d(TAG,"Online");
            }
        }
    }

    private void SendNotification(final String avata, final String name, final String messager, final String idcompany) {
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
              //  Toast.makeText(getApplicationContext(),
                //        error.getMessage(), Toast.LENGTH_LONG).show();
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

    }

    private void getStatusInBoxUser() {
        refe_send_child.child("UserReceive").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    CompanyInformation comin = dataSnapshot.getValue(CompanyInformation.class);
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
    private void changeStatusInBox(final int i) {
        refe_rec_child.child("UserReceive").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    SimpleDateFormat desiredFormat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime = desiredFormat.format(new Date());
                    CompanyInformation message = dataSnapshot.getValue(CompanyInformation.class);
                    newchat = message.getNewchat();
                    notseen = Integer.parseInt(message.getNotseen());
                    Map<String, String> mapcompany = new HashMap<String, String>();
                    mapcompany.put("namecompany", name);
                    mapcompany.put("logo", avata);
                    mapcompany.put("newchat", newchat+"");
                    mapcompany.put("notseen", notseen+"");
                    mapcompany.put("uid", idsend);
                    mapcompany.put("timenew", currentDateandTime);
                    mapcompany.put("statusInbox", i + "");
                    refe_rec_child.child("UserReceive").setValue(mapcompany);
                    refe_send_child.child("UserReceive").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                Log.d(TAG,newchat+"222222");
                                SimpleDateFormat desiredFormat = new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime = desiredFormat.format(new Date());
                                Map<String, String> mapuser = new HashMap<String, String>();
                                mapuser.put("name", namecompany);
                                mapuser.put("avata", logo);
                                mapuser.put("newchat", newchat+"");
                                mapuser.put("timenew", timenewss);
                                mapuser.put("uid", idrec);
                                mapuser.put("notseen", 0+"");
                                mapuser.put("statusInbox", statusInBox+"");
                                refe_send_child.child("UserReceive").setValue(mapuser);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }

                @Override
                public void onCancelled (FirebaseError firebaseError){

                }

        });


    }
    public void changeStatusHideShowTime(){
        for(int i=1;i<listdata.size();i++){
            Messager_Chat jobs = listdata.get(i);
            Messager_Chat jobscc = listdata.get(i - 1);
            if (jobs.getUid().equals(uid)) {

                if(jobscc.getUid().equals(uid)) {
                    listdata.get(i - 1).setView(true);
                }else{
                    listdata.get(i - 1).setView(false);
                }

            } else {
                if(jobscc.getUid().equals(uid)){
                    listdata.get(i - 1).setView(false);
                }else {
                    listdata.get(i - 1).setView(true);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
    private void Addview() {
        txt_writing= (TextView) findViewById(R.id.txt_writing);
        arrsalary=getResources().getStringArray(R.array.mucluong);
        arrbc= getResources().getStringArray(R.array.speducation);
        txt_sharejob = (TextView) findViewById(R.id.txt_sharejob);
        btn_send = (ImageButton) findViewById(R.id.btn_send);
        ed =(EditText) findViewById(R.id.rep_chat);
        rec = (RecyclerView)findViewById(R.id.rec_chat);
        rec.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                txt_sharejob.setVisibility(View.VISIBLE);
                return false;
            }
        });
        progressbar=(ProgressBar) findViewById(R.id.progressBar);
        adapter = new FirebaseChatAdapter(this,listdata);
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
            changeStatusInBox(0);
            super.onBackPressed();

        }
        return super.onOptionsItemSelected(item);

    }



    @Override
    public void onBackPressed() {
        final View activityRootView = findViewById(R.id.content_chatting);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();

                activityRootView.getWindowVisibleDisplayFrame(r);
                View view=getCurrentFocus();
                int heightDiff = view.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > 50) {
                    //enter your code here
                }else{
                    txt_sharejob.setVisibility(View.VISIBLE);
                }
            }
        });
        super.onBackPressed();

//        if (singleBackToExitPressedOnce) {
//            txt_sharejob.setVisibility(View.VISIBLE);
//            super.onBackPressed();
//            return;
//
//        }
//            this.singleBackToExitPressedOnce = true;
//            txt_sharejob.setVisibility(View.VISIBLE);
//             new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                singleBackToExitPressedOnce=false;
//            }
//        }, 5000);
//


    }






    private void getData(final String id) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_XUATHS1, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                parseJsonFeed2(result);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(ChattingActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("id", id);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(ChattingActivity.this).requestQueue.add(strReq);
    }
    private void parseJsonFeed2(String result) {
        System.out.println("Resulted Value: " + result);
        if(result.equals("") || result == null){
            Toast.makeText(ChattingActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        try {
            mang = new JSONArray(result);
            if(mang.length()>0) {
                JSONObject obs = mang.getJSONObject(0);
                dc = obs.getString("diachi");
                nn= obs.getString("nganhnghe");
                mt = obs.getString("motact");
                qm = obs.getString("quymo");
                logo1 = obs.getString("img");
                tenct =obs.getString("tenct");
                int stt=0;
                stt=obs.getInt("status");
                if(stt==0) {
                    for (int i = 0; i < mang.length(); i++) {
                        JSONObject ob = mang.getJSONObject(i);
                        JSONArray profile = ob.getJSONArray("hoso");
                        CongViec job_profile_apply =new CongViec();
                        if(profile.length()>0)
                        {
                            job_profile_apply.setProfile_apply(profile);
                        }
                        job_profile_apply.setMacv(ob.getString("macv"));//
                        job_profile_apply.setTencongviec(ob.getString("tencv"));//
                        job_profile_apply.setQuymo(ob.getString("quymo"));//
                        job_profile_apply.setTecongty(ob.getString("tenct"));//
                        job_profile_apply.setDiachict(ob.getString("diadiem"));//
                        job_profile_apply.setNganhNghe(ob.getString("nganhNghe"));//
                        job_profile_apply.setNgannghe(ob.getString("nganhnghe"));
                        job_profile_apply.setMotact(ob.getString("motact"));
                        job_profile_apply.setLuong(ob.getString("mucluong"));//
                        job_profile_apply.setDiadiem(ob.getString("diadiem"));
                        job_profile_apply.setDateup(ob.getString("hannophoso"));//
                        job_profile_apply.setSoluong(ob.getString("soluong"));
                        job_profile_apply.setMotacv(ob.getString("motacv"));//
                        job_profile_apply.setBangcap(ob.getString("bangcap"));//
                        job_profile_apply.setNgoaingu(ob.getString("ngoaingu"));//
                        job_profile_apply.setDotuoi(ob.getString("dotuoi"));//
                        job_profile_apply.setGioitinh(ob.getString("gioitinh"));//
                        job_profile_apply.setKhac(ob.getString("khac"));//
                        job_profile_apply.setKn(ob.getString("kynang"));//
                        job_profile_apply.setUrl(ob.getString("img"));//
                        job_profile_apply.setPhucloi(ob.getString("phucloi"));//
                        celebrities.add(job_profile_apply);
                      }
                 adapter2 = new ListJobs_ShareAdapter(ChattingActivity.this, android.R.layout.simple_list_item_1, celebrities);
                 lv.setAdapter(adapter2);
                    createjob.setVisibility(View.GONE);
                }else{
//                    progressBar.setVisibility(View.GONE);
                    createjob.setVisibility(View.VISIBLE);
                }
            }else {
                createjob.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.GONE);
    }

    private class SoftKeyboardHandledRelativelayout {
    }


//    @Override
//    public void onResume() {
//        if(adapter2!= null)
//        {
//            adapter2.clear();
//            adapter2.notifyDataSetChanged();
//        }
////        asyncRequestObject = new AsyncDataClass();
////        asyncRequestObject.execute(AppConfig.URL_XUATHS1, id);
//        getData(uid);
//        super.onResume();
//    }
}

