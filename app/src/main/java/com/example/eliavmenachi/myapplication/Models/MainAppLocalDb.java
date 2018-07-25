package com.example.eliavmenachi.myapplication.Models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.MyApplication;

public class MainAppLocalDb{
    static public MainAppLocalDbRepository db = Room.databaseBuilder(MyApplication.context,
            MainAppLocalDbRepository.class,
            "SaleAppDB.db").fallbackToDestructiveMigration().build();
}