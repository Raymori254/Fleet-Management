package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.activities.Login;
import com.example.ray.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class splashScreen extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        mFirebaseAuth = FirebaseAuth.getInstance();
        //hide title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        new Handler().postDelayed(new Runnable(){

            public void run(){
//                Intent intent = new Intent(splashScreen.this, Login.class);
//                startActivity(intent);
//                finish();

                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null){
                    //there is some user logged in take to home page

                    //check the access level before redirecting

                    //Checking the usertype before redirecting to appropriate screens

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference ref = db.getReference()
                            .child("Users")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .child("Personal Details")
                            .child("type");

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                String userlevel = snapshot.getValue(String.class);

                                if (userlevel.equals("driver")){
                                    startActivity(new Intent(splashScreen.this, MainActivity.class));
                                    finish();

                                }
                                else if (userlevel.equals("admin")){

                                    startActivity(new Intent(splashScreen.this, AdminMainActivity.class));
                                    finish();
                                }
                            } else{

                                Intent intent = new Intent(splashScreen.this, Login.class);
                                startActivity(intent);
                                finish();


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                           //Do nothing

                        }
                    });

                }
                else{
                    startActivity(new Intent(splashScreen.this, Login.class));
                }

            }
        },1000);

    }

}