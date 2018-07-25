package com.example.eliavmenachi.myapplication.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.eliavmenachi.myapplication.Entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getUsers();

    @Query("SELECT * FROM User where id = :id")
    List<User> getUserById(int id);

    @Query("SELECT * FROM User where username LIKE :username")
    User getUserByUsername(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(User... users);

    @Delete
    void delete(User user);

    @Query("DELETE FROM User")
    void deleteAll();
}
