package com.example.covid_19spreadresistor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.security.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Dailysynptomscheck extends AppCompatActivity {
TextView fc1,fc2,fc3,fc4,rc1,rc2,rc4;
Button b;
CheckBox box1,box2,box3;
EditText ans;
String msg;
String fctext2, fctext3,fctext4;
FirebaseAuth auth;
FirebaseUser user;
DatabaseReference reference;
Map<String,Object> problems;
SharedPreferences shared;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailysynptomscheck);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("People").child(user.getUid());
        problems = new HashMap<>();
        shared=(SharedPreferences)this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        b = findViewById(R.id.button);
        ans = findViewById(R.id.anstext);
        fc1 = findViewById(R.id.firstchatbox);
        rc1 = findViewById(R.id.firchatreply);
        fc2 = findViewById(R.id.secchat);
        rc2 = findViewById(R.id.secchatreply);
        fc3 = findViewById(R.id.thirdchat);

        box1 = findViewById(R.id.feverc);
        box2 = findViewById(R.id.NOFEVERc);
        box3 = findViewById(R.id.nocough);

        box1.setText("Cough & Fever & Fatigue");
        box2.setText("Cough & Fever");
        box3.setText("Nothing");

        box1.setVisibility(View.INVISIBLE);
        box2.setVisibility(View.INVISIBLE);
        box3.setVisibility(View.INVISIBLE);
        fc4 = findViewById(R.id.finalchat);
        rc4 = findViewById(R.id.finalchatreply);

        rc1.setVisibility(View.INVISIBLE);
        fc2.setText("");
        fc2.setVisibility(View.INVISIBLE);
        rc2.setText("");
        rc2.setVisibility(View.INVISIBLE);
        fc3.setText("");
        fc3.setVisibility(View.INVISIBLE);


        fc4.setText("");
        fc4.setVisibility(View.INVISIBLE);
        rc4.setText("");
        rc4.setVisibility(View.INVISIBLE);

        fctext2 = "";


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(ans.getText().toString())) {
                    if (fc1.getVisibility() == View.VISIBLE && fctext2.equals("")) {
                        msg = "\n " + ans.getText().toString();
                        rc1.setText(msg);
                        rc1.setVisibility(View.VISIBLE);
                        if (ans.getText().toString().equals("hi") || ans.getText().toString().equals("HI") || ans.getText().toString().equals("Hi")) {
                            fctext2 = "\n Are you having breathing Problems?";
                            fc2.setText(fctext2);
                            fc2.setVisibility(View.VISIBLE);
                            ans.setText("", TextView.BufferType.EDITABLE);
                        }
                    }
                    if (fc2.getVisibility() == View.VISIBLE && fc2.getText().toString().equals(fctext2) && ans.getText().toString().equals("yes") ||
                            ans.getText().toString().equals("Yes") || ans.getText().toString().equals("YES")) {
                        msg = "\n " + ans.getText().toString();
                        rc2.setText(msg);
                        rc2.setVisibility(View.VISIBLE);
                        fctext3 = "\nAre you suffering from dry Cough ?";

                        fc3.setText(fctext3);
                        fc3.setVisibility(View.VISIBLE);
                        box1.setVisibility(View.VISIBLE);
                        box2.setVisibility(View.VISIBLE);
                        box3.setVisibility(View.VISIBLE);

                        fctext4 = "\n please tell us what more are you feeling ?";
                        fc4.setText(fctext4);
                        fc4.setVisibility(View.VISIBLE);


                    } else if (fc2.getText().toString().equals(fctext2) && ans.getText().toString().equals("No")) {
                        fctext3 = "\n" + "Okay take care";
                        fc3.setText(fctext3);
                        fc3.setVisibility(View.VISIBLE);
                    }
                }

                if (box1.getVisibility() == View.VISIBLE) {
                    if (box1.isChecked() || box2.isChecked() || box3.isChecked()) {
                        if (box1.isChecked()) {
                            problems.put("Health Problems", box1.getText().toString());
                            shared.edit().putString("health problems",Integer.toString(100)).apply();
                            if (fc4.getVisibility() == View.VISIBLE) {
                                msg = "\n " + ans.getText().toString();
                                rc4.setText(msg);
                                rc4.setVisibility(View.VISIBLE);
                            }
                        } else if (box2.isChecked()) {
                            problems.put("Health Problems", box2.getText().toString());
                            shared.edit().putString("health problems",Integer.toString(75)).apply();
                            if (fc4.getVisibility() == View.VISIBLE) {
                                msg = "\n " + ans.getText().toString();
                                rc4.setText(msg);
                                rc4.setVisibility(View.VISIBLE);
                            }
                        } else {
                            shared.edit().putString("health problems",Integer.toString(0)).apply();
                            problems.put("Health Problems", box3.getText().toString());
                            if (fc4.getVisibility() == View.VISIBLE) {
                                msg = "\n " + ans.getText().toString();
                                rc4.setText(msg);
                                rc4.setVisibility(View.VISIBLE);
                            }

                        }
                    } else {
                        Toast.makeText(Dailysynptomscheck.this, "Please Select any given Option", Toast.LENGTH_SHORT).show();
                    }
                }
                reference.updateChildren(problems);


            }

        });
    }
    }
