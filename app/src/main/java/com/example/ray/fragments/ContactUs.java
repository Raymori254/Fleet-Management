package com.example.ray.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ray.R;
import com.example.ray.activities.AdminDriverDetailsPage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactUs extends Fragment {

    //Creating variables
    DatabaseReference ref;
    FirebaseDatabase db;
    TextView email,number,twitter,instagram,facebook;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_u_s, container, false);


        //initialize textviews
        email = view.findViewById(R.id.emailID);
        number = view.findViewById(R.id.numberID);
        twitter = view.findViewById(R.id.twitterID);
        instagram = view.findViewById(R.id.instagramID);
        facebook  = view.findViewById(R.id.fbID);

        //db to get the text from db and place into the text
        db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("App Settings").child("Contact");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String mail = (String) snapshot.child("email").getValue();
                String ig = (String) snapshot.child("instagram").getValue();
                String tweet = (String) snapshot.child("twitter").getValue();
                String no = (String) snapshot.child("phoneNumber").getValue();
                String fb = (String) snapshot.child("facebook").getValue();

                //send mail
                email.setText(mail);
                email.setPaintFlags(email.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                email.setOnLongClickListener(new View.OnLongClickListener() {
                    @SuppressLint("IntentReset")
                    @Override
                    public boolean onLongClick(View view) {

                        Log.i("Send email", "");
                        String[] TO = {mail};
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:" + mail));


                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "EMAIL FROM FLITRACKA");
                        startActivity(emailIntent);

                        return false;
                    }
                });
               //open ig
                instagram.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Uri uri = Uri.parse(ig);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        return false;
                    }
                });
                //open twitter
                twitter.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Uri uri = Uri.parse(tweet);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        return false;
                    }
                });
                //send text message
                number.setText(no);
                number.setPaintFlags(number.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                number.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.putExtra("address", no);
                        sendIntent.setType("vnd.android-dir/mms-sms");
                        startActivity(sendIntent);
                        return false;
                    }
                });
              //open facebook
                facebook.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Uri uri = Uri.parse(fb);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        return false;
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}