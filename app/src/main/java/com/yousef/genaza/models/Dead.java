package com.yousef.genaza.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;
import java.sql.Timestamp;

public class Dead implements Serializable {
    private String Id, IdOwner, Name, Phone, Photo, Notes, TimePlace;
    private Timestamp Timestamp;
    private int Status;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getIdOwner() {
        return IdOwner;
    }

    public void setIdOwner(String idOwner) {
        IdOwner = idOwner;
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

    public Bitmap getPhoto() {
        return base64ToBitmap(Photo);
    }

    private Bitmap base64ToBitmap(String base64){
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    public void setPhoto(String photo) {
        Photo = photo;
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
}
