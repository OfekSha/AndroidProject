package com.example.androidproject.data;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private String id;
    private String name;
    private String owner_name="none";
    private String phone="none";
    private String address="none";
    private LatLng pos;
    boolean isAvailable=true;

    public Restaurant(String name, String owner_name, String phone, String address,double x, double y, boolean isAvailable) {
        this( name,  owner_name,  phone,  address,  isAvailable);
        pos = new LatLng(x, y);
    }
    public Restaurant(String name, String owner_name, String phone, String address, LatLng pos, boolean isAvailable) {
        this( name,  owner_name,  phone,  address,  isAvailable);
        this.pos=pos;
    }
    private Restaurant(String name, String owner_name, String phone, String address, boolean isAvailable) {
        this.name = name;
        this.owner_name = owner_name;
        this.phone = phone;
        this.address = address;
        this.pos = pos;
        this.isAvailable = isAvailable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
