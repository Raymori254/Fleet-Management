package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ray.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AdminAssignedVehicleDetails extends AppCompatActivity {

    TextView modelTV, plateTV, driverTV;
    SupportMapFragment supportMapFragment;
    Button retrieveLocation;
    DatabaseReference ref;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assigned_vehicle_details);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);



        //Getting the details from the previous page
        String model = "modelDescription";
        String plate = "plateNumber";
        String vehicleId = "VehicleId";
        String Driver = "driverName";
        String DriverId = "driverId";


        //bundle to get extras passed through intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            model = extras.getString("modelDescription");
            plate = extras.getString("plateNumber");
            vehicleId = extras.getString("VehicleId");
            Driver = extras.getString("driverName");
            DriverId = extras.getString("driverId");



        }

        modelTV = findViewById(R.id.modelDescriptionID);
        plateTV = findViewById(R.id.plateNumberDescriptionID);
        driverTV = findViewById(R.id.DriverAssignedID);

        //setting textview
        modelTV.setText(model);
        plateTV.setText(plate);
        driverTV.setText(Driver);

        //initialize retrieve button and set click listener
        retrieveLocation = findViewById(R.id.retriveLocation);
        String finalDriverId = DriverId;
        String finalDriver = Driver;



        retrieveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance();
                ref = db.getReference().child("Users")
                        .child(finalDriverId)
                        .child("Current Location");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                            Double latitude = snapshot.child("latitude").getValue(Double.class);
                            Double longitude = (Double) snapshot.child("longitude").getValue(Double.class);



                            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull GoogleMap googleMap) {
                                    LatLng location = new LatLng(latitude, longitude);

                                    //create marker options

                                    MarkerOptions markerOptions = new MarkerOptions().position(location)
                                            .title(finalDriver +" location");
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(location));
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15));
                                    googleMap.addMarker(markerOptions);

                                    //Add Marker on map
                                    googleMap.addMarker(markerOptions);
                                }


                            });



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }
}