package com.example.eliavmenachi.myapplication.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

public class Store {
    @PrimaryKey
    @NonNull
    public int id;
    public String name;
    public int mallId;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getMallId() { return mallId;}

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setMallId(int cityId) {
        this.mallId = cityId;
    }
}
