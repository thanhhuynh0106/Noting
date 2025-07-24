package com.example.app.remote;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "my_app_prefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";
    private static final String KEY_EXPIRATION_TIME = "expiration_time";

    private static SharedPrefManager mInstance;
    private final SharedPreferences sharedPreferences;

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context.getApplicationContext());
        }
        return mInstance;
    }

    public void saveUser(String token, String userName, String userRole) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USERNAME, userName);
        editor.putString(KEY_ROLE, userRole);

        long expirationTime = System.currentTimeMillis() + 3600000;
        editor.putLong(KEY_EXPIRATION_TIME, expirationTime);

        editor.apply();
    }

    public boolean isLoggedIn() {
        String token = getToken();
        if (token == null) {
            return false;
        }

        long expirationTime = sharedPreferences.getLong(KEY_EXPIRATION_TIME, 0);
        if (System.currentTimeMillis() > expirationTime) {
            clear();
            return false;
        }

        return true;
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public String getUserRole() {
        return sharedPreferences.getString(KEY_ROLE, null);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}

