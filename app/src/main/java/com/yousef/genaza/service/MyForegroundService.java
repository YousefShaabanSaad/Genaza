package com.yousef.genaza.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.yousef.genaza.database.Repository;
import com.yousef.genaza.listener.Constants;
import com.yousef.genaza.listener.ItemsListener;
import com.yousef.genaza.models.Dead;
import java.util.List;

public class MyForegroundService extends Service implements Constants {

    private Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = new Repository(this);
        repository.createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, repository.startNotification());
        repository.getDead(new ItemsListener<>() {
            @Override
            public void getItems(List<Dead> items) {
                Timestamp now = Timestamp.now();
                for (Dead item : items) {
                    if(item.getStatus() == 1 && item.getTimestamp() != null && item.getTimestamp().compareTo(now) >= 0){
                        if(!repository.getBoolean(item.getId(), false)){
                            repository.putBoolean(item.getId(), true);
                            repository.sendNotification(item);
                        }
                    }
                }
            }

            @Override
            public void clickItem(Dead item) {

            }

            @Override
            public void failGetItems(String error) {

            }
        });

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                checkNotification();
                handler.postDelayed(this, 60000);
            }
        };

        handler.post(runnable);

        return START_STICKY;
    }

    private void checkNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        StatusBarNotification[] notifications = manager.getActiveNotifications();
        boolean found = false;
        for (StatusBarNotification n : notifications) {
            if (n.getId() == 1) {
                found = true;
                break;
            }
        }
        if (!found) {
            startForeground(1, repository.startNotification());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
