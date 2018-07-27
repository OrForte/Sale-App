package com.example.eliavmenachi.myapplication.Models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.CityMallStoreDetails;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.Models.CityMallAndStore.CityMallAndStoreModel;
import com.example.eliavmenachi.myapplication.Models.CityMallAndStore.CityMallAndStoreModelFirebase;
import com.example.eliavmenachi.myapplication.Models.Sale.SaleModelFirebase;
import com.example.eliavmenachi.myapplication.Models.Sale.SalesAsyncDao;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainModel {

    //region Data Members

    public static MainModel instance = new MainModel();
    MainModelFirebase mainModelFirebase;
    CityMallAndStoreModelFirebase cityMallAndStoreModelFirebase;
    SaleModelFirebase saleModelFirebase;

    //endregion

    //region C'Tors

    private MainModel()
    {
        mainModelFirebase = new MainModelFirebase();
        cityMallAndStoreModelFirebase = new CityMallAndStoreModelFirebase();
        saleModelFirebase = new SaleModelFirebase();
    }

    // endregion


    //region CityMallAndStoreListData

    public class CityMallAndStoreListData extends MutableLiveData<ListData>
    {
        @Override
        protected void onActive() {
            super.onActive();

            // TODO: 1. get the students list from the local DB
            // TODO: 2. update the live data with the new student list

            // 3. get the student list from firebase
            cityMallAndStoreModelFirebase.GetListOfCitiesMallsAndStores(new CityMallAndStoreModel.GetListOfCitiesMallsAndStoresListener() {
                @Override
                public void onGetListOfCitiesMallsANdStoresResults(ListData data) {
                    // 4. update the live data with the new student list
                    setValue(data);
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
        }
        public CityMallAndStoreListData()
        {
            super();
            setValue(new ListData());
        }
    }

    CityMallAndStoreListData cityMallAndStoreListData = new CityMallAndStoreListData();
    public LiveData<ListData> getAllCityMalssAndStores() {
        return cityMallAndStoreListData;
    }


    //endregion
}
