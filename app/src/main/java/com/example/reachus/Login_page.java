package com.example.reachus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Login_page extends AppCompatActivity {
 EditText number,pass;
 String s_number,s_pass;
 Button login,forgot_pass,signup;
    DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        number = findViewById(R.id.l_phone_number);
        pass = findViewById(R.id.l_password);
        login = findViewById(R.id.login_button);
        forgot_pass = findViewById(R.id.forgotpasword_button);
        signup = findViewById(R.id.Sign_up_button);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirects the user to the registration Page
                Intent i = new Intent(Login_page.this,registration_page.class);
                startActivity(i);
            }
        });

        s_number = number.getText().toString();
        s_pass = pass.getText().toString();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref    = FirebaseDatabase.getInstance().getReference().child("User Details");

                myref.orderByChild("Phone_number").equalTo(s_number).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            myref.orderByChild("Password").equalTo(s_pass).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Intent i = new Intent(Login_page.this,MainActivity.class);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            //username exist
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }
}