package com.example.eliavmenachi.myapplication.Models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Entities.UserPreview;
import com.example.eliavmenachi.myapplication.Models.CityMallAndStore.CityMallAndStoreDao;
import com.example.eliavmenachi.myapplication.Models.Sale.SaleDao;
import com.example.eliavmenachi.myapplication.Models.User.UserDao;
import com.example.eliavmenachi.myapplication.Models.User.UserPreviewDao;

@Database(entities = {Sale.class, User.class, City.class, Mall.class, Store.class, UserPreview.class}, version = 11)
public abstract class MainAppLocalDbRepository extends RoomDatabase {
    public abstract SaleDao saleDao();

    public abstract UserDao userDao();

    public abstract CityMallAndStoreDao CityMallAndStoreDao();

    public abstract UserPreviewDao userPreviewDao();
}
