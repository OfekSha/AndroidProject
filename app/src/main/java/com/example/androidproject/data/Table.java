package com.example.androidproject.data;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return id == table.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
