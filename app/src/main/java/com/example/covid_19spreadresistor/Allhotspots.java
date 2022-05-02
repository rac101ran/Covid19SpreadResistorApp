package com.example.covid_19spreadresistor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class Allhotspots extends AppCompatActivity {
DatabaseReference ref,ref2;
FirebaseAuth auth;
FirebaseUser user;
String str,country;
TextView confirmed,cured,deaths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allhotspots);
        auth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("People").child(user.getUid()).child("Location trace");
        ref2=FirebaseDatabase.getInstance().getReference("FACTS");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                str=dataSnapshot.getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it=dataSnapshot.getChildren().iterator();
                 while(it.hasNext()) {
                        DataSnapshot i=it.next();
                        if(i.getKey().equals(str)) {
                               Iterator<DataSnapshot> it2=i.getChildren().iterator();
                               while(it2.hasNext()) {
                                      DataSnapshot j=it2.next();
                                      j.child("Confirmed").getValue().toString();

                               }

                        }

                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
 }
