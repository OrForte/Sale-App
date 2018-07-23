package com.example.eliavmenachi.myapplication.Model;

import android.os.AsyncTask;

import java.util.List;

public class StudentAsynchDao {

    interface StudentAsynchDaoListener<T> {
        void onComplete(T data);
    }

    static public void getAll(final StudentAsynchDaoListener<List<Student>> listener) {
        class MyAsynchTask extends AsyncTask<String,String,List<Student>> {
            @Override
            protected List<Student> doInBackground(String... strings) {
                List<Student> stList = AppLocalDb.db.studentDao().getAll();
                return stList;
            }

            @Override
            protected void onPostExecute(List<Student> students) {
                super.onPostExecute(students);
                listener.onComplete(students);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }

    static void insertAll(final List<Student> students, final StudentAsynchDaoListener<Boolean> listener){
        class MyAsynchTask extends AsyncTask<List<Student>,String,Boolean>{
            @Override
            protected Boolean doInBackground(List<Student>... students) {
                for (Student st:students[0]) {
                    AppLocalDb.db.studentDao().insertAll(st);
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
        task.execute(students);
    }

}
