package com.example.reachus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Service_Provider_info_4 extends AppCompatActivity {

    EditText accName,accNumber,ifscCode;
    Spinner bankName;
    Button finish;

    FirebaseFirestore fStore;
    FirebaseAuth mAuth;

    private static final String TAG = "Storing data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__provider_info_4);

        accName=findViewById(R.id.accountName);
        accNumber=findViewById(R.id.accountNumber);
        ifscCode=findViewById(R.id.ifscCode);

        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        bankName=findViewById(R.id.bankName);

        finish=findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountName=accName.getText().toString();
                String accountNumber=accNumber.getText().toString();
                String ifsccode=ifscCode.getText().toString();

                String bankname = bankName.getSelectedItem().toString();

                String userId=mAuth.getCurrentUser().getUid();
                FirebaseFirestore db=FirebaseFirestore.getInstance();
                DocumentReference BankDocument = db.collection("provider").document(userId);

                Map<String,Object> BankDetails = new HashMap<>();
                BankDetails.put("AccountName",accountName);
                BankDetails.put("AccountNumber",accountNumber);
                BankDetails.put("BankName",bankname);
                BankDetails.put("ifscCode",ifsccode);

                BankDocument.collection("Bank Details").document("BankDetails").set(BankDetails, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                startActivity(new Intent(getApplicationContext(), Service_Provider_finished.class));
            }
        });
    }
}