package com.example.covid_19spreadresistor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Shieldstrength extends AppCompatActivity {
SeekBar seekbar,seek2;
SharedPreferences preferences;
ProgressBar p;
double strength=300;
TextView strengthtext;
DatabaseReference ref;
FirebaseAuth auth;
FirebaseUser user;
Map<String,Object> uploadstrength;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shieldstrength);
        p=findViewById(R.id.progressBar2);
        uploadstrength=new HashMap<>();

        new AlertDialog.Builder(this).setIcon(R.drawable.ic_language_black_24dp).setTitle("IMPORTANT")
                .setMessage("Would you like to fill your details ?").setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                  Intent i=new Intent(Shieldstrength.this,Profilesettings.class);
                   startActivity(i);
            }
        }).setNegativeButton("No",null).show();

         auth=FirebaseAuth.getInstance();
         user=auth.getCurrentUser();
         ref= FirebaseDatabase.getInstance().getReference("People").child(user.getUid());
        strengthtext=findViewById(R.id.textView2);

         preferences=(SharedPreferences)this.getSharedPreferences("com.example.covid_19spreadresistor", Context.MODE_PRIVATE);
            strength=strength-Double.parseDouble(preferences.getString("health","0"))-
                    Double.parseDouble(preferences.getString("age","0"))
                    -Double.parseDouble(preferences.getString("health problems","0"))
                    -Double.parseDouble(preferences.getString("zone","30"));

             if(strength>=200) {
                 p.setProgress(100);
             }
             else if(strength<200 && strength>=100) {
                 p.setProgress(60);
             }
             else if(strength<100){
                 p.setProgress(30);
             }

             strengthtext.setText(String.valueOf(strength));
             uploadstrength.put("shield strength",strength);
             ref.updateChildren(uploadstrength);
        Toast.makeText(this,"Your Shield Strength : "+ strength, Toast.LENGTH_SHORT).show();
    }

}
