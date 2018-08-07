package com.example.eliavmenachi.myapplication.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String MAIL = "MAIL";
    private static final String USER_NAME = "USER_NAME";
    private static final String END_DATE = "END_DATE";
    private static final String PASSWORD = "PASSWORD";
    private static final String REPEAT_PASSWORD = "REPEAT_PASSWORD";
    private static final String CITY = "CITY";

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

        firstNameEt = view.findViewById(R.id.fragment_register_etName);
        lastNameEt = view.findViewById(R.id.fragment_register_etFamily);
        passwordEt = view.findViewById(R.id.fragment_register_etPassword);
        mailEt = view.findViewById(R.id.fragment_register_etEmail);
        userNameEt = view.findViewById(R.id.fragment_register_etUser);
        etEndDate = view.findViewById(R.id.fragment_register_etEndDate);
        dropDownCities = view.findViewById(R.id.fragment_register_etCity);
        repeatPasswordEt = view.findViewById(R.id.fragment_register_etRepeatPassword);

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

                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onFailure(String exceptionMessage) {
                            Toast.makeText(getActivity(), exceptionMessage, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        if (savedInstanceState != null) {
            String firstName = savedInstanceState.getString(FIRST_NAME);
            if (firstName != null) {
                firstNameEt.setText(firstName);
            }
            String lastName = savedInstanceState.getString(LAST_NAME);
            if (lastName != null) {
                lastNameEt.setText(lastName);
            }
            String mail = savedInstanceState.getString(MAIL);
            if (mail != null) {
                mailEt.setText(mail);
            }
            String userName = savedInstanceState.getString(USER_NAME);
            if (userName != null) {
                userNameEt.setText(userName);
            }
            String password = savedInstanceState.getString(PASSWORD);
            if (password != null) {
                passwordEt.setText(password);
            }
            String repeatPassword = savedInstanceState.getString(REPEAT_PASSWORD);
            if (repeatPassword != null) {
                repeatPasswordEt.setText(repeatPassword);
            }
            String endDate = savedInstanceState.getString(END_DATE);
            if (endDate != null) {
                etEndDate.setText(endDate);
            }
            String cityString = savedInstanceState.getString(CITY);
            if (cityString != null) {
                int cityIndex = Integer.parseInt(cityString);
                if (dropDownCities != null) {
                    dropDownCities.setSelection(cityIndex, true);
                }
            }
        }

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

    @Override
    public void  onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putString(FIRST_NAME, firstNameEt.getText().toString());
        bundle.putString(LAST_NAME, lastNameEt.getText().toString());
        bundle.putString(MAIL, mailEt.getText().toString());
        bundle.putString(USER_NAME, userNameEt.getText().toString());
        bundle.putString(PASSWORD, passwordEt.getText().toString());
        bundle.putString(REPEAT_PASSWORD, repeatPasswordEt.getText().toString());
        bundle.putString(END_DATE, etEndDate.getText().toString());
        bundle.putString(CITY, cityId+"");
    }
}
