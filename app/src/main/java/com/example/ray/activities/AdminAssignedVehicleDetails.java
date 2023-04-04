package com.example.ray.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.ray.R;

public class AdminAssignedVehicleDetails extends AppCompatActivity {

    TextView modelTV, plateTV, driverTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assigned_vehicle_details);


        //Getting the details from the previous page
        String model = "modelDescription";
        String plate = "plateNumber";
        String vehicleId = "VehicleId";
        String Driver = "driverName";


        //bundle to get extras passed through intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            model = extras.getString("modelDescription");
            plate = extras.getString("plateNumber");
            vehicleId = extras.getString("VehicleId");
            Driver = extras.getString("driverName");



        }

        modelTV = findViewById(R.id.modelDescriptionID);
        plateTV = findViewById(R.id.plateNumberDescriptionID);
        driverTV = findViewById(R.id.DriverAssignedID);

        //setting textview
        modelTV.setText(model);
        plateTV.setText(plate);
        driverTV.setText(Driver);
    }
}