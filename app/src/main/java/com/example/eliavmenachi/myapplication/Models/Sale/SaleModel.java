package com.example.eliavmenachi.myapplication.Models.Sale;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.eliavmenachi.myapplication.Entities.Sale;

import java.util.LinkedList;
import java.util.List;

public class SaleModel {
    //region Data Members

    public static SaleModel instance = new SaleModel();
    SaleModelFirebase saleModelFirebase;

    //endregion

    //region C'Tors

    private SaleModel() {
        saleModelFirebase = new SaleModelFirebase();
    }

    // endregion

    //region Methods

    // region Regular Methods

    public interface GetPostsByStoreIdListener {
        void onGetPosts(List<Sale> p_postToReturn);
    }
    public void GetPostsByStoreId(final String storeId, final GetPostsByStoreIdListener listener) {
        saleModelFirebase.getPostsByStoreId(storeId, new GetPostsByStoreIdListener() {
            @Override
            public void onGetPosts(List<Sale> p_postToReturn) {
                // TODO: need to return the posts to the fragments of posts
                //listener.onGetPosts(p_postToReturn);
            }
        });
    }


    public void addPost(Sale p_postToSave) {
        saleModelFirebase.addPost(p_postToSave);
    }


    public interface addOrUpdateNewSaleListener{
        void onAddOrUpdateNewSaleResults(Sale SaleToReturn);
    }
    public void addOrUpdateNewSale(final Sale p_saleToAdd, final addOrUpdateNewSaleListener listener)
    {
        saleModelFirebase.addOrUpdateNewSale(p_saleToAdd, new addOrUpdateNewSaleListener() {
            @Override
            public void onAddOrUpdateNewSaleResults(Sale SaleToReturn) {
                listener.onAddOrUpdateNewSaleResults(p_saleToAdd);
            }
        });
    }

    public interface deleteLogicSaleListener {
        void onDeleteLogicSale(boolean b_isDelete);
    }
    public void deleteLogicSale(final Sale p_saleToDelete, final deleteLogicSaleListener listener) {
        saleModelFirebase.deleteLogicSale(p_saleToDelete, new deleteLogicSaleListener() {
            @Override
            public void onDeleteLogicSale(boolean b_isDelete) {
                listener.onDeleteLogicSale(b_isDelete);
            }
        });
    }

    public interface GetNextSequenceListener {
        void onGetNextSeq(String p_next);
    }
    public void GetNextSequenceSale(final String SeqName, final GetNextSequenceListener listener) {
        saleModelFirebase.GetNextSequenceSale(SeqName, new GetNextSequenceListener() {
            @Override
            public void onGetNextSeq(String p_next) {
                listener.onGetNextSeq(p_next);
            }
        });
    }

    //endregion

    // region Live Data Methods

    //region SaleListData

    public class SaleListData extends MutableLiveData<List<Sale>> {
        @Override
        protected void onActive() {
            super.onActive();

            // new thread tsks
            // 1. get the students list from the local DB
            SalesAsyncDao.getAll(new SalesAsyncDao.SaleAsynchDaoListener<List<Sale>>() {
                @Override
                public void onComplete(final List<Sale> data) {
                    // 2. update the live data with the new student list
                    setValue(data);

                    // 3. get the student list from firebase
                    saleModelFirebase.getAllSales(new SaleModelFirebase.GetAllSalesListener() {
                        @Override
                        public void onSuccess(final     List<Sale> salesList) {
                            // 4. update the live data with the new student list
                            setValue(salesList);

                            List<Sale> lstToDelete = new LinkedList<Sale>();

                            lstToDelete = GetSaleThatInSqlLiteButNotInFireBase(salesList, data);

                            if (lstToDelete.size() > 0) {
                                // try to delete sales that deleted from local storage
                        SalesAsyncDao.updateDeletedSales(lstToDelete, new SalesAsyncDao.SaleAsynchDaoListener<Boolean>() {
                            @Override
                            public void onComplete(Boolean data) {
                                // 5. update the local DB
                                SalesAsyncDao.insertAll(salesList, new SalesAsyncDao.SaleAsynchDaoListener<Boolean>() {
                                    @Override
                                    public void onComplete(Boolean data) {
                                        // Done
                                    }
                                });
                            }
                        });
                    }

                    // 5. update the local DB
                    SalesAsyncDao.insertAll(salesList, new SalesAsyncDao.SaleAsynchDaoListener<Boolean>() {
                        @Override
                        public void onComplete(Boolean data) {
                            // Done
                        }
                    });
                }
            });
        }
    });
}

