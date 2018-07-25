package com.example.eliavmenachi.myapplication.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.ViewModels.CityMallAndStoreViewModel;
import com.example.eliavmenachi.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ChooseLocationFragment extends Fragment {

    CityMallAndStoreViewModel dataModel;
    ListData listData = new ListData();
    Spinner dropDownCities;
    Spinner dropDownMalls;
    Spinner dropDownStores;
    List<String> mallNames;
    List<String> storeNames;
    String selectedMallName;
    String selectedCityName;
    String selectedStoreName;
    int storeId;
    int mallId;
    int cityId;
    Button btnShowSales;

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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_location, container, false);

        dropDownCities = view.findViewById(R.id.sCity);
        dropDownMalls = view.findViewById(R.id.sMall);
        dropDownStores = view.findViewById(R.id.sStore);
        btnShowSales = view.findViewById(R.id.btnShowSales);

        btnShowSales.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();
                SalesListFragment fragment = new SalesListFragment();
                Bundle args = new Bundle();
                args.putString("STORE_ID", storeId +"");
                fragment.setArguments(args);
                FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
                //FragmentTransaction tran = fragmentManager.beginTransaction();
                tran.replace(R.id.main_container, fragment);
                //tran.addToBackStack(null);
                tran.commit();

            }
        });

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

    public void OnSelectedCity(AdapterView<?> adapterView, View view, final int position, long l)
    {
        mallNames = new ArrayList<>();
        storeNames = new ArrayList<>();
        selectedCityName = adapterView.getItemAtPosition(position).toString();
        City selectedCity = dataModel.GetCityByCityName(selectedCityName, listData);
        if (selectedCity != null)
        {
            cityId = selectedCity.id;
        }
        mallNames = dataModel.GetMallNamesByCityId(selectedCity.id, listData);
        ArrayAdapter<String> adapter = SetAdapter(mallNames);
        dropDownMalls.setAdapter(adapter);
        dropDownMalls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                OnSelectedMall(adapterView,view,i,l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void OnSelectedMall(AdapterView<?> adapterView, View view, int position, long l)
    {
        storeNames = new ArrayList<>();
        selectedMallName = adapterView.getItemAtPosition(position).toString();
        Mall selectedMall = dataModel.GetMallByMallName(selectedMallName, listData);
        if (selectedMall != null)
        {
            mallId = selectedMall.id;
        }
        storeNames = dataModel.GetStoreNamesByMallId(selectedMall.id, listData);
        ArrayAdapter<String> adapter = SetAdapter(storeNames);
        dropDownStores.setAdapter(adapter);
        dropDownStores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                OnSelectedStore(adapterView,view,i,l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void OnSelectedStore(AdapterView<?> adapterView, View view, int position, long l)
    {
        selectedStoreName = adapterView.getItemAtPosition(position).toString();
        Store selectedStore = dataModel.GetStoreByStoreName(selectedStoreName,listData);
        if (selectedStore != null)
        {
            storeId = selectedStore.id;
        }
    }

    public ArrayAdapter<String> SetAdapter(List<String> collection)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, collection);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return adapter;
    }
}
