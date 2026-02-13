package com.yousef.genaza.service;

import android.annotation.SuppressLint;

import com.google.firebase.Timestamp;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yousef.genaza.database.Repository;
import com.yousef.genaza.listener.ItemListener;
import com.yousef.genaza.models.Dead;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        if(message.getNotification() != null){
            String id = message.getNotification().getTitle();
            Repository repository = new Repository(this);
            repository.getDeadByID(id, new ItemListener<>() {
                @Override
                public void getItem(Dead item) {
                    Timestamp now = Timestamp.now();
                    if(item.getTimestamp() != null && item.getTimestamp().compareTo(now) > 0){
                        repository.sendNotification(item);
                    }
                }

                @Override
                public void failGetItem(String error) {

                }

                @Override
                public void successAddOrEditItem() {

                }

                @Override
                public void failAddOrEditItem(String error) {

                }
            });
        }
    }
}

