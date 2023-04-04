package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.adapters.AdminVehiclesAdapter;
import com.example.ray.databinding.ActivityAdminMainBinding;
import com.example.ray.databinding.ActivityAdminVehicleDetailsPageBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AdminVehicleDetailsPage extends DrawerbaseActivity2 {

    //creating variables
    TextView modelTV, plateTV;
    ActivityAdminVehicleDetailsPageBinding activityAdminVehicleDetailsPageBinding;
    Button editPhoto, assignDriver, deleteVehicle;

    //initialize variables

    FirebaseDatabase db;
    DatabaseReference ref,refer,reference;
    RecyclerView recyclerView;
    AdminVehiclesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_vehicle_details_page);

        activityAdminVehicleDetailsPageBinding = ActivityAdminVehicleDetailsPageBinding.inflate(getLayoutInflater());
        setContentView(activityAdminVehicleDetailsPageBinding.getRoot());

        setTitle("Vehicle Description");

        //initialize variables
        modelTV = findViewById(R.id.modelDescriptionID);
        plateTV = findViewById(R.id.plateNumberID);


        //Getting the details from the previous page
        String model = "modelDescription";
        String plate = "plateNumber";
        String vehicleId = "VehicleId";


        //bundle to get extras passed through intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            model = extras.getString("modelDescription");
            plate = extras.getString("plateNumber");
            vehicleId = extras.getString("VehicleId");



        }

        //setting textview
        modelTV.setText(model);
        plateTV.setText(plate);


        //DB
        db = FirebaseDatabase.getInstance();
        refer = db.getReference().child("vehicles").push();
        String vehicleID = refer.getKey();
        assert vehicleID != null;
        ref = db.getReference().child("Vehicles").child(vehicleID);
        reference = db.getReference().child("Vehicles").child("Vehicles in Garage");

        //initialize buttons
        editPhoto = findViewById(R.id.editPhoto);
        String finalVehicleId = vehicleId;
        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AdminVehicleDetailsPage.this, ImagePicker.class);
                i.putExtra("vehicleIdentification", finalVehicleId);
                startActivity(i);


            }
        });

        assignDriver = findViewById(R.id.assignDriver);
        String finalModel = model;
        String finalPlate = plate;
        assignDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AdminVehicleDetailsPage.this, AdminAssignDriverVehicle.class);
                i.putExtra("vehicleModel", finalModel);
                i.putExtra("vehiclePlate", finalPlate);
                i.putExtra("vehicleIdentification", finalVehicleId);
                startActivity(i);

            }
        });

        deleteVehicle = findViewById(R.id.deleteVehicle);
        deleteVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminVehicleDetailsPage.this);
                builder.setTitle("Remove Vehicle from Garage");
                builder.setMessage("Confirm Deletion of "+finalModel+" of plate Number "+finalPlate+" From Garage?");

                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       reference.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {

                               for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                                   //Remove from list
                                   reference.child(finalVehicleId).removeValue();
                                   Toast.makeText(AdminVehicleDetailsPage.this, "Vehicle Deleted", Toast.LENGTH_SHORT).show();
                                   startActivity(new Intent(AdminVehicleDetailsPage.this, AdminVehiclesActivity.class));
                                   recreate();

                               }


                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });


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