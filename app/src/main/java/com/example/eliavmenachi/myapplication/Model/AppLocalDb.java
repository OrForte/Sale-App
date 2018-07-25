package com.example.eliavmenachi.myapplication.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Models.UserDao;
import com.example.eliavmenachi.myapplication.MyApplication;

@Database(entities = {Student.class}, version = 3)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract StudentDao studentDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db = Room.databaseBuilder(MyApplication.context,
            AppLocalDbRepository.class,
            "fileNameDB.db").fallbackToDestructiveMigration().build();
}
