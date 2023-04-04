package com.example.ray.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ray.R;
import com.example.ray.activities.AssignedVehiclesList;
import com.example.ray.activities.DrawerBaseActivity;

public class Home extends Fragment {
    
    Button selectAssignedVehicle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate the view
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        selectAssignedVehicle = view.findViewById(R.id.AssignedvehicleBtn);
        selectAssignedVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), AssignedVehiclesList.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
