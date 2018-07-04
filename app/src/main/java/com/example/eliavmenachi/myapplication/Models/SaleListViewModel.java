package com.example.eliavmenachi.myapplication.Models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Models.MainModel;

import java.util.List;

public class SaleListViewModel extends ViewModel {
    LiveData<List<Sale>> data;

    public LiveData<List<Sale>> getData()
    {
        //data = MainModel.instance.GetCities();
        return data;
    }
}
