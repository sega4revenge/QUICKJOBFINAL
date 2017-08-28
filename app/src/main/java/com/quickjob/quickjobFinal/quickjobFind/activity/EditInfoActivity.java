package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.quickjob.quickjobFinal.quickjobFind.fragment.DatePickerFragment;
import com.quickjob.quickjobFinal.quickjobFind.other.CircleTransform;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class EditInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SessionManager session;
    private String emailpref, namepref, logo = "", uniqueid = "", agepref= "", phonepref= "", locationpref= "";
    private String location;
    private String edName, edEmail, edAge, edPhone, edLocation;
    private ImageView imgProf;
    private TextView tvName, tvEmail, tvAge, tvPhone, tvLocation;
    private LinearLayout lnLogo, lnName, lnEmail, lnAge, lnLocation, lnPhone;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    int status = 0;
    private Button btnSave;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uniqueid = MainActivity.uid;

        session = new SessionManager(getApplicationContext());

        imgProf = (ImageView) findViewById(R.id.img_profile);
        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        btnSave = (Button) findViewById(R.id.btnSave);


        lnName = (LinearLayout) findViewById(R.id.lnName);
        lnEmail = (LinearLayout) findViewById(R.id.lnEmail);
        lnLocation = (LinearLayout) findViewById(R.id.lnLocation);
        lnPhone = (LinearLayout) findViewById(R.id.lnPhone);


        lnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        // get user data from session
