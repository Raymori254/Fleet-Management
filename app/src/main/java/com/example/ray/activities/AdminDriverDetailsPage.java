package com.example.ray.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ray.R;

public class AdminDriverDetailsPage extends AppCompatActivity {

    //creating variables
    TextView nameTV,emailTV, phoneTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_driver_details_page);

        //initialize variables
        nameTV = findViewById(R.id.fullNameID);
        emailTV = findViewById(R.id.emailID);
        phoneTV = findViewById(R.id.phoneNumberID);


        //Getting the details from the previous page
        String name = "name";
        String email = "email";
        String phoneNumber = "phoneNumber";


        //bundle to get extras passed through intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            email = extras.getString("email");
            phoneNumber = extras.getString("phoneNumber");

        }

        //setting textview
        nameTV.setText(name);
        emailTV.setText(email);
        phoneTV.setText(phoneNumber);

    }
}