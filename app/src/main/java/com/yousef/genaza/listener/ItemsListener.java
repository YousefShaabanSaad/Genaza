package com.yousef.genaza.listener;

import java.util.List;

public interface ItemsListener<T> {
    void getItems(List<T> items);
    void clickItem(T item);
    void failGetItems(String error);
}

