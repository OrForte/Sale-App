package com.example.eliavmenachi.myapplication.Models.Sale;

import com.example.eliavmenachi.myapplication.Entities.Sale;

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


    //endregion
}
