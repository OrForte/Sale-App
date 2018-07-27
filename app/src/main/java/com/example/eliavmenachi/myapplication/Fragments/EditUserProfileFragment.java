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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Models.User.UserModel;
import com.example.eliavmenachi.myapplication.R;
import com.example.eliavmenachi.myapplication.ViewModels.CityMallAndStoreViewModel;
import com.example.eliavmenachi.myapplication.ViewModels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditUserProfileFragment extends Fragment {
    // Data Members
    CityMallAndStoreViewModel cityDataModel;
    UserViewModel userViewModel;

    EditText etUserName;
    EditText etFirstName;
    EditText etLastName;
    EditText etPassword;
    Spinner spCity;
    EditText etBirthDate;
    EditText etEmail;

    ListData cityListData;
    City selectedCity;
    User currentUser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        cityDataModel = ViewModelProviders.of(this).get(CityMallAndStoreViewModel.class);

    }

    private void selectSpinnerValue(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public void setListOfCities(ListData data)
    {
        cityListData = data;
        List<String> citiesNames = cityDataModel.GetCityNames(cityListData);

        // set the adaper
        ArrayAdapter<String> adapter = setAdapter(citiesNames);

        // set the drop down cities
        spCity.setAdapter(adapter);

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                onSelectedCity(adapterView,view,position,l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void onSelectedCity(AdapterView<?> adapterView, View view, final int position, long l)
    {
        String selectedCityName = adapterView.getItemAtPosition(position).toString();
        selectedCity = cityDataModel.GetCityByCityName(selectedCityName, cityListData);
    }

    public ArrayAdapter<String> setAdapter(List<String> collection)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, collection);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user_profile, container, false);

        etUserName = view.findViewById(R.id.fragment_edit_user_etUserName);
        etFirstName = view.findViewById(R.id.fragment_edit_user_etFirstName);
        etLastName = view.findViewById(R.id.fragment_edit_user_etLastName);
        etPassword = view.findViewById(R.id.fragment_edit_user_etNewPassword);
        etEmail = view.findViewById(R.id.fragment_edit_user_etEmail);
        etBirthDate = view.findViewById(R.id.fragment_edit_user_etBirthDate);
        spCity = view.findViewById(R.id.fragment_edit_user_spCity);

        cityDataModel.getData().observe(this, new Observer<ListData>() {
            @Override
            public void onChanged(@Nullable ListData listData) {
                setListOfCities(listData);

                userViewModel.getCurrentUser().observe(EditUserProfileFragment.this, new Observer<User>() {
                    @Override
                    public void onChanged(@Nullable User user) {
                        currentUser = user;
                        etUserName.setText(user.username);
                        etFirstName.setText(user.firstName);
                        etLastName.setText(user.lastName);

                        selectSpinnerValue(spCity, cityDataModel.GetCityByCityId(user.city, cityListData).name);
                        etBirthDate.setText(user.birthDate);
                        etEmail.setText(user.email);
                        //myAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        Button btnUpdateProfile = view.findViewById(R.id.fragment_edit_user_btnUpdateProfile);

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser.username = etUserName.getText().toString();
                currentUser.lastName = etLastName.getText().toString();
                currentUser.firstName = etFirstName.getText().toString();

                currentUser.city = selectedCity.id;
                if (!etPassword.getText().toString().equals("")) {
                    currentUser.password = etPassword.getText().toString();
                }

                currentUser.birthDate = etBirthDate.getText().toString();
                currentUser.email = etFirstName.getText().toString();
                currentUser.city = selectedCity.id;

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
