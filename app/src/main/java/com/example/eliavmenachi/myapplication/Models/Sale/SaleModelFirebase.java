package com.example.eliavmenachi.myapplication.Models.Sale;

import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SaleModelFirebase {

    public void getPostsByStoreId(final String p_strStoreId, final SaleModel.GetPostsByStoreIdListener listener)
    {
        // TODO : need to get the collections if posts by store id
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void addPost(Sale p_postToSave)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("sale").child(p_postToSave.id).setValue(p_postToSave);
    }
}
