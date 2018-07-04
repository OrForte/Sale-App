package com.example.eliavmenachi.myapplication.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

public class City {

    @PrimaryKey
    @NonNull
    public String id;
    public String name;

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
}
