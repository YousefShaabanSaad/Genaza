package com.yousef.genaza.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

public class Dead {
    private String Id, Uid, Name, Phone, ZPhoto, Notes, TimePlace;
    private Timestamp Timestamp;
    private int Status;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getZPhoto() {
        return ZPhoto;
    }

    public void setZPhoto(String zPhoto) {
        ZPhoto = zPhoto;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getTimePlace() {
        return TimePlace;
    }

    public void setTimePlace(String timePlace) {
        TimePlace = timePlace;
    }

    public Timestamp getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        Timestamp = timestamp;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    @Exclude
    public Bitmap getPhotoBitmap() {
        return base64ToBitmap(ZPhoto);
    }
    @Exclude
    private Bitmap base64ToBitmap(String base64){
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
