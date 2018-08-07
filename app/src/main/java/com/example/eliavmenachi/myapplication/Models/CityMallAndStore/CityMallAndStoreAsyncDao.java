package com.example.eliavmenachi.myapplication.Models.CityMallAndStore;

import android.os.AsyncTask;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.Models.MainAppLocalDb;

public class CityMallAndStoreAsyncDao {
    interface CityMallAndStoreAsynchDaoListener<T> {
        void onComplete(T data);
    }

    static public void getCitiesMallsAndStores(final CityMallAndStoreAsynchDaoListener<ListData> listener) {
        class MyAsynchTask extends AsyncTask<String, String, ListData> {
            @Override
            protected ListData doInBackground(String... strings) {
                ListData data = new ListData();
                Object a = MainAppLocalDb.db.CityMallAndStoreDao();
                if (a != null) {
                    if (MainAppLocalDb.db.CityMallAndStoreDao().getCities() != null) {
                        data.cities = MainAppLocalDb.db.CityMallAndStoreDao().getCities();
                    }
                    if (MainAppLocalDb.db.CityMallAndStoreDao().getMalls() != null) {
                        data.malls = MainAppLocalDb.db.CityMallAndStoreDao().getMalls();
                    }
                    if (MainAppLocalDb.db.CityMallAndStoreDao().getStores() != null) {
                        data.stores = MainAppLocalDb.db.CityMallAndStoreDao().getStores();
                    }
                }
                return data;
            }

            @Override
            protected void onPostExecute(ListData data) {
                super.onPostExecute(data);
                listener.onComplete(data);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }

    static void insertAll(final ListData data, final CityMallAndStoreAsynchDaoListener<Boolean> listener) {
        class MyAsynchTask extends AsyncTask<ListData, String, Boolean> {
            @Override
            protected Boolean doInBackground(ListData... data) {

                if (data != null) {
                    if (data[0] != null) {
                        if (data[0].cities != null) {
                            for (City currCity : data[0].cities) {
                                if (currCity != null) {
                                    MainAppLocalDb.db.CityMallAndStoreDao().insertCities(currCity);
                                }
                            }

                            for (Mall currMall : data[0].malls) {
                                if (currMall != null) {
                                    MainAppLocalDb.db.CityMallAndStoreDao().insertMalls(currMall);
                                }
                            }

                            for (Store currStore : data[0].stores) {
                                if (currStore != null) {
                                    MainAppLocalDb.db.CityMallAndStoreDao().insertStores(currStore);
                                }
                            }
                        }
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute(data);
    }
}
