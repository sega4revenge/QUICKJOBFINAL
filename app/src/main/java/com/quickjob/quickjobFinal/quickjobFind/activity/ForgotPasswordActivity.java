package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.config.AppConfig;

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
import java.util.List;
import java.util.Random;


public class ForgotPasswordActivity extends AppCompatActivity {

    private Button btsend;
    private EditText edmail;
    private View focusView;
    private String msg,email;
    private int rand, ss;
    private TextView login;
    private Boolean wantToCloseDialog;
    CountDownTimer countdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        btsend = (Button) findViewById(R.id.btsend);
        edmail = (EditText) findViewById(R.id.edmail);
        login = (TextView) findViewById(R.id.link_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        btsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edmail.setError(null);
                email =edmail.getText().toString();
                boolean cancel = false;
                focusView = null;
                if (TextUtils.isEmpty(email)) {
                    edmail.setError(getString(R.string.error_field_required));
                    focusView = edmail;
                    cancel = true;
                } else if (!isValidEmail(email)) {
                    edmail.setError(getString(R.string.error_invalid_email));
                    focusView = edmail;
                    cancel = true;
                }else{
                    AsyncDataClass asyncRequestObject = new AsyncDataClass();
                    asyncRequestObject.execute(AppConfig.URL_FORGOTPASSWORD, email);
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
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
                Toast.makeText(ForgotPasswordActivity.this, R.string.st_errServer, Toast.LENGTH_SHORT).show();
                return;
            }
            int jsonResult = returnParsedJsonObject(result);
            if(jsonResult==1)
            {
                rand = rand(1000,99999);
                SendMailActivity sm = new SendMailActivity(ForgotPasswordActivity.this, email,"QUICKJOB", "Mã xác nhận : " + rand + "\nVerification code : "+rand);
                sm.execute();
                final AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view1 = inflater.inflate(R.layout.custom_dialog_createskill, null);
                builder.setTitle(R.string.st_forgotPass);
                builder.setMessage(R.string.st_nhapMaXN);
                builder.setView(view1);
                builder.setCancelable(false);
                final Button resendialog = (Button) view1.findViewById(R.id.resend);
                Button ok = (Button) view1.findViewById(R.id.okdialog);
                Button exit = (Button) view1.findViewById(R.id.exitdialog);
                final TextView count = (TextView) view1.findViewById(R.id.count);
                countdown = new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        count.setVisibility(View.VISIBLE);
                        resendialog.setVisibility(View.GONE);
                        count.setText(millisUntilFinished / 1000 + "");
                    }

                    @Override
                    public void onFinish() {
                        resendialog.setVisibility(View.VISIBLE);
                        count.setVisibility(View.GONE);
                    }
                }.start();
//                builder.setNeutralButton(R.string.st_sendagain, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                builder.setPositiveButton(R.string.st_thoat, null);
//                builder.setNegativeButton(R.string.st_chon, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
                final AlertDialog dialog = builder.create();
                dialog.show();
                resendialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rand = rand(1000, 99999);
                        SendMailActivity sm = new SendMailActivity(ForgotPasswordActivity.this, email, "QUICKJOB", "Mã xác nhận : " + rand+"\nVerification code : "+rand);
                        sm.execute();
                        countdown.start();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        rand = rand(1000, 99999);
//                    SendMailActivity sm = new SendMailActivity(ForgotPasswordActivity.this, email, "QUICKJOB QUÊN MẬT KHẨU", "MÃ XÁC NHẬN CỦA BẠN LÀ:" + rand);
//                    sm.execute();
                        wantToCloseDialog = false;
                        EditText e = (EditText) view1.findViewById(R.id.edkynang);
                        String k = e.getText().toString();
//                        try {
//                            ss = Integer.parseInt(k);
//                        } catch (Exception x) {
//                            e.setError(getString(R.string.st_maKHL));
////                            Toast.makeText(ForgotPasswordActivity.this, R.string.st_maKHL, Toast.LENGTH_SHORT).show();
//                        }
                        if (Integer.parseInt(k) == rand) {
                            wantToCloseDialog = true;
                            Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordSecondActivity.class);
                            intent.putExtra("email", email);
                            finish();
                            startActivity(intent);
                        } else {
                            e.setError(getString(R.string.st_maXN_khongdung));
//                            Toast.makeText(ForgotPasswordActivity.this, R.string.st_maXN_khongdung, Toast.LENGTH_SHORT).show();
                        }
                        if (wantToCloseDialog)
                            dialog.dismiss();
                    }
                });
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
//                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        rand = rand(1000, 99999);
//                        SendMailActivity sm = new SendMailActivity(ForgotPasswordActivity.this, email, "QUICKJOB QUÊN MẬT KHẨU", "MÃ XÁC NHẬN CỦA BẠN LÀ:" + rand);
//                        sm.execute();
//                    }
//                });
//                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        wantToCloseDialog = false;
//                        EditText e = (EditText) view1.findViewById(R.id.edkynang);
//                        String k = e.getText().toString();
//                        try {
//                            ss = Integer.parseInt(k);
//                        } catch (Exception x) {
//                            Toast.makeText(ForgotPasswordActivity.this, R.string.st_maKHL, Toast.LENGTH_SHORT).show();
//                        }
//                        if (ss == rand) {
//                            wantToCloseDialog=true;
//                            Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordSecondActivity.class);
//                            intent.putExtra("email", email);
//                            finish();
//                            startActivity(intent);
//                        } else {
//                            Toast.makeText(ForgotPasswordActivity.this, R.string.st_maXN_khongdung, Toast.LENGTH_SHORT).show();
//                        }
//                        if (wantToCloseDialog)
//                            dialog.dismiss();
//                    }
//                });
            }else{
            }
            Toast.makeText(ForgotPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();

        }

    }
    public  int rand(int min, int max) {
        try {
            Random rn = new Random();
            int range = max - min + 1;
            int randomNum = min + rn.nextInt(range);
            return randomNum;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
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
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
