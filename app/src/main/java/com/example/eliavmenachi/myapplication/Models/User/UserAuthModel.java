package com.example.eliavmenachi.myapplication.Models.User;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.google.firebase.auth.FirebaseAuth;

public class UserAuthModel {

    public final static UserAuthModel instance = new UserAuthModel();
    private UserAuthModelFirebase userAuthModelFirebase;

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

                // TODO: adding the local save to DB
                /*?
                if (!userDbHelper.findUser(userID)){
                    userDbHelper.addUser(new User(userID, userName, userEmail, password));
                }*/

                callback.onSuccess(userID, userName);
            }

            @Override
            public void onFailed() {
                callback.onFailed();
            }
        });
    }

    public void createUser(final String userName,
                           final String email,
                           final String password,
                           final UserAuthModelFirebase.CreateUserCallback callback){

        userAuthModelFirebase.createUserWithEmailAndPassword(userName, email, password, new UserAuthModelFirebase.CreateUserCallback() {
            @Override
            public void onSuccess(String userID, String userName) {
                //userDbHelper.addUser(new User(userID, userName, email, password));
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
}