//        HashMap<String, String> user = session.getUserDetails();
//        emailpref = user.get(SessionManager.KEY_EMAIL);
//        namepref = user.get(KEY_NAME);
//        logo = user.get(SessionManager.KEY_LOGO);
//        agepref = user.get(SessionManager.KEY_AGE);
//        phonepref = user.get(SessionManager.KEY_PHONE);
//        locationpref = user.get(SessionManager.KEY_LOCATION);

        getData();
        loadData();
        verifyStoragePermissions(this);

        lnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPhone();
            }
        });

        lnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogEmail();
            }
        });

        lnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogName();
            }
        });


        imgProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edName = tvName.getText().toString();
                edEmail = tvEmail.getText().toString();
                edAge = tvAge.getText().toString();
                edPhone = tvPhone.getText().toString();
                edLocation = tvLocation.getText().toString();

                if (edName == null || edName.equals("") || edEmail == null || edEmail.equals("") || edAge == null || edAge.equals("") || edPhone == null || edPhone.equals("") || edLocation == null || edLocation.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.st_err_taocv, Toast.LENGTH_SHORT).show();

                } else if (status == 1) {
                    uploadImage();
                } else {
                    AsyncDataClass asyncRequestObject = new AsyncDataClass();
                    asyncRequestObject.execute(AppConfig.URL_Infomation, uniqueid, edName, edEmail, edAge, edPhone, edLocation);
                }
            }
        });


    }


    public void showDialogEmail() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditInfoActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view1 = inflater.inflate(R.layout.dialog_email, null);
        builder.setView(view1);
        final EditText edm = (EditText) view1.findViewById(R.id.edkynang);
        edm.setText(edEmail);
        builder.setTitle(R.string.st_hintEmail);
        builder.setPositiveButton(R.string.st_xacNhan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String k = edm.getText().toString();
                tvEmail.setText(k);


            }
        });
        builder.setNegativeButton(R.string.st_cancel, null);

        builder.create().show();
    }

    public void showDialogName() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditInfoActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view1 = inflater.inflate(R.layout.dialog_name, null);
        builder.setView(view1);
        final EditText e = (EditText) view1.findViewById(R.id.edkynang);
        e.setText(edName);
        builder.setTitle(R.string.st_tenProf);
        builder.setPositiveButton(R.string.st_xacNhan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String k = e.getText().toString();
                tvName.setText(k);

            }
        });
        builder.setNegativeButton(R.string.st_cancel, null);

        builder.create().show();
    }

    public void showDialogPhone() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditInfoActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view1 = inflater.inflate(R.layout.dialog_phone, null);
        builder.setView(view1);
        final EditText e = (EditText) view1.findViewById(R.id.edkynang);
        e.setText(edPhone);
        builder.setTitle(R.string.st_sdt);
        builder.setPositiveButton(R.string.st_xacNhan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String k = e.getText().toString();
                tvPhone.setText(k);

            }
        });
        builder.setNegativeButton(R.string.st_cancel, null);

        builder.create().show();
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditInfoActivity.this);
        builder.setTitle(R.string.st_selectLocation);

        //list of items
        final String[] items = getResources().getStringArray(R.array.select_setting_information);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        location = items[which];
                    }
                });

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        tvLocation.setText(location);


                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }
    private void getData() {
        Intent s = getIntent();
        namepref=s.getStringExtra("ten");
        emailpref=s.getStringExtra("mail");
        agepref=s.getStringExtra("ngaysinh");
        phonepref =s.getStringExtra("phone");
        locationpref=s.getStringExtra("quequan");
        logo=s.getStringExtra("logo");


    }
    private void loadData() {

        tvName.setText(namepref);
        tvEmail.setText(emailpref);
        tvAge.setText(agepref);
        tvPhone.setText(phonepref);
        tvLocation.setText(locationpref);
        if(logo == null || logo.equals("")) {
            Glide.with(this).load(R.drawable.user_icon)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProf);
        }else {
            Glide.with(this).load(logo)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProf);
        }
    }

    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(params[0]);
            String jsonResult = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("uniqueid", params[1]));
                nameValuePairs.add(new BasicNameValuePair("name", params[2]));
                nameValuePairs.add(new BasicNameValuePair("email", params[3]));
                nameValuePairs.add(new BasicNameValuePair("age", params[4]));
                nameValuePairs.add(new BasicNameValuePair("phone", params[5]));
                nameValuePairs.add(new BasicNameValuePair("location", params[6]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
                System.out.println("Returned Json object " + jsonResult.toString());

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("Resulted Value: " + result);
            if (result.equals("") || result == null) {
                finish();
                return;
            }

            if (result.equals("1") ||result.equals("2") ) {
                Toast.makeText(EditInfoActivity.this,R.string.st_edit_success, Toast.LENGTH_SHORT).show();
                finish();
            }


        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return answer;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            //Getting the Bitmap from Gallery
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Getting the Bitmap from Gallery
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            try {
                if(currentapiVersion >= 19){

                    bitmap = getReducedBitmap(filePath, 512, 600000);

                }
                else if( currentapiVersion >= 11){

                    bitmap = getReducedBitmap(filePath, 512, 600000);
                }
                else {
                    bitmap = getReducedBitmap(filePath, 512, 600000);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Setting the Bitmap to ImageView
            imgProf.setImageBitmap(bitmap);
            status=1;
        }
    }
    public Bitmap getReducedBitmap(Uri imgPath, int MaxWidth, long MaxFileSize) throws FileNotFoundException, IOException {
        // MaxWidth = maximum image width
        // MaxFileSize = maximum image file size

        InputStream input = this.getContentResolver().openInputStream(imgPath);

        Cursor cursor = this.getContentResolver().query(imgPath,
                null, null, null, null);
        cursor.moveToFirst();
        long length = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
        cursor.close();



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
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, getString(R.string.st_uploading), getString(R.string.st_plsWait), false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog

                        loading.dismiss();
                        AsyncDataClass asyncRequestObject = new AsyncDataClass();
                        asyncRequestObject.execute(AppConfig.URL_EDIT_PROFILE, uniqueid,  edName, edEmail, edAge, edPhone, edLocation);
                        //Showing toast message of the response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        //Showing toast
                        Toast.makeText(EditInfoActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = uniqueid;
                Log.i("AAA",name);                //Creating parameters

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
    public void init() {
        imgProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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


}
