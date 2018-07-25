package com.example.eliavmenachi.myapplication.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Models.User.UserModel;
import com.example.eliavmenachi.myapplication.R;
import com.example.eliavmenachi.myapplication.ViewModels.UserViewModel;

public class EditUserProfileFragment extends Fragment {
    // Data Members
    UserViewModel userViewModel;
    EditText etUserName;
    EditText etFirstName;
    EditText etLastName;
    EditText etPassword;

    User currentUser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user_profile, container, false);

        etUserName = view.findViewById(R.id.fragment_edit_user_etUserName);
        etFirstName = view.findViewById(R.id.fragment_edit_user_etFirstName);
        etLastName = view.findViewById(R.id.fragment_edit_user_etLastName);
        etPassword = view.findViewById(R.id.fragment_edit_user_etNewPassword);

//currentUser =
        userViewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                currentUser = user;
                etUserName.setText(user.username);
                etFirstName.setText(user.firstName);
                etLastName.setText(user.lastName);
                //myAdapter.notifyDataSetChanged();
            }
        });

        Button btnUpdateProfile = view.findViewById(R.id.fragment_edit_user_btnUpdateProfile);

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser.username = etUserName.getText().toString();
                currentUser.lastName = etLastName.getText().toString();
                currentUser.firstName = etFirstName.getText().toString();

                if (!etPassword.getText().toString().equals("")) {
                    currentUser.password = etPassword.getText().toString();
                }

                userViewModel.setUser(currentUser);
            }
        });
//        Button btnLoginButton = view.findViewById(R.id.fragment_login_btnLogin);

//        btnLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String strUserName = userEt.getText().toString();
//                String strPassword = passwordEt.getText().toString();
//
//
//            }
//        });

        return view;
    }
}
