package com.example.eliavmenachi.myapplication.Models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.eliavmenachi.myapplication.Entities.ListData;

import java.util.List;

public class CityMallAndStoreViewModel extends ViewModel {
    LiveData<ListData> data;

    public LiveData<ListData> getData()
    {
        data = MainModel.instance.getAllCityMalssAndStores();
        return data;
    }
}
