package com.example.eliavmenachi.myapplication.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.Models.MainModel;
import com.example.eliavmenachi.myapplication.R;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

public class NewSaleFragment extends Fragment {

    ListData listData = new ListData();
    Spinner dropDownCities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_sale, container, false);
        dropDownCities = view.findViewById(R.id.fragment_register_etCity);

        MainModel.instance.GetListOfCitiesMallsAndStores(new MainModel.GetListOfCitiesMallsAndStoresListener() {
            @Override
            public void onGetListOfCitiesMallsANdStoresResults(ListData data) {
                listData = data;
                List<String> cities = new ArrayList<>();
                for (int nIndex = 0; nIndex < listData.cities.size();nIndex++)
                {
                    cities.add(listData.cities.get(nIndex).name);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, cities);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                dropDownCities.setAdapter(adapter);
            }
        });

        return view;
    }
}
