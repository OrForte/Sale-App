package com.example.eliavmenachi.myapplication.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Models.Sale.SaleModel;
import com.example.eliavmenachi.myapplication.Models.Sale.SalesAsyncDao;

import java.util.LinkedList;
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

    public void deleteLogicSale(final Sale p_saleToDelete, final SaleModel.deleteLogicSaleListener listener) {
        SaleModel.instance.deleteLogicSale(p_saleToDelete, new SaleModel.deleteLogicSaleListener() {
            @Override
            public void onDeleteLogicSale(boolean b_isDelete) {
                SalesAsyncDao.deleteSale(p_saleToDelete);
                listener.onDeleteLogicSale(b_isDelete);
            }
        });
    }

    public void addOrUpdateNewSale(final Sale p_saleToAdd, final SaleModel.addOrUpdateNewSaleListener listener)
    {
        SaleModel.instance.addOrUpdateNewSale(p_saleToAdd, new SaleModel.addOrUpdateNewSaleListener() {
            @Override
            public void onAddOrUpdateNewSaleResults(final Sale SaleToReturn) {
                List<Sale> lstToAdd = new LinkedList<Sale>();
                lstToAdd.add(SaleToReturn);
                SalesAsyncDao.insertAll(lstToAdd, new SalesAsyncDao.SaleAsynchDaoListener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        listener.onAddOrUpdateNewSaleResults(SaleToReturn);
                    }
                });
            }
        });
    }

    public void GetNextSequenceSale(final String SeqName, final SaleModel.GetNextSequenceListener listener) {
        SaleModel.instance.GetNextSequenceSale(SeqName, new SaleModel.GetNextSequenceListener() {
            @Override
            public void onGetNextSeq(String p_next) {
                listener.onGetNextSeq(p_next);
            }
        });
    }
}