    public List<Sale> GetSaleThatInSqlLiteButNotInFireBase(List<Sale> dataFireBase, List<Sale> dataSqlLite)
    {
        List<Sale> returnData = new LinkedList<Sale>();

        boolean bIsIn = false;
        for(Sale saleSqlLite : dataSqlLite)
        {
            bIsIn = false;
            for(Sale saleFireBase : dataFireBase)
            {
                if (saleSqlLite.id.contains(saleFireBase.id))
                {
                    bIsIn = true;
                        break;
                    }
                }
                if (bIsIn == false)
                {
                    returnData.add(saleSqlLite);
                }
            }

            return returnData;
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            saleModelFirebase.cancellGetAllSales();
        }

        public SaleListData() {
            super();
            setValue(new LinkedList<Sale>());
        }
    }

    SaleListData studentListData = new SaleListData();

    public LiveData<List<Sale>> getAllSales() {
        return studentListData;
    }




    //endregion

    //region SaleListDataByStore

    public class SaleListDataByStore extends MutableLiveData<List<Sale>> {
        String m_storeId = "";

        @Override
        protected void onActive() {
            super.onActive();

            // 1. get the students list from the local DB
            SalesAsyncDao.getSalesByStoreId(m_storeId, new SalesAsyncDao.SaleAsynchDaoListener<List<Sale>>() {
                @Override
                public void onComplete(List<Sale> data) {
                    // 2. update the live data with the new student list
                    setValue(data);
                    // 3. get the sale list from firebase
                    saleModelFirebase.GetAllSalesByStoreId(m_storeId, new SaleModelFirebase.GetAllSalesByStoreListener() {
                        @Override
                        public void onSuccess(List<Sale> results) {
                            // 4. update the live data with the new student list
                            setValue(results);
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
        }

        public SaleListDataByStore() {
            super();
            setValue(new LinkedList());
        }

        public void InitStoreId(String p_strStoreId) {
            m_storeId = p_strStoreId;
        }
    }


    public SaleListDataByStore saleListDataByStore = new SaleListDataByStore();

    public LiveData<List<Sale>> getAllSalesByStoreId(String p_strStoreId) {
        return saleListDataByStore;
    }

    public void InitStoreId(String p_strStoreId) {
        if (saleListDataByStore == null) {
            saleListDataByStore = new SaleListDataByStore();
        }

        saleListDataByStore.InitStoreId(p_strStoreId);
    }

    // endregion

    //region SalesByUserId
    public class SaleListDataByUserId extends MutableLiveData<List<Sale>> {
        String m_userName = "";

        @Override
        protected void onActive() {

            SalesAsyncDao.getSalesByUserName(m_userName, new SalesAsyncDao.SaleAsynchDaoListener<List<Sale>>() {
                @Override
                public void onComplete(List<Sale> data) {
                    setValue(data);
                    // 3. get the sale list from firebase
                    saleModelFirebase.GetSaleByUserName(m_userName, new SaleModelFirebase.GetSaleByUserName() {
                        @Override
                        public void onGetData(List<Sale> data) {
                            setValue(data);
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
        }

        public SaleListDataByUserId() {
            super();
            setValue(new LinkedList());
        }

        public void InitUserName(String p_strUserName) {
            m_userName = p_strUserName;
        }
    }

    public SaleListDataByUserId saleListDataByUserId = new SaleListDataByUserId();

    public LiveData<List<Sale>> getSalesByUserName(String p_strUserName) {
        return saleListDataByUserId;
    }

    public void InitUserName(String p_strUserName) {
        if (saleListDataByUserId == null) {
            saleListDataByUserId = new SaleListDataByUserId();
        }

        saleListDataByUserId.InitUserName(p_strUserName);
    }

    //endregion

    // region GetSaleBySaleId

    public class SaleBySaleIdData extends MutableLiveData<Sale> {
        String m_saleId = "";

        @Override
        protected void onActive() {
            super.onActive();

            SalesAsyncDao.getSaleBySaleId(m_saleId, new SalesAsyncDao.SaleAsynchDaoListener2<List<Sale>>() {
                @Override
                public void onCompleteOneSale(Sale data) {
                    setValue(data);

                    saleModelFirebase.GetSaleBySaleId(m_saleId, new SaleModelFirebase.GetSaleBySaleId() {
                        @Override
                        public void onGetData(Sale data) {
                            setValue(data);
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
        }

        public SaleBySaleIdData() {
            super();
            setValue(new Sale());
        }

        public void InitSaleId(String p_saleId) {
            m_saleId = p_saleId;
        }
    }

    public SaleBySaleIdData saleBySaleIdData = new SaleBySaleIdData();

    public LiveData<Sale> getSaleBySaleId(String p_strSaleId) {
        return saleBySaleIdData;
    }

    public void InitSaleId(String p_SaleId) {
        if (saleBySaleIdData == null) {
            saleBySaleIdData = new SaleBySaleIdData();
        }

        saleBySaleIdData.InitSaleId(p_SaleId);
    }

    //endregion

    // endregion

    //endregion
}
