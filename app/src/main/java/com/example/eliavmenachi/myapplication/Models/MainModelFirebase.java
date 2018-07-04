package com.example.eliavmenachi.myapplication.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.eliavmenachi.myapplication.Entities.City;
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

    //region cities, malls and store methods

    public void GetMallsByCityId(final String p_cityId, final MainModel.GetMallsByCityIdListener listener)
    {
        // TODO : need to get the collections of malls by city id
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("mall").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                List<Mall> lstMallsToReturn = new ArrayList<>();
                for (DataSnapshot curr : data)
                {
                    Mall currMall = curr.getValue(Mall.class);
                    if (currMall.cityId == Integer.parseInt(p_cityId))
                    {
                        lstMallsToReturn.add(currMall);
                    }
                }

                listener.onGetMallsByCityIdResults(lstMallsToReturn);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void GetStoresByMallId(final String mallId, final MainModel.GetStoreByMallIdListener listener)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("store").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                List<Store> lstStoresToReturn = new ArrayList<>();

                for (DataSnapshot curr : data)
                {
                    Store currStore = curr.getValue(Store.class);
                    if (currStore.mallId == Integer.parseInt(mallId))
                    {
                        lstStoresToReturn.add(currStore);
                    }
                }

                listener.onGetStoresByMallIdResults(lstStoresToReturn);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void GetCities(final MainModel.GetCitiesListener listener)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("city").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                List<City> lstCities = new ArrayList<>();

                for (DataSnapshot curr : data)
                {
                    lstCities.add(curr.getValue(City.class));
                }

                listener.onGetCitiesResults(lstCities);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void GetStoreByStoreId(final int storeId, final MainModel.GetStoreByStoreIdListener listener)
    {
        String storeIdString = storeId + "";
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("store").child(storeIdString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Store storeData = dataSnapshot.getValue(Store.class);
                listener.onGetStoreByStoreIdResults(storeData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void GetMallByStoreId(final int storeId, final MainModel.GetMallByStoreIdListener listener)
    {

    }

    public void GetCityByMallId(final int mallId, final MainModel.GetCityByMallIdListener listener)
    {

    }

    //endregion

    //region sales's methods

    public void addPost(Sale p_postToSave)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("sales").child(p_postToSave.id).setValue(p_postToSave);
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
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("sales");
        eventListener = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Sale> stSales = new LinkedList<>();
                for (DataSnapshot stSnapshot: dataSnapshot.getChildren()) {
                    stSales.add(stSnapshot.getValue(Sale.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void cancellGetAllSales() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("sales");
        stRef.removeEventListener(eventListener);
    }

    //endregion

    //region images's methods

    public void saveImage(Bitmap imageBitmap, final Model.SaveImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        Date d = new Date();
        String name = ""+ d.getTime();
        StorageReference imagesRef = storage.getReference().child("images").child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onDone(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.onDone(downloadUrl.toString());
            }
        });

    }

    public void getImage(String url, final Model.GetImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3* ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                Log.d("TAG","get image from firebase success");
                listener.onDone(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.d("TAG",exception.getMessage());
                Log.d("TAG","get image from firebase Failed");
                listener.onDone(null);
            }
        });
    }

    //endregion
}
