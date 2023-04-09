package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.adapters.AdminDriversAdapter;
import com.example.ray.interfaces.RecyclerViewInterface;
import com.example.ray.models.driversModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdminAssignDriverVehicle extends AppCompatActivity implements RecyclerViewInterface {


    //arraylist
    ArrayList<driversModel> driversList;

    //initialize variables

    FirebaseDatabase db;
    DatabaseReference ref, reff, reference, refer;
    RecyclerView recyclerView;
    AdminDriversAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assign_driver_vehicle);



        //DB
        db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("Users");


        //RecyclerView
        recyclerView = findViewById(R.id.driversRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        driversList = new ArrayList<>();
        adapter = new AdminDriversAdapter(driversList, this, this);


        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    //if statement to exclude the admin from being displayed as a user driver
                    if (!dataSnapshot.child("Personal Details").child("type").getValue(String.class).equals("admin")) {
                        driversModel vm = dataSnapshot.child("Personal Details").getValue(driversModel.class);
                        //if statement to only display drivers with no vehicle assigned
                        if(!dataSnapshot.child("Assigned Vehicle").exists()){

                            driversList.add(vm);

                            adapter.notifyDataSetChanged();
                            adapter.notifyItemInserted(driversList.size() - 1);

                        }

                    }
                }


                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onItemClick(int position) {

        reff = db.getReference().child("Vehicles");

        String driverName = driversList.get(position).getFullName();
        String driverId = driversList.get(position).getUserID();



        //Getting the details from the previous page
        String vehicleId = "vehicleIdentification";
        String modelDescription = "vehicleModel";
        String plateNumber = "vehiclePlate";

        //bundle to get extras passed through intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vehicleId = extras.getString("vehicleIdentification");
            modelDescription = extras.getString("vehicleModel");
            plateNumber = extras.getString("vehiclePlate");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminAssignDriverVehicle.this);
        builder.setTitle("Confirm Vehicle Assignment");
        builder.setMessage("Confirm Assignment of "+modelDescription+" of plate Number "+plateNumber+" To Driver "+driverName+"?");

        String finalModelDescription = modelDescription;
        String finalPlateNumber = plateNumber;
        String finalVehicleId = vehicleId;
        String finalVehicleId1 = vehicleId;
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                reff.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                            Map<String, Object> item = new HashMap<>();
                            item.put("model", finalModelDescription);
                            item.put("plate", finalPlateNumber);
                            item.put("vehicleId", finalVehicleId);
                            item.put("driver", driverName);
                            item.put("driverID", driverId);



                            reff.child("Assigned Vehicles").child(finalVehicleId).setValue(item);
                            reff.child("Vehicles in Garage").child(finalVehicleId).removeValue();

                            db = FirebaseDatabase.getInstance();
                            reference = db.getReference().child("Users")
                                    .child(driverId)
                                    .child("Current Location");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    Map<String, Object> item = new HashMap<>();
                                    item.put("latitude", -1.286389);
                                    item.put("longitude", 36.817223);
                                    reference.setValue(item);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            //Add assigned vehicle to user profile
                            refer = db.getReference().child("Users")
                                            .child(driverId).child("Assigned Vehicle");
                            refer.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    Map<String, Object> item = new HashMap<>();
                                    item.put("model", finalModelDescription);
                                    item.put("plate", finalPlateNumber);
                                    item.put("vehicleId", finalVehicleId);

                                    refer.child(finalVehicleId1).setValue(item);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            startActivity(new Intent(AdminAssignDriverVehicle.this, AdminVehiclesActivity.class));




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
}