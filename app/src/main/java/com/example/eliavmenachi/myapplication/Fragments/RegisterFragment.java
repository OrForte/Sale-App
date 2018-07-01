package com.example.eliavmenachi.myapplication.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    final User user = new User();

    EditText firstNameEt;
    EditText lastNameEt;
    EditText ageEt;
    EditText passwordEt;
    EditText mailEt;
    EditText userNameEt;
    Button register;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        firstNameEt = view.findViewById(R.id.fragment_register_etName);
        lastNameEt = view.findViewById(R.id.fragment_register_etFamily);
        ageEt = view.findViewById(R.id.fragment_register_etAge);
        passwordEt = view.findViewById(R.id.fragment_register_etPassword);
        mailEt = view.findViewById(R.id.fragment_register_etEmail);
        userNameEt = view.findViewById(R.id.fragment_register_etUser);

        register = view.findViewById(R.id.fragment_register_btnRegister);

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int a = 1;

                user.firstName = firstNameEt.getText().toString();
                user.lastName = lastNameEt.getText().toString();
                user.password = passwordEt.getText().toString();
                user.email = mailEt.getText().toString();
                user.userName = userNameEt.getText().toString();

                user.id = 0;
                try
                {
                    user.age = Integer.parseInt(ageEt.getText().toString());
                }
                catch(NumberFormatException nfe)
                {
                    System.out.println("Could not parse " + nfe);
                }
            }
        });

        return view;
    }

}
