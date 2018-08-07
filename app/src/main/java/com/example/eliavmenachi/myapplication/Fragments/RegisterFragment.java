package com.example.eliavmenachi.myapplication.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.R;
import com.example.eliavmenachi.myapplication.ViewModels.CityMallAndStoreViewModel;
import com.example.eliavmenachi.myapplication.ViewModels.UserViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    final User user = new User();

    EditText firstNameEt;
    EditText lastNameEt;
    EditText passwordEt;
    EditText repeatPasswordEt;
    EditText mailEt;
    EditText userNameEt;
    Button register;
    TextView etEndDate;
    Spinner dropDownCities;
    CityMallAndStoreViewModel cityDataModel;
    UserViewModel userViewModel;
    ListData listData = new ListData();
    List<String> citiesNames;
    ArrayAdapter<String> adapterCities;
    String selectedCityName;
    int cityId;
    ProgressBar pbProgressBar;
    TextView tvProgressBar;
    ConstraintLayout mainLayout;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cityDataModel = ViewModelProviders.of(this).get(CityMallAndStoreViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mainLayout = view.findViewById(R.id.fragment_register_constraintLayout);

        firstNameEt = view.findViewById(R.id.fragment_register_etName);
        lastNameEt = view.findViewById(R.id.fragment_register_etFamily);
        passwordEt = view.findViewById(R.id.fragment_register_etPassword);
        mailEt = view.findViewById(R.id.fragment_register_etEmail);
        userNameEt = view.findViewById(R.id.fragment_register_etUser);
        etEndDate = view.findViewById(R.id.fragment_register_etEndDate);
        dropDownCities = view.findViewById(R.id.fragment_register_etCity);
        repeatPasswordEt = view.findViewById(R.id.fragment_register_etRepeatPassword);

        pbProgressBar = view.findViewById(R.id.fragment_register_pbProgressBar);
        tvProgressBar = view.findViewById(R.id.fragment_register_tvProgressBarText);

        cityDataModel.getData().observe(this, new Observer<ListData>() {
            @Override
            public void onChanged(@Nullable ListData data) {
                SetListOfCities(data);
            }
        });

        register = view.findViewById(R.id.fragment_register_btnRegister);

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                boolean bIsValid = isInputsValid();

                if (bIsValid == true) {
                    try {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

                    } catch (Exception e) {

                    }

                    pbProgressBar.setVisibility(View.VISIBLE);
                    tvProgressBar.setVisibility(View.VISIBLE);

                    user.firstName = firstNameEt.getText().toString();
                    user.lastName = lastNameEt.getText().toString();
                    user.username = userNameEt.getText().toString();
                    user.birthDate = etEndDate.getText().toString();
                    user.city = cityId;

                    String password = passwordEt.getText().toString();
                    String email = mailEt.getText().toString();

                    userViewModel.registerUser(user, email, password, new UserViewModel.CreateUserListener() {
                        @Override
                        public void onSuccess(User user) {
                            if (user != null) {
                                String welcomeMsg = "Welcome " + user.username + " !!";
                                Toast.makeText(getActivity(), welcomeMsg, Toast.LENGTH_LONG).show();

                                pbProgressBar.setVisibility(View.GONE);
                                tvProgressBar.setVisibility(View.GONE);

                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onFailure(String exceptionMessage) {
                            Toast.makeText(getActivity(), exceptionMessage, Toast.LENGTH_LONG).show();

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
        final String firstName = firstNameEt.getText().toString();
        final String lastName = lastNameEt.getText().toString();
        final String password = passwordEt.getText().toString();
        final String repeatPassword = repeatPasswordEt.getText().toString();
        final String mail = mailEt.getText().toString();
        final String userName = userNameEt.getText().toString();
        boolean bIsValid = true;

        if (firstName.length() == 0) {
            firstNameEt.requestFocus();
            firstNameEt.setError("Field can't be empty.");
            bIsValid = false;
        }

        if (lastName.length() == 0) {
            lastNameEt.requestFocus();
            lastNameEt.setError("Field can't be empty.");
            bIsValid = false;
        }

        //String mailPattern = "/^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$/";
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            mailEt.requestFocus();
            mailEt.setError("Invalid mail format.");
            bIsValid = false;
        }

        if (userName.length() == 0) {
            userNameEt.requestFocus();
            userNameEt.setError("Field can't be empty.");
            bIsValid = false;
        }

        if (!password.equals(repeatPassword)) {
            passwordEt.requestFocus();
            passwordEt.setError("Passwords do not match.");
            bIsValid = false;
        }

        if (password.length() <= 5) {
            passwordEt.requestFocus();
            passwordEt.setError("Password must have 6 characters.");
            bIsValid = false;
        }

        return bIsValid;
    }

    public void SetListOfCities(ListData data) {
        listData = new ListData();
        listData = data;
        citiesNames = cityDataModel.GetCityNames(listData);
        adapterCities = SetAdapter(citiesNames);
        dropDownCities.setAdapter(adapterCities);

        dropDownCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedCityName = adapterView.getItemAtPosition(position).toString();
                City selectedCity = cityDataModel.GetCityByCityName(selectedCityName, listData);
                if (selectedCity != null) {
                    cityId = selectedCity.id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public ArrayAdapter<String> SetAdapter(List<String> collection) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, collection);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return adapter;
    }
}
