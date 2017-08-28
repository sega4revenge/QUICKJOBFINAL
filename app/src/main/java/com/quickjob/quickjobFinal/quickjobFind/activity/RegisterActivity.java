package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {

    private String type;
    private String pass1, email1, repass, ten,uid;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Intent i = getIntent();
        int key = i.getIntExtra("KEY", 10);
        type = key + "";
        Button bt = (Button)findViewById(R.id.bt);
        TextView movelogin = (TextView) findViewById(R.id.movelogin);
        movelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edname = (EditText) findViewById(R.id.name);
                EditText edmail = (EditText) findViewById(R.id.edmail);
                EditText edpass = (EditText) findViewById(R.id.edpass);
                EditText edrepass = (EditText) findViewById(R.id.edrepass);


                edname.setError(null);
                edmail.setError(null);
                edpass.setError(null);
                edrepass.setError(null);

                ten = edname.getText().toString();
                email1 = edmail.getText().toString();
                pass1 = edpass.getText().toString();
                repass = edrepass.getText().toString();


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
                if (TextUtils.isEmpty(repass)) {
                    edrepass.setError(getString(R.string.error_field_required));
                    focusView = edrepass;
                    cancel = true;
                } else if (!isPasswordValid(repass)) {
                    edrepass.setError(getString(R.string.error_invalid_password));
                    focusView = edrepass;
                    cancel = true;
                }

                //Kiểm tra tên công ty
                if (TextUtils.isEmpty(ten)) {
                    edname.setError(getString(R.string.error_field_required));
                    focusView = edname;
                    cancel = true;
                } else if (!isNameValid(ten)) {
                    edname.setError(getString(R.string.error_invalid_tenCY));
                    focusView = edname;
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
                if (!pass1.equals(repass)) {
                    edrepass.setError(getString(R.string.st_loiMK));
                    focusView = edmail;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                }
                if (cancel == false) {
                    AsyncDataClass asyncRequestObject = new AsyncDataClass();
                    asyncRequestObject.execute(AppConfig.URL_DANGKY_NTV,ten, email1, pass1, type);
                }
            }
        });

    }



    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }



    private boolean isNameValid(String ten) {
        return ten.length() > 2;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 9;
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
                nameValuePairs.add(new BasicNameValuePair("name", params[1]));
                nameValuePairs.add(new BasicNameValuePair("email", params[2]));
                nameValuePairs.add(new BasicNameValuePair("pass", params[3]));
                nameValuePairs.add(new BasicNameValuePair("type", params[4]));
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
                Toast.makeText(RegisterActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
                return;
            }
            int jsonResult = returnParsedJsonObject(result);
            if (jsonResult == 0) {
                Toast.makeText(RegisterActivity.this, R.string.st_accountUsed, Toast.LENGTH_SHORT).show();
                return;
            }
            if (jsonResult == 1) {
                session.createLoginSession(ten, email1, "", "", "", "", "", "","", uid);
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.putExtra("USERNAME", email1);
                intent.putExtra("name", ten);
                intent.putExtra("uid", uid);
                intent.putExtra("mtype", type);
                intent.putExtra("logo", "");
                startActivity(intent);
                finish();

//                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                intent.putExtra("USERNAME", email1);
//                intent.putExtra("name", ten);
//                intent.putExtra("logo", "");
//                intent.putExtra("uid", uid);
//                intent.putExtra("mtype", 1);
//                startActivity(intent);
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                Toast.makeText(getApplicationContext(), R.string.registry_success, Toast.LENGTH_SHORT).show();
//                finish();

            }
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


    private int returnParsedJsonObject(String result) {

        JSONObject resultObject = null;
        int returnedResult = 0;
        try {
            resultObject = new JSONObject(result);
            returnedResult = resultObject.getInt("success");
            uid =resultObject.getString("uid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }
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
