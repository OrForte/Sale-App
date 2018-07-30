package com.example.eliavmenachi.myapplication.Entities;

import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


public class Sequence {
    @PrimaryKey
    @NonNull
    public String name;
    public String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
