package com.quickjob.quickjobFinal.quickjobFind.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.other.CircleTransform;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.HashMap;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SettingActivity extends Fragment {
    Spinner spinnerctrl;

    Locale myLocale;

    private Toolbar toolbar;
    private LinearLayout lnInfo, lnChangePass,lnexit, lnabout,changelanguage;
    private SessionManager session;
    private String emailpref, namepref, logopref, logo="", logo1 ="";;
    private ImageView imAccount;
    private TextView tvName,tvMail;
    private Switch switchNoti;

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(this.getContext());
        view =  inflater.inflate(R.layout.activity_setting, container, false);
//        spinnerctrl = (Spinner) view.findViewById(R.id.spinner1);
//        spinnerctrl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int pos, long id) {
//
//                if (pos == 1) {
//
//                    Toast.makeText(parent.getContext(),
//                            "You have selected English", Toast.LENGTH_SHORT)
//                            .show();
//                    setLocale("en");
//                } else if (pos == 2) {
//
//                    Toast.makeText(parent.getContext(),
//                            "You have selected Korean", Toast.LENGTH_SHORT)
//                            .show();
//                    setLocale("ko");
//                } else if (pos == 3) {
//
//                    Toast.makeText(parent.getContext(),
//                            "You have selected Vietnamese", Toast.LENGTH_SHORT)
//                            .show();
//                    setLocale("vi");
//                }
//
//            }
//
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//            }
//
//        });

        lnInfo = (LinearLayout) view.findViewById(R.id.lnInfo);
        changelanguage = (LinearLayout) view.findViewById(R.id.changelanguage);
        lnChangePass = (LinearLayout) view.findViewById(R.id.lnChangePass);
        lnexit = (LinearLayout) view.findViewById(R.id.lnexit);
        lnabout = (LinearLayout) view.findViewById(R.id.lnabout);
        imAccount = (ImageView) view.findViewById(R.id.imAccount);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvMail= (TextView) view.findViewById(R.id.tvMail);
        switchNoti = (Switch) view.findViewById(R.id.switchNoti);
        session = new SessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        emailpref = user.get(SessionManager.KEY_EMAIL);
        namepref= user.get(SessionManager.KEY_NAME);
        logo = user.get(SessionManager.KEY_LOGO);
        if(session.isNotification())
        {
            Log.e("TRUE","TRUE");
            switchNoti.setChecked(true);
        } else{
            switchNoti.setChecked(false);
        }

        switchNoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    session.NotiOn();
                    Log.e("Noti","ON");
                }
                else {
                    session.NotiOff();
                    Log.e("Noti","OFF");
                }
            }
        });

        if(logo == null || logo.equals(""))
        {
            logo=MainActivity.logopref;
        }

        loadImage();
        event();
        lnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InfoActivity.class));
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
            }
        });
        changelanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle(R.string.selectlanguage);
                dialogBuilder.setItems(R.array.languages, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
//                        String[] selectedText = getResources().getStringArray(R.array.languages);  //Selected item in listview
//                        String language = selectedText[item];
//                        Toast.makeText(getContext(), language, Toast.LENGTH_SHORT).show();
                        if (item == 0) {

//                    Toast.makeText(getContext(),
//                            "You have selected English", Toast.LENGTH_SHORT)
//                            .show();
                            session.setLanguage("en");
                            setLocale("en");
                } else if (item == 1) {

//                    Toast.makeText(getContext(),
//                            "You have selected Korean", Toast.LENGTH_SHORT)
//                            .show();
                            session.setLanguage("ko");
                            setLocale("ko");
                } else if (item == 2) {

//                    Toast.makeText(getContext(),
//                            "You have selected Vietnamese", Toast.LENGTH_SHORT)
//                            .show();
                            session.setLanguage("vi");
                            setLocale("vi");
                }
                    }
                });
                //Create alert dialog object via builder
                AlertDialog alertDialogObject = dialogBuilder.create();
                //Show the dialog
                alertDialogObject.show();
            }

        });
                lnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getResources().getString(R.string.st_out))
                        .setCancelText(getResources().getString(R.string.st_xacNhanCancel))
                        .setConfirmText(getResources().getString(R.string.st_xacNhanOK))
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                session.logoutUser();
                                LoginManager.getInstance().logOut();
                                Intent s= new Intent(getActivity(), LoginActivity.class);
                                s.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(s);
                                getActivity().finish();
                            }
                        })
                        .show();
            }
        });

        lnabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
            }
        });
        return view;
    }

    private void event() {
        if(session.getLoginType()){
            lnChangePass.setVisibility(View.GONE);
        }
        lnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s= new Intent(getActivity(), ChangePasswordActivity.class);
                s.putExtra("logo",logo);
                startActivity(s);
            }
        });
    }
    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(getActivity(), MainActivity.class);
        refresh.putExtra("setting", false);
        getActivity().startActivity(refresh);
    }


    private void loadImage() {

        tvName.setText(namepref);
        if(logo.equals("")) {
            Glide.with(getActivity()).load(R.drawable.user_icon)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getActivity()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imAccount);
        }else {
            Glide.with(getActivity()).load(logo)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getActivity()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imAccount);
        }


    }

}
