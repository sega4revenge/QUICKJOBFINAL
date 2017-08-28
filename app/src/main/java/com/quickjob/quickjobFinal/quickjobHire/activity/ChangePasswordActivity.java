package com.quickjob.quickjobFinal.quickjobHire.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.config.AppController;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by SONTHO on 15/08/2016.
 */
public class ChangePasswordActivity extends AppCompatActivity {

    private String uid;
    private EditText old, newpass, renewpass;
    private Button btlog, cancel;
    SessionManager session;
    String logo = "";
    SharedPreferences.Editor edit;
    View focusView = null;
    boolean cancel1 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass_hire);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher);

        Intent s = getIntent();
        logo = s.getStringExtra("logo");
        init();
        session = new SessionManager(ChangePasswordActivity.this);
        SharedPreferences pref = getSharedPreferences("JobFindPref", MODE_PRIVATE);
        edit = pref.edit();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        uid = user.get(SessionManager.KEY_ID);
        events();
    }

    private void events() {
        btlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String op = old.getText().toString();
                String np = newpass.getText().toString();
                String rnp = renewpass.getText().toString();



                if (op == null || op.equals("") || np == null || np.equals("") || rnp == null || rnp.equals("")) {
//                    Toast.makeText(ChangePasswordActivity.this, R.string.st_err_taocv, Toast.LENGTH_SHORT).show();
                    btlog.setError(getString(R.string.st_err_taocv));
                    focusView = btlog;
                    cancel1 = true;
                } else if (!isPasswordValid(np)) {
                    newpass.setError(getString(R.string.error_invalid_password));
                    focusView = newpass;
                    cancel1 = true;
                }else if (np.equals(rnp)) {
//                    AsyncDataClass asyncRequestObject = new AsyncDataClass();
//                    asyncRequestObject.execute(AppConfig.URL_CHANGEPASSWORD2,uid, op, np);
            getData(uid,op,np);
                } else {
//                    Toast.makeText(ChangePasswordActivity.this, R.string.st_changeError, Toast.LENGTH_SHORT).show();
                    renewpass.setError(getString(R.string.st_changeError));
                    focusView = renewpass;
                    cancel1 = true;
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }
    private void init() {
        old = (EditText) findViewById(R.id.oldpass);
        newpass = (EditText) findViewById(R.id.newpass);
        renewpass = (EditText) findViewById(R.id.renewpass);
        btlog = (Button) findViewById(R.id.btlog);
        cancel = (Button) findViewById(R.id.cancel);

    }

    private void parseJsonFeed(String result) {
        if (result.equals("1")) {

//            Toast.makeText(ChangePasswordActivity.this, R.string.st_changeSuccess, Toast.LENGTH_SHORT).show();
            new SweetAlertDialog(ChangePasswordActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(ChangePasswordActivity.this.getResources().getString(R.string.st_changeSuccess))
                    .setConfirmText(getResources().getString(R.string.st_xacNhan))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
//            finish();

        } else if (!result.equals("1")) {
            old.setError(getString(R.string.st_changeOldError));
            focusView = old;
            cancel1 = true;
//            Toast.makeText(ChangePasswordActivity.this, R.string.st_changeOldError, Toast.LENGTH_SHORT).show();

        }
        if (result.equals("") || result == null) {
            Toast.makeText(ChangePasswordActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void getData(final String id, final String old, final String neww) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/
        // Tag used to cancel the request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGEPASSWORD2, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                parseJsonFeed(result);
            }
        }, new com.android.volley.Response.ErrorListener() {
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

                params.put("id", id);
                params.put("old", old);
                params.put("new", neww);

                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(this).requestQueue.add(strReq);
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
