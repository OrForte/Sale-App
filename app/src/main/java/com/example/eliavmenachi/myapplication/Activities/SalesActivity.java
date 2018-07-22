package com.example.eliavmenachi.myapplication.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.eliavmenachi.myapplication.Fragments.NewSaleFragment;
import com.example.eliavmenachi.myapplication.Fragments.SalesListFragment;
import com.example.eliavmenachi.myapplication.R;

public class SalesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SalesActivity", "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sale);

        if (savedInstanceState == null) {
            SalesListFragment fragment = new SalesListFragment();
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.add(R.id.main_container, fragment);
            //tran.addToBackStack("");
            tran.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add:
                Log.d("TAG","menu add selected");
                NewSaleFragment fragment = new NewSaleFragment();
                FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, fragment);
                tran.addToBackStack("tag");
                tran.commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}