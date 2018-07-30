package com.example.eliavmenachi.myapplication.Models.User;

import android.os.AsyncTask;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Models.MainAppLocalDb;

import java.util.List;

public class UserAsynchDao {

    public interface UserAsynchDaoListener<T> {
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
                List<User> users =  MainAppLocalDb.db.userDao().getUsers();
                if (users.size() == 0)
                    return null;

                User user = users.get(0);
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

    static public void GetUserByUserId(final String userId,final UserAsynchDaoListener<User> listener)
    {
        class MyAsynchTask extends AsyncTask<String, String, User> {
            @Override
            protected User doInBackground(String... usersnames) {
                User user =  MainAppLocalDb.db.userDao().getUserByUserId(userId);
                return user;
            }

            @Override
            protected void onPostExecute(User user) {
            }
        }

        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }

    static public void insert(final User users, final UserAsynchDaoListener<Boolean> listener) {
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

    static public void removeAll(final UserAsynchDaoListener<Boolean> listener) {
        class MyAsynchTask extends AsyncTask<User, String, Boolean> {
            @Override
            protected Boolean doInBackground(User... users) {
                int a = MainAppLocalDb.db.userDao().deleteAll();
                List<User> userslist = MainAppLocalDb.db.userDao().getUsers();
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
