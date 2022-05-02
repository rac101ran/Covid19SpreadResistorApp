package com.example.covid_19spreadresistor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Loginpage extends AppCompatActivity {
    FirebaseAuth auth;
    Button b;
    EditText emailtextview;
    EditText passwordtextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        b=findViewById(R.id.loginclick);
        emailtextview=(EditText)findViewById(R.id.emailidi);
        passwordtextview=(EditText)findViewById(R.id.passwordid);
        auth=FirebaseAuth.getInstance();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager in=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(passwordtextview.getWindowToken(),0);

                if(!TextUtils.isEmpty(emailtextview.getText().toString()) && !TextUtils.isEmpty(passwordtextview.getText().toString())) {
                    login(emailtextview.getText().toString(),passwordtextview.getText().toString());

                }
                else {
                    Toast.makeText(Loginpage.this,"Enter Again",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
    public void login(String Email,String Pass) {
        auth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    Intent i=new Intent(Loginpage.this,MainActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        });


    }
    }

