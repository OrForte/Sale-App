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

    //region sales's methods

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


    interface GetAllSalesByStoreListener{
        public void onSuccess(List<Sale> results);
    }

    ValueEventListener eventListener2;

    public void GetAllSalesByStoreId(String storeId,final GetAllSalesByStoreListener listener)
    {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("sale");
        eventListener2 = stRef.addValueEventListener(new ValueEventListener() {
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

    public void cancellGetAllSalesByStoreId() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("sale");
        stRef.removeEventListener(eventListener);
    }

    //endregion
}
