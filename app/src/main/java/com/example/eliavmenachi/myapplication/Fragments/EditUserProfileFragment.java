package com.example.eliavmenachi.myapplication.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;

import com.example.eliavmenachi.myapplication.Activities.SalesActivity;
import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Models.User.UserAuthModel;
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
    EditText etRetypeNewPassword;
    Spinner spCity;
    EditText etBirthDate;
    EditText etEmail;

    ListData cityListData;
    City selectedCity;
    User currentUser;
    View rlProgressBar;

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
                if (spinner.getSelectedItemPosition() != i) {
                    spinner.setSelection(i);
                }
                break;
            }
        }
    }

    public void setListOfCities(ListData data) {
        cityListData = data;
        List<String> citiesNames = cityDataModel.GetCityNames(cityListData);

        // set the adaper
        ArrayAdapter<String> adapter = setAdapter(citiesNames);

        // set the drop down cities
        spCity.setAdapter(adapter);

        if (currentUser != null) {
            if (cityListData.cities.size() > 0) {
                int selectedCityIndex = ((ArrayAdapter<String>)spCity.getAdapter()).getPosition(cityDataModel.GetCityByCityId(currentUser.city, cityListData).name);
                spCity.setSelection(selectedCityIndex);
            }
        }

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedCityName = adapterView.getItemAtPosition(position).toString();
                selectedCity = cityDataModel.GetCityByCityName(selectedCityName, cityListData);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public ArrayAdapter<String> setAdapter(List<String> collection) {
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
        etRetypeNewPassword = view.findViewById(R.id.fragment_edit_user_etRetypeNewPassword);
        etEmail = view.findViewById(R.id.fragment_edit_user_etEmail);
        etBirthDate = view.findViewById(R.id.fragment_edit_user_etBirthDate);
        spCity = view.findViewById(R.id.fragment_edit_user_spCity);
        rlProgressBar = view.findViewById(R.id.fragment_edit_user_rlProgressBar);

        cityDataModel.getData().observe(this, new Observer<ListData>() {
            @Override
            public void onChanged(@Nullable ListData listData) {
                setListOfCities(listData);

                userViewModel.getCurrentUser().observe(EditUserProfileFragment.this, new Observer<User>() {
                    @Override
                    public void onChanged(@Nullable User user) {
                        currentUser = user;

                        if (user != null) {
                            etUserName.setText(user.username);
                            etFirstName.setText(user.firstName);
                            etLastName.setText(user.lastName);

                            if (cityListData.cities.size() > 0) {
                                int selectedCityIndex = ((ArrayAdapter<String>)spCity.getAdapter()).getPosition(cityDataModel.GetCityByCityId(user.city, cityListData).name);
                                spCity.setSelection(selectedCityIndex);
                                // adapterCities.getPosition(selectedCityName);
                                //selectSpinnerValue(spCity, cityDataModel.GetCityByCityId(user.city, cityListData).name);
                            }
                            etBirthDate.setText(user.birthDate);
                            etEmail.setText(user.email);
                        }

                        rlProgressBar.setVisibility(View.GONE);
                        //myAdapter.notifyDataSetChanged();
                    }
                });
            }
        });


//        userViewModel.getCurrentUser().observe(EditUserProfileFragment.this, new Observer<User>() {
//            @Override
//            public void onChanged(@Nullable User user) {
//                currentUser = user;
//                etUserName.setText(user.username);
//                etFirstName.setText(user.firstName);
//                etLastName.setText(user.lastName);
//
//                selectSpinnerValue(spCity, cityDataModel.GetCityByCityId(user.city, cityListData).name);
//                etBirthDate.setText(user.birthDate);
//                etEmail.setText(user.email);
//                //myAdapter.notifyDataSetChanged();
//            }
//        });

        Button btnUpdateProfile = view.findViewById(R.id.fragment_edit_user_btnUpdateProfile);

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;

                currentUser.username = etUserName.getText().toString();
                currentUser.lastName = etLastName.getText().toString();
                currentUser.firstName = etFirstName.getText().toString();

                currentUser.birthDate = etBirthDate.getText().toString();
                currentUser.email = etFirstName.getText().toString();
                currentUser.city = selectedCity.id;

                currentUser.city = selectedCity.id;
                if (!etPassword.getText().toString().equals("")) {
                    if (!etRetypeNewPassword.getText().toString().equals(etPassword.getText().toString())) {
                        isValid = false;
                        Toast.makeText(getActivity(), "Passwords don't match!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        currentUser.password = etPassword.getText().toString();
                    }
                }

                if (isValid) {
                    // TODO: make async
                    userViewModel.setUser(currentUser);
                    Toast.makeText(getActivity(), "User updated successfully!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btnUserSales = view.findViewById(R.id.fragment_edit_user_btnUserSales);
        btnUserSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSalesListFragment fragment = new UserSalesListFragment();
                FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, fragment);
                tran.addToBackStack(null);
                tran.commit();
            }
        });

        Button btnLogut = view.findViewById(R.id.fragment_edit_user_btnLogout);

        /*
        btnLogut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userViewModel.logoutCurrentUser(new UserViewModel.LogoutCompleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(), "User was logout successfully!",
                                Toast.LENGTH_LONG).show();

                        SalesListFragment fragment = new SalesListFragment();
                        FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
                        tran.replace(R.id.main_container, fragment);
                        tran.commit();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getActivity(), "Logout has faild!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });*/

        // TODO: need to change to view model...
        btnLogut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAuthModel.instance.signOut();

                Toast.makeText(getActivity(), "User was logout successfully!",
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(view.getContext(), SalesActivity.class);
                startActivity(intent);
                /*
                SalesListFragment fragment = new SalesListFragment();
                FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, fragment);
                tran.commit();*/
            }
        });

        Button btnRegister = view.findViewById(R.id.fragment_edit_user_btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment fragment = new RegisterFragment();
                FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, fragment);
                tran.addToBackStack(null);
                tran.commit();
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
