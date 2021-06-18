package com.example.reachus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Service_provider_step_3 extends AppCompatActivity {

    private EditText storeName,pincode,addrLine1,addrLine2,city,district;
    private String Storename,pin,addr1,addr2,cityName,districtName,userId;
    private Button continuestep3;
    boolean valid;
    ProgressDialog pg;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;

    private static final String TAG = "Storing data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_step_3);

        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        storeName=findViewById(R.id.storename);
        pincode=findViewById(R.id.pincode);
        addrLine1=findViewById(R.id.Addr_1);
        addrLine2=findViewById(R.id.Addr_2);
        city=findViewById(R.id.city);
        district=findViewById(R.id.district);

        pg = new ProgressDialog(Service_provider_step_3.this);
        pg.setMessage("Saving Info..");
        pg.setIndeterminate(true);
        pg.setCancelable(false);

        continuestep3=findViewById(R.id.continueStep3);

        continuestep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(validate()==true)
              {
                  storeAddress();
              }
              else
              {
                  Toast.makeText(getApplicationContext(),"Fill the form Correctlty",Toast.LENGTH_SHORT).show();
              }
            }
        });
    }

    private boolean validate() {
        valid = true;
        Storename=storeName.getText().toString();
        if(Storename.isEmpty())
        {
            storeName.setError("Field Empty");
            return valid=false;
        }

        pin=pincode.getText().toString();
        if(pin.isEmpty())
        {
            pincode.setError("Field Empty");
            return valid=false;
        }
        else if(!pin.matches("^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$"))
        {
            pincode.setError("Invalid Pincode");
            return valid = false;
        }


        addr1=addrLine1.getText().toString();
        if(addr1.isEmpty())
        {
            addrLine1.setError("Field Empty");
            return valid=false;
        }
        addr2=addrLine2.getText().toString();
        if(addr2.isEmpty())
        {
            addrLine2.setError("Field Empty");
            return valid=false;
        }
        cityName=city.getText().toString();
        if(cityName.isEmpty())
        {
            city.setError("Field Empty");
            return valid=false;
        }
        districtName=district.getText().toString();
        if(districtName.isEmpty())
        {
            district.setError("Field Empty");
            return valid=false;
        }
        return  valid;
    }

    public void storeAddress(){
        Storename=storeName.getText().toString();
        pin=pincode.getText().toString();
        addr1=addrLine1.getText().toString();
        addr2=addrLine2.getText().toString();
        cityName=city.getText().toString();
        districtName=district.getText().toString();

        userId=mAuth.getCurrentUser().getUid();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference AddressDocument = db.collection("provider").document("userId"+userId);
        DocumentReference ServiceStorage = db.collection("Services").document("userId"+userId);

        Map<String,Object> provider = new HashMap<>();
        provider.put("StoreName", Storename);
        provider.put("pincode", pin);
        provider.put("Address_1", addr1);
        provider.put("Address_2", addr2);
        provider.put("City", cityName);
        provider.put("District", districtName);

        ServiceStorage.set(provider,SetOptions.merge());

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
        startActivity(new Intent(getApplicationContext(), Service_Provider_step_4.class ));
        finish();
        Toast.makeText(Service_provider_step_3.this,"User Registered successfully", Toast.LENGTH_LONG).show();
    }
}