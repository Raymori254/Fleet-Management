package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.models.driversModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class AdminDriverDetailsPage extends AppCompatActivity {

    //creating variables
    TextView nameTV,emailTV,phoneTV, statusTV;
    Button deleteUser;

    ImageView imageView;

    //Database Variable
    DatabaseReference ref,reference;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_driver_details_page);

        //initialize variables
        nameTV = findViewById(R.id.fullNameID);
        emailTV = findViewById(R.id.emailID);
        phoneTV = findViewById(R.id.phoneID);
        statusTV = findViewById(R.id.statusID);



        //Getting the details from the previous page
        String name = "name";
        String email = "email";
        String phone = "phoneNumber";
        String driverId = "driverId";



        //bundle to get extras passed through intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            email = extras.getString("email");
            phone = extras.getString("phoneNumber");
            driverId = extras.getString("driverId");


        }

        //setting textview
        nameTV.setText(name);
        emailTV.setText(email);
        emailTV.setPaintFlags(emailTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        phoneTV.setText(phone);
        phoneTV.setPaintFlags(phoneTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        String finalEmail = email;



        //when email is clicked send mail
        emailTV.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"LongLogTag", "IntentReset"})
            @Override
            public void onClick(View view) {
                Log.i("Send email", "");
                String[] TO = {finalEmail};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:" + finalEmail));


                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "EMAIL FROM FLITRACKA");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                    Log.i("Finished sending email...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(AdminDriverDetailsPage.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //when phone icon is clicked
        String finalPhone = phone;
        String finalPhone1 = phone;
        phoneTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("address", finalPhone1);
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);

            }
        });



        //initialize the db variables
        db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("Users").child(driverId);

        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    //if statement to check if driver is assigned vehicle
                    if(snapshot.child("Assigned Vehicle").exists()) {

                        statusTV.setText("Vehicle Assigned");
                        statusTV.setTextColor(getResources().getColor(R.color.blue));

                    }
                    else{

                        statusTV.setText("No Vehicle Assigned");
                        statusTV.setTextColor(getResources().getColor(R.color.red));
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageView = findViewById(R.id.ImageID);
        reference = db.getReference().child("Driver Images").child(driverId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String vehicleImage = (String) snapshot.child("imageUri").getValue();

                Picasso.get().load(vehicleImage).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}