package com.example.eliavmenachi.myapplication.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eliavmenachi.myapplication.Activities.LoginActivity;
import com.example.eliavmenachi.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button loginButton = view.findViewById(R.id.button);

        loginButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  Intent intent = new Intent(view.getContext(), LoginActivity.class);
                                                  startActivity(intent);
                                              }
                                          });

        return view;
    }

}
