package com.example.eliavmenachi.myapplication.Models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.eliavmenachi.myapplication.Entities.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class MainModel {
    public static MainModel instance = new MainModel();
    MainModelFirebase mainModelFirebase;

    private MainModel()
    {
        mainModelFirebase = new MainModelFirebase();
    }

    public void addUser(User userToAdd)
    {
        mainModelFirebase.addUser(userToAdd);
    }

    public interface IsUserVisibleListener{
        void onDone(boolean p_bIsValid);
    }

    public void IsUserVisible(final String p_strUserName, final String p_strPassword ,  final IsUserVisibleListener listener ){
        boolean bIsValid = true;

        mainModelFirebase.IsUserVisible(p_strUserName, p_strPassword, new IsUserVisibleListener() {
            @Override
            public void onDone(boolean p_bIsValid) {
                // Its happen when we get response from firebase
            }
        });

        listener.onDone(bIsValid);
    }
}
