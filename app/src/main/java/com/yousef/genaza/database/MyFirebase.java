package com.yousef.genaza.database;


import androidx.annotation.NonNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yousef.genaza.listener.Constants;
import com.yousef.genaza.listener.ItemListener;
import com.yousef.genaza.listener.ItemsListener;
import com.yousef.genaza.models.Dead;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
                 .addOnSuccessListener(unused -> {
                     sendToAdmin(dead.getId());
                     listener.successAddOrEditItem();
                 })
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

    private void sendToAdmin(String id){
        OkHttpClient client = new OkHttpClient();
        try {
            JSONObject json = new JSONObject();
            json.put(TITLE, id);
            json.put(BODY, id);
            json.put(TARGET, ADMIN);
            RequestBody requestBody = RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call,@NonNull IOException e) {
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {

                }
            });

        } catch (Exception ignore) {
        }
    }

    public void subscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic(USER);
    }

}
