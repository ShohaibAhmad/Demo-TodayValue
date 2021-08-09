package com.promoteprovider.demotodayvalue.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.promoteprovider.demotodayvalue.R;
import com.promoteprovider.demotodayvalue.UploadActivity;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    ConstraintLayout mainLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mainLayout = view.findViewById(R.id.mainLayout);

        mainLayout.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(getContext(), UploadActivity.class);
                     startActivity(intent);
                 }
             });

        return view;
    }
}