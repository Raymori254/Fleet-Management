package com.example.ray.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.models.imagesModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ImagePicker extends AppCompatActivity {

    //creating variables

    Button galleryBT, cameraBT, upload;
    ImageView imageView;

    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseAuth authProfile;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    Uri imageUri;


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


        imageView = findViewById(R.id.imageView);

//        progressBar.setVisibility(View.INVISIBLE);

        //DB
        db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("Vehicles Images").child(vehicleId);
        storageReference = FirebaseStorage.getInstance().getReference("Images");

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        Uri uri = firebaseUser.getPhotoUrl();

        Picasso.get().load(uri).into(imageView);



        //initialize variables
        galleryBT = findViewById(R.id.gallery);
        galleryBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();

            }

            private void selectImage() {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,2);
            }


        });



        //initialize upload button and set click listener
        upload = findViewById(R.id.picsubmit);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadPic();



            }

        });


    }

    private void uploadPic() {
        if(imageUri != null){

            StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "."
                    + getFileExtension(imageUri));

            //upload to storage and realtime db
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            firebaseUser = authProfile.getCurrentUser();

                            //set image after upload
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileUpdates);

                            imagesModel model = new imagesModel(uri.toString());
                            String modelId = ref.push().getKey();
                            ref.setValue(model);
                            Toast.makeText(ImagePicker.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                    Toast.makeText(ImagePicker.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });



        }else{
            Toast.makeText(ImagePicker.this, "Please Select Image to upload", Toast.LENGTH_SHORT).show();
        }

    }


    private String getFileExtension(Uri uri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            imageView.setImageURI(imageUri);


        }
    }




}