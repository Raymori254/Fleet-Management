package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ray.R;
import com.example.ray.adapters.AdminVehiclesAdapter;
import com.example.ray.databinding.ActivityAdminVehiclesBinding;
import com.example.ray.databinding.ActivityAssignedVehiclesListBinding;
import com.example.ray.fragments.AssignedVehiclesFragment;
import com.example.ray.fragments.VehiclesInGarageFragment;
import com.example.ray.interfaces.RecyclerViewInterface;
import com.example.ray.models.vehiclesModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminVehiclesActivity extends DrawerbaseActivity2 {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_vehicles);

        ActivityAdminVehiclesBinding activityAdminVehiclesBinding;


        activityAdminVehiclesBinding = ActivityAdminVehiclesBinding.inflate(getLayoutInflater());
        setContentView(activityAdminVehiclesBinding.getRoot());
        setTitle("Vehicles");


        Fragment fragment = new VehiclesInGarageFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        LinearLayout assignedVehicles, vehiclesInGarage;

        assignedVehicles = findViewById(R.id.tv_assigned_vehicles);
        assignedVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AssignedVehiclesFragment())
                        .commit();

            }
        });
        vehiclesInGarage = findViewById(R.id.tv_vehicles_in_garage);
        vehiclesInGarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new VehiclesInGarageFragment())
                        .commit();

            }
        });


    }


}