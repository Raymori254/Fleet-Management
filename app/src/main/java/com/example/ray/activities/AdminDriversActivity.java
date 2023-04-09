package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.ray.R;
import com.example.ray.adapters.AdminDriversAdapter;
import com.example.ray.adapters.AdminVehiclesAdapter;
import com.example.ray.databinding.ActivityAdminDriversBinding;
import com.example.ray.interfaces.RecyclerViewInterface;
import com.example.ray.models.driversModel;
import com.example.ray.models.vehiclesModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminDriversActivity extends DrawerbaseActivity2 implements RecyclerViewInterface {

    //arraylist
    ArrayList<driversModel> driversList;

    //Drawebase
    ActivityAdminDriversBinding activityAdminDriversBinding;

    //initialize variables

    FirebaseDatabase db;
    DatabaseReference ref;
    RecyclerView recyclerView;
    AdminDriversAdapter adapter;
    String imageString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_drivers);

        activityAdminDriversBinding = ActivityAdminDriversBinding.inflate(getLayoutInflater());
        setContentView(activityAdminDriversBinding.getRoot());

        setTitle("Drivers");

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
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    //if statement to exclude the admin from being displayed as a user driver
                    if(!dataSnapshot.child("Personal Details").child("type").getValue(String.class).equals("admin")) {
                        driversModel vm = dataSnapshot.child("Personal Details").getValue(driversModel.class);
                        driversList.add(vm);

                        adapter.notifyDataSetChanged();
                        adapter.notifyItemInserted(driversList.size() - 1);
                    }
                }


                recyclerView.setAdapter(adapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //on clicking the recyclerview item
    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(this, AdminDriverDetailsPage.class);
        intent.putExtra("name",(driversList.get(position).getFullName()));
        intent.putExtra("email",(driversList.get(position).getEmail()));
        intent.putExtra("phoneNumber",(driversList.get(position).getPhoneNumber()));
        intent.putExtra("driverId",(driversList.get(position).getUserID()));

        startActivity(intent);

    }
}