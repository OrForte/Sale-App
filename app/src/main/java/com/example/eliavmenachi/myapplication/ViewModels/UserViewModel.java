package com.example.eliavmenachi.myapplication.ViewModels;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Entities.UserPreview;
import com.example.eliavmenachi.myapplication.Models.User.UserModel;

import java.util.List;

public class UserViewModel extends ViewModel {
    LiveData<User> data;

    public LiveData<User> getCurrentUser() {
        data = UserModel.instance.getCurrentUser();
        return data;
    }

    public LiveData<User> getUserByUserId(String p_strUserId)
    {
        UserModel.instance.InitUserId(p_strUserId);
        data = UserModel.instance.getUserByUserId(p_strUserId);
        return data;
    }

    public LiveData<List<UserPreview>> getAllUsersPreview()
    {
        LiveData<List<UserPreview>> results = UserModel.instance.getUsersPreviewData();
        return results;
    }

    public LiveData<UserPreview> getUsersPreviewByUserId(String p_strUserId)
    {
        UserModel.instance.InitUserPreviewId(p_strUserId);
        LiveData<UserPreview> results = UserModel.instance.getUserPreviewByUserId(p_strUserId);
        return results;
    }

    public interface SignInListener {
        void onSuccess();

        void onFailure(String exceptionMessage);
    }

    public void signIn(final String email, final String password, final SignInListener listener) {
        UserModel.instance.signIn(email, password, new UserModel.SignInListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onFailure(String exceptionMessage) {
                listener.onFailure(exceptionMessage);
            }
        });
    }

    public interface CreateUserListener {
        void onSuccess(User user);

        void onFailure(String exceptionMessage);
    }

    public void registerUser(final User user,
                             final String email,
                             final String password,
                             final CreateUserListener listener) {
        UserModel.instance.createUser(user, email, password, new UserModel.CreateUserListener() {
            @Override
            public void onSuccess(User user) {
                listener.onSuccess(user);
            }

            @Override
            public void onFailure(String exceptionMessage) {
                listener.onFailure(exceptionMessage);
            }
        });
    }

    public interface SignOutListener {
        void onSuccess();

        void onFailure(String exceptionMessage);
    }

    public void signOut(final SignOutListener listener) {
        UserModel.instance.signOut(new UserModel.SignOutListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onFailure(String exceptionMessage) {
                listener.onFailure(exceptionMessage);
            }
        });
    }

    public interface UpdateUserListener {
        void onSuccess();

        void onFailure(String exceptionMessage);
    }

    public void updateUser(User user, final UpdateUserListener listener) {
        UserModel.instance.updateUser(user, new UserModel.UpdateUserListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onFailure(String exceptionMessage) {
                listener.onFailure(exceptionMessage);
            }
        });
    }

}
