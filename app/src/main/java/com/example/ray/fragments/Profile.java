package com.example.ray.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.activities.AdminAddVehiclesDialog;
import com.example.ray.activities.DriverEditDetailsDialog;
import com.example.ray.activities.Login;
import com.example.ray.activities.MainActivity;
import com.example.ray.activities.driverImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.common.subtyping.qual.Bottom;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Profile extends Fragment {
    
    //Creating variables
    Button editPicture,editDetails;
    ImageView imageView;
    TextView name,phonenumber,status;


    //DB
    DatabaseReference imageRef,reference,ref;
    FirebaseDatabase db = FirebaseDatabase.getInstance();




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        
        //initialize variables
        editPicture = view.findViewById(R.id.editPicture);


        imageView = view.findViewById(R.id.ImageID);
        
        name = view.findViewById(R.id.fullNameID);
        phonenumber = view.findViewById(R.id.phoneNumberID);
        status = view.findViewById(R.id.statusID);
        editDetails = view.findViewById(R.id.editDetails);

        //making buttons clickable
        editPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send to edit pic activity
                startActivity(new Intent(getContext(), driverImagePicker.class));

            }
        });


        String driverImageID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        imageRef = db.getReference().child("Driver Images").child(driverImageID);
        imageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String vehicleImage = (String) snapshot.child("imageUri").getValue();

                Picasso.get().load(vehicleImage).into(imageView);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //setName to name Textview
        reference = db.getReference().child("Users").child((FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child("Personal Details");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String driverName = (String) snapshot.child("fullName").getValue();
                String driverPhone = (String) snapshot.child("phoneNumber").getValue();
                name.setText(driverName);
                phonenumber.setText(driverPhone);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //check if assigned vehicle exists
        ref =  db.getReference().child("Users").child((FirebaseAuth.getInstance().getCurrentUser()).getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Assigned Vehicle").exists()){

                    status.setText("You have a Vehicle Assigned");
                    status.setTextColor(getResources().getColor(R.color.blue));

                }
                else{

                    status.setText("You do not have a Vehicle Assigned");
                    status.setTextColor(getResources().getColor(R.color.red));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //editName
        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDialog();



            }
        });


        return view;
    }

    private void openDialog() {

        DriverEditDetailsDialog driverEditDetailsDialog = new DriverEditDetailsDialog(getContext());
        driverEditDetailsDialog.show();
        driverEditDetailsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {

                    startActivity(new Intent(getContext(), MainActivity.class));

                }
            });
            Window window = driverEditDetailsDialog.getWindow();

            if (window != null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(window.getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // Set the width of the dialog window
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // Set the height of the dialog window
                window.setAttributes(layoutParams);
            }



    }
}