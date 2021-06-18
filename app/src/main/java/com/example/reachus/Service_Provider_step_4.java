package com.example.reachus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
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

public class Service_Provider_step_4 extends AppCompatActivity {

    String  statusofprovider;
    EditText accName,accNumber;
    Button finish;
    Boolean valid;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    ProgressDialog pg;

    private static final String TAG = "Storing data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__provider_info_4);

        accName=findViewById(R.id.accountName);
        accName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        accNumber=findViewById(R.id.accountNumber);

        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        pg = new ProgressDialog(Service_Provider_step_4.this);
        pg.setMessage("Saving Info..");
        pg.setIndeterminate(true);
        pg.setCancelable(false);


        finish=findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pg.show();
                if(validate()==true)
                {
                    storedata();
                }
                else
                {
                    pg.dismiss();
                    Log.w(TAG, "Validation Error");
                }
            }
        });
    }

    private boolean validate() {
        valid = true;
        String accountName=accName.getText().toString();
        if(accountName.isEmpty())
        {
            accName.setError("Field Empty");
            return  valid = false;
        }
        String accountNumber=accNumber.getText().toString();
         if(accountNumber.isEmpty())
        {
            accNumber.setError("Field Empty");
            return valid = false;
        }

        return  valid;
    }

    private void storedata() {
        String accountName=accName.getText().toString();
        String accountNumber=accNumber.getText().toString();

        String userId=mAuth.getCurrentUser().getUid();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference BankDocument = db.collection("provider").document("userId"+userId);
        DocumentReference isServiceProvider=db.collection("users").document(userId);
        Map<String, Object> user=new HashMap<>();
        user.put("isServiceProvider", true);
        isServiceProvider.set(user, SetOptions.merge());

        Map<String,Object> BankDetails = new HashMap<>();
        BankDetails.put("AccountName",accountName);
        BankDetails.put("AccountNumber",accountNumber);

        BankDocument.collection("Bank Details").document("BankDetails").set(BankDetails, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                startActivity(new Intent(getApplicationContext(), complete_profile.class));
                Toast.makeText(getApplicationContext(),"Bank details Sucessfully Stored",Toast.LENGTH_SHORT).show();
                pg.dismiss();
                Intent intent = new Intent(getApplicationContext(), Service_Provider_finished.class);
                String status = "success";
                intent.putExtra("BEP", status);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
                Toast.makeText(getApplicationContext(),"Failed to store Bank Details",Toast.LENGTH_SHORT).show();
            }
        });
    }
}