package com.quickjob.quickjobFinal.quickjobHire.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobHire.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends Activity {

    private EditText edtk, edmk;
    private String tk = "", mk = "", unid = "", email = "", url="";
    private String loggedUser = "", name = "", key = "";
    private int mtype = 2,  status;
    private TextView txtDangKy,tv_qjFind;
    private Button log;
    ProgressDialog progress;
    private TextView tv;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__hire);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        tk = user.get(SessionManager.KEY_EMAIL);
        if(tk==null)
        {

        }   else{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        Controles();
        Events();
    }



    private void Events() {

        Intent intent = getIntent();
        loggedUser = intent.getStringExtra("USERNAME");
        name = intent.getStringExtra("name");
        String pass = intent.getStringExtra("pass");
        String message = intent.getStringExtra("MESSAGE");
        edtk.setText(loggedUser);
        edmk.setText(pass);

        tv_qjFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(com.quickjob.quickjobFinal.quickjobHire.activity.LoginActivity.this, com.quickjob.quickjobFinal.quickjobFind.activity.LoginActivity.class);
                startActivity(i);
                finish();

            }
        });
        txtDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent i = new Intent(LoginActivity.this,Register_Employer.class);
              startActivity(i);
            }
        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkErrorInput();
            }
        });
    }

    private void checkErrorInput() {

        edtk.setError(null);
        edmk.setError(null);
        tk = edtk.getText().toString();
        mk = edmk.getText().toString();
        // Kiểm tra email
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(tk)) {
            edtk.setError(getString(R.string.error_field_required));
            focusView = edtk;
            cancel = true;
        } else if (!isValidEmail(tk)) {
            edtk.setError(getString(R.string.error_invalid_email));
            focusView = edtk;
            cancel = true;
        }
        if (TextUtils.isEmpty(mk)) {
            edmk.setError(getString(R.string.error_field_required));
            focusView = edtk;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        if(cancel == false){
//            AsyncDataClass asyncRequestObject = new AsyncDataClass();
//            asyncRequestObject.execute(AppConfig.URL_LOGIN, tk, mk);
            getData(tk,mk);

        }
//        if (tk.equals("") || mk.equals("")) {
//            Toast.makeText(LoginActivity.this, R.string.st_hayNhapUSERPASS, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (tk.length() <= 1 || mk.length() <= 1) {
//            Toast.makeText(LoginActivity.this, R.string.st_erroDodaiUSERPASS, Toast.LENGTH_SHORT).show();
//            return;
//        }
//      request authentication with remote server4
    }

    private void Controles() {
        edtk = (EditText) findViewById(R.id.edtk);
        edmk = (EditText) findViewById(R.id.edmk);
        log = (Button) findViewById(R.id.btlog);
        txtDangKy = (TextView) findViewById(R.id.link_login);
        tv = (TextView) findViewById(R.id.link_fw);
        tv_qjFind = (TextView) findViewById(R.id.txt_qjFind);

    }

    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }
    private void parseJsonFeed(String result) {
        if (result.equals("") || result == null) {
            Toast.makeText(LoginActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
            return;
        }
        int jsonResult = returnParsedJsonObject(result);
        if (jsonResult == 0) {
            Toast.makeText(LoginActivity.this, R.string.st_saiTKMK, Toast.LENGTH_SHORT).show();
            return;
        }
        if (jsonResult == 1) {
           /*   SharedPreferences share = getSharedPreferences("MyShare", MODE_PRIVATE);
                SharedPreferences.Editor editor = share.edit();
                //Đẩy 2 chuỗi lấy từ 2 editext ở file SharedPreferences
                editor.putString("USER_NAME", edtk.getText().toString());
                editor.putString("PASS_WORLD", edmk.getText().toString());
                editor.putString("LOGO",url);
                editor.putString("mtype", String.valueOf(mtype));
                editor.putString("uid", unid); */
            session.createLoginSession(key, tk, url, mk, unid);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("USERNAME", tk);
            intent.putExtra("name", key);
            intent.putExtra("uid", unid);
            intent.putExtra("mtype", mtype);
            intent.putExtra("logo", url);
            finish();
            startActivity(intent);

        }
    }
    private void getData(final String email, final String pass) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/
        // Tag used to cancel the request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                parseJsonFeed(result);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("password", pass);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(this).requestQueue.add(strReq);
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
                nameValuePairs.add(new BasicNameValuePair("email", params[1]));
                nameValuePairs.add(new BasicNameValuePair("password", params[2]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

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
                Toast.makeText(LoginActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
                return;
            }
            int jsonResult = returnParsedJsonObject(result);
            if (jsonResult == 0) {
                Toast.makeText(LoginActivity.this, R.string.st_saiTKMK, Toast.LENGTH_SHORT).show();
                return;
            }
            if (jsonResult == 1) {
           /*   SharedPreferences share = getSharedPreferences("MyShare", MODE_PRIVATE);
                SharedPreferences.Editor editor = share.edit();
                //Đẩy 2 chuỗi lấy từ 2 editext ở file SharedPreferences
                editor.putString("USER_NAME", edtk.getText().toString());
                editor.putString("PASS_WORLD", edmk.getText().toString());
                editor.putString("LOGO",url);
                editor.putString("mtype", String.valueOf(mtype));
                editor.putString("uid", unid); */
                    session.createLoginSession(key, tk, url, mk, unid);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USERNAME", tk);
                    intent.putExtra("name", key);
                    intent.putExtra("uid", unid);
                    intent.putExtra("mtype", mtype);
                    intent.putExtra("logo", url);
                    finish();
                    startActivity(intent);

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

    private int returnParsedJsonObject(String result) {

        JSONObject resultObject = null;
        int returnedResult = 0;

        try {
            resultObject = new JSONObject(result);
            returnedResult = resultObject.getInt("success");
            key = resultObject.getString("company");
            if (key.equals("")) {
                key = resultObject.getString("objectname");
            }
            mtype = resultObject.getInt("mtype");
            status = resultObject.getInt("status");
            if (status == 1) {
             url = resultObject.getString("logo");
            } else {}
            unid = resultObject.getString("uid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }


}
