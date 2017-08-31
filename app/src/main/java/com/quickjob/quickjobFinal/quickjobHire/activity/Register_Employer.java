package com.quickjob.quickjobFinal.quickjobHire.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Scene;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobHire.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Register_Employer extends AppCompatActivity {

    private String type, pass1, repass1, email1, namecompany1, phone1, uid;
    private FrameLayout mFrtContent;
    private Scene mSceneSignUp;
    private Scene mSceneLogging;
    private Scene mSceneMain;
    private int mTvSighUpWidth, mTvSighUpHeight;
    private int mDuration;
    View viewobject;
    TextView txttk;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__employer_hire);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ScrollView scroll = (ScrollView)findViewById(R.id.scrollEm);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Intent i = getIntent();
        int key = i.getIntExtra("KEY", 10);
        type = key + "";
        Button bt = (Button) findViewById(R.id.bt);
        txttk = (TextView) findViewById(R.id.txttk);
        txttk.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        finish();
    }
});
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edname = (EditText) findViewById(R.id.namecompany);
                EditText edmail = (EditText) findViewById(R.id.edmail);
                EditText edpass = (EditText) findViewById(R.id.edpass);
                EditText edrepass = (EditText) findViewById(R.id.edrepass);
                EditText sdt = (EditText) findViewById(R.id.phone);
//                txttk = (TextView) findViewById(R.id.txttk);
                edname.setError(null);
                edmail.setError(null);
                edpass.setError(null);
                sdt.setError(null);
                edrepass.setError(null);
//                txttk.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.e("AAAAA","BBBB");
//                        Intent movelogin = new Intent(Register_Employer.this,LoginActivity.class);
//                        startActivity(movelogin);
//                        finish();
//                    }
//                });
                email1 = edmail.getText().toString();
                pass1 = edpass.getText().toString();
                repass1 = edrepass.getText().toString();
                namecompany1 = edname.getText().toString();
                phone1 = sdt.getText().toString();

                boolean cancel = false;
                View focusView = null;

                // Check for a valid password, if the user entered one.
                if (TextUtils.isEmpty(pass1)) {
                    edpass.setError(getString(R.string.error_field_required));
                    focusView = edpass;
                    cancel = true;
                } else if (!isPasswordValid(pass1)) {
                    edpass.setError(getString(R.string.error_invalid_password));
                    focusView = edpass;
                    cancel = true;
                }
                // Kiểm tra mật khẩu nhập lại
                if (TextUtils.isEmpty(repass1)) {
                    edrepass.setError(getString(R.string.error_field_required));
                    focusView = edrepass;
                    cancel = true;
                }
//                else if (!isPasswordValid(repass1)) {
//                    edrepass.setError(getString(R.string.error_invalid_password));
//                    focusView = edrepass;
//                    cancel = true;
//                }

                //Kiểm tra tên công ty
                if (TextUtils.isEmpty(namecompany1)) {
                    edname.setError(getString(R.string.error_field_required));
                    focusView = edname;
                    cancel = true;
                } else if (!isCompanyValid(namecompany1)) {
                    edname.setError(getString(R.string.error_invalid_tenCY));
                    focusView = edname;
                    cancel = true;
                }

                //Kiểm tra số điện thoại
                if (TextUtils.isEmpty(phone1)) {
                    sdt.setError(getString(R.string.error_field_required));
                    focusView = sdt;
                    cancel = true;
                } else if (!isValidPhoneNumber(phone1)) {
                    sdt.setError(getString(R.string.error_invalid_phone));
                    focusView = sdt;
                    cancel = true;
                }

                // Kiểm tra email
                if (TextUtils.isEmpty(email1)) {
                    edmail.setError(getString(R.string.error_field_required));
                    focusView = edmail;
                    cancel = true;
                } else if (!isValidEmail(email1)) {
                    edmail.setError(getString(R.string.error_invalid_email));
                    focusView = edmail;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                }
                if (pass1.equals(repass1) && cancel == false) {
                    //   Toast.makeText(Register_Employer.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
//                    AsyncDataClass asyncRequestObject = new AsyncDataClass();
//                    asyncRequestObject.execute(AppConfig.URL_DANGKY_NTD, email1, pass1, type, namecompany1, phone1);
                    getData(email1, pass1, type, namecompany1, phone1);
                } else if (pass1.equals(repass1) == false) {
//                    Toast.makeText(Register_Employer.this, R.string.st_loiMK, Toast.LENGTH_SHORT).show();
                    edrepass.setError(getString(R.string.st_loiMK));
                    focusView = edrepass;
                    cancel = true;
                }
            }
        });

    }


//    public void clickmove() {
//        Log.e("AAAA","BBBBBB");
//    Intent movelogin = new Intent(Register_Employer.this,LoginActivity.class);
//        startActivity(movelogin);
//        finish();
//    }


    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    private boolean isNameValid(String ten) {
        return ten.length() > 2;
    }

    private boolean isCompanyValid(String namecompany) {
        return namecompany.length() > 1;
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    private void parseJsonFeed(String result) {
        Log.e("Result",result);
        if (result.equals("") || result == null) {
            Toast.makeText(Register_Employer.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
            return;
        }
        int jsonResult = returnParsedJsonObject(result);
        if (jsonResult == 0) {
            Toast.makeText(Register_Employer.this, R.string.st_account_tontai, Toast.LENGTH_SHORT).show();
            return;
        }
        if (jsonResult == 1) {
            session.createLoginSession(namecompany1, email1, "", pass1, uid);
            Intent intent = new Intent(Register_Employer.this, MainActivity.class);
            intent.putExtra("USERNAME", email1);
            intent.putExtra("name", namecompany1);
            intent.putExtra("uid", uid);
            intent.putExtra("mtype", type);
            intent.putExtra("logo", "");
            startActivity(intent);
            finish();
        }
    }

    private void getData(final String email1, final String pass1, final String type, final String namecompany1, final String phone1) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/
        // Tag used to cancel the request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DANGKY_NTD, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                parseJsonFeed(result);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("email", email1);
                params.put("pass", pass1);
                params.put("type", type);
                params.put("namecompany", namecompany1);
                params.put("phone", phone1);

                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(this).requestQueue.add(strReq);
    }
    private int returnParsedJsonObject(String result) {

        JSONObject resultObject = null;
        int returnedResult = 0;
        Log.d("BBBBBB",result);
        try {
            resultObject = new JSONObject(result);
            returnedResult = resultObject.getInt("success");
            uid = resultObject.getString("uid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }
    @Override
    public void onBackPressed() {
//        Intent a= new Intent(getApplicationContext(), SettingActivity.class);
//        a.putExtra("logo",logo);
//        startActivity(a);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
