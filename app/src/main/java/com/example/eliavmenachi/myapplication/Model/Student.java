package com.example.eliavmenachi.myapplication.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by eliav.menachi on 11/04/2018.
 */

@Entity
public class Student {
    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String bDate;
    public String phone;
    public String avatar;
    public boolean checked;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getbDate() {
        return bDate;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setbDate(String bDate) {
        this.bDate = bDate;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
