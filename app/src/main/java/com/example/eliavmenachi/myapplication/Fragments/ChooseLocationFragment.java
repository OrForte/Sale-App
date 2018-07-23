package com.example.eliavmenachi.myapplication.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Models.ViewModels.CityMallAndStoreViewModel;
import com.example.eliavmenachi.myapplication.R;

import java.util.List;

public class ChooseLocationFragment extends Fragment {

    CityMallAndStoreViewModel dataModel;
    ListData listData = new ListData();
    Spinner dropDownCities;
    Spinner dropDownMalls;
    Spinner dropDownStores;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataModel = ViewModelProviders.of(this).get(CityMallAndStoreViewModel.class);
        dataModel.getData().observe(this, new Observer<ListData>() {
            @Override
            public void onChanged(@Nullable ListData listData) {
                SetListOfCities(listData);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_location, container, false);

        dropDownCities = view.findViewById(R.id.sCity);
        dropDownMalls = view.findViewById(R.id.sMall);
        dropDownStores = view.findViewById(R.id.sStore);

        return view;
    }

    public void SetListOfCities(ListData data)
    {
        listData = new ListData();
        listData = data;

        List<String> citiesNames = dataModel.GetCityNames(listData);

        // set the adaper
        ArrayAdapter<String> adapter = SetAdapter(citiesNames);

        // set the drop down cities
        dropDownCities.setAdapter(adapter);
    }

    public ArrayAdapter<String> SetAdapter(List<String> collection)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, collection);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return adapter;
    }
}
