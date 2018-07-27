package com.example.eliavmenachi.myapplication.Models.Sale;

import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Models.SaleAsyncDao;

import java.util.List;

public class SaleModel {
    //region Data Members

    public static SaleModel instance = new SaleModel();
    SaleModelFirebase saleModelFirebase;

    //endregion

    //region C'Tors

    private SaleModel()
    {
        saleModelFirebase = new SaleModelFirebase();
    }

    // endregion

    //region Methods
    public interface GetPostsByStoreIdListener{
        void onGetPosts(List<Sale> p_postToReturn);
    }
    public void GetPostsByStoreId(final String storeId, final GetPostsByStoreIdListener listener)
    {
        saleModelFirebase.getPostsByStoreId(storeId, new GetPostsByStoreIdListener() {
            @Override
            public void onGetPosts(List<Sale> p_postToReturn) {
                // TODO: need to return the posts to the fragments of posts
                //listener.onGetPosts(p_postToReturn);
            }
        });
    }


    public void addPost(Sale p_postToSave)
    {
        saleModelFirebase.addPost(p_postToSave);
    }

    public interface  deleteLogicSaleListener{
        void onDeleteLogicSale(boolean b_isDelete);
    }
    public void deleteLogicSale(final Sale p_saleToDelete, final deleteLogicSaleListener listener)
    {
        saleModelFirebase.deleteLogicSale(p_saleToDelete, new deleteLogicSaleListener() {
            @Override
            public void onDeleteLogicSale(boolean b_isDelete) {
                SaleAsyncDao.deleteSale(p_saleToDelete);
                listener.onDeleteLogicSale(b_isDelete);
            }
        });
    }

    public interface GetNextSequenceListener{
        void onGetNextSeq(String p_next);
    }
    public void GetNextSequenceSale(final String SeqName, final GetNextSequenceListener listener)
    {
        saleModelFirebase.GetNextSequenceSale(SeqName, new GetNextSequenceListener() {
            @Override
            public void onGetNextSeq(String p_next) {
                listener.onGetNextSeq(p_next);
            }
        });
    }

    //endregion
}
