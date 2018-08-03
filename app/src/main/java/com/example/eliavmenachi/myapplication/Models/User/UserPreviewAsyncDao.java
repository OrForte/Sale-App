package com.example.eliavmenachi.myapplication.Models.User;

import android.os.AsyncTask;

import com.example.eliavmenachi.myapplication.Entities.UserPreview;
import com.example.eliavmenachi.myapplication.Models.MainAppLocalDb;

import java.util.List;

public class UserPreviewAsyncDao {
    public interface UserAsynchDaoListener<T> {
        void onComplete(T data);
    }

    static public void GetUserPreviewByUserId(final String userId, final UserAsynchDaoListener<UserPreview> listener) {
        class MyAsynchTask extends AsyncTask<String, String, UserPreview> {
            @Override
            protected UserPreview doInBackground(String... usersnames) {
                UserPreview user = MainAppLocalDb.db.userPreviewDao().getUserPreviewByUserId(userId);
                return user;
            }

            @Override
            protected void onPostExecute(UserPreview user) {
                super.onPostExecute(user);
                listener.onComplete(user);
            }
        }

        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }

    static public void GetAllPreviewUsers(final UserAsynchDaoListener<List<UserPreview>> listener) {
        class MyAsynchTask extends AsyncTask<String, String, List<UserPreview>> {
            @Override
            protected List<UserPreview> doInBackground(String... usersnames) {
                List<UserPreview> user = MainAppLocalDb.db.userPreviewDao().getUsersPreview();
                return user;
            }

            @Override
            protected void onPostExecute(List<UserPreview> user) {
                super.onPostExecute(user);
                listener.onComplete(user);
            }
        }

        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }

    static public void insert(final UserPreview users, final UserAsynchDaoListener<Boolean> listener) {
        class MyAsynchTask extends AsyncTask<UserPreview, String, Boolean> {
            @Override
            protected Boolean doInBackground(UserPreview... users) {
                MainAppLocalDb.db.userPreviewDao().insertUsersPreview(users);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }

        MyAsynchTask task = new MyAsynchTask();
        task.execute(users);
    }

    public static void insertAll(final List<UserPreview> sales, final UserAsynchDaoListener<Boolean> listener) {
        class MyAsynchTask extends AsyncTask<List<UserPreview>, String, Boolean> {
            @Override
            protected Boolean doInBackground(List<UserPreview>... data) {
                for (UserPreview user : data[0]) {
                    MainAppLocalDb.db.userPreviewDao().insertUsersPreview(user);
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute(sales);
    }
    public static void updateDeletedSales(final List<UserPreview> sales, final UserAsynchDaoListener<Boolean> listener) {
        class MyAsynchTask extends AsyncTask<List<UserPreview>, String, Boolean> {
            @Override
            protected Boolean doInBackground(List<UserPreview>... data) {
                for (UserPreview sl : data[0]) {
                    MainAppLocalDb.db.userPreviewDao().delete(sl);
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute(sales);
    }
}
