package com.example.reachus;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Personal_Information extends AppCompatActivity {
    EditText name,email,phone,age,enterOtp;
    Button subm,verifyPhoneNo,verify;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userId;
    boolean phoneverify=false;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    private static final String TAG = "Store Data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal__information);

        name = findViewById(R.id.PI_name);
        email = findViewById(R.id.PI_email);
        phone = findViewById(R.id.PI_phone);
        age = findViewById(R.id.PI_age);
        subm = findViewById(R.id.submit);
        verifyPhoneNo=findViewById(R.id.verifyPhoneNo);
        enterOtp=findViewById(R.id.enterOtp);
        verify=findViewById(R.id.verify);
        mAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();

        userId=mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.collection("users").document("Info").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document=task.getResult();
                    if(document.exists()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        name.setText(document.getString("fullName"));
                        email.setText(document.getString("Email"));
                        phone.setText(document.getString("Phone"));
                        age.setText(document.getString("Age"));
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });

        verifyPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify.setVisibility(View.VISIBLE);
                enterOtp.setVisibility(View.VISIBLE);
                String phoneNumber = phone.getText().toString();
                String phNo;

                if(phoneNumber.isEmpty())
                    Toast.makeText(Personal_Information.this,"Please Enter a valid Phone Number", Toast.LENGTH_LONG).show();
                else{
                    phNo="+91"+phoneNumber;
                    sendVerificationCode(phNo);
                }
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(enterOtp.getText().toString())) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                    Toast.makeText(Personal_Information.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    verifyCode(enterOtp.getText().toString());
                }
            }
        });
        subm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Build an AlertDialog

                    AlertDialog.Builder builder = new AlertDialog.Builder(Personal_Information.this);
                    builder.setMessage("Are you sure u want to save the changes?");

                    // Set the alert dialog yes button click listener
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when user clicked the Yes button
                            // Set the TextView visibility GONE
                            fStore = FirebaseFirestore.getInstance();
                            userId = mAuth.getCurrentUser().getUid();
                            //Ha code ahee bg databse mse add cha
                            DocumentReference Reference = fStore.collection("users").document(userId).collection("users").document("Info");
                            Map<String, Object> updatedUserInfo = new HashMap<>();
                            updatedUserInfo.put("fullName", name.getText().toString());
                            updatedUserInfo.put("Email", email.getText().toString());
                            updatedUserInfo.put("Phone", phone.getText().toString());
                            updatedUserInfo.put("Age", age.getText().toString());
                            updatedUserInfo.put("personalInfoAdded", true);

                            Reference.set(updatedUserInfo, SetOptions.merge());
                            Toast.makeText(getApplicationContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Set the alert dialog no button click listener
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when No button clicked
                            Toast.makeText(getApplicationContext(),
                                    "No Button Clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                final String code = credential.getSmsCode();
                Log.d("Otp",code+"");
                if (code != null) {
                    enterOtp.setText(code);
                    verifyCode(code);
                }
                Log.d(TAG, "onVerificationCompleted:" + credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    public void sendVerificationCode(String number){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(Personal_Information.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void verifyCode(String code) {
        Log.d("Data", code+"");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        Log.d("Data", credential.getSmsCode()+"");
        if(code.equals(credential)){
            DocumentReference docRef = fStore.collection("users").document(userId).collection("users").document("Info");
            Map<String,Object> phoneVerified=new HashMap<>();
            phoneVerified.put("isPhoneVerified",true);
            docRef.set(phoneVerified,SetOptions.merge());
        }
        Toast.makeText(Personal_Information.this,"Verification Completed", Toast.LENGTH_LONG).show();
    }
}