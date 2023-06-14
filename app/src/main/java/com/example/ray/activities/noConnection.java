package com.example.ray.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ray.R;

public class noConnection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);
    }
    //action for when the back button on device is pressed
    public void onBackPressed(){

        stopService(new Intent(this, noConnection.class));
        super.onBackPressed();
        finishAffinity();
        }

}