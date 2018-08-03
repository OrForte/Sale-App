package com.example.eliavmenachi.myapplication.Models.User;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.eliavmenachi.myapplication.Entities.UserPreview;

import java.util.List;

@Dao
public interface UserPreviewDao {
    @Query("SELECT * FROM UserPreview")
    List<UserPreview> getUsersPreview();

    @Query("SELECT * FROM UserPreview where id LIKE :id")
    UserPreview getUserPreviewByUserId(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsersPreview(UserPreview... usersPreview);

    @Delete
    void delete(UserPreview userPreview);
}
