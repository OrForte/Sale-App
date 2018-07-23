package com.example.eliavmenachi.myapplication.Models;

import android.os.AsyncTask;

import com.example.eliavmenachi.myapplication.Entities.Sale;

import java.util.ArrayList;
import java.util.List;

public class SaleAsyncDao {

    interface SaleAsynchDaoListener<T>{
        void onComplete(T data);
    }

    static public void getAll(final SaleAsynchDaoListener<List<Sale>> listener) {
        class MyAsynchTask extends AsyncTask<String,String,List<Sale>> {
            @Override
            protected List<Sale> doInBackground(String... strings) {
                List<Sale> sList = new ArrayList<Sale>();
                Object a = MainAppLocalDb.db.saleDao();
                if (a != null)
                {
                    if (MainAppLocalDb.db.saleDao().getAll() != null)
                    {
                        sList = MainAppLocalDb.db.saleDao().getAll();
                    }
                }
                return sList;
            }

            @Override
            protected void onPostExecute(List<Sale> sales) {
                super.onPostExecute(sales);
                listener.onComplete(sales);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }

    static public void getSalesByStoreId(final String storeId ,final SaleAsynchDaoListener<List<Sale>> listener) {
        class MyAsynchTask extends AsyncTask<String,String,List<Sale>> {
            @Override
            protected List<Sale> doInBackground(String... strings) {
                List<Sale> sList = new ArrayList<Sale>();
                List<Sale> toSend = new ArrayList<Sale>();
                Object a = MainAppLocalDb.db.saleDao();
                if (a != null)
                {
                    if (MainAppLocalDb.db.saleDao().getAll() != null)
                    {
                        sList = MainAppLocalDb.db.saleDao().getAll();
                        for (Sale curr : sList)
                        {
                            if (curr.storeId == Integer.parseInt(storeId))
                            {
                                toSend.add(curr);
                            }
                        }
                    }
                }
                return toSend;
            }

            @Override
            protected void onPostExecute(List<Sale> sales) {
                super.onPostExecute(sales);
                listener.onComplete(sales);
            }
        }

        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }

    static void insertAll(final List<Sale> sales, final SaleAsynchDaoListener<Boolean> listener){
        class MyAsynchTask extends AsyncTask<List<Sale>,String,Boolean>{
            @Override
            protected Boolean doInBackground(List<Sale>... sales) {
                for (Sale sl:sales[0]) {
                    MainAppLocalDb.db.saleDao().insertAll(sl);
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
        task.execute(sales);
    }
}
