package com.example.eliavmenachi.myapplication.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Sale {
    @PrimaryKey
    @NonNull
    public String id;
    public int storeId;
    public String userId;
    public int mallId;
    public int cityId;
    public String pictureUrl;
    public String description;
    public String endDate;
    public String createdDate;
    public int category;
    public String storeName;
    public String mallName;
    public String cityName;
    public boolean active;
    public String title;

    public String getId() {
        return id;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getMallId() {
        return mallId;
    }

    public int getCityId() {
        return cityId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public int getCategory() {
        return category;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getMallName() {
        return mallName;
    }

    public String getCityName() {
        return cityName;
    }

    public boolean getActive() {
        return active;
    }

    public String getTitle() {
        return title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setDescription(String describtion) {
        this.description = describtion;
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

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setMallId(int mallId) {
        this.mallId = mallId;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setMallName(String mallName) {
        this.mallName = mallName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setActive(boolean p_active) {
        this.active = p_active;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
