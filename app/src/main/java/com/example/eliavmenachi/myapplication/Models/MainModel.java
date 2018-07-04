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
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Entities.Store;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class MainModel {

    //region Data Members

    public static MainModel instance = new MainModel();
    MainModelFirebase mainModelFirebase;

    //endregion

    //region C'Tors

    private MainModel()
    {
        mainModelFirebase = new MainModelFirebase();
    }

    // endregion

    // region Interfaces

    public interface IsUserVisibleListener{
        void onDone(boolean p_bIsValid);
    }

    public interface GetPostsByStoreIdListener{
        void onGetPosts(List<Sale> p_postToReturn);
    }

    public interface GetMallsByCityIdListener{
        void onGetMallsByCityIdResults(List<Mall> p_mallList);
    }

    public interface  GetStoreByMallIdListener{
        void onGetStoresByMallIdResults(List<Store> p_storeList);
    }

    public interface  GetCitiesListener{
        void onGetCitiesResults(List<City> p_citiesList);
    }

    public interface GetStoreByStoreIdListener{
        void onGetStoreByStoreIdResults(Store storeData);
    }

    public interface GetMallByStoreIdListener {
        void onGetMallByStoreIdResult(Mall mallData);
    }

    public interface GetCityByMallIdListener{
        void onGetCityByMallIdResult(City cityData);
    }

    //endregion

    //region Methods

    //region posts

    public void addPost(Sale p_postToSave)
    {
        mainModelFirebase.addPost(p_postToSave);
    }

    //endregion

    //region users

    public void addUser(User userToAdd)
    {
        mainModelFirebase.addUser(userToAdd);
    }

    public void IsUserVisible(final String p_strUserName, final String p_strPassword ,  final IsUserVisibleListener listener ){
        boolean bIsValid = true;

        mainModelFirebase.IsUserVisible(p_strUserName, p_strPassword, new IsUserVisibleListener() {
            @Override
            public void onDone(boolean p_bIsValid) {
                // Its happen when we get response from firebase
                listener.onDone(p_bIsValid);
            }
        });

        //listener.onDone(bIsValid);
    }

    //endregion

    //region get posts

    public void GetPostsByStoreId(final String storeId, final GetPostsByStoreIdListener listener)
    {
        mainModelFirebase.getPostsByStoreId(storeId, new GetPostsByStoreIdListener() {
            @Override
            public void onGetPosts(List<Sale> p_postToReturn) {
                // TODO: need to return the posts to the fragments of posts
                //listener.onGetPosts(p_postToReturn);
            }
        });
    }

    //endregion

    //region malls by city id

    public void GetMallsByCityId(final String cityId, final GetMallsByCityIdListener listener)
    {
        mainModelFirebase.GetMallsByCityId(cityId, new GetMallsByCityIdListener() {
            @Override
            public void onGetMallsByCityIdResults(List<Mall> p_mallList) {
                listener.onGetMallsByCityIdResults(p_mallList);
            }
        });
    }

    //endregion

    //region Stores by mall id
    public void GetStoresByMallId(final String mallId, final GetStoreByMallIdListener listener)
    {
        mainModelFirebase.GetStoresByMallId(mallId, new GetStoreByMallIdListener() {
            @Override
            public void onGetStoresByMallIdResults(List<Store> p_storeList) {
                listener.onGetStoresByMallIdResults(p_storeList);
            }
        });
    }

    //endregion

    //region get Cities

    public void GetCities(final GetCitiesListener listener)
    {
        mainModelFirebase.GetCities(new GetCitiesListener() {
            @Override
            public void onGetCitiesResults(List<City> p_citiesList) {
                listener.onGetCitiesResults(p_citiesList);
            }
        });
    }

    //endregion

    public void GetStoreByStoreId(final int storeId, final GetStoreByStoreIdListener listener)
    {
        mainModelFirebase.GetStoreByStoreId(storeId, new GetStoreByStoreIdListener() {
            @Override
            public void onGetStoreByStoreIdResults(Store storeData) {
                listener.onGetStoreByStoreIdResults(storeData);
            }
        });
    }

    public void GetMallByStoreId(final int storeId, final GetMallByStoreIdListener listener)
    {
        mainModelFirebase.GetMallByStoreId(storeId, new GetMallByStoreIdListener() {
            @Override
            public void onGetMallByStoreIdResult(Mall mallData) {
                listener.onGetMallByStoreIdResult(mallData);
            }
        });
    }

    public void GetCityByMallId(final int mallId, final GetCityByMallIdListener listener)
    {
        mainModelFirebase.GetCityByMallId(mallId, new GetCityByMallIdListener() {
            @Override
            public void onGetCityByMallIdResult(City cityData) {
                listener.onGetCityByMallIdResult(cityData);
            }
        });
    }

    //endregion

    //region live data

    public class SaleListData extends MutableLiveData<List<Sale>>
    {
        @Override
        protected void onActive() {
            super.onActive();
            mainModelFirebase.getAllSales(new MainModelFirebase.GetAllSalesListener() {
                @Override
                public void onSuccess(List<Sale> studentslist) {
                    // 4. update the live data with the new student list
                    setValue(studentslist);
                    Log.d("TAG","got students from firebase " + studentslist.size());
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            mainModelFirebase.cancellGetAllSales();
        }

        public SaleListData()
        {
            super();
            setValue(new LinkedList<Sale>());
        }
    }

    SaleListData studentListData = new SaleListData();

    public LiveData<List<Sale>> getAllSales() { return studentListData;}

    //endregion
}
