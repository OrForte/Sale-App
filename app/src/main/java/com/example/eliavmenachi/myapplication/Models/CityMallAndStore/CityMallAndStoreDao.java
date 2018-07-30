package com.example.eliavmenachi.myapplication.Models.CityMallAndStore;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.Store;

import java.util.List;


@Dao
public interface CityMallAndStoreDao {
    @Query("SELECT * FROM city")
    List<City> getCities();

    @Query("SELECT * FROM mall")
    List<Mall> getMalls();

    @Query("SELECT * FROM store")
    List<Store> getStores();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCities(City... cities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMalls(Mall... malls);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStores(Store... stores);

    @Delete
    void delete(City city);

    @Delete
    void delete(Mall mall);

    @Delete
    void delete(Store store);

    @Query("DELETE FROM city")
    void deleteCities();

    @Query("DELETE FROM mall")
    void deleteMalls();

    @Query("DELETE FROM store")
    void deleteStores();
}
