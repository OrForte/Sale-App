package com.example.eliavmenachi.myapplication.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.example.eliavmenachi.myapplication.Models.MainModel;
import com.example.eliavmenachi.myapplication.R;

public class NewSaleFragment extends Fragment {

    ListData listData = new ListData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_sale, container, false);

        MainModel.instance.GetListOfCitiesMallsAndStores(new MainModel.GetListOfCitiesMallsAndStoresListener() {
            @Override
            public void onGetListOfCitiesMallsANdStoresResults(ListData data) {
                listData = data;
            }
        });

        return view;
    }
}
