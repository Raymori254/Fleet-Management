package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.databinding.ActivityMainBinding;
import com.example.ray.fragments.FAQs;
import com.example.ray.fragments.Home;
import com.example.ray.fragments.Profile;
import com.example.ray.fragments.Vehicles;
import com.example.ray.fragments.moreInfo;
import com.example.ray.fragments.settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends DrawerBaseActivity {

    //Variables declaration
    public DrawerLayout drawerLayout;
    private long backPressed;
    private static  final int TIME_INTERVAL = 2000;

    BottomNavigationView bottomNavigationView;

    //creating the binding variable

    ActivityMainBinding mainActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityBinding =   ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainActivityBinding.getRoot());

        setTitle("FLEET MANAGEMENT");

        //Bottom Navigation Icons

        Home homeFragment = new Home();
        Vehicles vehiclesFragment = new Vehicles();
        settings settingsFragment = new settings();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.Home:

                        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, homeFragment).commit();

                        return true;

                    case R.id.Vehicles:

                        startActivity(new Intent(MainActivity.this, userLocation.class));

//                        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, vehiclesFragment).commit();

                        return true;

                    case R.id.Settings_bottom:

                        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, settingsFragment).commit();

                        return true;
                }

                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.Home);

    }





    //action for when the back button on device is pressed
    public void onBackPressed(){

        if(backPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            finishAffinity();
        }
        else {
            Toast.makeText(this, "Press Back Again To Exit", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();

    }

}