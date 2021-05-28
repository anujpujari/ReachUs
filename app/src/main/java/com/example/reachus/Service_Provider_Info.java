package com.example.reachus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    Spinner myspinner,myspinner2;
    EditText Phone_number,Age,about,name;
    RadioButton mal,femal;
    RadioGroup Gen;
    String s_number,s_age,s_gender,s_about,s_name,s_service,s_region;
    Button submit;
    String MobilePattern = "[0-9]{10}";
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userId;
    boolean valid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__provider__info);

        myspinner2 = findViewById(R.id.sp_region);
        name  = findViewById(R.id.sp_Username);
        myspinner = findViewById(R.id.u_service);
        submit = findViewById(R.id.sp_submit1);
        Phone_number = findViewById(R.id.sp_numberr);
        Age = findViewById(R.id.sp_age);
        mal = findViewById(R.id.sp_gen_male);
        femal = findViewById(R.id.sp_gen_female);
        about = findViewById(R.id.aboutsp);
        mAuth=FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();




        Gen = findViewById(R.id.sp_gender);
        ArrayAdapter<String> myadapter = new ArrayAdapter<>(Service_Provider_Info.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Services));
        ArrayAdapter<String> myadapter2 = new ArrayAdapter<>(Service_Provider_Info.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Region));
        myadapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner2.setAdapter(myadapter2);
        myspinner.setAdapter(myadapter);
         s_service = myspinner.getSelectedItem().toString();
        s_region = myspinner2.getSelectedItem().toString();
        s_name = name.getText().toString();
        s_number = Phone_number.getText().toString();
        s_age = Age.getText().toString();
        s_about = about.getText().toString();


        myspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Select the Service Please",Toast.LENGTH_SHORT).show();
                valid = false;
            }
        });

        ///////////////////////////////////////////////////////
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();


            }
        });

    }


    private void setGender() {
        if(mal.isChecked())
        {
            s_gender="Male";
        }
        else if (femal.isChecked())
        {
            s_gender="Female";

        }
        else
        {
            mal.setError("Select Gender");
        }
    }

    public void signup () {
        if (validateform()==true) {
            setGender();
            Toast.makeText(Service_Provider_Info.this,"Successfully Registered as a Service Provider",Toast.LENGTH_LONG).show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myref = database.getReference("Service Provider Details");
            ServiceProviderDetails val;
            val = new ServiceProviderDetails(s_number,s_age,s_gender,s_name,s_about,s_service,s_region);
            myref.child(userId).setValue(val);
            if(s_service.equals("Car Service"))
            {
                 myref = database.getReference("Car Service Provider Details");
                val = new ServiceProviderDetails(s_number,s_age,s_gender,s_name,s_about,s_service,s_region);
                myref.child(userId).setValue(val);
            }
            else if (s_service=="Repairing Service")
            {
                 myref = database.getReference("Repairing Service Provider Details");
                val = new ServiceProviderDetails(s_number,s_age,s_gender,s_name,s_about,s_service,s_region);
                myref.child(s_service).setValue(val);
            }
            else if (s_service=="Maid Service")
            {
                 myref = database.getReference("Maid Service Provider Details");
                val = new ServiceProviderDetails(s_number,s_age,s_gender,s_name,s_about,s_service,s_region);
                myref.child(s_service).setValue(val);
            }
            if (s_service=="Cleaning Service")
            {
              myref = database.getReference("Cleaning Service Provider Details");
                val = new ServiceProviderDetails(s_number,s_age,s_gender,s_name,s_about,s_service,s_region);
                myref.child(s_service).setValue(val);
            }
        }else{
            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
        }
}

    public boolean validateform() {
         valid = true;
        s_name = name.getText().toString();
        if (TextUtils.isEmpty(s_name)) {
            name.setError("Field Empty");
            valid = false;
        }

        s_number = Phone_number.getText().toString();
// onClick of button perform this simplest code.
        if (TextUtils.isEmpty(s_number)) {
            Phone_number.setError("Field Empty");
            valid = false;
        } else if (!s_number.matches(MobilePattern)) {
            Phone_number.setError("Invalid Number");
            valid = false;
        } else {
            Phone_number.setError(null);
        }

        s_age = Age.getText().toString();
        if (TextUtils.isEmpty(s_age)) {
            Age.setError("Field Empty");
            valid = false;
        } else {
            Age.setError(null);
        }

        s_about = about.getText().toString();
        if (TextUtils.isEmpty(s_about)) {
            about.setError("Field Empty");
            valid = false;
        } else {
            about.setError(null);
        }

        return valid;
    }
    }
