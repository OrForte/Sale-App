package com.example.eliavmenachi.myapplication.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.Models.MainModel;
import com.example.eliavmenachi.myapplication.R;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

public class NewSaleFragment extends Fragment {

    ListData listData = new ListData();
    Spinner dropDownCities;
    List<String> citiesNames;
    List<String> mallNames;
    List<String> storeNames;
    Spinner dropDownMalls;
    Spinner dropDownStores;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_sale, container, false);
        dropDownCities = view.findViewById(R.id.fragment_register_etCity);
        dropDownMalls = view.findViewById(R.id.fragment_register_etMall);
        dropDownStores = view.findViewById(R.id.fragment_register_etStore);

        MainModel.instance.GetListOfCitiesMallsAndStores(new MainModel.GetListOfCitiesMallsAndStoresListener() {
            @Override
            public void onGetListOfCitiesMallsANdStoresResults(ListData data) {
                listData = new ListData();
                listData = data;

                // get the ciry names
                citiesNames = GetCityNames();

                // set the adaper
                ArrayAdapter<String> adapter = SetAdapter(citiesNames);

                // set the drop down cities
                dropDownCities.setAdapter(adapter);

                dropDownCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        OnSelectedCity(adapterView,view,position,l);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });

        return view;
    }

    public void OnSelectedCity(AdapterView<?> adapterView, View view, int position, long l)
    {
        mallNames = new ArrayList<>();
        String selectedCityName = adapterView.getItemAtPosition(position).toString();
        City selectedCity = GetCityByCityName(selectedCityName);
        mallNames = GetMallNamesByCityId(selectedCity.id);
        ArrayAdapter<String> adapter = SetAdapter(mallNames);
        dropDownMalls.setAdapter(adapter);
        dropDownMalls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public List<String> GetCityNames()
    {
        List<String> cities = new ArrayList<>();

        for (Iterator iterator = listData.cities.iterator(); iterator.hasNext();)
        {
            cities.add(((City) iterator.next()).name);
        }
        return cities;
    }

    public List<String> GetMallNamesByCityId(int cityId)
    {
        List<String> malls = new ArrayList<>();
        for (Iterator iterator = listData.malls.iterator(); iterator.hasNext();)
        {
            Mall mall = (Mall)iterator.next();
            if (mall.cityId == cityId)
            {
                malls.add(mall.name);
            }
        }
        return malls;
    }

    public List<String> GetStoreNames()
    {
        List<String> stores = new ArrayList<>();

        for (Iterator iterator = listData.stores.iterator(); iterator.hasNext();)
        {
            stores.add(((Store) iterator.next()).name);
        }
        return stores;
    }

    public City GetCityByCityName(String selectedCityName)
    {
        for (Iterator iterator = listData.cities.iterator(); iterator.hasNext();)
        {
            City city = (City) iterator.next();
            if (selectedCityName == city.name)
            {
                return city;
            }
        }
        return null;
    }

    public ArrayAdapter<String> SetAdapter(List<String> collection)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, collection);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return adapter;
    }
}
