package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.adapters.AdminVehiclesAdapter;
import com.example.ray.databinding.ActivityAssignedVehiclesListBinding;
import com.example.ray.databinding.ActivityMainBinding;
import com.example.ray.fragments.Vehicles;
import com.example.ray.interfaces.RecyclerViewInterface;
import com.example.ray.models.vehiclesModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AssignedVehiclesList extends DrawerBaseActivity {


    //arraylist
    ArrayList<vehiclesModel> AssignedvehiclesList;

    //Variables
    TextView modelTV, plateTV, tv1, tv2, tv3, tv4, tv5;
    ImageView image;
    ActivityAssignedVehiclesListBinding activityAssignedVehiclesListBinding;
    ActivityMainBinding mainActivityBinding;

    //Firebase
    DatabaseReference ref;

    //FirebaseDatabase
    FirebaseDatabase db;
    DatabaseReference reference, imageRef, checkRef;

    //Location
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    SupportMapFragment supportMapFragment;
    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        activityAssignedVehiclesListBinding =   ActivityAssignedVehiclesListBinding.inflate(getLayoutInflater());
//        setContentView(activityAssignedVehiclesListBinding.getRoot());

        setTitle("FLITRACKA");


        db = FirebaseDatabase.getInstance();
        checkRef = db.getReference().child("Users").child((FirebaseAuth.getInstance().getCurrentUser()).getUid());
        checkRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Assigned Vehicle").exists()) {
                    setContentView(R.layout.activity_assigned_vehicles_list);


                    modelTV = findViewById(R.id.modelDescriptionIDFrag);
                    plateTV = findViewById(R.id.plateNumberIDFrag);
                    image = findViewById(R.id.ImageID);

                    //DB
                    db = FirebaseDatabase.getInstance();


                    ref = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .child("Assigned Vehicle");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                String modelDescription = (String) dataSnapshot.child("model").getValue();
                                String NumberPlate = (String) dataSnapshot.child("plate").getValue();
                                String vehicleId = (String) dataSnapshot.child("vehicleId").getValue();

                                modelTV.setText(modelDescription);
                                plateTV.setText(NumberPlate);

                                //imageview
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
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    //Location, initializing fusedLocationProviderClient
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AssignedVehiclesList.this);
                    fetchLastLocation();

                    //initialize textviews
                    tv1 = findViewById(R.id.tv1);
                    tv2 = findViewById(R.id.tv2);
                    tv3 = findViewById(R.id.tv3);
                    tv4 = findViewById(R.id.tv4);
                    tv5 = findViewById(R.id.tv5);

                    //check permission location
                    if (ActivityCompat.checkSelfPermission(AssignedVehiclesList.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //when permission is granted

                        getLocation();

                    } else {
                        //when permission is not granted
                        ActivityCompat.requestPermissions((Activity) AssignedVehiclesList.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                    }


                } else {

                    setContentView(R.layout.activity_assigned_no_vehicles);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //initialize location
                Location location = task.getResult();
                if (location != null) {
                    //Initialize geoCoder
                    Geocoder geocoder = new Geocoder(AssignedVehiclesList.this,
                            Locale.getDefault());
                    //Initialize address list
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        //set latitude on TextView
                        tv1.setText(Html.fromHtml(
                                "<font color = '#6200EE'><b>Latitude:</b><br></font>"
                                        + addresses.get(0).getLatitude()
                        ));
                        //set longitude on TextView
                        tv2.setText(Html.fromHtml(
                                "<font color = '#6200EE'><b>Longitude:</b><br></font>"
                                        + addresses.get(0).getLongitude()
                        ));
                        //set country name
                        tv3.setText(Html.fromHtml(
                                "<font color = '#6200EE'><b>Country Name:</b><br></font>"
                                        + addresses.get(0).getCountryName()
                        ));
                        //set locality
                        tv4.setText(Html.fromHtml(
                                "<font color = '#6200EE'><b>Locality:</b><br></font>"
                                        + addresses.get(0).getLocality()
                        ));
                        //set address
                        tv5.setText(Html.fromHtml(
                                "<font color = '#6200EE'><b>Address:</b><br></font>"
                                        + addresses.get(0).getAddressLine(0)
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(AssignedVehiclesList.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AssignedVehiclesList.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( AssignedVehiclesList.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
//                    Toast.makeText(getContext(), currentLocation.getLatitude()
//                            +""+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager()
                            .findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(AssignedVehiclesList.this:: onMapReady);

                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("I am Here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        googleMap.addMarker(markerOptions);

        //Add location to database
        db = FirebaseDatabase.getInstance();
        reference = db.getReference().child("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child("Current Location");

        reference.setValue(latLng).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
//                    Toast.makeText(AssignedVehiclesList.this, "Location Saved", Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(AssignedVehiclesList.this, "Location Not Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }
}