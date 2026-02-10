package com.yousef.genaza.database;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yousef.genaza.listener.Constants;
import com.yousef.genaza.listener.ItemListener;
import com.yousef.genaza.listener.ItemsListener;
import com.yousef.genaza.models.Dead;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyFirebase implements Constants {
    private final FirebaseFirestore firestore;
     public MyFirebase(){
         firestore = FirebaseFirestore.getInstance();
     }

     public void addOrEditDead(Dead dead, ItemListener listener){
         if(dead.getId().isEmpty())
             dead.setId(firestore.collection(DEAD).document().getId());
         firestore.collection(DEAD)
                 .document(dead.getId())
                 .set(dead)
                 .addOnSuccessListener(unused -> listener.successAddOrEditItem())
                 .addOnFailureListener(e -> listener.failAddOrEditItem(e.getMessage()));
     }

    public void getDead(ItemsListener<Dead> listener){
        // بداية اليوم
        Calendar startCal = Calendar.getInstance();
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = startCal.getTime();
        Timestamp startTimestamp = new Timestamp(startDate);

        List<Dead> dead = new ArrayList<>();
        firestore.collection(DEAD)
                .whereGreaterThanOrEqualTo(DATE_GENAZA, startTimestamp)
                .whereEqualTo(STATUS, 1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    dead.clear();
                    queryDocumentSnapshots.getDocuments().forEach(documentSnapshot ->
                            dead.add(documentSnapshot.toObject(Dead.class))
                    );
                    listener.getItems(dead);
                })
                .addOnFailureListener(e -> listener.failGetItems(e.getMessage()));
    }


}
