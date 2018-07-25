package com.example.eliavmenachi.myapplication.Models.User;

import android.os.AsyncTask;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Models.MainAppLocalDb;

import java.util.List;

public class UserAsynchDao {

    interface UserAsynchDaoListener<T> {
        void onComplete(T data);
    }

    static public void getData(String username, final UserAsynchDaoListener<User> listener) {
        class MyAsynchTask extends AsyncTask<String, String, User> {
            @Override
            protected User doInBackground(String... usersnames) {
                if (usersnames.length == 0)
                    return null;

                User user = MainAppLocalDb.db.userDao().getUserByUsername(usersnames[0]);
                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                listener.onComplete(user);
            }
        }

        MyAsynchTask task = new MyAsynchTask();
        task.execute(username);
    }

    static public void getCurrentUser(final UserAsynchDaoListener<User> listener) {
        class MyAsynchTask extends AsyncTask<String, String, User> {
            @Override
            protected User doInBackground(String... usersnames) {
                User user = MainAppLocalDb.db.userDao().getUsers().get(0);
                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                listener.onComplete(user);
            }
        }

        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }

    static void insert(final User users, final UserAsynchDaoListener<Boolean> listener) {
        class MyAsynchTask extends AsyncTask<User, String, Boolean> {
            @Override
            protected Boolean doInBackground(User... users) {
                MainAppLocalDb.db.userDao().insertUsers(users);
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

    static void removeAll(final UserAsynchDaoListener<Boolean> listener) {
        class MyAsynchTask extends AsyncTask<User, String, Boolean> {
            @Override
            protected Boolean doInBackground(User... users) {
                MainAppLocalDb.db.userDao().deleteAll();
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }

        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }
}
