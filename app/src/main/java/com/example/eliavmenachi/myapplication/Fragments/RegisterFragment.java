package com.example.eliavmenachi.myapplication.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.example.eliavmenachi.myapplication.Models.User.UserAuthModel;
import com.example.eliavmenachi.myapplication.Models.User.UserAuthModelFirebase;
import com.example.eliavmenachi.myapplication.Models.User.UserModel;
import com.example.eliavmenachi.myapplication.R;
import com.example.eliavmenachi.myapplication.ViewModels.CityMallAndStoreViewModel;
import com.example.eliavmenachi.myapplication.ViewModels.UserViewModel;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    final User user = new User();

    EditText firstNameEt;
    EditText lastNameEt;
    EditText passwordEt;
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
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        firstNameEt = view.findViewById(R.id.fragment_register_etName);
        lastNameEt = view.findViewById(R.id.fragment_register_etFamily);
        passwordEt = view.findViewById(R.id.fragment_register_etPassword);
        mailEt = view.findViewById(R.id.fragment_register_etEmail);
        userNameEt = view.findViewById(R.id.fragment_register_etUser);
        etEndDate = view.findViewById(R.id.fragment_register_etEndDate);
        dropDownCities = view.findViewById(R.id.fragment_register_etCity);

        cityDataModel.getData().observe(this, new Observer<ListData>() {
            @Override
            public void onChanged(@Nullable ListData data) {
                SetListOfCities(data);
            }
        });

        register = view.findViewById(R.id.fragment_register_btnRegister);

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                user.firstName = firstNameEt.getText().toString();
                user.lastName = lastNameEt.getText().toString();
                user.password = passwordEt.getText().toString();
                user.email = mailEt.getText().toString();
                user.username = userNameEt.getText().toString();
                user.id = userNameEt.getText().toString();
                user.birthDate = etEndDate.getText().toString();
                user.city = cityId;

                userViewModel.createUser(user, new UserAuthModelFirebase.CreateUserCallback() {
                    @Override
                    public void onSuccess(String userID, String userName) {
                        String welcomeMsg = "welcome " + userName + " !!";
                        Toast.makeText(getActivity(), welcomeMsg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(String message) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        return view;
    }

    public void SetListOfCities(ListData data)
    {
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
                if (selectedCity != null)
                {
                    cityId = selectedCity.id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    public ArrayAdapter<String> SetAdapter(List<String> collection)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, collection);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return adapter;
    }
}
