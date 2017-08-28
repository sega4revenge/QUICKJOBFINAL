package com.quickjob.quickjobFinal.quickjobFind.pref;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.quickjob.quickjobFinal.quickjobFind.activity.LoginActivity;

import java.util.HashMap;



public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "JobFindPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String IS_NOTIFICATION = "IsNotification";
    public static final String IS_LOGINTYPE = "IsLoginType";
    public static final String KEY_NAME = "name";
    public static final String LANGUAGE = "";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASS = "pass";
    public static final String KEY_LOGO = "logo";
    public static final String KEY_ID = "id";

    public static final String KEY_AGE = "age";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_LOCATION = "location";

    public static final String KEY_BIRTHDAY = "birthdate";
    public static final String KEY_SEX = "sex";
    public static final String KEY_ADDRESS = "andress";
    public static final String KEY_HOMELESS = "homeless";
    public static final String KEY_SLOGAN = "slogan";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public String getProfilePic(){
       return pref.getString(KEY_LOGO, null);
    }
    public void  setLanguage(String languaage){
        editor.remove(LANGUAGE).commit();
        editor.putString(LANGUAGE,languaage);
        editor.commit();
    }
    public String getLanguage(){
        return pref.getString(LANGUAGE,"");
    }
    public void setName(String name){
        editor.putString(KEY_NAME, name);
        editor.commit();
    }
    public void setEmail(String email){

        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }
    public void setAge(String age){

        editor.putString(KEY_AGE, age);
        editor.commit();
    }
    public void setPhone(String phone){

        editor.putString(KEY_PHONE, phone);
        editor.commit();
    }
    public void setLocation(String location){

        editor.putString(KEY_LOCATION, location);
        editor.commit();
    }
    public void setProfilepic(String profilepic){

        editor.putString(KEY_LOGO, profilepic);
        editor.commit();
    }
    public void createInfoUser(String age, String phone, String location){

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_AGE, age);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_LOCATION, location);

        editor.commit();
    }
    /**
     * Create login session
     * */
    public void createLoginSession(String name, String email,String birthdate, String sex,String phone,String andress,String homeless,String logo,String slogan,String id){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putBoolean(IS_NOTIFICATION, true);
        // Storing name in pref
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL,  email);
        editor.putString(KEY_BIRTHDAY, birthdate);
        editor.putString(KEY_SEX, sex);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_ADDRESS, andress);
        editor.putString(KEY_HOMELESS, homeless);
        editor.putString(KEY_LOGO, logo);
        editor.putString(KEY_SLOGAN, slogan);
        editor.putString(KEY_ID, id);


        // commit changes
        editor.commit();
    }
    public String getName(){
        return pref.getString(KEY_NAME, "");
    }
    public String getEmail(){
        return pref.getString(KEY_EMAIL, "");
    }
    public String getBirthday(){
        return pref.getString(KEY_BIRTHDAY, "");
    }
    public String getSex(){
        return pref.getString(KEY_SEX, "");
    }
    public String getPhone(){
        return pref.getString(KEY_PHONE, "");
    }
        public String getAddress(){
            return pref.getString(KEY_ADDRESS, "");
        }
    public String getHomeless(){
        return pref.getString(KEY_HOMELESS, "");
    }
    public String getLogo(){
        return pref.getString(KEY_LOGO, "");
    }
    public String getSlogan(){
        return pref.getString(KEY_SLOGAN, "");
    }
    public String getId(){
        return pref.getString(KEY_ID, "");
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public boolean checkLogin(){
        // Check login status
        boolean p =false;
        if(!this.isLoggedIn()){
            p=true;
//            // user is not logged in redirect him to Login Activity
//            Intent i = new Intent(_context, HomeActivity.class);
//            // Closing all the Activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            // Staring Login Activity
//            _context.startActivity(i);
        }
        return p;

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();


        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_PASS, pref.getString(KEY_PASS, null));
        user.put(KEY_LOGO, pref.getString(KEY_LOGO, null));
        user.put(KEY_ID, pref.getString(KEY_ID, null));


        // return user
        return user;
    }
    public void NotiOn(){
        // Storing login value as TRUE
        editor.putBoolean(IS_NOTIFICATION, true);
        // commit changes
        editor.commit();
    }
    public void NotiOff(){
        // Storing login value as TRUE
        editor.putBoolean(IS_NOTIFICATION, false);
        // commit changes
        editor.commit();
    }
    public void LoginQJ(){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGINTYPE, false);
        // commit changes
        editor.commit();
    }
    public void LoginFB(){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGINTYPE, true);
        // commit changes
        editor.commit();
    }
    public boolean getLoginType(){
        return pref.getBoolean(IS_LOGINTYPE,false);
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);  // Closing all the Activities

    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    public boolean isNotification(){
        return pref.getBoolean(IS_NOTIFICATION, false);
    }
    public boolean isLoginType(){
        return pref.getBoolean(IS_LOGINTYPE, false);
    }

}