package com.example.eliavmenachi.myapplication.Models.Sale;

import android.os.AsyncTask;

import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Models.MainAppLocalDb;

import java.util.ArrayList;
import java.util.List;

public class SalesAsyncDao {
    public interface SaleAsynchDaoListener<T> {
        void onComplete(T data);
    }

    public interface SaleAsynchDaoListener2<T> {
        void onCompleteOneSale(Sale data);
    }

    static public void getAll(final SaleAsynchDaoListener<List<Sale>> listener) {
        class MyAsynchTask extends AsyncTask<String, String, List<Sale>> {
            @Override
            protected List<Sale> doInBackground(String... strings) {
                List<Sale> sList = new ArrayList<Sale>();
                Object a = MainAppLocalDb.db.saleDao();
                if (a != null) {
                    if (MainAppLocalDb.db.saleDao().getAll() != null) {
                        sList = MainAppLocalDb.db.saleDao().getAll();
                        //DeleteAllSales(sList);
                    }
                }
                return sList;
            }

            public void DeleteAllSales(List<Sale> p_listToDelete) {
                for (Sale curr : p_listToDelete) {
                    MainAppLocalDb.db.saleDao().delete(curr);
                }
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

    static public void getSaleBySaleId(final String SaleId, final SaleAsynchDaoListener2<List<Sale>> listener) {
        class MyAsynchTask extends AsyncTask<String, String, Sale> {
            @Override
            protected Sale doInBackground(String... strings) {
                Sale sList = new Sale();
                Object a = MainAppLocalDb.db.saleDao();
                if (a != null) {
                    if (MainAppLocalDb.db.saleDao().getAll() != null) {
                        sList = MainAppLocalDb.db.saleDao().getSaleBySaleId(SaleId);

                    }
                }
                return sList;
            }

            @Override
            protected void onPostExecute(Sale sales) {
                super.onPostExecute(sales);
                listener.onCompleteOneSale(sales);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }

    static public void deleteSale(final Sale toDelete) {
        class MyAsynchTask extends AsyncTask<String, String, Sale> {
            @Override
            protected Sale doInBackground(String... strings) {
                Sale sList = new Sale();
                Object a = MainAppLocalDb.db.saleDao();
                if (a != null) {
                    if (MainAppLocalDb.db.saleDao().getAll() != null) {
                        MainAppLocalDb.db.saleDao().delete(toDelete);
                    }
                }
                return sList;
            }

            @Override
            protected void onPostExecute(Sale sales) {
                super.onPostExecute(sales);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }

    static public void getSalesByStoreId(final String storeId, final SaleAsynchDaoListener<List<Sale>> listener) {
        class MyAsynchTask extends AsyncTask<String, String, List<Sale>> {
            @Override
            protected List<Sale> doInBackground(String... strings) {
                List<Sale> sList = new ArrayList<Sale>();
                Object a = MainAppLocalDb.db.saleDao();
                if (a != null) {
                    int nStoreId = Integer.parseInt(storeId);
                    if (MainAppLocalDb.db.saleDao().getSaleByStoreId(nStoreId) != null) {
                        sList = MainAppLocalDb.db.saleDao().getSaleByStoreId(nStoreId);
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


    static public void getSalesByUserName(final String userName, final SaleAsynchDaoListener<List<Sale>> listener) {
        class MyAsynchTask extends AsyncTask<String, String, List<Sale>> {
            @Override
            protected List<Sale> doInBackground(String... strings) {
                List<Sale> sList = new ArrayList<Sale>();
                Object a = MainAppLocalDb.db.saleDao();
                if (a != null) {

                    if (MainAppLocalDb.db.saleDao().getSaleByUserName(userName) != null) {
                        sList = MainAppLocalDb.db.saleDao().getSaleByUserName(userName);
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

    public static void insertAll(final List<Sale> sales, final SaleAsynchDaoListener<Boolean> listener) {
        class MyAsynchTask extends AsyncTask<List<Sale>, String, Boolean> {
            @Override
            protected Boolean doInBackground(List<Sale>... sales) {
                for (Sale sl : sales[0]) {
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

    public static void updateDeletedSales(final List<Sale> sales, final SaleAsynchDaoListener<Boolean> listener) {
        class MyAsynchTask extends AsyncTask<List<Sale>, String, Boolean> {
            @Override
            protected Boolean doInBackground(List<Sale>... sales) {
                for (Sale sl : sales[0]) {
                    MainAppLocalDb.db.saleDao().delete(sl);
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
