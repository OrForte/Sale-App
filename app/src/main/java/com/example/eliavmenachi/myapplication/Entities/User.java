package com.example.eliavmenachi.myapplication.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "birthDate")
    public String birthDate;

    @ColumnInfo(name = "city")
    public int city;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "firstName")
    public String firstName;

    @ColumnInfo(name = "lastName")
    public String lastName;

//    public User() {
//    }
//
//    public User(User user) {
//        this.birthDate = user.birthDate;
//        this.city = user.city;
//        this.email = user.email;
//        this.username = user.username;
//        this.password = user.password;
//        this.firstName = user.firstName;
//        this.lastName = user.lastName;
//    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public int getCity() {
        return city;
    }

    public String getUsername() {
        return username;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
