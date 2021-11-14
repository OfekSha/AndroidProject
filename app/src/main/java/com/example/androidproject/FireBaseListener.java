package com.example.androidproject;

import java.util.Map;

public interface FireBaseListener {
    public void onFailed();
    public void onDataChanged(Map<String, Object> doc);
    public void onDataRemoved(Map<String, Object> doc);
    public void onDataAdded(Map<String, Object> doc);
}
