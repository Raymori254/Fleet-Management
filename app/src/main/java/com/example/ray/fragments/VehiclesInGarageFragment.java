package com.example.ray.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.activities.AdminAddVehiclesDialog;
import com.example.ray.activities.AdminVehicleDetailsPage;
import com.example.ray.adapters.AdminVehiclesAdapter;
import com.example.ray.interfaces.RecyclerViewInterface;
import com.example.ray.models.vehiclesModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VehiclesInGarageFragment extends Fragment implements RecyclerViewInterface {

    //arraylist
    ArrayList<vehiclesModel> vehiclesList;

    //initialize variables

    FirebaseDatabase db;
    DatabaseReference ref,refer,reference;
    RecyclerView recyclerView;
    AdminVehiclesAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicles_in_garage, container, false);


        //DB
        db = FirebaseDatabase.getInstance();
        refer = db.getReference().child("vehicles").push();
        String vehicleID = refer.getKey();
        assert vehicleID != null;
        ref = db.getReference().child("Vehicles").child(vehicleID);
        reference = db.getReference().child("Vehicles").child("Vehicles in Garage");

        //RecyclerView
        recyclerView = view.findViewById(R.id.vehiclesRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        vehiclesList = new ArrayList<>();
        adapter = new AdminVehiclesAdapter(getContext(), vehiclesList, this);

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

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }

            private void openDialog() {
                AdminAddVehiclesDialog adminAddVehiclesDialog = new AdminAddVehiclesDialog(getContext());
                adminAddVehiclesDialog.show();
                adminAddVehiclesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                        getActivity().recreate();

                    }
                });
                Window window = adminAddVehiclesDialog.getWindow();

                if (window != null) {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(window.getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // Set the width of the dialog window
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // Set the height of the dialog window
                    window.setAttributes(layoutParams);
                }


            }

        });

        return view;
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(getContext(), AdminVehicleDetailsPage.class);
        intent.putExtra("modelDescription",(vehiclesList.get(position).getModel()));
        intent.putExtra("plateNumber",(vehiclesList.get(position).getPlate()));
        intent.putExtra("VehicleId",(vehiclesList.get(position).getVehicleId()));
        startActivity(intent);

    }
}