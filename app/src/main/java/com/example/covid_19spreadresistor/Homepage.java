package com.example.covid_19spreadresistor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid_19spreadresistor.databinding.ActivityHomepageBinding;
import com.example.covid_19spreadresistor.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Homepage extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    Map<String,Object> healthproblems,varyinglocation;
    SharedPreferences sharedprefrence;
    String age="";
    String complications="";
    ActivityHomepageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        healthproblems = new HashMap<>();
        user = FirebaseAuth.getInstance().getCurrentUser();

        sharedprefrence =  this.getSharedPreferences("com.example.covid_19spreadresistor", Context.MODE_PRIVATE);
        varyinglocation = new HashMap<>();

        try {
            if (Locationservice.postalcode.equals("")) {
                new AlertDialog.Builder(this).setIcon(R.drawable.ic_info_black_24dp).setTitle("Please fill the following Details!")
                        .setMessage("Fill your details for ultimate protection").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Homepage.this, Profilesettings.class);
                        startActivity(i);
                    }
                }).setNegativeButton("No", null).show();


            }
        }catch(Exception e) {
              e.printStackTrace();
        }

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user!=null) {
                    reference = FirebaseDatabase.getInstance().getReference("People").child(user.getUid());
                     reference.addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                age=dataSnapshot.child("Age").getValue().toString();
                                complications=dataSnapshot.child("Complications").getValue().toString();
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {

                         }
                     });
                }

                if (binding.rbAge1.isChecked()) {
                    healthproblems.put("Age",binding.rbAge1.getText().toString());
                    sharedprefrence.edit().putString("age", String.valueOf(3)).apply();
                    reference.updateChildren(healthproblems);

                }
                if (binding.rbAge2.isChecked()) {
                    healthproblems.put("Age",binding.rbAge2.getText().toString());
                    sharedprefrence.edit().putString("age", String.valueOf(19)).apply();
                    reference.updateChildren(healthproblems);
                }

                if (binding.rbAge3.isChecked()) {
                    healthproblems.put("Age",binding.rbAge3.getText().toString());
                    sharedprefrence.edit().putString("age", String.valueOf(45)).apply();
                    reference.updateChildren(healthproblems);

                }

                if (binding.rbAge4.isChecked()) {
                    healthproblems.put("Age",binding.rbAge4.getText().toString());
                    sharedprefrence.edit().putString("age", String.valueOf(70)).apply();
                    reference.updateChildren(healthproblems);
                }

                if (binding.cb1.isChecked()) {
                    healthproblems.put("Complications", binding.cb1.getText().toString());
                    sharedprefrence.edit().putString("health", String.valueOf(50)).apply();
                    reference.updateChildren(healthproblems);
                }

                if (binding.cb2.isChecked()) {
                    healthproblems.put("Complications",binding.cb2.getText().toString());
                    sharedprefrence.edit().putString("health", String.valueOf(50)).apply();
                    reference.updateChildren(healthproblems);
                }

                if (binding.cb3.isChecked()) {
                    healthproblems.put("Complications",binding.cb3.getText().toString());
                    sharedprefrence.edit().putString("health", String.valueOf(90)).apply();
                    reference.updateChildren(healthproblems);
                }

                if (binding.cb4.isChecked()) {
                    healthproblems.put("Complications",binding.cb4.getText().toString());
                    sharedprefrence.edit().putString("health", String.valueOf(70)).apply();
                    reference.updateChildren(healthproblems);
                }

                if (!complications.equals("")) {
                    binding.cb1.setVisibility(View.INVISIBLE);
                    binding.cb2.setVisibility(View.INVISIBLE);
                    binding.cb3.setVisibility(View.INVISIBLE);
                    binding.cb4.setVisibility(View.INVISIBLE);
                }
                if (!age.equals("")) {
                    binding.rbAge1.setVisibility(View.INVISIBLE);
                    binding.rbAge2.setVisibility(View.INVISIBLE);
                    binding.rbAge3.setVisibility(View.INVISIBLE);
                    binding.rbAge4.setVisibility(View.INVISIBLE);
                    binding.button3.setVisibility(View.INVISIBLE);
                }
                 Toast.makeText(Homepage.this, "Thank you!", Toast.LENGTH_SHORT).show();
                 binding.button3.setText("Final Changes ?");
                 Intent intent = new Intent(Homepage.this,MainActivity.class);
                 startActivity(intent);

            }
        });

    }
}
