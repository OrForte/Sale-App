package com.example.eliavmenachi.myapplication.Models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Entities.User;

@Database(entities = {Sale.class, User.class}, version = 3)
public abstract class MainAppLocalDbRepository extends RoomDatabase {
    public abstract SaleDao saleDao();
    public abstract UserDao userDao();
}
