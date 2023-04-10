package com.example.ray.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ray.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FAQs extends Fragment {

    //Creating variables
    DatabaseReference ref;
    FirebaseDatabase db;
    TextView q1,q2,q3,q4,a1,a2,a3,a4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_f_a_qs, container, false);

        //initialize textviews
        q1 = view.findViewById(R.id.Question1ID);
        a1 = view.findViewById(R.id.Answer1ID);
        q2 = view.findViewById(R.id.Question2ID);
        a2 = view.findViewById(R.id.Answer2ID);
        q3 = view.findViewById(R.id.Question3ID);
        a3 = view.findViewById(R.id.Answer3ID);
        q4 = view.findViewById(R.id.Question4ID);
        a4 = view.findViewById(R.id.Answer4ID);

        //db to get the text from db and place into the text
        db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("App Settings").child("FAQs");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Question1 = (String) snapshot.child("Question1").getValue();
                String Question2 = (String) snapshot.child("Question2").getValue();
                String Question3 = (String) snapshot.child("Question3").getValue();
                String Question4 = (String) snapshot.child("Question4").getValue();
                String Answer1 = (String) snapshot.child("Answer1").getValue();
                String Answer2 = (String) snapshot.child("Answer2").getValue();
                String Answer3 = (String) snapshot.child("Answer3").getValue();
                String Answer4 = (String) snapshot.child("Answer4").getValue();

                //set The texts from db into textviews
                q1.setText(Question1);
                q2.setText(Question2);
                q3.setText(Question3);
                q4.setText(Question4);
                a1.setText(Answer1);
                a2.setText(Answer2);
                a3.setText(Answer3);
                a4.setText(Answer4);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}