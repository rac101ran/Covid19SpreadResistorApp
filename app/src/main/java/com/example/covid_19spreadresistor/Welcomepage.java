package com.example.covid_19spreadresistor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Welcomepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomepage);
    }

    public void signup(View view) {
        Intent i=new Intent(Welcomepage.this,Signuppage.class);
        startActivity(i);
        finish();

    }

    public void login(View view) {
        Intent i=new Intent(Welcomepage.this,Loginpage.class);
        startActivity(i);
        finish();

    }

}
