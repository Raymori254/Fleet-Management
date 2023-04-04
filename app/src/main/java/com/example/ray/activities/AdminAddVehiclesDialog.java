package com.example.ray.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.cardview.widget.CardView;

import com.example.ray.R;
import com.example.ray.models.vehiclesModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AdminAddVehiclesDialog extends AppCompatDialog{

    //initialize variables
    FirebaseDatabase db;
    DatabaseReference ref,refer,reference;

    public AdminAddVehiclesDialog(@NonNull Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_admin_add_vehicles);

        //DB
        db = FirebaseDatabase.getInstance();
        refer = db.getReference().child("vehicles").push();
        String vehicleID = refer.getKey();
        assert vehicleID != null;
        ref = db.getReference().child("Vehicles").child("Vehicles in Garage").child(vehicleID);
        reference = db.getReference().child("Vehicles");

        //edit text for model and plate
        EditText modelTV = findViewById(R.id.ModelName);
        EditText plateNumber = findViewById(R.id.plate2);

        //submit button to send the details to db
        Button submit = findViewById(R.id.usubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String modelD = modelTV.getText().toString();
                String plateD = plateNumber.getText().toString();
                String vehicleIde = vehicleID;

                //confirm content is provided on the editTexts
                if(modelD.isEmpty()){
                    modelTV.setError("A description of the model is required");
                    return;
                }
                else if(plateD.isEmpty()){
                    plateNumber.setError("Please Provide a number plate");
                    return;
                }
                else{
                    addDataToFirebase(modelD,plateD,vehicleIde);
                    dismiss();
                }
            }



            private void addDataToFirebase(String modelDescription, String plateDescription, String vehicleIdentification) {
                vehiclesModel vehicles = new vehiclesModel();


                //add the data captured to DB
                Map<String, Object> item = new HashMap<>();
                item.put("model", modelDescription);
                item.put("plate", plateDescription);
                item.put("vehicleId", vehicleIdentification);
                ref.setValue(item);
                Toast.makeText(getContext(), "Vehicle Added", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
