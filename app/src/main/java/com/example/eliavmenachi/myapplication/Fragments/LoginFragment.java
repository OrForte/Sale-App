package com.example.eliavmenachi.myapplication.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eliavmenachi.myapplication.Activities.SalesActivity;
import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Models.User.UserAuthModel;
import com.example.eliavmenachi.myapplication.Models.User.UserAuthModelFirebase;
import com.example.eliavmenachi.myapplication.R;
import com.example.eliavmenachi.myapplication.ViewModels.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    // Data Members
    EditText userEt;
    EditText passwordEt;
    UserViewModel userViewModel;

//    public LoginFragment() {
//        // Required empty public constructor
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
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
        Button btnSignOutButton = view.findViewById(R.id.fragment_login_btnLogOff);

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

        btnSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = UserAuthModel.instance.getCurrentUser();
                Toast.makeText(getActivity(), "User was logout successfully!",
                        Toast.LENGTH_LONG).show();
            }
        });

        btnLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUsername = userEt.getText().toString();
                String strPassword = passwordEt.getText().toString();
                final View copyView = view;

                boolean bIsValid = isInputsValid();
                if (bIsValid == true) {
                    userViewModel.signIn(strUsername, strPassword, new UserAuthModelFirebase.SigninCallback() {
                        @Override
                        public void onSuccess(String userID, String userName) {
                            String wellcomeMsg = userName + " Welcome !!";
                            Toast.makeText(getActivity(), wellcomeMsg, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(copyView.getContext(), SalesActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailed() {
                            Toast.makeText(getActivity(), "error in the details", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        return view;
    }

    private boolean isInputsValid() {
        final String mail = userEt.getText().toString();
        final String password = passwordEt.getText().toString();
        boolean bIsValid = true;

        if (mail.length() == 0) {
            userEt.requestFocus();
            userEt.setError("FIELD CANNOT BE EMPTY");
            bIsValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            userEt.requestFocus();
            userEt.setError("INVALID EMAIL FORMAT");
            bIsValid = false;
        }

        if (password.length() == 0) {
            passwordEt.requestFocus();
            passwordEt.setError("FIELD CANNOT BE EMPTY");
            bIsValid = false;
        } else if (password.length() <= 5) {
            passwordEt.requestFocus();
            passwordEt.setError("Password must be 6 CHARACTERs");
            bIsValid = false;
        }

        return bIsValid;
    }
}
