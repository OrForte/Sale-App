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

public class EditUserProfileFragment extends Fragment {
    // Data Members
    CityMallAndStoreViewModel cityDataModel;
    UserViewModel userViewModel;

    //    EditText etUserName;
    EditText etFirstName;
    EditText etLastName;
    //    EditText etPassword;
//    EditText etRetypeNewPassword;
    Spinner spCity;
    EditText etBirthDate;
//    EditText etEmail;

    ListData cityListData;
    City selectedCity;
    User currentUser;
    View rlProgressBar;
    TextView tvProgressBar;
    ProgressBar pbProgressBar;
    View mainLayout;

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
                City city = cityDataModel.GetCityByCityId(currentUser.city, cityListData);
                if (city != null) {
                    ArrayAdapter<String> adapterString = (ArrayAdapter<String>) spCity.getAdapter();
                    if (adapterString != null) {
                        int selectedCityIndex = adapterString.getPosition(city.name);
                        spCity.setSelection(selectedCityIndex);
                    }
                }
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

        mainLayout = view.findViewById(R.id.fragment_edit_user_main_layout);

//        etUserName = view.findViewById(R.id.fragment_edit_user_etUserName);
        etFirstName = view.findViewById(R.id.fragment_edit_user_etFirstName);
        etLastName = view.findViewById(R.id.fragment_edit_user_etLastName);
//        etPassword = view.findViewById(R.id.fragment_edit_user_etNewPassword);
//        etRetypeNewPassword = view.findViewById(R.id.fragment_edit_user_etRetypeNewPassword);
//        etEmail = view.findViewById(R.id.fragment_edit_user_etEmail);
        etBirthDate = view.findViewById(R.id.fragment_edit_user_etBirthDate);
        spCity = view.findViewById(R.id.fragment_edit_user_spCity);
        rlProgressBar = view.findViewById(R.id.fragment_edit_user_rlProgressBar);

        pbProgressBar = view.findViewById(R.id.fragment_edit_user_pbProgressBar2);
        tvProgressBar = view.findViewById(R.id.fragment_edit_user_tvProgressBar);

        cityDataModel.getData().observe(this, new Observer<ListData>() {
            @Override
            public void onChanged(@Nullable ListData listData) {
                setListOfCities(listData);

                userViewModel.getCurrentUser().observe(EditUserProfileFragment.this, new Observer<User>() {
                    @Override
                    public void onChanged(@Nullable User user) {
                        currentUser = user;

                        if (user != null) {
//                                etUserName.setText(user.username);
                            etFirstName.setText(user.firstName);
                            etLastName.setText(user.lastName);

                            if (cityListData.cities.size() > 0) {
                                City city = cityDataModel.GetCityByCityId(user.city, cityListData);
                                if (city != null) {
                                    ArrayAdapter<String> adapterString = (ArrayAdapter<String>) spCity.getAdapter();
                                    if (adapterString != null) {
                                        int selectedCityIndex = adapterString.getPosition(city.name);
                                        spCity.setSelection(selectedCityIndex);
                                    }
                                }
                            }
                            etBirthDate.setText(user.birthDate);
//                                etEmail.setText(user.email);

                            rlProgressBar.setVisibility(View.GONE);
                        }
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

        final Button btnUpdateProfile = view.findViewById(R.id.fragment_edit_user_btnUpdateProfile);

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;

                User user = new User();
                user.id = currentUser.id;
//                user.username = etUserName.getText().toString();
                user.lastName = etLastName.getText().toString();
                user.firstName = etFirstName.getText().toString();

                user.birthDate = etBirthDate.getText().toString();
//                user.email = etEmail.getText().toString();
                user.city = selectedCity.id;

                user.city = selectedCity.id;
//                if (!etPassword.getText().toString().equals("")) {
//                    if (!etRetypeNewPassword.getText().toString().equals(etPassword.getText().toString())) {
//                        isValid = false;
//                        Toast.makeText(getActivity(), "Passwords don't match!",
//                                Toast.LENGTH_LONG).show();
//                    } else {
//                        //user.password = etPassword.getText().toString();
//                    }
//                }

                if (isValid) {
                    try {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

                    } catch (Exception e) {

                    }

                    pbProgressBar.setVisibility(View.VISIBLE);
                    tvProgressBar.setVisibility(View.VISIBLE);

                    // TODO: make async
                    userViewModel.updateUser(user, new UserViewModel.UpdateUserListener() {
                        @Override
                        public void onSuccess() {
                            pbProgressBar.setVisibility(View.GONE);
                            tvProgressBar.setVisibility(View.GONE);

                            Toast.makeText(getActivity(), "User updated successfully!",
                                    Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(String exceptionMessage) {
                            pbProgressBar.setVisibility(View.GONE);
                            tvProgressBar.setVisibility(View.GONE);

                            Toast.makeText(getActivity(), exceptionMessage,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
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
                userViewModel.signOutCurrentUser(new UserViewModel.LogoutListener() {
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
                userViewModel.signOut(new UserViewModel.SignOutListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(), "User was logout successfully!",
                                Toast.LENGTH_LONG).show();

                        getActivity().finish();
//                        Intent intent = new Intent(EditUserProfileFragment.this.getContext(), SalesActivity.class);
//                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String exceptionMessage) {
                        Toast.makeText(getActivity(), exceptionMessage,
                                Toast.LENGTH_LONG).show();
                    }
                });
                //UserModel.instance.signOut();


                /*
                SalesListFragment fragment = new SalesListFragment();
                FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
                tran.replace(R.id.main_container, fragment);
                tran.commit();*/
            }
        });

//        Button btnRegister = view.findViewById(R.id.fragment_edit_user_btnRegister);
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RegisterFragment fragment = new RegisterFragment();
//                FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
//                tran.replace(R.id.main_container, fragment);
//                tran.addToBackStack(null);
//                tran.commit();
//            }
//        });

        return view;
    }
}
