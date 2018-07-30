package com.example.eliavmenachi.myapplication.Models;

import android.arch.persistence.room.Room;

import com.example.eliavmenachi.myapplication.MyApplication;

public class MainAppLocalDb {
    static public MainAppLocalDbRepository db = Room.databaseBuilder(MyApplication.context,
            MainAppLocalDbRepository.class,
            "SaleAppDB.db").fallbackToDestructiveMigration().build();
}