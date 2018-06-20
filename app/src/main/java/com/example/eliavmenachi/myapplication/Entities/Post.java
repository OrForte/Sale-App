package com.example.eliavmenachi.myapplication.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

public class Post {
    @PrimaryKey
    @NonNull
    public int id;
    public int storeId;
    public int userId;
    public String avatar;
    public String describtion;
    public String endDate;
    public String createdDate;
    public int category;

    public int getId() {
        return id;
    }
    public int getStoreID() { return storeId;}
    public int getUserId() { return userId;}
    public String getAvatar() { return avatar;}
    public String getDescribtion() { return describtion;}
    public String getEndDate() { return endDate;}
    public String getCreatedDate() { return createdDate;}
    public int getCategory() { return category;}

    public void setId(int id) {
        this.id = id;
    }
    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
    public void setUserId(int lastName) {
        this.userId = userId;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    public void setCategory(int category) {
        this.category = category;
    }
}
