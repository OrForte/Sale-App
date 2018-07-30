package com.example.eliavmenachi.myapplication.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Models.Sale.SaleModel;

import java.util.List;

public class SaleListViewModel extends ViewModel {
    LiveData<List<Sale>> data;

    public LiveData<List<Sale>> getData() {
        data = SaleModel.instance.getAllSales();
        return data;
    }

    public LiveData<List<Sale>> getDataByStoreId(boolean p_bIsGetAllSales, String p_strStoreId) {
        if (p_bIsGetAllSales == true) {
            data = SaleModel.instance.getAllSales();
        } else {
            SaleModel.instance.InitStoreId(p_strStoreId);
            data = SaleModel.instance.getAllSalesByStoreId(p_strStoreId);
        }
        return data;
    }

    public LiveData<Sale> GetSaleBySaleId(String p_strSaleId) {
        SaleModel.instance.InitSaleId(p_strSaleId);
        LiveData<Sale> currSale = SaleModel.instance.getSaleBySaleId(p_strSaleId);
        ;
        return currSale;
    }

    public LiveData<List<Sale>> getSaleListByUserId(String p_strUserName) {
        SaleModel.instance.InitUserName(p_strUserName);
        LiveData<List<Sale>> results = SaleModel.instance.getSalesByUserName(p_strUserName);
        ;
        return results;
    }
}
