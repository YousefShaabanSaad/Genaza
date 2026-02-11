package com.yousef.genaza.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.yousef.genaza.listener.Constants;

public class MySharedPreferences implements Constants {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public MySharedPreferences(Context context) {
        // Initialize SharedPreferences and SharedPreferences.Editor for managing app preferences
        sharedPreferences = context.getSharedPreferences(APP, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putString(String key, String value) {
        // Store a int value in SharedPreferences
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        // Retrieve a Boolean value from SharedPreferences
        return sharedPreferences.getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        // Store a int value in SharedPreferences
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        // Retrieve a Boolean value from SharedPreferences
        return sharedPreferences.getBoolean(key, defValue);
    }
}
