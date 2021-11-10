package com.example.androidproject;

public interface FireBaseListener {
    public void onFailed();
    public void onDataChanged();
    public void onDataRemoved();
    public void onDataAdded();
}
