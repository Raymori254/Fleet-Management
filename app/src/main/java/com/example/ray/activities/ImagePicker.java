package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.adapters.AdminVehiclesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ImagePicker extends AppCompatActivity {

    //creating variables

    Button galleryBT, cameraBT, upload;
    ImageView imageView;
    Uri imageUri;

    FirebaseDatabase db;
    DatabaseReference ref,refer,reference;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_SELECT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        //Getting the details from the previous page
        String vehicleId = "vehicleIdentification";

        //bundle to get extras passed through intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vehicleId = extras.getString("vehicleIdentification");
        }


        //DB
        db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("VehiclesImages").child(vehicleId);

        //initialize variables
        galleryBT = findViewById(R.id.gallery);
        galleryBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();


            }

            private void selectImage() {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_SELECT);
            }


        });

        cameraBT = findViewById(R.id.camera);
        cameraBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        imageView = findViewById(R.id.imageView);

        //initialize upload button and set click listener
        upload = findViewById(R.id.picsubmit);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadImage();





            }

        });


}

    private void uploadImage() {


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> item = new HashMap<>();
                item.put("vehicleImage", imageUri);
                ref.setValue(item);
                Toast.makeText(ImagePicker.this, "Image Updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle error
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && data != null && data.getData() != null){

            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }
    }
}