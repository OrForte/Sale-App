package com.example.eliavmenachi.myapplication.Models.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.Models.MainModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CityMallAndStoreViewModel extends ViewModel {
    LiveData<ListData> data;

    public LiveData<ListData> getData()
    {
        data = MainModel.instance.getAllCityMalssAndStores();
        return data;
    }

    public List<String> GetCityNames(ListData listData)
    {
        List<String> cities = new ArrayList<>();
        for (Iterator iterator = listData.cities.iterator(); iterator.hasNext();)
        {
            cities.add(((City) iterator.next()).name);
        }
        return cities;
    }

    public List<String> GetMallNamesByCityId(int cityId, ListData listData)
    {
        List<String> malls = new ArrayList<>();
        for (Iterator iterator = listData.malls.iterator(); iterator.hasNext();)
        {
            Mall mall = (Mall)iterator.next();
            if (mall.cityId == cityId)
            {
                malls.add(mall.name);
            }
        }
        return malls;
    }

    public List<String> GetStoreNamesByMallId(int mallId, ListData listData)
    {
        List<String> stores = new ArrayList<>();
        for (Iterator iterator = listData.stores.iterator(); iterator.hasNext();)
        {
            Store store = (Store)iterator.next();
            if (store.mallId == mallId)
            {
                stores.add(store.name);
            }
        }
        return stores;
    }

    public City GetCityByCityName(String selectedCityName, ListData listData)
    {
        for (Iterator iterator = listData.cities.iterator(); iterator.hasNext();)
        {
            City city = (City) iterator.next();
            if (selectedCityName == city.name)
            {
                return city;
            }
        }
        return null;
    }

    public City GetCityByCityId(int cityId, ListData listData)
    {
        for (Iterator iterator = listData.cities.iterator(); iterator.hasNext();)
        {
            City city = (City) iterator.next();
            if (cityId == city.id)
            {
                return city;
            }
        }
        return null;
    }

    public Mall GetMallByMallName(String selectedMallName, ListData listData)
    {
        for (Iterator iterator = listData.malls.iterator(); iterator.hasNext();)
        {
            Mall mall = (Mall) iterator.next();
            if (selectedMallName == mall.name)
            {
                return mall;
            }
        }
        return null;
    }

    public Mall GetMallByMallId(int mallId, ListData listData)
    {
        for (Iterator iterator = listData.malls.iterator(); iterator.hasNext();)
        {
            Mall mall = (Mall) iterator.next();
            if (mallId == mall.id)
            {
                return mall;
            }
        }
        return null;
    }

    public Store GetStoreByStoreName(String selectedStoreName, ListData listData)
    {
        for (Iterator iterator = listData.stores.iterator(); iterator.hasNext();)
        {
            Store store = (Store) iterator.next();
            if (selectedStoreName == store.name)
            {
                return store;
            }
        }
        return null;
    }

    public Store GetStoreByStoreId(int storeId, ListData listData)
    {
        for (Iterator iterator = listData.stores.iterator(); iterator.hasNext();)
        {
            Store store = (Store) iterator.next();
            if (storeId == store.id)
            {
                return store;
            }
        }
        return null;
    }
}
