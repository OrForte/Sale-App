package com.example.eliavmenachi.myapplication.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Model.Model;
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
import java.util.Date;

public class MainModelFirebase {

    public void addUser(User userToAdd)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(userToAdd.userName).setValue(userToAdd);
    }

    public void IsUserVisible(final String p_strUserName, final String p_strPassword, final MainModel.IsUserVisibleListener listener)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(p_strUserName).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 User userData = dataSnapshot.getValue(User.class);

                 listener.onDone(true);
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {
                int a = 1;
             }
         });
    }

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
}
