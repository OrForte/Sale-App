package com.example.eliavmenachi.myapplication.Models.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Models.MainModel;

import java.util.List;

public class CityMallAndStoreViewModel extends ViewModel {
    LiveData<ListData> data;

    public LiveData<ListData> getData()
    {
        data = MainModel.instance.getAllCityMalssAndStores();
        return data;
    }
}
