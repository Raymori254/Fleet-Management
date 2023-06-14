package com.example.ray.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ray.R;
import com.example.ray.fragments.ContactUs;
import com.example.ray.fragments.FAQs;
import com.example.ray.fragments.Profile;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //variables declaration

    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    FirebaseUser firebaseUser;
    private FirebaseUser user;
    private DatabaseReference reference;
    public String userID;
    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view){

        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Drawer_open,R.string.Drawer_close );
        toggle.syncState();


        //initializing firebase variable
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");//the referenced Users is from the database//
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();


    }
    //toolbar menu to enable option for log out
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.LogoutToolbar) {


            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(DrawerBaseActivity.this, SignInAs.class));
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Fragment fragment;

        switch(id){


            case R.id.Profile:

                fragment = new Profile();

            {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout, fragment);
                ft.commit();

            }
            break;


            case R.id.FAQ:

                fragment = new FAQs();

            {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout, fragment);
                ft.commit();

            }
            break;


            case R.id.nav_settings:

                fragment = new ContactUs();

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout, fragment);
                ft.commit();
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;


    }

    protected void allocateActivityTitle(String titleString){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }

    public abstract void onMapReady(@NonNull GoogleMap googleMap);
}

