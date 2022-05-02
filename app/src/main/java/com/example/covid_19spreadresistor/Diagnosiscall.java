package com.example.covid_19spreadresistor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Diagnosiscall extends AppCompatActivity {
  CheckBox c1,c2,c3,c4;
  Button nut;
  DatabaseReference ref,selfref,ref2;
  FirebaseAuth auth;
  FirebaseUser user;
  String pos,neg,qur,cure;
  Map<String,Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosiscall);
        c1=findViewById(R.id.positivebox);
        c2=findViewById(R.id.negativebox);
        c3=findViewById(R.id.quarantinedbox);
        c4=findViewById(R.id.curedbox);
        nut=findViewById(R.id.button4);
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        map=new HashMap<>();
        pos="Positives";
        neg="Negatives";
        qur="Quarantined";
        cure="Cured";
        nut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c1.isChecked()) {
                    ref=FirebaseDatabase.getInstance().getReference().child("Diagnosed").child(pos).child(user.getUid());
                    ref2=ref.child("All possible victims");
                    selfref=FirebaseDatabase.getInstance().getReference("People").child(user.getUid());
                    selfref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            map.put("name",dataSnapshot.child("Name").getValue().toString());
                            Iterator<DataSnapshot> possiblevictims=dataSnapshot.child("Bluetooth Exposure").getChildren().iterator();
                             while(possiblevictims.hasNext()) {

                                 DataSnapshot victim = possiblevictims.next();
                                 map.put(victim.getKey(), victim.getValue().toString());
                                 ref2.updateChildren(map);

                             }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
    }



}
