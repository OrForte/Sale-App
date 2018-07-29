package com.example.eliavmenachi.myapplication.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.eliavmenachi.myapplication.Entities.Consts;
import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Fragments.EditUserProfileFragment;
import com.example.eliavmenachi.myapplication.Fragments.LoginFragment;
import com.example.eliavmenachi.myapplication.Fragments.NewSaleFragment;
import com.example.eliavmenachi.myapplication.Fragments.SalesListFragment;
import com.example.eliavmenachi.myapplication.Fragments.ChooseLocationFragment;
import com.example.eliavmenachi.myapplication.R;
import com.example.eliavmenachi.myapplication.ViewModels.UserViewModel;

public class SalesActivity extends AppCompatActivity {
    UserViewModel userViewModel;
    Menu toolBarMenu;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SalesActivity", "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sale);

        if (savedInstanceState == null) {
            SalesListFragment fragment = new SalesListFragment();
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.add(R.id.main_container, fragment);
            //tran.addToBackStack("tag");
            tran.commit();
        }

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userViewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                currentUser = user;
                SalesActivity.this.invalidateOptionsMenu();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        toolBarMenu = menu;

        if (currentUser == null) {
            toolBarMenu.removeItem(R.id.menu_user_profile);
        }

        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        getMenuInflater().inflate(R.menu.main_menu, menu);
        toolBarMenu = menu;

        if (currentUser == null) {
            toolBarMenu.removeItem(R.id.menu_user_profile);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_sale: {
                if (currentUser == null) {
                    Intent intent = new Intent(SalesActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return true;
//                    LoginFragment fragment = new LoginFragment();
//                    FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
//                    tran.replace(R.id.main_container, fragment);
//                    tran.addToBackStack(Consts.instance.TAG_NEW_SALE); //TODO: make const
//                    tran.commit();
//                    return true;
                } else {
                    NewSaleFragment fragment = new NewSaleFragment();
                    FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                    tran.replace(R.id.main_container, fragment);
                    tran.addToBackStack(Consts.instance.TAG_NEW_SALE);
                    tran.commit();
                    return true;
                }
            }
            case R.id.menu_list_sales_by_category: {

                ChooseLocationFragment fragment = new ChooseLocationFragment();
                FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, fragment);
                tran.addToBackStack(Consts.instance.TAG_CHOOSE_STORE);
                tran.commit();
                return true;
            }
            case R.id.menu_user_profile: {
                EditUserProfileFragment fragment = new EditUserProfileFragment();
                FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, fragment);
                tran.addToBackStack(Consts.instance.TAG_CHOOSE_STORE);
                tran.commit();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}