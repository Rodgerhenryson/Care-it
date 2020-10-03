package com.rodgerskips.care_it.constants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentActivity;

import com.rodgerskips.care_it.Models.User;
import com.rodgerskips.care_it.activities.LogInActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

public class PrefManager {
    private static final String SHARED_PREF_NAME = "prodevsblogpref";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_AccType = "user_type";
    private static final String KEY_lat = "latitude";
    private static final String KEY_long = "longitude";
    private static final String KEY_PHONE = "user_PHONE";
    private static final String KEY_PASSWORD = "user_passsword";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static PrefManager mInstance;
    private static Context mCtx;

    private PrefManager (Context context) {
        mCtx = context;
    }

    public static synchronized PrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PrefManager(context);
        }
        return mInstance;
    }



    public void setUserLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE,user.getPhone());
        editor.putBoolean(KEY_IS_LOGGED_IN,true);
        editor.apply();
    }
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void  setAccountType(String type){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AccType,type);
        editor.apply();
    }

    public void setLatLong(double lat,double lon) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_lat, String.valueOf(lat));
        editor.putString(KEY_long, String.valueOf(lon));
        editor.apply();
    }

    public String getAccoutntType(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_AccType,null);
    }

    public String getLat(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_lat,null);
    }

    public String getLong(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_long,null);
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
//                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_PHONE, null)
        );
    }
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LogInActivity.class));
    }

    public static void Toast(Context context, String messsage){
        FancyToast.makeText(context,messsage,FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();

    }

    public static void ErrorToast(Context context, String messsage) {
        FancyToast.makeText(context, messsage, FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

    }
}