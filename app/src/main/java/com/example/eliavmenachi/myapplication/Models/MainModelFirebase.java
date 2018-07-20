package com.example.eliavmenachi.myapplication.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.CityMallStoreDetails;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Model.Model;
import com.example.eliavmenachi.myapplication.Model.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class MainModelFirebase {

    //region DataMembers

    String userName;
    String password;
    CityMallStoreDetails details = new CityMallStoreDetails();
    ListData listData = new ListData();

    //endregion

    //region users's methods

    public void addUser(User userToAdd)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        // TODO: ADD FUNCTION TO ADD REAL ADDITION OF USER
        mDatabase.child("users").child(userToAdd.userName).setValue(userToAdd);
    }

    public void IsUserVisible(final String p_strUserName, final String p_strPassword, final MainModel.IsUserVisibleListener listener)
    {
        userName = p_strUserName;
        password = p_strPassword;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(p_strUserName).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 boolean bIsValid = false;
                 User userData = dataSnapshot.getValue(User.class);

                 if (userData != null)
                 {
                     if (userData.userName != null &&
                             userData.password !=null &&
                             userData.userName.equals(userName) &&
                             userData.password.equals(password))
                     {
                         bIsValid = true;
                     }
                     else
                     {
                         bIsValid = false;
                     }
                 }

                 listener.onDone(bIsValid);
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {
                 listener.onDone(false);
             }
         });
    }

    //endregion

    //region sales's methods

    public void addPost(Sale p_postToSave)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("sale").child(p_postToSave.id).setValue(p_postToSave);
    }

    public void getPostsByStoreId(final String p_strStoreId, final MainModel.GetPostsByStoreIdListener listener)
    {
        // TODO : need to get the collections if posts by store id
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    interface GetAllSalesListener{
        public void onSuccess(List<Sale> studentslist);
    }

    ValueEventListener eventListener;

    public void getAllSales(final GetAllSalesListener listener)
    {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("sale");
        eventListener = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Sale> stSales = new LinkedList<>();
                for (DataSnapshot stSnapshot: dataSnapshot.getChildren()) {
                    Sale currSale = stSnapshot.getValue(Sale.class);
                    stSales.add(currSale );
                }

                listener.onSuccess(stSales);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void cancellGetAllSales() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("sale");
        stRef.removeEventListener(eventListener);
    }

    //endregion
}
