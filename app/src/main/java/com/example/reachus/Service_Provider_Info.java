package com.example.reachus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Service_Provider_Info extends AppCompatActivity {
    Spinner myspinner;
    EditText Phone_number,Age,about,name;
    RadioButton mal,femal;
    RadioGroup Gen;
    String s_number,s_age,s_gender,s_about,s_name,s_service;
    Button submit;
    String MobilePattern = "[0-9]{10}";
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__provider__info);

        name  = findViewById(R.id.sp_Username);
        myspinner = findViewById(R.id.u_service);
        submit = findViewById(R.id.sp_submit1);
        Phone_number = findViewById(R.id.sp_number);
        Age = findViewById(R.id.sp_age);
        mal = findViewById(R.id.sp_gen_male);
        femal = findViewById(R.id.sp_gen_female);
        about = findViewById(R.id.aboutsp);
        mAuth=FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        SharedPreferences mySharedPreferences = this.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE);




        Gen = findViewById(R.id.sp_gender);
        ArrayAdapter<String> myadapter = new ArrayAdapter<>(Service_Provider_Info.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Services));
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(myadapter);
         s_service = myspinner.getSelectedItem().toString();
        s_name = name.getText().toString();
        s_number = Phone_number.getText().toString();
        s_age = Age.getText().toString();
        s_about = about.getText().toString();


        ///////////////////////////////////////////////////////
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
                //calls the signup method for the form validation`

            }
        });

    }

    public void  storedata(){
        setGender();
        Toast.makeText(Service_Provider_Info.this,"Successfully Registered as a Service Provider",Toast.LENGTH_LONG).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("Service Provider Details");
        ServiceProviderDetails val;
        val = new ServiceProviderDetails(s_number,s_age,s_gender,s_name,s_about,s_service);
        myref.child(userId).setValue(val);
    }
    private void setGender() {
        if(mal.isChecked()==true)
        {
            s_gender="Male";
        }
        else if (femal.isChecked()==true)
        {
            s_gender="Female";

        }
        else
        {
            mal.setError("Select Gender");
        }
    }

    public void signup () {
        if (validateform()==false) {
            Toast.makeText(getApplicationContext(),"Something went Wrong",Toast.LENGTH_SHORT).show();
        }
        else if(validateform()==true)
        {
            storedata();
            Log.w("DATA","Data stored in Database");
        }


}

    private boolean validateform() {
        boolean valid = true;
        if (TextUtils.isEmpty(s_name)) {
            name.setError("Field Empty");
            valid = false;
        } else {

        }

// onClick of button perform this simplest code.
        if (TextUtils.isEmpty(s_number)) {
            name.setError("Field Empty");
            valid = false;
        } else if (!Phone_number.getText().toString().matches(MobilePattern)) {
            Phone_number.setError("Field Empty");
            valid = false;
        } else {
            Phone_number.setError(null);
        }


        if (TextUtils.isEmpty(s_age)) {
            Age.setError("Field Empty");
            valid = false;
        } else {
            Age.setError(null);
        }
        if (TextUtils.isEmpty(s_about)) {
            about.setError("Field Empty");
            valid = false;
        } else {
            name.setError(null);
        }

        return valid;
    }
    }
