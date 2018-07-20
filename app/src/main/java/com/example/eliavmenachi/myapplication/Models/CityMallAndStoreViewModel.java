package com.example.eliavmenachi.myapplication.Models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.eliavmenachi.myapplication.Entities.ListData;

import java.util.List;

public class CityMallAndStoreViewModel extends ViewModel {
    LiveData<List<ListData>> data;

    public LiveData<List<ListData>> getData()
    {
        //data = MainModel.instance.getAllSales();
        return data;
    }
}
