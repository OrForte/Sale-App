package com.example.eliavmenachi.myapplication.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eliavmenachi.myapplication.R;

public class ChooseLocationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_location, container, false);

//        // get the user and password details
//        userEt = view.findViewById(R.id.fragment_login_etUsername);
//        passwordEt = view.findViewById(R.id.fragment_login_etPassword);
//
//        // get instance of the login and register button
//        Button loginButton = view.findViewById(R.id.fragment_login_btnRegister);
//        Button btnLoginButton = view.findViewById(R.id.fragment_login_btnLogin);
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RegisterFragment fragment = new RegisterFragment();
//                FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
//                tran.replace(R.id.main_container, fragment);
//                tran.addToBackStack("tag");
//                tran.commit();
//            }
//        });
//
//        btnLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String strUserName = userEt.getText().toString();
//                String strPassword = passwordEt.getText().toString();
//
//                UserModel.instance.IsUserVisible(strUserName, strPassword, new UserModel.IsUserVisibleListener() {
//                    @Override
//                    public void onDone(boolean p_bIsValid) {
//                        if (p_bIsValid) {
//                            Toast.makeText(getActivity(), "log on successfully",
//                                    Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(getActivity(), "error in the details",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//            }
//        });

        return view;
    }
}
