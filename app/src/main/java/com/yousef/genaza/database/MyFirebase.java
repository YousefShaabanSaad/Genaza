package com.yousef.genaza.database;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yousef.genaza.listener.Constants;
import com.yousef.genaza.listener.ItemListener;
import com.yousef.genaza.listener.ItemsListener;
import com.yousef.genaza.models.Dead;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyFirebase implements Constants {
    private final FirebaseFirestore firestore;
     public MyFirebase(){
         firestore = FirebaseFirestore.getInstance();
     }

     public void addOrEditDead(Dead dead, ItemListener<Dead> listener){
         if(dead.getId().isEmpty())
             dead.setId(firestore.collection(DEAD).document().getId());
         firestore.collection(DEAD)
                 .document(dead.getId())
                 .set(dead)
                 .addOnSuccessListener(unused -> listener.successAddOrEditItem())
                 .addOnFailureListener(e -> listener.failAddOrEditItem(e.getMessage()));
     }

    public void getDead(ItemsListener<Dead> listener){
        List<Integer> statusList = Arrays.asList(-1, 1);
        firestore.collection(DEAD)
                .whereIn(STATUS, statusList)
                .addSnapshotListener((value, error) -> {
                    if (error != null){
                        listener.failGetItems(error.getMessage());
                        return;
                    }
                    if (value == null){
                        listener.getItems(new ArrayList<>());
                        return;
                    }
                    List<Dead> dead = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()){
                        Dead d = doc.toObject(Dead.class);
                        if(d != null)
                            dead.add(d);
                    }
                    listener.getItems(dead);
                });
    }


    public void getDeadByID(String id, ItemListener<Dead> listener){
        firestore.collection(DEAD)
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot ->
                    listener.getItem(documentSnapshot.toObject(Dead.class))
                )
                .addOnFailureListener(e -> listener.failGetItem(e.getMessage()));
    }
}
