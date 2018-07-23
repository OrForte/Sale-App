package com.example.eliavmenachi.myapplication.Models.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Models.MainModel;

import java.util.List;

public class SaleListViewModel extends ViewModel {
    LiveData<List<Sale>> data;

    public LiveData<List<Sale>> getData()
    {
        data = MainModel.instance.getAllSales();
        return data;
    }

    public LiveData<List<Sale>> getDataByStoreId(String p_strStoreId)
    {
        data = MainModel.instance.getAllSalesByStoreId(p_strStoreId);
        return data;
    }
}
