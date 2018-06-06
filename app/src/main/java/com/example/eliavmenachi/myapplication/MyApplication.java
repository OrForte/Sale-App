package com.example.eliavmenachi.myapplication;

import android.app.Application;
import android.content.Context;

/**
 * Created by eliav.menachi on 09/05/2018.
 */

public class MyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
