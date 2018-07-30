package com.example.eliavmenachi.myapplication.Models.User;

import android.support.annotation.NonNull;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    interface getUserByUsernamePasswordListener {
        public void onSuccess(User user);

        public void onFailure();
    }

    public void getUserByUsernamePassword(String username, final String password, final getUserByUsernamePasswordListener listener) {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("users");

        //eventListener = stRef.child(key).addChildEventListener(new
        eventListener = stRef.orderByChild("username").equalTo(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = null;
                for (DataSnapshot stSnapshot : dataSnapshot.getChildren()) {
                    user = stSnapshot.getValue(User.class);
                }

                if (user != null && user.password.equals(password)) {
                    listener.onSuccess(user);
                } else {
                    listener.onFailure();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    interface getUserByIdListener {
        public void onSuccess(User user);
    }

    public void getUserById(String id, final getUserByIdListener listener) {
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

    // TODO: MAKE IT ASYNC
    public void addUser(User userToAdd) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        // TODO: ADD FUNCTION TO ADD REAL ADDITION OF USER
        mDatabase.child("users").child(userToAdd.username).setValue(userToAdd);
    }

    public void setUser(User user) {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference();
        stRef.child("users").child(user.id).setValue(user);
    }

    interface setUserListener {
        public void onSuccess();

        public void onFailure(Exception e);
    }

    public void setUser(User user, final setUserListener listener) {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference();
        stRef.child("users").child(user.id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e);
            }
        });
    }

    public interface IsUserExistsListener {
        void onDone(boolean p_bIsExists);
    }

    public void IsUserExists(final String p_strUserName, final String p_strPassword, final IsUserExistsListener listener) {
        userName = p_strUserName;
        password = p_strPassword;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(p_strUserName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean bIsValid = false;
                User userData = dataSnapshot.getValue(User.class);

                if (userData != null) {
                    if (userData.username != null &&
                            userData.password != null &&
                            userData.username.equals(userName) &&
                            userData.password.equals(password)) {
                        bIsValid = true;
                    } else {
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

//    public void createUser(String email, String pwd) {
//        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference();
//        FirebaseDatabase.getInstance().au
//        myFirebaseRef.cstRefreateUser(email, pwd, new Firebase.ResultHandler() {   @Override   public void onSuccess() { ...   }
//            @Override   public void onError(FirebaseError firebaseError) { ...   } });
//    }


}
