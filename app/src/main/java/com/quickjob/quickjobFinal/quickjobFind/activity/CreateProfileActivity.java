package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


/**
 * Created by VinhNguyen on 7/6/2016.
 */
public class CreateProfileActivity extends AppCompatActivity {
    String[] arr={"Tất cả ngành nghề","Dược sỹ","Trình dược viên","Bảo trì/Sửa chửa","Bán hàng","Bảo hiểm","Bất động sản","Biên dịch viên","Chứng khoáng","Công nghệ cao","IT Phần cứng/Mạng","Internet/Online Media","IT -Phần mềm","Cơ khí,chế tạo","Dệt may/Da giày","Dịch vụ khách hàng","Hàng không/Du lịch","Điện/Điện tử","Giáo dục/Đào tạo","Kế toán","Kiểm toán","Y tế/Chăm sóc sức khỏe","Kiến trúc/Thiết kế nội thất","Ngân hàng","Mỹ thuật/Nghệ thuật/Thiết kế","Nhân sự","Nông nghiệp/Lâm nghiệp","Pháp lý","Phi chính phủ/Phi lợi nhuận","Cấp quản lý điều hành","Quản cáo/Khuyến mại/Đối thoại","Sản Xuất","Thời vụ/Hợp đồng ngắn hạn","Tài chính/Đầu tư","Thời trang","Thực phẩm đồ uống","Truyền hình/Truyền thông/Báo chí","Marketing","Tư vấn","Vận chuyển/Giao nhận","Thu mua/Vật tư/Cung vận","Viễn thông","Xây dựng","Xuất nhập khẩu","Tự động hóa/Ôtô","Overseass Jop","Khác"};
    private RadioGroup rdgroup;
    private RadioButton rdnam, rdnu;
    private Button ngaysinh, create, rep;
    private EditText spvitri, edname, edmail, edphone, eddiachi, edquequan, edtentruong, edchuyennganh, edthanhtuu, ednamkinhnghiem, edtencongty, edchucdanh, edmotacongviec, edtencv, edluong,edslogan;
    private Spinner spxeploai, mucluong, nganhNghe, eddd,speducation;
    private LinearLayout lin, ngoaingu1, ngoaingu2, ngoaingu3, lin2, kynang1, kynang2, kynang3;
    private TextView nn1, nn2, nn3, kn1, kn2, kn3, txtNN, txtKN;
    private ImageView logo;
    private ImageButton n1, n2, n3, k1, k2, k3, themnn, themkn;
    private int key = 0, key2 = 0, key3 = 0, key4 = 0, key5 = 0, key6 = 0,posedu;
    private String KEY_IMAGE = "image", KEY_NAME = "name" , ngoaiNgu = "", kyNang = "", gioitinh = "", uniqueid = "", mail = "", sdt = "", diachi = "", quequan = "", tentruong = "", chuyennganh = "", thanhtuu = "", tencongty = "", chucdanh = "", motacv = "", tencv = "", dd = "", vt = "", ten1 = "", birtdate = "", ten = "", gt = "", ns = "", em = "", p = "", dc = "", qq = "", logo1 = "",yearskill="0",slogan="";
    private Calendar calendar;
    private int PICK_IMAGE_REQUEST = 2;
    private  int SELECT_PICTURE = 1;
    private Bitmap bitmap;
    private boolean isfocused;
    private Uri filePath;
    private int year, month, day;
    private ArrayAdapter adapter, adaptennr, adapterdiadiem, adapterluong,adaptereducation;
    private int statuss = 0, stt = 0;
    private int ss = 0,bd=0,nn=0,ml,xeploai1;
    static String image="";
    private ProgressDialog pDialog;
    // Session Manager Class
    public ScrollView scroll;
    private SessionManager session;
    private String emailpref, namepref;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_createprofile);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        em = user.get(SessionManager.KEY_EMAIL);
        ten= user.get(SessionManager.KEY_NAME);
        logo = (ImageView) findViewById(R.id.upimage);
        logo.setImageResource(R.drawable.profile);
        getData();
        addView();
        setData();
        verifyStoragePermissions(this);
        n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = 0;
                ngoaingu1.setVisibility(View.GONE);
            }
        });
        n2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key2 = 0;
                ngoaingu2.setVisibility(View.GONE);
            }
        });
        n3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key3 = 0;
                ngoaingu3.setVisibility(View.GONE);
            }
        });
        k1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key4 = 0;
                kynang1.setVisibility(View.GONE);

            }
        });
        k2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key5 = 0;
                kynang2.setVisibility(View.GONE);
            }
        });
        k3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key6 = 0;
                kynang3.setVisibility(View.GONE);
            }
        });
        ngaysinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });
        scroll = (ScrollView)findViewById(R.id.scrollview);

        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });
        ednamkinhnghiem.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateProfileActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view1 = inflater.inflate(R.layout.dialog_number_skill, null);
                builder.setTitle(R.string.st_skill);
                builder.setView(view1);
                final NumberPicker np = (NumberPicker) view1.findViewById(R.id.numberpicker);
                np.setMaxValue(30);
                np.setMinValue(0);
                builder.setPositiveButton(R.string.st_xacNhanOK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yearskill = np.getValue() + "";
                        ednamkinhnghiem.setText(yearskill);
                    }
                });
                builder.create().show();
            }
        });


        themkn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtKN.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateProfileActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view1 = inflater.inflate(R.layout.dialog_createskill, null);
                builder.setTitle(R.string.st_kinang);
                builder.setView(view1);
                builder.setNegativeButton(R.string.st_thoat, null);
                builder.setPositiveButton(R.string.st_chon, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText e = (EditText) view1.findViewById(R.id.edkynang);

                        String k = e.getText().toString();
                        if (k == null || k.equals("")) {
                        } else {
                            if (key4 == 0) {
                                kynang1.setVisibility(View.VISIBLE);
                                kn1.setText(k + "");
                                key4 = 1;
                            } else if (key5 == 0) {
                                kynang2.setVisibility(View.VISIBLE);
                                kn2.setText(k + "");
                                key5 = 1;
                            } else {
                                kynang3.setVisibility(View.VISIBLE);
                                kn3.setText(k + "");
                                key6 = 1;
                            }
                        }
                    }
                });

                builder.create().show();
            }
        });
        themnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNN.setVisibility(View.GONE);

                final String[] ngoaingu = getResources().getStringArray(R.array.spNgoaiNgu);
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateProfileActivity.this);
                builder.setTitle(R.string.st_ngoaingu).setItems(R.array.spNgoaiNgu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        if (key == 0) {
                            ngoaingu1.setVisibility(View.VISIBLE);
                            nn1.setText(ngoaingu[position] + "");
                            key = 1;
                        } else if (key2 == 0) {
                            ngoaingu2.setVisibility(View.VISIBLE);
                            nn2.setText(ngoaingu[position] + "");
                            key2 = 1;
                        } else {
                            ngoaingu3.setVisibility(View.VISIBLE);
                            nn3.setText(ngoaingu[position] + "");
                            key3 = 1;
                        }
                    }
                });
                builder.create().show();
            }
        });
        rdgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int idnam = rdnam.getId();
                int idnu = rdnu.getId();
                if (checkedId == idnam) {
                    gioitinh = "1";
                } else if (checkedId == idnu) {
                    gioitinh = "2";
                }else{
                    gioitinh = "0";
                }
            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ss == 0) {
                    mail = edmail.getText().toString();
                    sdt = edphone.getText().toString();
                    diachi = eddiachi.getText().toString();
                    quequan = edquequan.getText().toString();
                    tentruong = edtentruong.getText().toString();
                    chuyennganh = edchuyennganh.getText().toString();
                    thanhtuu = edthanhtuu.getText().toString();
                    //        namkn = ednamkinhnghiem.getText().toString() + " Năm";
                    tencongty = edtencongty.getText().toString();
                    chucdanh = edchucdanh.getText().toString();
                    motacv = edmotacongviec.getText().toString();
                    tencv = edtencv.getText().toString();
                    //  nn = String.valueOf(nganhNghe.getSelectedItem());
                    nn = nganhNghe.getSelectedItemPosition();
                    // ml = String.valueOf(mucluong.getSelectedItem());
                    ml = mucluong.getSelectedItemPosition();
                    int andress = eddd.getSelectedItemPosition();
                    dd = String.valueOf(eddd.getSelectedItem());
                    vt = spvitri.getText().toString();
                    ten1 = edname.getText().toString();
                    birtdate = ngaysinh.getText().toString();
                    slogan=edslogan.getText().toString();
                    posedu=speducation.getSelectedItemPosition();
                    boolean cancel = false;
                    View focusView = null;

                    nn = nganhNghe.getSelectedItemPosition();

                    if (TextUtils.isEmpty(ten1)) {
                        edname.setError(getString(R.string.error_field_required));
                        focusView = edname;
                        cancel = true;

                    }

                    if (TextUtils.isEmpty(birtdate)) {
                        ngaysinh.setError(getString(R.string.error_field_required));
                        focusView = ngaysinh;
                        cancel = true;

                    }
                    if (TextUtils.isEmpty(mail)) {
                        edmail.setError(getString(R.string.error_field_required));
                        focusView = edmail;
                        cancel = true;
                    }
                    if (TextUtils.isEmpty(sdt)) {
                        edphone.setError(getString(R.string.error_field_required));
                        focusView = edphone;
                        cancel = true;
                    }
                    if (TextUtils.isEmpty(diachi)) {
                        eddiachi.setError(getString(R.string.error_field_required));
                        focusView = eddiachi;
                        cancel = true;
                    }
                    if (TextUtils.isEmpty(quequan)) {
                        edquequan.setError(getString(R.string.error_field_required));
                        focusView = edquequan;
                        cancel = true;
                    }
                    if (TextUtils.isEmpty(slogan)) {
                        edslogan.setError(getString(R.string.error_field_required));
                        focusView = edslogan;
                        cancel = true;
                    }
                    if (TextUtils.isEmpty(tentruong)) {
                        edtentruong.setError(getString(R.string.error_field_required));
                        focusView = edtentruong;
                        cancel = true;
                    }
                    if (TextUtils.isEmpty(chuyennganh)) {
                        edchuyennganh.setError(getString(R.string.error_field_required));
                        focusView = edchuyennganh;
                        cancel = true;
                    }
                    if (TextUtils.isEmpty(thanhtuu)) {
                        edthanhtuu.setError(getString(R.string.error_field_required));
                        focusView = edthanhtuu;
                        cancel = true;
                    }
                    if (TextUtils.isEmpty(tencv)) {
                        edtencv.setError(getString(R.string.error_field_required));
                        focusView = edquequan;
                        cancel = true;
                    }
                    if (nn == 0) {
                        Toast.makeText(CreateProfileActivity.this,"Hãy chọn ngành nghề", Toast.LENGTH_SHORT).show();
                        focusView = nganhNghe;
                        cancel = true;
                    }



                    //  spxeploai.setSelection(2);
                    // xeploai1 = String.valueOf(spxeploai.getSelectedItem());
                    xeploai1 = spxeploai.getSelectedItemPosition();
                    if (andress==0 ||ten1 == null || ten1.equals("") || mail == null || mail.equals("") || sdt == null || sdt.equals("") || diachi == null || diachi.equals("") || quequan == null || quequan.equals("") || tentruong == null || tentruong.equals("") || chuyennganh == null || chuyennganh.equals("")  || bd==0 || vt == null || vt.equals("") || dd == null || dd.equals("") || tencv == null || tencv.equals("")) {
                        Toast.makeText(CreateProfileActivity.this, R.string.st_err_taocv, Toast.LENGTH_SHORT).show();
                    } else {
                        if (key != 0) {
                            ngoaiNgu = nn1.getText().toString();
                        }
                        if (key2 != 0) {
                            ngoaiNgu = ngoaiNgu + " / " + nn2.getText().toString();
                        }
                        if (key3 != 0) {
                            ngoaiNgu = ngoaiNgu + " / " + nn3.getText().toString();
                        }

                        if (key4 != 0) {
                            kyNang = kn1.getText().toString();
                        }
                        if (key5 != 0) {
                            kyNang = kyNang + " / " + kn2.getText().toString();
                        }
                        if (key6 != 0) {
                            kyNang = kyNang + " / " + kn3.getText().toString();
                        }

                        if (stt == 2) {
                            if (statuss == 1) {
                                uploadImage();
                            } else {
                                CustomerAsyncTask asyncRequestObject = new CustomerAsyncTask();
                                asyncRequestObject.execute(AppConfig.URL_TAOHOSO, uniqueid, gioitinh, birtdate, mail, sdt, diachi, quequan, tentruong, chuyennganh, xeploai1+"", thanhtuu, yearskill, tencongty, chucdanh, motacv, ngoaiNgu, kyNang, ten1, tencv, dd, ml+"", nn+"", vt,posedu+"",slogan);
                            }
                            ss = 1;
                        } else {
                            if (statuss == 0) {
                                Toast.makeText(CreateProfileActivity.this, R.string.st_err_anh, Toast.LENGTH_SHORT).show();
                            } else {
                                uploadImage();
                                ss = 1;
                            }
                        }

                    }
                }else {
                    Toast.makeText(CreateProfileActivity.this, R.string.st_errHoso, Toast.LENGTH_SHORT).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void getData() {
        Intent s = getIntent();
        uniqueid = session.getId();
        final int status = s.getIntExtra("status", 1);
        if (status == 1) {
//            ten = s.getStringExtra("name");
//            em = s.getStringExtra("email");
//            ns = s.getStringExtra("birthdate");
//            gt = s.getStringExtra("sex");
//            p = s.getStringExtra("phone");
//            dc = s.getStringExtra("andress");
//            qq = s.getStringExtra("homeless");
//            logo1 = s.getStringExtra("logo");
//            slogan= s.getStringExtra("slogan");
            ten = session.getName();
            em = session.getEmail();
            ns = session.getBirthday();
            gt = session.getSex();
            p = session.getPhone();
            dc = session.getAddress();
            qq = session.getHomeless();
            logo1 = session.getLogo();
            slogan = session.getSlogan();

            Log.e("AAAAAAAAAA",ten+ns+gt+em+p+dc+qq+logo1+slogan);

            if(logo1.equals(""))
            {
            }else{
                //new LoadImage().execute(logo1);
                Glide.with(CreateProfileActivity.this).load(logo1)
                        .crossFade()
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(logo);
                stt = 2;
            }
        }else{

        }
    }

    private void setData() {

        if(statuss!=0)
        {
            edname.setText(ten+"");
            edmail.setText(em+"");

        }else{
            edname.setText(ten+"");
            edphone.setText(p + "");
            edquequan.setText(qq + "");
            edmail.setText(em+"");
            eddiachi.setText(dc + "");
            edslogan.setText(slogan+"");
            if(ns.equals("")) {

            }else{
                ngaysinh.setText(ns + "");
                bd=1;
            }
            if (gt.equals("1")) {
                rdnam.setChecked(true);
                gioitinh = "1";
            } else {
                rdnu.setChecked(true);
                gioitinh = "2";
            }
        }

    }
    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            System.out.println(filePath);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Getting the Bitmap from Gallery
            int currentapiVersion = Build.VERSION.SDK_INT;
            try {
                if(currentapiVersion >= 19){
                    bitmap = getReducedBitmap(filePath, 1024, 600000);
                }
                else if( currentapiVersion >= 11){
                    bitmap = getReducedBitmap(filePath, 1024, 600000);
                }
                else {
                    bitmap = getReducedBitmap(filePath, 1024, 600000);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            logo.setImageBitmap(bitmap);
            statuss = 1;
        }else if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            logo.setImageBitmap(bitmap);
            statuss = 1;
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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

    private void uploadImage() {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, getString(R.string.st_uploading), getString(R.string.st_plsWait), false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        loading.dismiss();
                        image=s;
                        Log.d("aaaaaaaaaaaa",s);
                      //  Toast.makeText(CreateProfileActivity.this, s, Toast.LENGTH_SHORT).show();
                        CustomerAsyncTask asyncRequestObject = new CustomerAsyncTask();
                        asyncRequestObject.execute(AppConfig.URL_TAOHOSO, uniqueid, gioitinh, birtdate, mail, sdt, diachi, quequan, tentruong, chuyennganh, xeploai1+"", thanhtuu, yearskill, tencongty, chucdanh, motacv, ngoaiNgu, kyNang, ten1, tencv, dd, ml+"", nn+"", vt,posedu+"",slogan);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(CreateProfileActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = uniqueid;

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    public class CustomerAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(new File(getCacheDir(), "okdata"), cacheSize);
            CacheControl cacheControl = new CacheControl.Builder().maxAge(2, TimeUnit.HOURS).build();

            OkHttpClient client = new OkHttpClient.Builder()

                    .cache(cache)
                    .build();
            RequestBody body;

            body = new FormBody.Builder()
                    .add("uniqueid", params[1])
                    .add("gioitinh", params[2])
                    .add("birtdate", params[3])
                    .add("mail", params[4])
                    .add("sdt", params[5])
                    .add("diachi", params[6])
                    .add("quequan", params[7])
                    .add("tentruong", params[8])
                    .add("chuyennganh", params[9])
                    .add("xeploai", params[10])
                    .add("thanhtuu", params[11])
                    .add("namkn", params[12])
                    .add("tencongty", params[13])
                    .add("chucdanh", params[14])
                    .add("motacv", params[15])
                    .add("ngoaiNgu", params[16])
                    .add("kyNang", params[17])
                    .add("hoten", params[18])
                    .add("tencv", params[19])
                    .add("diadiem", params[20])
                    .add("mucluong", params[21])
                    .add("nganhnghe", params[22])
                    .add("vitri", params[23])
                    .add("education", params[24])
                    .add("slogan", params[25])
                    .build();





            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(params[0])
                    .cacheControl(cacheControl)
                    .post(body)
                    .build();
            okhttp3.Response forceCacheResponse = null;
            String responsestring = "";
            try {
                forceCacheResponse = client.newCall(request).execute();

                responsestring = forceCacheResponse.body().string();

                if(forceCacheResponse.isSuccessful()){


                }

                else{


                }


            } catch (Exception e) {

                e.printStackTrace();


            }

            return responsestring;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            super.onPostExecute(result);
            System.out.println("Resulted Value: " + result);
            if (result.equals("") || result == null) {
                Toast.makeText(CreateProfileActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
                return;
            }
            int jsonResult = returnParsedJsonObject(result);
            if (jsonResult == 0) {
                Toast.makeText(CreateProfileActivity.this, R.string.st_errNamePass, Toast.LENGTH_SHORT).show();
                return;
            }
            if (jsonResult == 1) {
                Toast.makeText(CreateProfileActivity.this, R.string.st_create_success, Toast.LENGTH_SHORT).show();
                //finish();
              /*  fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                List_Profile main = new List_Profile();
                fragmentTransaction.replace(R.id.container, main);
                fragmentTransaction.commit(); */
        /*    Intent i = new Intent(CreateProfileActivity.this, SlingdingMenuActivity.class);
                i.putExtra("update", 1);
                i.putExtra("USERNAME", em);
                i.putExtra("name", ten);
                i.putExtra("logo", logo1);
                i.putExtra("uid", uniqueid);
                startActivity(i);*/
                finish();
            }
        }
    }
    private int returnParsedJsonObject(String result) {

        JSONObject resultObject = null;
        int returnedResult = 0;
        try {
            resultObject = new JSONObject(result);
            returnedResult = resultObject.getInt("success");
            //   kqq = resultObject.getString("kynang");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }
    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateProfileActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                logo.setImageBitmap(image);
                pDialog.dismiss();

            }else{
                pDialog.dismiss();
                Toast.makeText(CreateProfileActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            int thang = arg2+1;
            ngaysinh.setText(arg1 + "/" + thang + "/" + arg3);
            bd=1;
        }
    };

    private void addView() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        speducation= (Spinner) findViewById(R.id.speducation);
        edslogan = (EditText) findViewById(R.id.edgt);
        eddd = (Spinner) findViewById(R.id.eddiadiem);
        edtencv = (EditText) findViewById(R.id.edtencv);
        mucluong = (Spinner) findViewById(R.id.spluong);
        nganhNghe = (Spinner) findViewById(R.id.spnganhnghe);
        spvitri = (EditText) findViewById(R.id.edvitri);
        //  rep = (Button) findViewById(R.id.rep);
        rdgroup = (RadioGroup) findViewById(R.id.radioGroup);
        rdnam = (RadioButton) findViewById(R.id.radioNam);
        rdnu = (RadioButton) findViewById(R.id.radioNu);
        ngaysinh = (Button) findViewById(R.id.btngaysinh);
        create = (Button) findViewById(R.id.createhs);
        edmail = (EditText) findViewById(R.id.edmail);
        edphone = (EditText) findViewById(R.id.edphone);
        eddiachi = (EditText) findViewById(R.id.eddiachi);
        edquequan = (EditText) findViewById(R.id.edquequan);
        edtentruong = (EditText) findViewById(R.id.edtentruong);
        edchuyennganh = (EditText) findViewById(R.id.edchuyennganh);
        edthanhtuu = (EditText) findViewById(R.id.edthanhtuu);
        edname = (EditText) findViewById(R.id.edname);
        ednamkinhnghiem = (EditText) findViewById(R.id.ednamkinhnghiem);
        edtencongty = (EditText) findViewById(R.id.edtencongty);
        edmotacongviec = (EditText) findViewById(R.id.edmotacv);
        edchucdanh = (EditText) findViewById(R.id.edchucdanh);
        themnn = (ImageButton) findViewById(R.id.themnn);
        themkn = (ImageButton) findViewById(R.id.themkn1);
        n1 = (ImageButton) findViewById(R.id.n1);
        n2 = (ImageButton) findViewById(R.id.n2);
        n3 = (ImageButton) findViewById(R.id.n3);
        k1 = (ImageButton) findViewById(R.id.k1);
        k2 = (ImageButton) findViewById(R.id.k2);
        k3 = (ImageButton) findViewById(R.id.k3);
        TextView txtnn = (TextView) findViewById(R.id.txtnn);
        TextView txtkn = (TextView) findViewById(R.id.txtkn);
        nn1 = (TextView) findViewById(R.id.nn1);
        nn2 = (TextView) findViewById(R.id.nn2);
        nn3 = (TextView) findViewById(R.id.nn3);
        kn1 = (TextView) findViewById(R.id.kn1);
        kn2 = (TextView) findViewById(R.id.kn2);
        kn3 = (TextView) findViewById(R.id.kn3);
        ngoaingu1 = (LinearLayout) findViewById(R.id.ngoaingu1);
        ngoaingu2 = (LinearLayout) findViewById(R.id.ngoaingu2);
        ngoaingu3 = (LinearLayout) findViewById(R.id.ngoaingu3);
        kynang1 = (LinearLayout) findViewById(R.id.kynang1);
        kynang2 = (LinearLayout) findViewById(R.id.kynang2);
        kynang3 = (LinearLayout) findViewById(R.id.kynang3);
        spxeploai = (Spinner) findViewById(R.id.spxeploai);
        txtNN = (TextView) findViewById(R.id.txtnn);
        txtKN = (TextView) findViewById(R.id.txtkn);
        adaptennr = ArrayAdapter.createFromResource(CreateProfileActivity.this, R.array.nganhNghe, android.R.layout.simple_spinner_item);
        adaptennr.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        nganhNghe.setAdapter(adaptennr);
        adapterluong = ArrayAdapter.createFromResource(CreateProfileActivity.this, R.array.mucluong, android.R.layout.simple_spinner_item);
        adapterluong.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        mucluong.setAdapter(adapterluong);
        adapterdiadiem = ArrayAdapter.createFromResource(CreateProfileActivity.this, R.array.diadiem, android.R.layout.simple_spinner_item);
        adapterdiadiem.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        eddd.setAdapter(adapterdiadiem);
        adapter = ArrayAdapter.createFromResource(CreateProfileActivity.this, R.array.spXepLoai, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spxeploai.setAdapter(adapter);
        adaptereducation = ArrayAdapter.createFromResource(CreateProfileActivity.this, R.array.speducation, android.R.layout.simple_spinner_item);
        adaptereducation.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        speducation.setAdapter(adaptereducation);

    }
    public void init() {
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder getImageFrom = new AlertDialog.Builder(CreateProfileActivity.this);
                getImageFrom.setTitle("Select");
                final CharSequence[] opsChars = {getResources().getString(R.string.st_camera), getResources().getString(R.string.st_gallery)};
                getImageFrom.setItems(opsChars, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
//                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                            startActivityForResult(cameraIntent, PICK_IMAGE_REQUEST);
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, PICK_IMAGE_REQUEST);
                            }
                        } else if (which == 1) {

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent,getResources().getString(R.string.st_chon)), SELECT_PICTURE);
                        }
                    }
                });
                getImageFrom.create().show();
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

    }
    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        else
        {
            init();
        }
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    init();
                }
        }
    }
//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) activity.getSystemService(
//                        Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(
//                activity.getCurrentFocus().getWindowToken(), 0);
//    }
//    public void setupUI(View view) {
//
//        // Set up touch listener for non-text box views to hide keyboard.
//        if (!(view instanceof EditText)) {
//            view.setOnTouchListener(new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
//                    hideSoftKeyboard(CreateProfileActivity.this);
//                    return false;
//                }
//
//
//            });
//        }
//
//        //If a layout container, iterate over children and seed recursion.
//        if (view instanceof ViewGroup) {
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                View innerView = ((ViewGroup) view).getChildAt(i);
//                setupUI(innerView);
//            }
 //       }
  //  }
}