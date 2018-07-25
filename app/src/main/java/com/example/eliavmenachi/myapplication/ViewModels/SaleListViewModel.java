package com.example.eliavmenachi.myapplication.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

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

    public LiveData<List<Sale>> getDataByStoreId(boolean p_bIsGetAllSales,String p_strStoreId)
    {
        if (p_bIsGetAllSales == true)
        {
            data = MainModel.instance.getAllSales();
        }
        else {
            MainModel.instance.InitStoreId(p_strStoreId);
            data = MainModel.instance.getAllSalesByStoreId(p_strStoreId);
        }
        return data;
    }

    public LiveData<Sale> GetSaleBySaleId(String p_strSaleId)
    {
        MainModel.instance.InitSaleId(p_strSaleId);
        LiveData<Sale> currSale = MainModel.instance.getSaleBySaleId(p_strSaleId);;
        return currSale;
    }
}
