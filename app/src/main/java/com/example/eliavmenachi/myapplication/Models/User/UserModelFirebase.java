package com.example.eliavmenachi.myapplication.Models.User;

import android.support.annotation.NonNull;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class UserModelFirebase {
    String userName;
    String password;
    ValueEventListener eventListener;

    public void cancelGetUser() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("users");

        if (eventListener != null) {
            stRef.removeEventListener(eventListener);
        }
    }

    interface GetUserByIdListener {
        public void onSuccess(User user);
    }

    public void getUserById(String id, final GetUserByIdListener listener) {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("users");

        //eventListener = stRef.child(key).addChildEventListener(new
        eventListener = stRef.orderByChild("id").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = null;
                for (DataSnapshot stSnapshot : dataSnapshot.getChildren()) {
                    user = stSnapshot.getValue(User.class);
                }

                if (user != null) {
                    listener.onSuccess(user);
                } else {
                    listener.onSuccess(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    interface GetAllUsersListener  {
        public void onSuccess(List<User> user);
    }

    public void getAllUsers(final GetAllUsersListener listener) {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("users");

        eventListener = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new LinkedList<User>();
                for (DataSnapshot stSnapshot : dataSnapshot.getChildren()) {
                    User user = stSnapshot.getValue(User.class);
                    users.add(user);
                }
                listener.onSuccess(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    interface AddUserListener {
        public void onSuccess(User user);
        public void onFailure(String exceptionMessage);
    }

    public void addUser(final User user, final AddUserListener listener) {
        DatabaseReference mUsersRef = FirebaseDatabase.getInstance().getReference("users");

        mUsersRef.child(user.id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess(user);
                } else {
                    String exception = task.getException().getMessage();
                    listener.onFailure("Adding user to firebase DB has failed: " + exception);
                }
            }
        });
    }

    interface SetUserListener {
        public void onSuccess();

        public void onFailure(String exceptionMessage);
    }

    public void setUser(User user, final SetUserListener listener) {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference();
        stRef.child("users").child(user.id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e.getMessage());
            }
        });
    }


}
