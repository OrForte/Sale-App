package com.example.eliavmenachi.myapplication.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Model.Student;

import java.util.List;

@Dao
public interface    SaleDao {
    @Query("select * from Sale")
    List<Sale> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Sale... sales);

    @Delete
    void delete(Sale sale);

    @Query("SELECT * FROM Sale where id LIKE :id")
    Sale getSaleBySaleId(String id);
}
