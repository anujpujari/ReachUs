package com.example.reachus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registration_page extends AppCompatActivity {
    EditText p_number, uname, pass, conf_pass;
    Button nxtbtn;
    String Phone_number, Name, Password, Confirm_pass;
    DatabaseReference myref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);


        p_number = findViewById(R.id.phn_number);
        uname = findViewById(R.id.Username);
        pass = findViewById(R.id.password);
        conf_pass = findViewById(R.id.conf_password);
        nxtbtn = findViewById(R.id.nextbtn);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myref = database.getReference("User Details");


        nxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
                //calls the signup method for the form validation`
            }
        });

    }
    public void signup () {
        if (!validateform()) {
            return;
        }
        Name = uname.getText().toString();
        Password = pass.getText().toString();
        Confirm_pass = conf_pass.getText().toString();
        Phone_number = getIntent().getStringExtra("phone-number");

        UserDetail val;
        val = new UserDetail(Name, Password, Phone_number);
        myref.child(Name).setValue(val);
        Toast.makeText(registration_page.this, "Details sucessfully Entered", Toast.LENGTH_SHORT).show();

    }
    private boolean validateform () {
        boolean valid = true;

        String us = uname.getText().toString();
        if (TextUtils.isEmpty(us)) {
            uname.setError("Field Empty");
            valid = false;
        } else {
            uname.setError(null);
        }

// onClick of button perform this simplest code.
        String pw = pass.getText().toString();
        if (TextUtils.isEmpty(pw)) {
            pass.setError("Field Empty");
            valid = false;
        } else {
            pass.setError(null);
        }
        String cpw = conf_pass.getText().toString();
        if (TextUtils.isEmpty(cpw)) {
            conf_pass.setError("Field Empty");
            valid = false;
        } else {
            conf_pass.setError(null);
        }
        if (!(cpw.equals(pw))) {
            conf_pass.setError("Password Not Match");
        } else {
            conf_pass.setError(null);
        }
        return valid;
    }

}