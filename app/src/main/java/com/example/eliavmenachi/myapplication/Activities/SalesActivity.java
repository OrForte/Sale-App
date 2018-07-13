package com.example.eliavmenachi.myapplication.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.eliavmenachi.myapplication.Fragments.SalesListFragment;
import com.example.eliavmenachi.myapplication.R;

public class SalesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SalesActivity", "onCreate");
        super.onCreate(savedInstanceState);

        //super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        if (savedInstanceState == null) {
            SalesListFragment fragment = new SalesListFragment();
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.add(R.id.main_container, fragment);
            tran.addToBackStack("");
            tran.commit();
        }
    }
}