package com.quickjob.quickjobFinal.quickjobHire.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.other.CircleTransform;
import com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager;

import java.util.HashMap;
import java.util.Locale;

public class SettingActivity extends Fragment {

    private Toolbar toolbar;
    private LinearLayout lnInfo, lnChangePass,lnexit, lnabout,changelanguage;
    private SessionManager session;
    private String emailpref, namepref, logopref, logo="", logo1 ="";;
    private ImageView imAccount;

    private TextView tvName,tvMail;
    private Switch switchNoti;
    View view;
    Locale myLocale;
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.activity_setting, container, false);
        lnInfo = (LinearLayout) view.findViewById(R.id.lnInfo);
        changelanguage = (LinearLayout) view.findViewById(R.id.changelanguage);
        lnChangePass = (LinearLayout) view.findViewById(R.id.lnChangePass);
        lnexit = (LinearLayout) view.findViewById(R.id.lnexit);
        lnabout = (LinearLayout) view.findViewById(R.id.lnabout);
        imAccount = (ImageView) view.findViewById(R.id.imAccount);
        tvName = (TextView) view.findViewById(R.id.tvName);
        switchNoti = (Switch) view.findViewById(R.id.switchNoti);
        session = new SessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();

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
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        getActivity());

                // Setting Dialog Title
                alertDialog2.setTitle(getResources().getString(R.string.confirm_delete));

                // Setting Dialog Message
                alertDialog2.setMessage(getResources().getString(R.string.delete_detail));


                // Setting Icon to Dialog
                alertDialog2.setIcon(R.drawable.logo_hire);

                // Setting Positive "Yes" Btn
                alertDialog2.setPositiveButton(R.string.btn_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
//                                               Toast.makeText(getActivity(),
//                                                              "You clicked on YES", Toast.LENGTH_SHORT)
//                                                       .show();
                                session.logoutUser();
                                Intent s= new Intent(getActivity(), LoginActivity.class);
                                s.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(s);
                                getActivity().finish();
                            }
                        });

                // Setting Negative "NO" Btn
                alertDialog2.setNegativeButton(R.string.btn_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
//                                               Toast.makeText(getActivity(),
//                                                              "You clicked on NO", Toast.LENGTH_SHORT)
//                                                       .show();
//                                               dialog.cancel();
                                dialog.cancel();
                            }
                        });

                // Showing Alert Dialog
                alertDialog2.show();


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

    private void event() {
        lnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s= new Intent(getActivity(), ChangePasswordActivity.class);
                s.putExtra("logo",logo);
                startActivity(s);

            }
        });
    }

    private void loadImage() {

        tvName.setText(namepref);

        if(logo.equals("")) {
            Glide.with(getActivity()).load(R.drawable.profile)
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

