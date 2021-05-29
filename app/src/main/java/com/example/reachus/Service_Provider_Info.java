package com.example.reachus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Service_Provider_Info extends AppCompatActivity {
    EditText Name;
    Button continueBtn;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userId,LegalName;
    private static final String TAG = "Storing data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__provider__info);

        Name=findViewById(R.id.Name);
        continueBtn=findViewById(R.id.continueStep2);

        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeInfo();
            }
        });
    }

    public void storeInfo(){
        LegalName=Name.getText().toString();

        userId=mAuth.getCurrentUser().getUid();
        FirebaseFirestore db=FirebaseFirestore.getInstance();

        DocumentReference NameDocument = db.collection("provider").document(userId);

        Map<String,Object> provider = new HashMap<>();
        provider.put("LegalName", LegalName);

        NameDocument.collection("NameCollection").document("Name").set(provider, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot added");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
        startActivity(new Intent(getApplicationContext(),Service_provider_step2.class ));
    }
}

