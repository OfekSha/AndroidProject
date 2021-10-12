package com.example.androidproject.data;

import com.google.android.gms.maps.model.LatLng;

public class Restaurant {
    private int id;
    private String name;
    private LatLng pos;
    boolean isAvailable=true;
    public Restaurant(double x, double y, String name) {
        pos = new LatLng(x, y);
        this.name = name;
    }
    public Restaurant(LatLng pos, String name) {
        this.pos = pos;
        this.name = name;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getPos() {
        return pos;
    }

    public void setPos(LatLng pos) {
        this.pos = pos;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
