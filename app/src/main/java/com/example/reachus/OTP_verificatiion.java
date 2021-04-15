package com.example.reachus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static com.google.firebase.auth.PhoneAuthProvider.*;

public class OTP_verificatiion extends AppCompatActivity {
    Button generate_otp,verify_otp;
    EditText number,otpp;
    String phone_number;
    FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verificatiion);

        generate_otp= findViewById(R.id.gen_otp);
        verify_otp = findViewById(R.id.verfy_otp);
        number = findViewById(R.id.phn_number);
        otpp = findViewById(R.id.otp);
        mAuth = FirebaseAuth.getInstance();




        generate_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // below line is for checking weather the user
                // has entered his mobile number or not.
                if (TextUtils.isEmpty(number.getText().toString())) {
                    // when mobile number text field is empty
                    // displaying a toast message.
                    Toast.makeText(OTP_verificatiion.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    // if the text field is not empty we are calling our 
                    // send OTP method for getting OTP from Firebase.
                    String phone = "+91" + number.getText().toString();
                    sendVerificationCode(phone);
                }
            }
        });

        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(otpp.getText().toString())) {
                    // if the OTP text field is empty display 
                    // a message to user to enter OTP
                    Toast.makeText(OTP_verificatiion.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if OTP field is not empty calling 
                    // method to verify the OTP.
                    verifyCode(otpp.getText().toString());
                }
                
            }
        });




    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            phone_number = number.getText().toString();
                            Intent i = new Intent(OTP_verificatiion.this,registration_page.class);
                            i.putExtra("phone-number",phone_number);


                            startActivity(i);
                            finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(OTP_verificatiion.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
    /* PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, // first parameter is user's mobile number
                60, // second parameter is time limit for OTP
                // verification which is 60 seconds in our case.
                TimeUnit.SECONDS, // third parameter is for initializing units
                // for time period which is in seconds in our case.
                (Activity) TaskExecutors.MAIN_THREAD, // this task will be excuted on Main thread.
                mCallBack // we are calling callback method when we recieve OTP for
                // auto verification of user.
        );*/
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setActivity(this)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(mCallBack)
                        .build());
    }
    private OnVerificationStateChangedCallbacks

            mCallBack = new OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            //below line is used for getting OTP code which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();
            //checking if the code is null or not.
            if (code != null) {
                //if the code is not null then we are setting that code to our OTP edittext field.
                otpp.setText(code);
                //after setting this code to OTP edittext field we are calling our verifycode method.
                verifyCode(code);

            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            //displaying error message with firebase exception.
            Toast.makeText(OTP_verificatiion.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }

        //below method is used when OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //when we recieve the OTP it contains a unique id wich we are storing in our string which we have already created.
            verificationId = s;
        }




    };

    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }


}
