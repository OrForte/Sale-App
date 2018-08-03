package com.example.eliavmenachi.myapplication.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.eliavmenachi.myapplication.Fragments.UserSalesListFragment;
import com.example.eliavmenachi.myapplication.R;

public class UserSalesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("UserSalesActivity", "onCreate");
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.ic_shopping_basket_black_24dp);

        setContentView(R.layout.activity_user_sales);

        if (savedInstanceState == null) {
            UserSalesListFragment fragment = new UserSalesListFragment();
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.add(R.id.main_container, fragment);
            tran.addToBackStack(null);
            tran.commit();
        }
    }
}
