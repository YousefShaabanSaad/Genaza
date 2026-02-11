package com.yousef.genaza.database;

import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import com.yousef.genaza.listener.ItemListener;
import com.yousef.genaza.listener.ItemsListener;
import com.yousef.genaza.models.Dead;

public class Repository {
    private final Helper helper;
    private final MySharedPreferences sharedPreferences;
    private final MyFirebase firebase;

    public Repository(Context context){
        helper = new Helper(context);
        sharedPreferences = new MySharedPreferences(context);
        firebase = new MyFirebase();
    }

    // TODO Helper
    public void setIntent(Class<?> clc){
        helper.setIntent(clc);
    }

    public String generateRandomID() {
        return helper.generateRandomID();
    }

    public String bitmapToBase64(Bitmap bitmap){
        return helper.bitmapToBase64(bitmap);
    }

    public Dialog createProgress(){
        return helper.createProgress();
    }

    public String getHijriDate() {
        return helper.getHijriDate();
    }

    public void createNotificationChannel() {
        helper.createNotificationChannel();
    }

    public Notification startNotification(){
        return helper.startNotification();
    }
    public void sendNotification(Dead item) {
        helper.sendNotification(item);
    }
    // TODO MySharedPreferences
    public void putString(String key, String value) {
        sharedPreferences.putString(key, value);
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        sharedPreferences.putBoolean(key, value);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    // TODO MyFirebase
    public void addOrEditDead(Dead dead, ItemListener<Dead> listener){
       firebase.addOrEditDead(dead, listener);
    }

    public void getDead(ItemsListener<Dead> listener){
        firebase.getDead(listener);
    }
    public void getDeadByID(String id, ItemListener<Dead> listener){
        firebase.getDeadByID(id, listener);
    }
}
