package com.example.eliavmenachi.myapplication.Models.CityMallAndStore;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.CityMallStoreDetails;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.Store;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CityMallAndStoreModel {

    //region Data Members

    public static CityMallAndStoreModel instance = new CityMallAndStoreModel();
    CityMallAndStoreModelFirebase cityMallAndStoreModelFirebase;
    List<Store> lstAllStores = new ArrayList<Store>();
    List<Mall> lstAllMalls = new ArrayList<Mall>();
    List<City> lstAllCities = new ArrayList<City>();

    //endregion

    //region C'Tors

    private CityMallAndStoreModel()
    {
        cityMallAndStoreModelFirebase = new CityMallAndStoreModelFirebase();
    }

    // endregion

    public interface GetListOfCitiesMallsAndStoresListener{
        void onGetListOfCitiesMallsANdStoresResults(ListData data);
    }
    public void GetListOfCitiesMallsAndStores(final GetListOfCitiesMallsAndStoresListener listener)
    {
        cityMallAndStoreModelFirebase.GetListOfCitiesMallsAndStores(new GetListOfCitiesMallsAndStoresListener() {
            @Override
            public void onGetListOfCitiesMallsANdStoresResults(ListData data) {
                listener.onGetListOfCitiesMallsANdStoresResults(data);
            }
        });
    }


    public interface GetDetailsByStoreIdListener{
        void onGetDetailsByStoreIdResults(CityMallStoreDetails data);
    }
    public void GetDetailsByStoreId(final int storeId, final GetDetailsByStoreIdListener listener)
    {
        cityMallAndStoreModelFirebase.GetDetailsByStoreId(storeId, new GetDetailsByStoreIdListener() {
            @Override
            public void onGetDetailsByStoreIdResults(CityMallStoreDetails data) {
                listener.onGetDetailsByStoreIdResults(data);
            }
        });
    }



    public interface GetMallsByCityIdListener{
        void onGetMallsByCityIdResults(List<Mall> p_mallList);
    }
    public void GetMallsByCityId(final String cityId, final GetMallsByCityIdListener listener)
    {
        cityMallAndStoreModelFirebase.GetMallsByCityId(cityId, new GetMallsByCityIdListener() {
            @Override
            public void onGetMallsByCityIdResults(List<Mall> p_mallList) {
                listener.onGetMallsByCityIdResults(p_mallList);
            }
        });
    }



    public interface  GetStoreByMallIdListener{
        void onGetStoresByMallIdResults(List<Store> p_storeList);
    }
    public void GetStoresByMallId(final String mallId, final GetStoreByMallIdListener listener)
    {
        cityMallAndStoreModelFirebase.GetStoresByMallId(mallId, new GetStoreByMallIdListener() {
            @Override
            public void onGetStoresByMallIdResults(List<Store> p_storeList) {
                listener.onGetStoresByMallIdResults(p_storeList);
            }
        });
    }


    public interface  GetCitiesListener{
        void onGetCitiesResults(List<City> p_citiesList);
    }
    public void GetCities(final GetCitiesListener listener)
    {
        cityMallAndStoreModelFirebase.GetCities(new GetCitiesListener() {
            @Override
            public void onGetCitiesResults(List<City> p_citiesList) {
                listener.onGetCitiesResults(p_citiesList);
            }
        });
    }


    public interface GetStoreByStoreIdListener{
        void onGetStoreByStoreIdResults(Store storeData);
    }
    public void GetStoreByStoreId(final int storeId, final GetStoreByStoreIdListener listener)
    {
        cityMallAndStoreModelFirebase.GetStoreByStoreId(storeId, new GetStoreByStoreIdListener() {
            @Override
            public void onGetStoreByStoreIdResults(Store storeData) {
                listener.onGetStoreByStoreIdResults(storeData);
            }
        });
    }

    /*
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
    }*/


    //region CityMallAndStoreListData

    public class CityMallAndStoreListData extends MutableLiveData<ListData>
    {
        @Override
        protected void onActive() {
            super.onActive();

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
