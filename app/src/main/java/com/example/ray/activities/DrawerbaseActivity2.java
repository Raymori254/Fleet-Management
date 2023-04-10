package com.example.ray.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.ray.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DrawerbaseActivity2 extends AppCompatActivity{
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
            startActivity(new Intent(DrawerbaseActivity2.this, Login.class));
            finish();

        }

        return super.onOptionsItemSelected(item);
    }


    protected void allocateActivityTitle(String titleString){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }
}
