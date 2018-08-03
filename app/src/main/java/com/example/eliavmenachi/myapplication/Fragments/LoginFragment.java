package com.example.eliavmenachi.myapplication.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliavmenachi.myapplication.R;
import com.example.eliavmenachi.myapplication.ViewModels.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    // Data Members
    EditText emailEt;
    EditText passwordEt;
    UserViewModel userViewModel;
    ConstraintLayout mainLayout;
    ProgressBar pbProgressBar;
    TextView tvProgressBar;

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

        mainLayout = view.findViewById(R.id.fragment_login_constraintLayout);

        pbProgressBar = view.findViewById(R.id.fragment_login_pbProgressBar);
        tvProgressBar = view.findViewById(R.id.fragment_login_tvProgressBarText);

        // get the user and password details
        emailEt = view.findViewById(R.id.fragment_login_etEmail);
        passwordEt = view.findViewById(R.id.fragment_login_etPassword);

        // get instance of the login and register button
        Button registerButton = view.findViewById(R.id.fragment_login_btnRegister);
        Button btnLoginButton = view.findViewById(R.id.fragment_login_btnLogin);
//        Button btnSignOutButton = view.findViewById(R.id.fragment_login_btnLogOff);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment fragment = new RegisterFragment();
                FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, fragment);
                tran.addToBackStack("tag");
                tran.commit();
            }
        });

//        btnSignOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //emailEt.getText(), passwordEt.getText()
//                UserModel.instance.signOut(new UserModel.SignOutListener() {
//                    @Override
//                    public void onSuccess() {
//                        Toast.makeText(getActivity(), "User was logout successfully!",
//                                Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onFailure(String exceptionMessage) {
//                        Toast.makeText(getActivity(), exceptionMessage,
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        });

        btnLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = emailEt.getText().toString();
                String strPassword = passwordEt.getText().toString();

                if (isInputsValid() == true) {
                    try {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

                    } catch (Exception e) {

                    }

                    pbProgressBar.setVisibility(View.VISIBLE);
                    tvProgressBar.setVisibility(View.VISIBLE);

                    userViewModel.signIn(strEmail, strPassword, new UserViewModel.SignInListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getActivity(), "Signed in successfully!", Toast.LENGTH_LONG).show();

                            pbProgressBar.setVisibility(View.GONE);
                            tvProgressBar.setVisibility(View.GONE);

//                            Intent intent = new Intent();
//                            intent.putExtra("SignIn", true);
//                            getActivity().setResult(1);
                            getActivity().finish();
//                            Intent intent = new Intent(LoginFragment.this.getContext(), SalesActivity.class);
//                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(String exceptionMessage) {
                            Toast.makeText(getActivity(), "One of the fields above isn't valid.", Toast.LENGTH_LONG).show();

                            pbProgressBar.setVisibility(View.GONE);
                            tvProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        return view;
    }

    private boolean isInputsValid() {
        final String mail = emailEt.getText().toString();
        final String password = passwordEt.getText().toString();
        boolean bIsValid = true;

        if (mail.length() == 0) {
            emailEt.requestFocus();
            emailEt.setError("Mail address is required.");
            bIsValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            emailEt.requestFocus();
            emailEt.setError("Invalid email format");
            bIsValid = false;
        }

        if (password.length() == 0) {
            passwordEt.requestFocus();
            passwordEt.setError("Password is required.");
            bIsValid = false;
        } else if (password.length() <= 5) {
            passwordEt.requestFocus();
            passwordEt.setError("Password must have at least 6 characters.");
            bIsValid = false;
        }

        return bIsValid;
    }
}
