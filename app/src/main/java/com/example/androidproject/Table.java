package com.example.androidproject;

public class Table {
    private final int id;
    private int seats;
    private boolean isFull;
    private boolean isSmoke;
    public Table(int id,int seats,boolean isSmoke){
        this.id=id;
        this.seats=seats;
        this.isSmoke=isSmoke;
    }

    public int getId() {
        return id;
    }

    public int getSeats() {
        return seats;
    }

    public boolean isFull() {
        return isFull;
    }

    public boolean isSmoke() {
        return isSmoke;
    }

    public void setFull(boolean full) {
        isFull = full;
    }
}
