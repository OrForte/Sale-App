package com.example.eliavmenachi.myapplication.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Fragments.EditUserProfileFragment;
import com.example.eliavmenachi.myapplication.R;
import com.example.eliavmenachi.myapplication.ViewModels.UserViewModel;

public class UserProfileActivity extends AppCompatActivity {
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("UserProfileActivity", "onCreate");
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.ic_account_circle_black_24dp);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        setContentView(R.layout.activity_user_profile);

        if (savedInstanceState == null) {
            EditUserProfileFragment fragment = new EditUserProfileFragment();
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.add(R.id.main_container, fragment);
            //tran.addToBackStack("");
            tran.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout: {
                userViewModel.signOut(new UserViewModel.SignOutListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(UserProfileActivity.this, "Signed out successfully", Toast.LENGTH_LONG).show();

                        User user = userViewModel.getCurrentUser().getValue();
                        finish();
                    }

                    @Override
                    public void onFailure(String exceptionMessage) {
                        Toast.makeText(UserProfileActivity.this, exceptionMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
