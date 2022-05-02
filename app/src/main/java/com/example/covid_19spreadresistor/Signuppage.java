package com.example.covid_19spreadresistor;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signuppage extends AppCompatActivity {
    FirebaseAuth auth;
    TextView textView;
    EditText nametextview;
    EditText emailtextview;
    EditText passwordtextview;
    Button button;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage2);

        nametextview=(EditText)findViewById(R.id.nameid);
        emailtextview=(EditText)findViewById(R.id.emailidi);
        passwordtextview=(EditText)findViewById(R.id.passid);
        button=(Button)findViewById(R.id.button2);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        auth= FirebaseAuth.getInstance();

        progressBar.setVisibility(View.INVISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(nametextview.getText().toString()) && !TextUtils.isEmpty(emailtextview.getText().toString()) && !TextUtils.isEmpty(passwordtextview.getText().toString())) {

                    progressBar.setVisibility(View.VISIBLE);
                    register(emailtextview.getText().toString(), passwordtextview.getText().toString());

                    SharedPreferences pref = Signuppage.this.getSharedPreferences("com.example.covid_19spreadresistor", Context.MODE_PRIVATE);
                    pref.edit().putString("Name", nametextview.getText().toString()).apply();

                    InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    input.hideSoftInputFromWindow(passwordtextview.getWindowToken(), 0);

                }
                else {

                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Signuppage.this,"Enter again",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void register(String emaill,String pass) {
        auth.createUserWithEmailAndPassword(emaill,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Intent i=new Intent(Signuppage.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });


    }
    }

