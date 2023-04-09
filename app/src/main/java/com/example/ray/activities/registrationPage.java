package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ray.R;
import com.example.ray.models.usersModel;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class registrationPage extends AppCompatActivity {

    //Variable declarations
    EditText FullName, Email, Password, confirmPasswordET,PhoneNumber;
    Button createUser;
    TextView driver,loginBT;

    //firebase declarations
    FirebaseAuth myAuth;
    DatabaseReference reference;
    FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        //variable initialization
        FullName = findViewById(R.id.fullNames);
        Email = findViewById(R.id.enter_email);
        PhoneNumber = findViewById(R.id.phoneNumber);
        Password = findViewById(R.id.enter_password);
        confirmPasswordET = findViewById(R.id.confirm_password);
        driver = findViewById(R.id.driver);
        loginBT = findViewById(R.id.Login);

        createUser = findViewById(R.id.create_account);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        myAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

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

        ToggleButton confirmpasswordToggleButton = findViewById(R.id.password_toggle_button2);
        confirmpasswordToggleButton.setTextOn("On");
        confirmpasswordToggleButton.setTextOff("Off");



        confirmpasswordToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show the password characters
                confirmPasswordET.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                // Hide the password characters
                confirmPasswordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }

            // Move the cursor to the end of the text
            confirmPasswordET.setSelection(confirmPasswordET.getText().length());
        });



        //make button clickable
        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(registrationPage.this, Login.class);
                startActivity(i);
            }
        });

            createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                //onclick activates register user method
                registerUser();
                
            }


            private void registerUser() {

                //Takes the inputs from the registration forms and converting them to strings
                
                String email = Email.getText().toString().trim();
                String fullName = FullName.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String phoneNumber = PhoneNumber.getText().toString().trim();
                String confirmpassword = confirmPasswordET.getText().toString().trim();
                String type = driver.getText().toString().trim();


//                //to ensure confirm password matches with password provided
//
//                confirmPasswordET.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        //nothing happens
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//
//                        // Compare the strings
//                        if (password.equals(confirmpassword)) {
//                            // Passwords match, do something here
//                            // Passwords match, clear the error message
//
//                            confirmPasswordET.setText("");
//                        } else {
//                            // Passwords don't match, show the error message
//                            confirmPasswordET.setError("Passwords should match");
//
//                        }
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//
//                    }
//                });



                //if statements to validate the inputs above

                if (fullName.isEmpty()) {
                    FullName.setError("full names are required");
                    FullName.requestApplyInsets();
                    return;
                }
                if (email.isEmpty()) {
                    Email.setError("An email is required");
                    Email.requestApplyInsets();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Email.setError("please provide a valid email");
                    Email.requestFocus();
                    return;
                }
                if (phoneNumber.isEmpty()) {
                    PhoneNumber.setError("A Phone Number is required");
                    PhoneNumber.requestApplyInsets();
                    return;
                }
                if (password.isEmpty()) {
                    Password.setError("Password is required");
                    Password.requestApplyInsets();
                    return;
                }
                if (password.length() < 6) {
                    Password.setError("password should consist of more than 6 characters");
                    Password.requestFocus();
                    return;
                }
                if (password.equals(confirmpassword)) {
                    // Passwords match, do something here
                    // Passwords match, clear the error message

                    confirmPasswordET.setText("");
                } else if(!password.equals(confirmpassword)){
                    // Passwords don't match, show the error message
                    confirmPasswordET.setError("Passwords should match");
                    return;

                }







                //activate progressdialog if all is ok
                ProgressDialog dialog = new ProgressDialog(registrationPage.this);
                dialog.setMessage("Registering...");
                dialog.show();

                //create and add user to db
                myAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //check to see if the user is registered

                                if (task.isSuccessful()){

                                    String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                                    @SuppressLint("RestrictedApi")
                                    usersModel user =  new usersModel(fullName, email,phoneNumber, password, type, userId);

                                    //sending user to DB
                                    reference = FirebaseDatabase.getInstance().getReference("Users")
                                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                            .child("Personal Details");
                                    Map<String, Object> item = new HashMap<>();
                                    item.put("fullName", fullName);
                                    item.put("email", email);
                                    item.put("password", password);
                                    item.put("type", type);
                                    item.put("phoneNumber", phoneNumber);
                                    item.put("userID", userId);

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                            .child("Personal Details")
                                            .setValue(item).addOnCompleteListener(new OnCompleteListener<Void>(){
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {




                                                    if (task.isSuccessful()) {
                                                        dialog.setMessage("Checking Verification...");
                                                        dialog.show();
                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                        if (user.isEmailVerified()) {
                                                            dialog.dismiss();
                                                            Intent intent = new Intent(registrationPage.this, Login.class);
                                                            intent.putExtra("userId", userId);
                                                            startActivity(intent);
                                                        } else {

                                                            user.sendEmailVerification();
                                                            Toast.makeText(registrationPage.this, "Check your Email to verify account", Toast.LENGTH_LONG).show();
                                                            dialog.dismiss();
                                                            finish();
                                                        }
                                                    } else {
                                                        Toast.makeText(registrationPage.this, "Registration Failed! Try again!", Toast.LENGTH_LONG).show();
                                                        dialog.dismiss();
                                                    }

                                                }
                                            });

                                }else {
                                    Toast.makeText(registrationPage.this, "Registration Failed! Try again!", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            }
                        });

                dialog.dismiss();



                
            }
        });

    }
}