package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.databinding.ActivityAdminAssignedVehicleDetailsBinding;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdminAssignedVehicleDetails extends DrawerbaseActivity2 {

    TextView modelTV, plateTV, driverTV;
    SupportMapFragment supportMapFragment;
    ImageView image;
    Button retrieveLocation, returnVehicle;
    DatabaseReference ref, reference, reff, refer, imageRef;
    FirebaseDatabase db;
    ActivityAdminAssignedVehicleDetailsBinding activityAdminAssignedVehicleDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assigned_vehicle_details);

//        activityAdminAssignedVehicleDetailsBinding = ActivityAdminAssignedVehicleDetailsBinding.inflate(getLayoutInflater());
//        setContentView(activityAdminAssignedVehicleDetailsBinding.getRoot());



        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);



        //Getting the details from the previous page
        String model = "modelDescription";
        String plate = "plateNumber";
        String vehicleId = "VehicleId";
        String Driver = "driverName";
        String DriverId = "driverId";

        setTitle(model+" details");


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
        image = findViewById(R.id.ImageID);

        //setting textview
        modelTV.setText(model);
        plateTV.setText(plate);
        driverTV.setText(Driver);

        //get the image
        db = FirebaseDatabase.getInstance();
        imageRef = db.getReference().child("Vehicles Images").child(vehicleId);
        imageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String vehicleImage = (String) snapshot.child("imageUri").getValue();

                Picasso.get().load(vehicleImage).into(image);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

                            Double latitude = (Double) snapshot.child("latitude").getValue();
                            Double longitude = (Double) snapshot.child("longitude").getValue();



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





        //initialize return vehicle button
        returnVehicle = findViewById(R.id.ReturnVehicleToGarage);
        String finalVehicleId = vehicleId;

        String finalModel = model;
        String finalPlate = plate;
        String finalVehicleId1 = vehicleId;
        returnVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminAssignedVehicleDetails.this);
                builder.setTitle("Return Vehicle To Garage");
                builder.setMessage("Confirm return of "+finalModel+" of plate Number "+finalPlate+" to Garage?");

                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db = FirebaseDatabase.getInstance();

                        //remove the assigned vehicle from Assigned vehicle Object
                        reference = db.getReference().child("Vehicles")
                                .child("Assigned Vehicles").child(finalVehicleId);
                        reference.removeValue();



                        //Add the removed vehicle to vehicles in garage

                        reff =  db.getReference().child("Vehicles")
                                .child("Vehicles in Garage");
                        reff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                                    Map<String, Object> item = new HashMap<>();
                                    item.put("model", finalModel);
                                    item.put("plate", finalPlate);
                                    item.put("vehicleId", finalVehicleId1);

                                    reff.child(finalVehicleId).setValue(item);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        //remove assigned vehicle from users profile
                        refer = db.getReference().child("Users")
                                .child(finalDriverId)
                                .child("Assigned Vehicle");
                        refer.removeValue();

                        startActivity(new Intent(AdminAssignedVehicleDetails.this, AdminMainActivity.class));



                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                });
                builder.show();


            }
        });
    }
}