package com.yousef.genaza.listener;

public interface ItemListener<T>{
    void getItem(T item);
    void failGetItem(String error);
    void successAddOrEditItem();
    void failAddOrEditItem(String error);
}

