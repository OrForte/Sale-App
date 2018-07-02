package com.example.eliavmenachi.myapplication.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.eliavmenachi.myapplication.Activities.LoginActivity;
import com.example.eliavmenachi.myapplication.Models.MainModel;
import com.example.eliavmenachi.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText userEt;
    EditText passwordEt;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // get the user and password details
        userEt = view.findViewById(R.id.fragment_login_etUsername);
        passwordEt = view.findViewById(R.id.fragment_login_etPassword);

        // get instance of the login and register button
        Button loginButton = view.findViewById(R.id.fragment_login_btnRegister);
        Button btnLoginButton = view.findViewById(R.id.fragment_login_btnLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment fragment = new RegisterFragment();
                FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, fragment);
                tran.addToBackStack("tag");
                tran.commit();
            }
        });

        btnLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = 1;

                String strUserName = userEt.getText().toString();
                String strPassword = passwordEt.getText().toString();

                MainModel.instance.IsUserVisible(strUserName, strPassword, new MainModel.IsUserVisibleListener() {
                    @Override
                    public void onDone(boolean p_bIsValid) {
                        int aa = 1;
                    }
                });
            }
        });

        return view;
    }

}
