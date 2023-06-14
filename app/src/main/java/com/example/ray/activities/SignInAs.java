package com.example.ray.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ViewSwitcher;

import com.example.ray.R;

public class SignInAs extends AppCompatActivity {

    Button driverBtn,adminBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_as);

        driverBtn = (Button) findViewById(R.id.driverBtn);
        adminBtn = (Button) findViewById(R.id.adminBtn);

        //driver button
        driverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent i = new Intent(SignInAs.this, Login.class);
               startActivity(i);
            }
        });
        //admin button
        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SignInAs.this, LoginAdmin.class);
                startActivity(i);
            }
        });
    }

    //action for when the back button on device is pressed
    public void onBackPressed(){
        finishAffinity();

    }
}