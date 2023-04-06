package com.example.ray.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.activities.AdminAssignedVehicleDetails;
import com.example.ray.activities.AdminVehicleDetailsPage;
import com.example.ray.adapters.AdminAssignedVehiclesAdapter;
import com.example.ray.interfaces.RecyclerViewInterface;
import com.example.ray.models.vehiclesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssignedVehiclesFragment extends Fragment implements RecyclerViewInterface {

    //arraylist
    ArrayList<vehiclesModel> vehiclesList;

    //initialize variables

    FirebaseDatabase db;
    DatabaseReference ref,refer,reference;
    RecyclerView recyclerView;
    AdminAssignedVehiclesAdapter adapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assigned_vehicles, container, false);

        //DB
        db = FirebaseDatabase.getInstance();
        reference = db.getReference().child("Vehicles").child("Assigned Vehicles");

        //RecyclerView
        recyclerView = view.findViewById(R.id.vehiclesRecyclerAssigned);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        vehiclesList = new ArrayList<>();
        adapter = new AdminAssignedVehiclesAdapter(getContext(), vehiclesList, this);

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

        return view;
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(getContext(), AdminAssignedVehicleDetails.class);
        intent.putExtra("modelDescription",(vehiclesList.get(position).getModel()));
        intent.putExtra("plateNumber",(vehiclesList.get(position).getPlate()));
        intent.putExtra("VehicleId",(vehiclesList.get(position).getVehicleId()));
        intent.putExtra("driverName",(vehiclesList.get(position).getDriver()));
        intent.putExtra("driverId",(vehiclesList.get(position).getDriverID()));
        startActivity(intent);

        String drID = vehiclesList.get(position).getDriverID();



    }
}