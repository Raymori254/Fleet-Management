package com.example.ray.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.ray.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DriverEditDetailsDialog extends AppCompatDialog {

    Context context;
    //initialize variables
    FirebaseDatabase db;
    DatabaseReference reference;


    public DriverEditDetailsDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_edit_driver_name);


        //EditText
        EditText name = findViewById(R.id.DriverName);
        EditText phoneNumber = findViewById(R.id.PhoneNumber);

        //DB
        db = FirebaseDatabase.getInstance();
        reference = db.getReference().child("Users").child((FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child("Personal Details");

        //edit text for model and plate
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentName = (String) snapshot.child("fullName").getValue();
                String currentPhone = (String) snapshot.child("phoneNumber").getValue();

                name.setText(currentName);
                phoneNumber.setText(currentPhone);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button submit = findViewById(R.id.usubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String driverName = name.getText().toString();
                String mobileNumber = phoneNumber.getText().toString();

                //confirm content is provided on the editTexts
                if (driverName.isEmpty()) {
                    name.setError("A description of the model is required");
                    return;
                } else if (mobileNumber.isEmpty()) {
                    phoneNumber.setError("Please Provide a number plate");
                    return;
                } else {
                    addDataToFirebase(driverName, mobileNumber);
                    dismiss();
                }

            }
        });
    }


    private void addDataToFirebase(String driverName, String mobileNumber) {

        //add the data captured to DB
        Map<String, Object> item = new HashMap<>();
        item.put("fullName", driverName);
        item.put("phoneNumber", mobileNumber);
        reference.updateChildren(item);
        Toast.makeText(getContext(), "Details Updated", Toast.LENGTH_SHORT).show();



    }

}
