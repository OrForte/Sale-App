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
import com.example.eliavmenachi.myapplication.Entities.Post;
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
        void onGetPosts(List<Post> p_postToReturn);
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

    //endregion

    //region Methods

    //region posts

    public void addPost(Post p_postToSave)
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
            public void onGetPosts(List<Post> p_postToReturn) {
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

    //endregion
}
