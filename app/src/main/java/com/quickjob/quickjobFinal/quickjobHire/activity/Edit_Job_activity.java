package com.quickjob.quickjobFinal.quickjobHire.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.AutoLabelUISettings;
import com.dpizarro.autolabel.library.Label;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobHire.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobHire.config.AppController;
import com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import static com.quickjob.quickjobFinal.R.array.nganhNghe;
import static com.quickjob.quickjobFinal.R.id.btnreplogo;

/**
 * Created by VinhNguyen on 3/31/2017.
 */

public class Edit_Job_activity extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;
    private static final int PLACE_PICKER_REQUEST = 3;
    public ScrollView scroll;

    private EditText ed_namejob,ed_address_company,ed_infor_company,ed_age,
            ed_number_apply,ed_infor_job,ed_pl,ed_request,ed_address_job,ed_datelimit,txt_namecompany;
    private Bitmap bitmap;
    private RadioButton ra_nam,ra_nu,ra_not;
    private Spinner sp_salary,sp_carrer,sp_education;
    private Button btn_register,btn_delete,btn_photo,btn_repphoto;
    private String macv="",matd="",age,languages="",skill="",namejob,address_company,infor_company,number_apply,infor_job,pl,request,address_job,namecompany,url,uid,datelimt;
    private int salary=0,carrer=0,gender=2,education=0;
    private ArrayList<String> arrskill = new ArrayList<>(),arrlanguage = new ArrayList<>();
    private AutoLabelUI mAutoLabellang,mAutoLabelskill;
    private ImageView img_logocompany;double latitude,longitude;SimpleDateFormat dateFormatter;
    private boolean getlogo =false,setimage=false,delete_create_satus=false;private Uri filePath;SessionManager session;SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_job_layout_hire);
        addView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setAutoLabelUISettings();
        getIntentData();
        setListeners();


    }
    private void addView() {
        ra_nam = (RadioButton) findViewById(R.id.radio_man);
        ra_nu = (RadioButton) findViewById(R.id.radio_woman);
        ra_not = (RadioButton) findViewById(R.id.radio_notrequest);
        mAutoLabellang =(AutoLabelUI) findViewById(R.id.label_lang);
        mAutoLabelskill=(AutoLabelUI) findViewById(R.id.label_skill);
        ed_datelimit =(EditText) findViewById(R.id.ed_datelimit);
        img_logocompany = (ImageView) findViewById(R.id.img_logcompany);
        btn_photo =(Button) findViewById(R.id.btnphoto);
        btn_repphoto =(Button) findViewById(btnreplogo);
        txt_namecompany = (EditText) findViewById(R.id.tencongty);
        ed_namejob      = (EditText) findViewById(R.id.ed_namejob);
        ed_address_company      = (EditText) findViewById(R.id.ed_address_company);
        ed_infor_company      = (EditText) findViewById(R.id.ed_infor_company);
        ed_number_apply      = (EditText) findViewById(R.id.ed_number_apply);
        ed_infor_job      = (EditText) findViewById(R.id.ed_infor_job);
        ed_pl      = (EditText) findViewById(R.id.ed_pl);
        ed_request      = (EditText) findViewById(R.id.ed_request);
        ed_address_job      = (EditText) findViewById(R.id.ed_address_job);
        btn_register        =(Button) findViewById(R.id.btn_reg);
        btn_register.setText(getResources().getString(R.string.st_btnEdit));
        btn_delete        =(Button) findViewById(R.id.btn_delete);
        btn_delete.setVisibility(View.VISIBLE);
        sp_salary      = (Spinner) findViewById(R.id.sp_salary);
        sp_carrer  = (Spinner) findViewById(R.id.sp_carrer);
        sp_education  = (Spinner) findViewById(R.id.sp_education);
        ed_age =(EditText) findViewById(R.id.ed_age);
        ArrayAdapter adaptereducation= ArrayAdapter.createFromResource(Edit_Job_activity.this,R.array.spHocVan,android.R.layout.simple_spinner_item);
        adaptereducation.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_education.setAdapter(adaptereducation);
        ArrayAdapter carreradapter= ArrayAdapter.createFromResource(Edit_Job_activity.this, nganhNghe,android.R.layout.simple_spinner_item);
        carreradapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_carrer.setAdapter(carreradapter);
        ArrayAdapter salaryadapter= ArrayAdapter.createFromResource(Edit_Job_activity.this, R.array.mucluong,android.R.layout.simple_spinner_item);
        salaryadapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_salary.setAdapter(salaryadapter);
        ed_address_company.setError(null);
        ed_namejob.setError(null);
        ed_infor_company.setError(null);
        ed_number_apply.setError(null);
        ed_infor_job.setError(null);
        ed_pl.setError(null);
        ed_request.setError(null);
        ed_address_job.setError(null);
        txt_namecompany.setError(null);
        ed_age.setError(null);
    }
    private void getIntentData() {
        session = new SessionManager(Edit_Job_activity.this);
        SharedPreferences pref=getSharedPreferences("JobFindPref", MODE_PRIVATE);
        edit=pref.edit();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        uid=user.get(SessionManager.KEY_ID);
        Intent get = getIntent();
        macv = get.getStringExtra("macv");
       // matd = get.getStringExtra("matd");
        namecompany = get.getStringExtra("tencongty");
        address_company = get.getStringExtra("diachi");
        infor_company = get.getStringExtra("motact");
        namejob = get.getStringExtra("tencongviec");
        address_job = get.getStringExtra("diadiem");
        salary =Integer.parseInt( get.getIntExtra("mucluong",2)+"");
        datelimt = get.getStringExtra("ngayup");
        age = get.getStringExtra("dotuoi");
        gender = Integer.parseInt(get.getStringExtra("gioitinh")+"");
        education = Integer.parseInt(get.getStringExtra("yeucaubangcap")+"");
        request = get.getStringExtra("khac");
        infor_job = get.getStringExtra("motacv");
        pl = get.getStringExtra("phucloi");
        carrer = Integer.parseInt(get.getStringExtra("nganhNghe"));
        number_apply = get.getStringExtra("soluong");
        url = get.getStringExtra("img");
        ed_namejob.setText(namejob);
        ed_address_job.setText(address_job);
        sp_salary.setSelection(salary);
        sp_carrer.setSelection(carrer);
        ed_datelimit.setText(datelimt);
        ed_age.setText(age);
        if(gender==0)
        {
            ra_nam.setChecked(true);
            ra_not.setChecked(false);
        }else if(gender ==1 )
        {
            ra_nu.setChecked(true);
            ra_not.setChecked(false);
        }
        sp_education.setSelection(education);
        ed_request.setText(request);
        ed_infor_job.setText(infor_job);
        ed_pl.setText(pl);
        ed_number_apply.setText(number_apply);

        String[] languages = get.getStringExtra("ngoaingu").split("/");
        for(int i =0;i<languages.length;i++)
        {
            arrlanguage.add(languages[i]);
            if(!languages[i].equals("") || languages[i]!=null) {
                boolean success = mAutoLabellang.addLabel(languages[i]);
            }
        }
        String[] skills = get.getStringExtra("kn").split("/");
        for(int i =0;i<skills.length;i++)
        {
            arrskill.add(skills[i]);
            if(!skills[i].equals("") || skills[i]!=null) {
                boolean success = mAutoLabelskill.addLabel(skills[i]);
            }
        }
       
        if(url == null || url.equals("") || url.equals("null"))
        {

        }else{
            getlogo=true;
            btn_photo.setVisibility(View.GONE);
            btn_repphoto.setVisibility(View.VISIBLE);
            Glide.with(Edit_Job_activity.this).load(url)
                    .crossFade()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img_logocompany);
        }
        if(!namecompany.equals("") || namecompany!=null) {
            txt_namecompany.setText(namecompany);
        }
        if(!address_company.equals("") || address_company!=null) {
            ed_address_company.setText(address_company);
        }
        if(!infor_company.equals("") || infor_company!=null) {
            ed_infor_company.setText(infor_company);
        }

    }
    public void onAddLanguages(View v)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Job_activity.this);
        LayoutInflater inflater = (LayoutInflater) Edit_Job_activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_createlangues, null);
        ListView lv = (ListView) view.findViewById(R.id.lvnn);
        final String[] ngoaingu = getResources().getStringArray(R.array.spNgoaiNgu);
        ArrayAdapter ad = ArrayAdapter.createFromResource(Edit_Job_activity.this, R.array.spNgoaiNgu, android.R.layout.simple_spinner_item);
        lv.setAdapter(ad);
        builder.setTitle(R.string.st_ngoaingu);
        builder.setView(view);
        builder.setPositiveButton(R.string.st_thoat, null);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    boolean success = mAutoLabellang.addLabel(ngoaingu[position].toString());
                    if(success) {
                        arrlanguage.add(ngoaingu[position]+"");
                    }
                }
            }
        });
        builder.create().show();
    }
    public void onAddSkill(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Job_activity.this);
        LayoutInflater inflater = (LayoutInflater) Edit_Job_activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view1 = inflater.inflate(R.layout.dialog_createskill1_hire, null);
        builder.setTitle(R.string.st_kinang);
        builder.setView(view1);
        builder.setNegativeButton(R.string.st_thoat, null);
        builder.setPositiveButton(R.string.st_chon, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText e = (EditText) view1.findViewById(R.id.edkynang);
                boolean success = mAutoLabelskill.addLabel(e.getText().toString());
                if(success)
                {
                    arrskill.add(e.getText().toString());
                }
            }
        });
        builder.create().show();
    }
    private void setAutoLabelUISettings() {
        AutoLabelUISettings autoLabelUISettings =
                new AutoLabelUISettings.Builder()
                        .withBackgroundResource(R.drawable.goctron_nn)
                        .withIconCross(R.drawable.cross)
                        .withMaxLabels(6)
                        .withShowCross(true)
                        .withLabelsClickables(true)
                        .withTextColor(android.R.color.white)
                        .withTextSize(R.dimen.label_title_size)
                        .withLabelPadding(30)
                        .build();

        mAutoLabellang.setSettings(autoLabelUISettings);
        mAutoLabelskill.setSettings(autoLabelUISettings);
    }

    private void setListeners() {
        scroll = (ScrollView)findViewById(R.id.scrollPre);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_create_satus=true;
                getDataFromInternet(AppConfig.URL_DeleteJOB,"","","","","","","","","","","","","","","","","","","","","","","","",macv,uid);
            }
        });
        ed_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Job_activity.this);
                LayoutInflater inflater = (LayoutInflater) Edit_Job_activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view2 = inflater.inflate(R.layout.dialog_mucluong_hire, null);
                builder.setTitle(R.string.st_doTuoiYC);
                builder.setView(view2);
                builder.setNegativeButton(R.string.st_thoat, null);
                builder.setPositiveButton(R.string.st_chon, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText to = (EditText) view2.findViewById(R.id.to);
                        EditText from = (EditText) view2.findViewById(R.id.from);
                        String a = to.getText().toString();
                        String b = from.getText().toString();
                        ed_age.setText(a+"~"+b);
                    }
                });
                builder.create().show();
            }
        });
        ra_nam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ra_nu.setChecked(false);
                ra_not.setChecked(false);
                gender=0;
            }
        });
        ra_nu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ra_nam.setChecked(false);
                ra_not.setChecked(false);
                gender=1;
            }
        });
        ra_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  if(ra_not.isChecked()) {
                ra_nam.setChecked(false);
                ra_nu.setChecked(false);
                gender = 2;
                //  }
            }
        });
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setimage=true;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        btn_repphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setimage=true;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        mAutoLabellang.setOnRemoveLabelListener(new AutoLabelUI.OnRemoveLabelListener() {
            @Override
            public void onRemoveLabel(Label removedLabel, int position) {
                String textToRemove = removedLabel.getText().toString();
                if (!textToRemove.isEmpty()) {
                    boolean success = mAutoLabellang.removeLabel(textToRemove);
                    arrlanguage.remove(textToRemove);
                    //  arrlanguage.remove(position);
                }

            }
        });
        mAutoLabelskill.setOnRemoveLabelListener(new AutoLabelUI.OnRemoveLabelListener() {
            @Override
            public void onRemoveLabel(Label removedLabel, int position) {
                String textToRemove = removedLabel.getText().toString();
                if (!textToRemove.isEmpty()) {
                    boolean success = mAutoLabelskill.removeLabel(textToRemove);
                    arrskill.remove(textToRemove);
                }

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Edit_Job_activity.this.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            System.out.println(filePath);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Getting the Bitmap from Gallery
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            try {
                if (currentapiVersion >= 19) {

                    bitmap = getReducedBitmap(filePath, 1024, 600000);

                } else if (currentapiVersion >= 11) {
                    bitmap = getReducedBitmap(filePath, 1024, 600000);
                } else {
                    bitmap = getReducedBitmap(filePath, 1024, 600000);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            img_logocompany.setImageBitmap(bitmap);
        }
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(Edit_Job_activity.this, data);
                if (place != null) {
                    LatLng latLng = place.getLatLng();
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                    String add = (String) place.getAddress();
                    ed_address_job.setText(add);
                }
            }
        }
    }
    public Bitmap getReducedBitmap(Uri imgPath, int MaxWidth, long MaxFileSize) throws FileNotFoundException, IOException {
        // MaxWidth = maximum image width
        // MaxFileSize = maximum image file size
        InputStream input = this.getContentResolver().openInputStream(imgPath);
        long length = 500000;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            // read image file
            BitmapFactory.decodeStream(input, null, options);
            int srcWidth = options.outWidth;
            int scale = 1;
            while ((srcWidth > MaxWidth) || (length > MaxFileSize)) {
                srcWidth /= 2;
                scale *= 2;
                length = length / 2;
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            options.inScaled = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            // read again image file with new constraints
            input = this.getContentResolver().openInputStream(imgPath);
            return (BitmapFactory.decodeStream(input, null, options));
        } catch (Exception e) {
            return null;
        }
    }

    public void  onDateLimitApply(View v){
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(Edit_Job_activity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                ed_datelimit.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }
    public void onRegister(View v)
    {
        age = ed_age.getText().toString();
        namecompany = txt_namecompany.getText().toString();
        namejob = ed_namejob.getText().toString();
        address_company = ed_address_company.getText().toString();
        infor_company =ed_infor_company.getText().toString();
        number_apply = ed_number_apply.getText().toString();
        infor_job = ed_infor_job.getText().toString();
        pl = ed_pl.getText().toString();
        request = ed_request.getText().toString();
        address_job = ed_address_job.getText().toString();
        datelimt = ed_datelimit.getText().toString();
        education=sp_education.getSelectedItemPosition();
        boolean cancel = false;
        View focusView = null;
        salary = sp_salary.getSelectedItemPosition();
        carrer = sp_carrer.getSelectedItemPosition();
        int countskill =arrskill.size();
        for(int i=0 ;i<countskill;i++)
        {

            if(skill.equals("") || skill == null){
                skill=arrskill.get(i);
            }else{
                skill=skill+"/"+arrskill.get(i);
            }
        }
        int countlanguage =arrlanguage.size();
        for(int i=0 ;i<countlanguage;i++)
        {
            if(languages.equals("") || languages == null){
                languages=arrlanguage.get(i);
            }else{
                languages=languages+"/"+arrlanguage.get(i);
            }
        }
        if (TextUtils.isEmpty(namejob)) {
            ed_namejob.setError(getString(R.string.error_field_required));
            focusView = ed_namejob;
            cancel = true;
        }
        if (TextUtils.isEmpty(number_apply)) {
            ed_number_apply.setError(getString(R.string.error_field_required));
            focusView = ed_number_apply;
            cancel = true;
        }
        if (TextUtils.isEmpty(request)) {
            ed_request.setError(getString(R.string.error_field_required));
            focusView = ed_request;
            cancel = true;
        }
        if (TextUtils.isEmpty(address_job)) {
            ed_address_job.setError(getString(R.string.error_field_required));
            focusView = ed_address_job;
            cancel = true;
        }
        if (TextUtils.isEmpty(datelimt)) {
            ed_datelimit.setError(getString(R.string.error_field_required));
            focusView = ed_datelimit;
            cancel = true;
        }
//        if (salary == 0) {
//            Toast.makeText(this,"hãy chọn mức lương", Toast.LENGTH_SHORT).show();
//            focusView = ed_address_job;
//            cancel = true;
//        }
        if (carrer == 0) {
            Toast.makeText(this,"hãy chọn ngành nghề", Toast.LENGTH_SHORT).show();
            focusView = ed_address_job;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }else{
            if(setimage) {
                uploadImage();
            }else{
                if(getlogo)
                {
                    getDataFromInternet(AppConfig.URL_EDITJOB,namejob, "", salary+"", address_job+"", infor_job+"", age+"", education+"", gender+"", languages+"", skill+"", request+"", namecompany+"", "", address_company+"", "", infor_company+"", uid+"",carrer+"", datelimt+"", number_apply+"", pl+"",matd+"",latitude+"",longitude+"",macv+"","");
                }else{
                    Toast.makeText(Edit_Job_activity.this, R.string.st_err_anh, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
    private void parseJsonFeed(String result) {
        System.out.println("Resulted Value: " + result);
        if(result.equals("") || result == null){
            Toast.makeText(Edit_Job_activity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
            return;
        }
        int jsonResult = returnParsedJsonObject(result);
        if(jsonResult == 0){
            Toast.makeText(Edit_Job_activity.this, R.string.st_errNamePass, Toast.LENGTH_SHORT).show();
            return;
        }
        if(jsonResult == 1){
            if(!delete_create_satus) {
                Toast.makeText(this, getResources().getString(R.string.st_edit_success) + "", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, R.string.st_xacXoaCV, Toast.LENGTH_SHORT).show();
                finish();
            }
//            Intent s = new Intent(Edit_Job_activity.this, JobDetailActivity.class);
//            s.putExtra("macv", "");
//            s.putExtra("matd", "");
//            s.putExtra("tencongty", namecompany);
//            s.putExtra("diachi",address_company);
//            s.putExtra("nganhnghe", "");
//            s.putExtra("quymo", "");
//            s.putExtra("motact", moct);
//            s.putExtra("nganhNghe",jop);
//            s.putExtra("chucdanh", cd);
//            s.putExtra("soluong", soluongtuyen);
//            s.putExtra("phucloi",phucloi);
//            s.putExtra("tencongviec", namejop);
//            s.putExtra("diadiem",address_job);
//            s.putExtra("mucluong",arrsalary[Integer.parseInt(ml)]);
//            s.putExtra("ngayup", hnhs);
//            s.putExtra("yeucaubangcap",hv);
//            s.putExtra("dotuoi", dotuoi);
//            s.putExtra("ngoaingu", pp);
//            s.putExtra("gioitinh", gioitinh);
//            s.putExtra("khac", yck);
//            s.putExtra("motacv", mtcv);
//            s.putExtra("kn", oo);
//            s.putExtra("img", img);
//            startActivity(s);
//            finish();
        }
    }
    private int returnParsedJsonObject(String result){

        JSONObject resultObject = null;
        int returnedResult = 0;
        try {
            resultObject = new JSONObject(result);
            returnedResult = resultObject.getInt("success");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }
    //getDataFromInternet(AppConfig.URL_EDITJOB,namejob, "", salary+"", address_job+"", infor_job+"", age+"", education+"", gender+"", languages+"", skill+"", request+"", namecompany+"", "", address_company+"", "", infor_company+"", uid+"",carrer+"", datelimt+"", number_apply+"", pl+"",matd+"",latitude+"",longitude+"",macv+"","");

    private void getDataFromInternet(final String url,final String namejop,final String chucdanh,final String mucluong,final String diadiem,final String motacongviec
            ,final String tuoiyeucau,final String hocvan,final String gioitinh,final String ngoaingu,final String kynang
            ,final String yeucaukhac,final String tencongty,final String quymo,final String diachi,final String nganhnghe
            ,final String gioithieucongty,final String uid,final String tennn,final String hannophoso,final String soluong
            ,final String phucloi,final String matd,final String lat,final String lot
            ,final String macv,final String id) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/
        // Tag used to cancel the request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                parseJsonFeed(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                if(!delete_create_satus) {
                    params.put("namejop", namejop);
                    params.put("chucdanh", chucdanh);
                    params.put("mucluong", mucluong);
                    params.put("diadiem", diadiem);
                    params.put("motacongviec", motacongviec);
                    params.put("tuoiyeucau", tuoiyeucau);
                    params.put("hocvan", hocvan);
                    params.put("gioitinh", gioitinh);
                    params.put("ngoaingu", ngoaingu);
                    params.put("kynang", kynang);
                    params.put("yeucaukhac", yeucaukhac);
                    params.put("tencongty", tencongty);
                    params.put("quymo", quymo);
                    params.put("diachi", diachi);
                    params.put("nganhnghe",nganhnghe);
                    params.put("gioithieucongty", gioithieucongty);
                    params.put("uid", uid);
                    params.put("tennn", tennn);
                    params.put("hannophoso", hannophoso);
                    params.put("soluong", soluong);
                    params.put("phucloi",phucloi);
                    params.put("macv", macv);
                    params.put("matd", matd);
                    params.put("lat", lat);
                    params.put("lot", lot);
                }else{
                    params.put("id", id);
                    params.put("macv", macv);
                }

                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(this).requestQueue.add(strReq);
    }
    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(Edit_Job_activity.this,getString(R.string.st_uploading),getString(R.string.st_plsWait),false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        getDataFromInternet(AppConfig.URL_EDITJOB,namejob, "", salary+"", address_job+"", infor_job+"", age+"", education+"", gender+"", languages+"", skill+"", request+"", namecompany+"", "", address_company+"", "", infor_company+"", uid+"",carrer+"", datelimt+"", number_apply+"", pl+"",matd+"",latitude+"",longitude+"",macv+"","");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = uid;

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();
                //Adding parameters
                params.put("image", image);
                params.put("name", name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(Edit_Job_activity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public void onPickPlaceWork(View v){
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(Edit_Job_activity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
