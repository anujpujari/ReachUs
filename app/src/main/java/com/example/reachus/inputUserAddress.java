package com.example.reachus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class inputUserAddress extends AppCompatActivity {

    EditText userPincode,userAddr1,userAddr2,userCity,userDistrict;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    Button Add;
    String userId;
    TextView addressdisplay;
    boolean userAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_user_address);
        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        userPincode=findViewById(R.id.userPincode);
        userAddr1=findViewById(R.id.userAddr1);
        userAddr2=findViewById(R.id.userAddr2);
        userCity=findViewById(R.id.userCity);
        userDistrict=findViewById(R.id.userDistrict);

        Add=findViewById(R.id.addAddress);

        userId=mAuth.getCurrentUser().getUid();

        addressdisplay=findViewById(R.id.userAddressesDisplay);

        DocumentReference documentReference = fStore.collection("users").document(userId);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> userAddress = new HashMap<>();

                userAddress.put("userAddress", true);
                userAddress.put("userId", userId);
                userAddress.put("userPincode", userPincode.getText().toString());
                userAddress.put("userAddr1", userAddr1.getText().toString());
                userAddress.put("userAddr2", userAddr2.getText().toString());
                userAddress.put("userCity", userCity.getText().toString());
                userAddress.put("userDistrict", userDistrict.getText().toString());

                documentReference.collection("User Address").document("Address").set(userAddress, SetOptions.merge());
                Toast.makeText(getApplicationContext(),"Address Added", Toast.LENGTH_LONG).show();;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DocumentReference coRef= fStore.collection("users").document(userId).collection("User Address").document("Address");
        coRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Data", "DocumentSnapshot data: " + document.getData());
                        addressdisplay.setText(document.getString("userAddr2")+","+document.getString("userAddr1")+","+
                                document.getString("userCity")+"-"+document.getString("userPincode")+","+document.getString("userDistrict")+","+"Maharashtra"+","+"India");
                    } else {
                        Log.d("data", "No such document");
                        addressdisplay.setText("Please Add Delivery Address");
                    }
                } else {
                    Log.d("data", "get failed with ", task.getException());
                }
            }
        });
    }
}