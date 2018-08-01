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
//
//    interface GetUserByUsernamePasswordListener {
//        public void onSuccess(User user);
//
//        public void onFailure();
//    }
//
//    public void getUserByUsernamePassword(String username, final String password, final GetUserByUsernamePasswordListener listener) {
//        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("users");
//
//        //eventListener = stRef.child(key).addChildEventListener(new
//        eventListener = stRef.orderByChild("username").equalTo(username).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                User user = null;
//                for (DataSnapshot stSnapshot : dataSnapshot.getChildren()) {
//                    user = stSnapshot.getValue(User.class);
//                }
//
//                if (user != null && user.password.equals(password)) {
//                    listener.onSuccess(user);
//                } else {
//                    listener.onFailure();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//    }

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

//    public void setUser(User user) {
//        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference();
//        stRef.child("users").child(user.id).setValue(user);
//    }
//
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

//    public interface IsUserExistsListener {
//        void onDone(boolean p_bIsExists);
//    }
//
//    public void IsUserExists(final String p_strUserName, final String p_strPassword, final IsUserExistsListener listener) {
//        userName = p_strUserName;
//        password = p_strPassword;
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("users").child(p_strUserName).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                boolean bIsValid = false;
//                User userData = dataSnapshot.getValue(User.class);
//
//                if (userData != null) {
//                    if (userData.username != null &&
//                            userData.password != null &&
//                            userData.username.equals(userName) &&
//                            userData.password.equals(password)) {
//                        bIsValid = true;
//                    } else {
//                        bIsValid = false;
//                    }
//                }
//
//                listener.onDone(bIsValid);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                listener.onDone(false);
//            }
//        });
//    }

//    public void createUser(String email, String pwd) {
//        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference();
//        FirebaseDatabase.getInstance().au
//        myFirebaseRef.cstRefreateUser(email, pwd, new Firebase.ResultHandler() {   @Override   public void onSuccess() { ...   }
//            @Override   public void onError(FirebaseError firebaseError) { ...   } });
//    }
}
