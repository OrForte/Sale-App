package com.example.eliavmenachi.myapplication.Models.User;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.google.firebase.auth.FirebaseAuth;

public class UserAuthModel {

    public final static UserAuthModel instance = new UserAuthModel();
    private UserAuthModelFirebase userAuthModelFirebase;
    private User currentUser;

    private UserAuthModel() {
        userAuthModelFirebase = new UserAuthModelFirebase();
    }

    public User getCurrentUser(){
        return userAuthModelFirebase.getCurrentUser();
    }

    public void getUsers(String userID, UserAuthModelFirebase.GetAllUsersCallback callback) {
        userAuthModelFirebase.getUsers(userID, callback);
    }

    public void signIn(final String userEmail, final String password, final UserAuthModelFirebase.SigninCallback callback){
        userAuthModelFirebase.signInWithEmailAndPassword(userEmail, password, new UserAuthModelFirebase.SigninCallback() {
            @Override
            public void onSuccess(String userID, String userName) {
                callback.onSuccess(userID, userName);
            }

            @Override
            public void onFailed() {
                callback.onFailed();
            }
        });
    }

    public void createUser(User userToAdd,
                           final UserAuthModelFirebase.CreateUserCallback callback){

        userAuthModelFirebase.createUserWithEmailAndPassword(userToAdd, new UserAuthModelFirebase.CreateUserCallback() {
            @Override
            public void onSuccess(String userID, String userName) {
                callback.onSuccess(userID, userName);
            }

            @Override
            public void onFailed(String message) {
                callback.onFailed(message);
            }
        });
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }



    public LiveData<User> getCurrentUserNew()
    {
        UserDataNew user = new UserDataNew();
        return user;
    }

    class UserDataNew extends MutableLiveData<User> {
        @Override
        protected void onActive() {
            super.onActive();

            currentUser = UserAuthModel.instance.getCurrentUser();
            setValue(currentUser);

            if (currentUser != null) {
                UserAsynchDao.GetUserByUserId(currentUser.id, new UserAsynchDao.UserAsynchDaoListener<User>() {
                    @Override
                    public void onComplete(User data) {
                        setValue(data);
                        userAuthModelFirebase.getUserById(currentUser.id, new UserAuthModelFirebase.getUserByIdListener() {
                            @Override
                            public void onSuccess(User user) {
                                setValue(user);
                            }
                        });
                    }
                });
            }
        }

        @Override
        protected void onInactive() {
            super.onInactive();
        }
        public UserDataNew()
        {
            super();
            setValue(new User());
        }
    }

    UserDataNew userDataNew = new UserDataNew();
    public LiveData<User> getAllSales() { return userDataNew;}
}
