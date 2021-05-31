package com.example.reachus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Service_Provider_Info extends AppCompatActivity {
    EditText Name;
    Button continueBtn;
    CheckBox terms;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userId,LegalName,fullName;
    boolean valid;
    private static final Pattern NAME_PATTERN =
            Pattern.compile("[a-zA-Z]+\\.?");
    private static final String TAG = "Storing data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__provider__info);

        Name=findViewById(R.id.Name);
        continueBtn=findViewById(R.id.continueStep2);
        terms = findViewById(R.id.termsandconditions);

        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()==true)
                {
                    storeInfo();
                }
               else {
                    Log.d(TAG, "Validation Gone Wrong");

                }
            }
        });
    }

    public  boolean validate()
    {
        valid=true;

        LegalName=Name.getText().toString();
        if(TextUtils.isEmpty(LegalName))
        {
            Name.setError("Field Empty");
            return valid = false;
        }else if(!LegalName.matches("[a-zA-Z]+\\.?"))
        {
            Name.setError("Invalid Name");
            return valid = false;
        }
        if(terms.isChecked()==true)
        {
            valid = true;
        }
        else {
            Toast.makeText(getApplicationContext(),"You must agree to our terms  and conditions",Toast.LENGTH_SHORT).show();
            terms.setError("Please check the checkbox");
             return valid = false;
             }


        return valid;
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
                startActivity(new Intent(getApplicationContext(),Service_Provider_Step_2.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });

    }
}

