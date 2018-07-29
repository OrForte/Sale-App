package com.example.eliavmenachi.myapplication.Models.User;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.example.eliavmenachi.myapplication.Entities.User;


public class UserAuthModelFirebase {


    public interface GetAllUsersCallback {
        void onCompleted(List<User> users);
        void onCanceled();
    }
    public void getUsers(final String userID, final GetAllUsersCallback callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef1 = database.getReference("users");
        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> list = new LinkedList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    User user = snap.getValue(User.class);
                    user.id =snap.getKey();
                    if (!user.id.equals(userID)){
                        list.add(user);
                    }
                }

                callback.onCompleted(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onCanceled();
            }
        };

        myRef1.addListenerForSingleValueEvent(listener);
    }

    public User getCurrentUser(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = null;

        if (firebaseUser != null){
            user = new User();
            user.id = firebaseUser.getUid();
            user.username = firebaseUser.getDisplayName();
            user.email = firebaseUser.getEmail();
        }

        return user;
    }

    public interface SigninCallback {
        void onSuccess(String userID, String userName);
        void onFailed();
    }
    public void signInWithEmailAndPassword(String userEmail, String password, final SigninCallback callback) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (!task.isSuccessful())
                        {
                            callback.onFailed();
                        }
                        else
                            {
                            FirebaseUser user = task.getResult().getUser();
                            callback.onSuccess(user.getUid(), user.getDisplayName());
                        }
                    }
                });
    }

    public interface CreateUserCallback {
        void onSuccess(String userID, String userName);
        void onFailed(String message);
    }
    public void createUserWithEmailAndPassword(final User userToAdd,
                                               final CreateUserCallback callback) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userToAdd.email, userToAdd.password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    String exception = task.getException().getMessage();
                    callback.onFailed("Registretion failed");
                } else {

                    FirebaseUser firebaseUser = task.getResult().getUser();
                    updateUserProfile(firebaseUser, userToAdd.username, callback);
                }
            }
        });
    }
    private void updateUserProfile(final FirebaseUser firebaseUser,
                                   final String userName,
                                   final CreateUserCallback callback) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build();

        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()){
                    callback.onFailed("failed to update user profile");
                } else {

                    User user = new User();
                    user.id = firebaseUser.getUid();
                    user.email = firebaseUser.getEmail();
                    user.username = userName;

                    addUser(user, callback);
                }
            }
        });
    }

    private void addUser(final User user, final CreateUserCallback callback){
        DatabaseReference mUsersRef = FirebaseDatabase.getInstance().getReference("users");

        mUsersRef.child(user.username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    callback.onSuccess(user.id, user.username);
                } else {
                    callback.onFailed("faild on adding user to firebase DB");
                }
            }
        });
    }
}
