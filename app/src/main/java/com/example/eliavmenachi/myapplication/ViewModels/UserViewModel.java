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

    public void signIn(final String userEmail, final String password ,final UserAuthModelFirebase.SigninCallback callback)
    {
        UserAuthModel.instance.signIn(userEmail, password, new UserAuthModelFirebase.SigninCallback() {
            @Override
            public void onSuccess(final String userID, final String userName) {
                /*
                UserAsynchDao.GetUserByUserId(userID, new UserAsynchDao.UserAsynchDaoListener<User>() {
                    @Override
                    public void onComplete(User data) {
                        if (data != null){
                            callback.onSuccess(userID, userName);
                        }
                        else
                        {
                            InsertToFileBase(userID, userEmail ,userName, callback);
                        }
                    }
                });*/

                callback.onSuccess(userID, userName);
            }

            @Override
            public void onFailed() {
                callback.onFailed();
            }
        });
    }

    public void createUser(final User userToAdd,
                           final UserAuthModelFirebase.CreateUserCallback callback)
    {
        UserAuthModel.instance.createUser(userToAdd, new UserAuthModelFirebase.CreateUserCallback() {
            @Override
            public void onSuccess(final String userID,final  String userName) {
                InsertToFileBase(userID, userName, userToAdd, callback);
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

    public void InsertToFileBase(final String userID, final String userName, User userToAdd, final UserAuthModelFirebase.CreateUserCallback callback)
    {
        User userToSqlite = new User();
        userToSqlite.id = userID;
        userToSqlite.username = userName;
        userToSqlite.city = userToAdd.city;
        userToSqlite.birthDate = userToAdd.birthDate;
        userToSqlite.lastName = userToAdd.lastName;
        userToSqlite.firstName = userToAdd.firstName;
        userToSqlite.email = userToAdd.email;

        UserAsynchDao.insert(userToSqlite, new UserAsynchDao.UserAsynchDaoListener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                callback.onSuccess(userID, userName);
            }
        });
    }

    public void InsertToFileBase(final String userID, final String userEmail ,final String userName, final UserAuthModelFirebase.SigninCallback callback)
    {
        User userToSqlite = new User();
        userToSqlite.id = userID;
        userToSqlite.username = userName;
        userToSqlite.email = userEmail;

        UserAsynchDao.insert(userToSqlite, new UserAsynchDao.UserAsynchDaoListener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                callback.onSuccess(userID, userName);
            }
        });
    }
}
