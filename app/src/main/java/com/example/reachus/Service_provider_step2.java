package com.example.reachus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Service_provider_step2 extends AppCompatActivity {

    private EditText storeName,pincode,addrLine1,addrLine2,city,district;
    private String storename,pin,addr1,addr2,cityName,districtName,userId;
    private Button continuestep3;

    FirebaseFirestore fStore;
    FirebaseAuth mAuth;

    private static final String TAG = "Storing data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_step2);

        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        storeName=findViewById(R.id.storename);
        pincode=findViewById(R.id.pincode);
        addrLine1=findViewById(R.id.Addr_1);
        addrLine2=findViewById(R.id.Addr_2);
        city=findViewById(R.id.city);
        district=findViewById(R.id.district);

        continuestep3=findViewById(R.id.continueStep3);

        continuestep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAddress();
            }
        });
    }

    public void storeAddress(){
        storename=storeName.getText().toString();
        pin=pincode.getText().toString();
        addr1=addrLine1.getText().toString();
        addr2=addrLine2.getText().toString();
        cityName=city.getText().toString();
        districtName=district.getText().toString();

        userId=mAuth.getCurrentUser().getUid();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference AddressDocument = db.collection("provider").document(userId);

        Map<String,Object> provider = new HashMap<>();
        provider.put("StoreName", storename);
        provider.put("pincode", pin);
        provider.put("Address_1", addr1);
        provider.put("Address_2", addr2);
        provider.put("City", cityName);
        provider.put("District", districtName);

        AddressDocument.collection("AddressCollection").document("address").set(provider,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
        startActivity(new Intent(getApplicationContext(),Service_Provider_Step3.class ));
        Toast.makeText(Service_provider_step2.this,"User Registered successfully", Toast.LENGTH_LONG);
    }
}