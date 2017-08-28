package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobFind.pref.SessionManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends Activity {

    private EditText edtk, edmk;
    private String tk = "", mk = "", unid = "", email = "", url = "";
    private String loggedUser = "", key = "";
    private int mtype = 2, status, st = 0;
    private TextView txtDangKy;
    private Button log;
    ProgressDialog progress;
    private TextView tv,tv_qjhire;
    // Session Manager Class
    SessionManager session;
    LoginButton loginButton;
    CallbackManager callbackManager;
    String firstName,lastName,gender,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        tk = user.get(SessionManager.KEY_EMAIL);
        if(tk==null)
        {
            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();
            setContentView(R.layout.activity_login);
            AppEventsLogger.activateApp(this);
            Controles();
            Events();

        }   else{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

//        if (session.isLoggedIn()) {
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//
//        }
    }
    private void Events() {
        tv_qjhire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(com.quickjob.quickjobFinal.quickjobFind.activity.LoginActivity.this, com.quickjob.quickjobFinal.quickjobHire.activity.LoginActivity.class);
                startActivity(i);
                finish();

            }
        });
        txtDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkErrorInput();
            }
        });
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

            }
        };

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                try {
                                    Log.i("Response", response.toString());

                                      email = response.getJSONObject().getString("email");
                                   /* session.setProfilepic(response.getJSONObject().getString("picture"));*/
                                   firstName = response.getJSONObject().getString("first_name");
                                     lastName = response.getJSONObject().getString("last_name");
                                    gender = response.getJSONObject().getString("gender");
                                    name = response.getJSONObject().getString("name");
                                    tk = response.getJSONObject().getString("id");
                                    url = "https://graph.facebook.com/" + tk + "/picture?type=large";
                                    st = 1;
                                    session.LoginFB();
                                    CustomerAsyncTask asyncRequestObject = new CustomerAsyncTask();
                                    asyncRequestObject.execute(AppConfig.URL_LOGINFACE, tk, name, url);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                System.out.println(AccessToken.getCurrentAccessToken().getToken());
                Bundle parameters = new Bundle();
                System.out.println(firstName + " " + lastName+" ");
                parameters.putString("fields", "id,email,first_name,last_name,gender, birthday, name,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
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
            // form field with an error..
            focusView.requestFocus();
        }
        if (!cancel) {
            session.LoginQJ();
            CustomerAsyncTask asyncRequestObject = new CustomerAsyncTask();
            asyncRequestObject.execute(AppConfig.URL_LOGIN, tk, mk);

        }

    }

    private void Controles() {
        edtk = (EditText) findViewById(R.id.edtk);
        edmk = (EditText) findViewById(R.id.edmk);
        log = (Button) findViewById(R.id.btlog);
        txtDangKy = (TextView) findViewById(R.id.link_login);
        tv = (TextView) findViewById(R.id.link_fw);
        tv_qjhire= (TextView) findViewById(R.id.txt_qjHire);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));
    }

    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
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
            if (st == 0) {
                body = new FormBody.Builder()
                        .add("email", params[1])
                        .add("password", params[2])
                        .build();
            } else {
                body = new FormBody.Builder()
                        .add("id", params[1])
                        .add("name", params[2])
                        .add("url", params[3])
                        .build();

            }

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(params[0])
                    .cacheControl(cacheControl)
                    .post(body)
                    .build();
            Response forceCacheResponse = null;
            String responsestring = "";
            try {
                forceCacheResponse = client.newCall(request).execute();

                responsestring = forceCacheResponse.body().string();

                if (forceCacheResponse.isSuccessful()) {


                } else {


                }


            } catch (Exception e) {

                e.printStackTrace();


            }

            return responsestring;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

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
                Log.e("BBBBBB",key + " " + tk + " " +url + " " +mk + " " +unid);
                session.createLoginSession(key, tk, "","","","","",url,"", unid);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();


            }
        }
    }
 /*  private class AsyncDataClass extends AsyncTask<String, Void, String> {

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
                if(st==0) {
                    nameValuePairs.add(new BasicNameValuePair("email", params[1]));
                    nameValuePairs.add(new BasicNameValuePair("password", params[2]));
                }else{
                    nameValuePairs.add(new BasicNameValuePair("id", params[1]));
                    nameValuePairs.add(new BasicNameValuePair("name", params[2]));
                    nameValuePairs.add(new BasicNameValuePair("url", params[3]));
                }
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
    }*/

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
            } else {
            }
            unid = resultObject.getString("uid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
