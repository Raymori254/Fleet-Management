package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ray.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginAdmin extends AppCompatActivity {

    //variables declarations

    EditText email, Password;
    Button LoginBT;
    TextView register,forgotPassword;
    Handler handler;

    //firebase variables declarations

    private FirebaseAuth mAuth;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase db;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        //variables initialization

        LoginBT = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.enter_email1);
        Password = (EditText) findViewById(R.id.enter_password1);


        //Toggle Button
        ToggleButton passwordToggleButton = findViewById(R.id.password_toggle_button);
        passwordToggleButton.setTextOn("On");
        passwordToggleButton.setTextOff("Off");



        passwordToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show the password characters
                Password.setInputType(InputType.TYPE_CLASS_TEXT);

            } else {
                // Hide the password characters
                Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }

            // Move the cursor to the end of the text
            Password.setSelection(Password.getText().length());
        });


        //initializing firebase variables
        mAuth = FirebaseAuth.getInstance();

        //making buttons clickable

        LoginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //starts userLogin method

                userLogin();

            }

            private void userLogin() {



                //user credentials are gotten and converted back to String
                String Email = email.getText().toString().trim();//trim caters for any extra space the user might create
                String password = Password.getText().toString().trim();

                //validation of credentials
                if(Email.isEmpty()){
                    email.setError("Email is required!");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    email.setError("Enter a valid Email!");
                    email.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    Password.setError("Password is required!");
                    Password.requestFocus();
                    return;
                }
                if(password.length() <6){
                    Password.setError("Password should contain a minimum of  6 characters");
                    Password.requestFocus();
                }

                ProgressDialog dialog = new ProgressDialog(LoginAdmin.this);




                //sign user in with email and password
                mAuth.signInWithEmailAndPassword(Email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                if(task.isSuccessful()){
                                    dialog.setMessage("Signing In...");
                                    dialog.show();

                                    handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            //checking to see if email is verified

                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            assert user != null;
                                            if(user.isEmailVerified()) {

                                                String currentUserId = user.getUid();

                                                db = FirebaseDatabase.getInstance();
                                                ref = db.getReference();

                                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                                DatabaseReference ref = db.getReference()
                                                        .child("Users")
                                                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                                        .child("Personal Details")
                                                        .child("type");


                                                //Checking the usertype before redirecting to appropriate screens

                                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()){
                                                            String userlevel = snapshot.getValue(String.class);

                                                            if (userlevel.equals("driver")){

                                                                Toast.makeText(LoginAdmin.this, "You Do Not Have Credentials To Log In as Admin", Toast.LENGTH_SHORT).show();
                                                                FirebaseAuth.getInstance().signOut();
                                                                dialog.dismiss();

                                                            }
                                                            else if (userlevel.equals("admin")){

                                                                startActivity(new Intent(LoginAdmin.this, AdminMainActivity.class));
                                                                finish();
                                                            }
                                                        } else{
                                                            Toast.makeText(LoginAdmin.this, "User data does not exist.", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                        Toast.makeText(LoginAdmin.this, "Error getting user data.", Toast.LENGTH_SHORT).show();

                                                    }
                                                });






                                            } else{
                                                //send email verification link
                                                user.sendEmailVerification();
                                                Toast.makeText(LoginAdmin.this, "Check your Email to verify account To sign In", Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                            }

                                        }
                                    },1500);



                                }
                                else {

                                    Toast.makeText(LoginAdmin.this, "Failed to Login Admin!", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();


                                }

                            }
                        });

            }
        });


    }


}