package com.example.eliavmenachi.myapplication.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Models.User.UserAsynchDao;
import com.example.eliavmenachi.myapplication.Models.User.UserAuthModel;
import com.example.eliavmenachi.myapplication.Models.User.UserAuthModelFirebase;
import com.example.eliavmenachi.myapplication.Models.User.UserModel;

public class UserViewModel extends ViewModel {
    LiveData<User> data;

//    public LiveData<User> getUserById(int id)
//    {
//        data = UserModel.instance.getUser(id);
//        return data;
//    }

    public LiveData<User> getUserByUserNamePassword(String username, String password) {
        data = UserModel.instance.getUser(username, password);
        return data;
    }

    public LiveData<User> getCurrentUser() {
        data = UserModel.instance.getCurrentUser();
        return data;
    }

    public void setUser(User user) {
        UserModel.instance.setUser(user);
    }

    public interface LogoutCompleteListener {
        public void onSuccess();
        public void onFailure();

    }

    public void logoutCurrentUser(final LogoutCompleteListener listener) {
        UserModel.instance.removeAllUsersLocally(new UserAsynchDao.UserAsynchDaoListener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                if (data)
                    listener.onSuccess();
                else
                    listener.onFailure();
            }
        });
    }

    public void signIn(final String userEmail, final String password, final UserAuthModelFirebase.SigninCallback callback)
    {
        UserAuthModel.instance.signIn(userEmail, password, new UserAuthModelFirebase.SigninCallback() {
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
                           final UserAuthModelFirebase.CreateUserCallback callback)
    {
        UserAuthModel.instance.createUser(userToAdd, new UserAuthModelFirebase.CreateUserCallback() {
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

    public void signOut()
    {
        UserAuthModel.instance.signOut();
    }
}
