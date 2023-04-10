package com.example.ray.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.activities.AssignedVehiclesList;
import com.example.ray.activities.DrawerBaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends Fragment {

    //creating variables
    Button selectAssignedVehicle;
    TextView text1,text2,text3;
    DatabaseReference ref;
    FirebaseDatabase db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate the view
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //initialize variable
        text1 = view.findViewById(R.id.welcomeTextId1);
        text2 = view.findViewById(R.id.welcomeTextId2);
        text3 = view.findViewById(R.id.welcomeTextId3);

        //db to get the text from db and place into the text
        db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("App Settings").child("WelcomeText");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String line1 = (String) snapshot.child("Line1").getValue();
                String line2 = (String) snapshot.child("Line2").getValue();
                String line3 = (String) snapshot.child("Line3").getValue();

                text1.setText(line1);
                text2.setText(line2);
                text3.setText(line3);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

}
