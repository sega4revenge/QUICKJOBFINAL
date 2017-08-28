package com.quickjob.quickjobFinal.quickjobHire.activity;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.quickjob.quickjobFinal.quickjobHire.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobHire.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobHire.config.AppController;

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

/**
 * Created by SONTHO on 15/08/2016.
 */
public class ForgotPasswordSecondActivity extends AppCompatActivity {

    private EditText edmk,edremk;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_second);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        final String email = i.getStringExtra("email");
        Button bt = (Button) findViewById(R.id.btchange);
        edmk = (EditText) findViewById(R.id.edpass);
        edremk = (EditText) findViewById(R.id.edrepass);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mk = edmk.getText().toString();
                String remk=edremk.getText().toString();
                if(mk == null || mk.equals("") || remk == null || remk.equals(""))
                {
                    edmk.setError(getString(R.string.st_err_taocv));
//                    Toast.makeText(ForgotPasswordSecondActivity.this,R.string.st_err_taocv, Toast.LENGTH_SHORT).show();
                }
                else if(!isPasswordValid(mk)){
                    edmk.setError(getString(R.string.error_invalid_password));
//                    Toast.makeText(ForgotPasswordSecondActivity.this,R.string.error_invalid_password, Toast.LENGTH_SHORT).show();
                }
                else if(mk.equals(remk)){
//                    AsyncDataClass asyncRequestObject = new AsyncDataClass();
//                    asyncRequestObject.execute(AppConfig.URL_CHANGEPASSWORD, email,mk);
                    getData(email,mk);
                }else{
                    edremk.setError(getString(R.string.st_loiMK));
//                    Toast.makeText(ForgotPasswordSecondActivity.this,R.string.st_loiMK, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void parseJsonFeed(String result) {
        System.out.println("Resulted Value: " + result);
        if(result.equals("") || result == null){
            Toast.makeText(ForgotPasswordSecondActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
            return;
        }
        int jsonResult = returnParsedJsonObject(result);
        if(jsonResult==1)
        {
            Intent i = new Intent(ForgotPasswordSecondActivity.this,LoginActivity.class);
            finish();
            startActivity(i);
        }else{
        }
        Toast.makeText(ForgotPasswordSecondActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void getData(final String email, final String mk) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/
        // Tag used to cancel the request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGEPASSWORD, new com.android.volley.Response.Listener<String>() {
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

                params.put("email", email);
                params.put("mk", mk);
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
                nameValuePairs.add(new BasicNameValuePair("mk", params[2]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
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
            if(result.equals("") || result == null){
                Toast.makeText(ForgotPasswordSecondActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
                return;
            }
            int jsonResult = returnParsedJsonObject(result);
            if(jsonResult==1)
            {
                Intent i = new Intent(ForgotPasswordSecondActivity.this,LoginActivity.class);
                finish();
                startActivity(i);
            }else{
            }
            Toast.makeText(ForgotPasswordSecondActivity.this, msg, Toast.LENGTH_SHORT).show();

        }
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }
    private int returnParsedJsonObject(String result) {

        JSONObject resultObject = null;
        int returnedResult = 0;
        try {
            resultObject = new JSONObject(result);
            returnedResult = resultObject.getInt("success");
            msg = resultObject.getString("error_msg");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
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
