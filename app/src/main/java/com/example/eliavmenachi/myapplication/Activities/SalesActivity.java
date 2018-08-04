package com.example.eliavmenachi.myapplication.Activities;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.eliavmenachi.myapplication.Entities.Consts;
import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Fragments.ChooseLocationFragment;
import com.example.eliavmenachi.myapplication.Fragments.NewSaleFragment;
import com.example.eliavmenachi.myapplication.Fragments.SalesListFragment;
import com.example.eliavmenachi.myapplication.R;
import com.example.eliavmenachi.myapplication.ViewModels.UserViewModel;

public class SalesActivity extends AppCompatActivity {
    UserViewModel userViewModel;
    Menu toolBarMenu;
    User currentUser;
    final int REQUEST_WRITE_STORAGE = 1;
//    View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SalesActivity", "onCreate");
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.ic_shopping_basket_black_24dp);

        currentUser = null;

        setContentView(R.layout.activity_sale);

//        progressBar = findViewById(R.id.activity_sale_rlProgressBar);

        if (savedInstanceState == null) {
            SalesListFragment fragment = new SalesListFragment();
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.add(R.id.main_container, fragment);
            tran.commit();
        }

        // check whenever has permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            }
        }

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        // TODO: need to change to view model...
        userViewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                updateMenuIfNeeded(user);
            }
        });
    }

    private void updateMenuIfNeeded(User newUser) {
        boolean hasChanged = false;

        if (currentUser != newUser) {
            hasChanged = true;
        }

        currentUser = newUser;

        if (hasChanged) {
            SalesActivity.this.invalidateOptionsMenu();
        }

//        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        User user = userViewModel.getCurrentUser().getValue();
        updateMenuIfNeeded(user);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1) {
//            progressBar.setVisibility(View.VISIBLE);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        toolBarMenu = menu;
        if (currentUser == null) {
            toolBarMenu.removeItem(R.id.menu_logout);
        }

        return true;
    }
//
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.clear();
//
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        toolBarMenu = menu;
//
//        if (currentUser == null) {
//            toolBarMenu.removeItem(R.id.menu_logout);
//        }
//
//        return super.onPrepareOptionsMenu(menu);
//    }

    /*
    public void InitActivity()
    {
        if (toolBarMenu != null) {
            if (currentUser == null) {
                toolBarMenu.removeItem(R.id.menu_user_profile);
            } else {
                toolBarMenu.add(Menu.NONE, R.id.menu_user_profile, Menu.NONE, "USER PROFILE");
            }
        }
    }*/

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
                    NewSaleFragment newSaleFragment = (NewSaleFragment) getSupportFragmentManager().findFragmentByTag(Consts.instance.TAG_NEW_SALE);
                    if (newSaleFragment == null || !newSaleFragment.isVisible()) {
                        NewSaleFragment fragment = new NewSaleFragment();
                        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                        tran.replace(R.id.main_container, fragment, Consts.instance.TAG_NEW_SALE);
                        tran.addToBackStack(Consts.instance.TAG_NEW_SALE);
                        tran.commit();
                    }
                    return true;
                }
            }
            case R.id.menu_list_sales_by_category: {
                ChooseLocationFragment chooseLocationFragment = (ChooseLocationFragment) getSupportFragmentManager().findFragmentByTag(Consts.instance.TAG_CHOOSE_STORE);
                if (chooseLocationFragment == null || !chooseLocationFragment.isVisible()) {
                    ChooseLocationFragment fragment = new ChooseLocationFragment();
                    FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
                    tran.replace(R.id.main_container, fragment, Consts.instance.TAG_CHOOSE_STORE);
                    tran.addToBackStack(Consts.instance.TAG_CHOOSE_STORE);
                    tran.commit();
                }

                return true;
            }
            case R.id.menu_user_profile: {
                if (currentUser == null) {
                    Intent intent = new Intent(SalesActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 1);
                    return true;
                } else {

                    Intent intent = new Intent(SalesActivity.this, UserProfileActivity.class);
                    startActivity(intent);
//                    EditUserProfileFragment fragment = new EditUserProfileFragment();
//                    FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
//                    tran.replace(R.id.main_container, fragment);
//                    tran.addToBackStack(Consts.instance.TAG_CHOOSE_STORE);
//                    tran.commit();
                    return true;
                }
            }
            case R.id.menu_logout: {
                userViewModel.signOut(new UserViewModel.SignOutListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(SalesActivity.this, "Signed out successfully", Toast.LENGTH_LONG).show();

                        User user = userViewModel.getCurrentUser().getValue();
                        updateMenuIfNeeded(user);
//                        SalesListFragment fragment = new SalesListFragment();
//                        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
//                        tran.replace(R.id.main_container, fragment);
//                        tran.commit();
//
//                        startActivity(getIntent());
//                        finish();
//                        SalesActivity.this.invalidateOptionsMenu();
                    }

                    @Override
                    public void onFailure(String exceptionMessage) {
                        Toast.makeText(SalesActivity.this, exceptionMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }
}