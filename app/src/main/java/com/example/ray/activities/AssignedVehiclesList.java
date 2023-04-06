package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.adapters.AdminVehiclesAdapter;
import com.example.ray.databinding.ActivityAssignedVehiclesListBinding;
import com.example.ray.interfaces.RecyclerViewInterface;
import com.example.ray.models.vehiclesModel;
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

public class AssignedVehiclesList extends DrawerBaseActivity implements RecyclerViewInterface {

    //arraylist
    ArrayList<vehiclesModel> vehiclesList;

    //initialize variables
    ActivityAssignedVehiclesListBinding activityAssignedVehiclesListBinding;
    FirebaseDatabase db;
    DatabaseReference ref,reff,refer,reference,reference2,ref3;
    RecyclerView recyclerView;
    AdminVehiclesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_vehicles_list);

        activityAssignedVehiclesListBinding = ActivityAssignedVehiclesListBinding.inflate(getLayoutInflater());
        setContentView(activityAssignedVehiclesListBinding.getRoot());
        setTitle("Available Vehicles");


        //DB
        db = FirebaseDatabase.getInstance();
        refer = db.getReference().child("vehicles").push();
        String vehicleID = refer.getKey();
        assert vehicleID != null;
        ref = db.getReference().child("Vehicles").child(vehicleID);
        reference = db.getReference().child("Vehicles");

        //RecyclerView
        recyclerView = findViewById(R.id.vehiclesRecycler2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        vehiclesList = new ArrayList<>();
        adapter = new AdminVehiclesAdapter(this, vehiclesList, this);


        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    vehiclesModel vm = dataSnapshot.getValue(vehiclesModel.class);
                    vehiclesList.add(vm);

                    adapter.notifyDataSetChanged();
                    adapter.notifyItemInserted(vehiclesList.size() - 1);


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



        String model = vehiclesList.get(position).getModel();
        String plate = vehiclesList.get(position).getPlate();
        String vehID = vehiclesList.get(position).getVehicleId();


        //DB
        db = FirebaseDatabase.getInstance();
        refer = db.getReference().child("vehicles").push();
        String vehicleID = refer.getKey();
        assert vehicleID != null;
        ref = db.getReference().child("Vehicles").child(vehicleID);
        reference = db.getReference().child("Vehicles");




        AlertDialog.Builder builder = new AlertDialog.Builder(AssignedVehiclesList.this);
        builder.setTitle("Confirm Vehicle Assigned Selection");
        builder.setMessage("Confirm selection of "+model+" of plate Number "+plate+"?");

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                reff = reference.child("Assigned Vehicles").push();
                String assignedVehicleId = reff.getKey();
                assert assignedVehicleId != null;

                reference2 = reference.child("Assigned Vehicles").child(assignedVehicleId);

                Map<String, Object> value = new HashMap<>();
                value.put("model", model);
                value.put("plate", plate);
                value.put("vehicleId",assignedVehicleId);

                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                        .child("Assigned Vehicle").child(vehID).setValue(value);
                //remove from vehicles in garage
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                            FirebaseDatabase.getInstance().getReference().child("Vehicles")
                                    .child(vehID).removeValue();


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                ref3 =  FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                        .child("Assigned Vehicle");

                ref3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                            String assignedModel = (String) dataSnapshot.child("model").getValue();

                            if(assignedModel != null){

                                Toast.makeText(AssignedVehiclesList.this, "Driver Can only be assigned one Vehicle", Toast.LENGTH_SHORT).show();

                            }
                        }

                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                recreate();



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