package com.example.eliavmenachi.myapplication.Models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Models.CityMallAndStore.CityMallAndStoreAsyncDao;

@Database(entities = {Sale.class, User.class, City.class, Mall.class, Store.class}, version = 7)
public abstract class MainAppLocalDbRepository extends RoomDatabase {
    public abstract SaleDao saleDao();
    public abstract UserDao userDao();
    public abstract CityMallAndStoreDao CityMallAndStoreDao();
}
