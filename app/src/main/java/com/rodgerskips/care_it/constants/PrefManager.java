package com.rodgerskips.care_it.constants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.rodgerskips.care_it.Models.User;
import com.rodgerskips.care_it.activities.LogInActivity;

public class PrefManager {
    private static final String SHARED_PREF_NAME = "prodevsblogpref";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_EMAIL = "user_email";
    //private static final String KEY_ID = "user_id";
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
        editor.putString(KEY_NAME, user.getEmail());
        editor.putString(KEY_EMAIL, user.getName());
        editor.putBoolean(KEY_IS_LOGGED_IN,true);
        editor.apply();
        }
public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        }

//this method will give the logged in user
public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
       // sharedPreferences.getInt(KEY_ID, -1),
                userJson.getString("email"), sharedPreferences.getString(KEY_NAME, null),
        sharedPreferences.getString(KEY_EMAIL, null)
        );
        }
public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LogInActivity.class));
        }
        }