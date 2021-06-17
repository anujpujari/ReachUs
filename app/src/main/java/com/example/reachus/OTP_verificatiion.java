package com.example.reachus;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.google.firebase.auth.PhoneAuthProvider.*;

public class OTP_verificatiion extends AppCompatActivity {
    Button generate_otp,verify_otp;
    EditText number,otpp;

    FirebaseAuth mAuth;
    private String verificationId;
    FirebaseFirestore fStore;
    String userId,Phonenumber;
    int randonnumber;
    String name;
    Bundle extras;
    ProgressBar progressBar;
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
        progressBar = findViewById(R.id.progressbar);
     /*   StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/
        Phonenumber = "91"+number.getText().toString();
        if (savedInstanceState == null)

        {

            /*fetching extra data passed with intents in a Bundle type variable*/
            extras = getIntent().getExtras();
            if(extras == null)
            {
               name= null;
            }
            else
            {
                /* fetching the string passed with intent using ‘extras’*/
                name= extras.getString("name");
            }
        }

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
                    progressBar.setVisibility(View.VISIBLE);

                    // if the text field is not empty we are calling our 
                    // send OTP method for getting OTP from Firebase.
                    sendsms();

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
                        if(randonnumber==Integer.valueOf(otpp.getText().toString()))
                        {
                            Toast.makeText(OTP_verificatiion.this, "OTP verified Sucessfully", Toast.LENGTH_SHORT).show();
                            storephonenumber();
                            startActivity(new Intent(OTP_verificatiion.this,Login_page.class));
                            finish();
                        }
                        else
                        {
                            Toast.makeText(OTP_verificatiion.this, "Wring OTP", Toast.LENGTH_SHORT).show();
                        }
                }

                
            }
        });




    }

    private void sendsms() {
        try {
            // Construct data
            String apiKey = "apikey=" + "NDk1Nzc0NmM0NDRiNDg2NzYyNjk1MzU3NmI2MTZmNDg";
            Random random = new Random();
            randonnumber = random.nextInt(999999);

            String message = "&message=" + "Hey"+name+"Your OTP is "+randonnumber;

            String sender = "&sender=" + "ReachUs Team ";
            String numbers = "&numbers=" + Phonenumber;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();
            Toast.makeText(this, "OTP sent Sucessfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

            Toast.makeText(this, "Error "+e, Toast.LENGTH_SHORT).show();

            Toast.makeText(this, "Error Sending SMS", Toast.LENGTH_SHORT).show();
        }
    }



    private void storephonenumber() {
        fStore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userId);

        Map<String,Object> user = new HashMap<>();
        user.put("Phone-no", number.getText().toString());
        documentReference.collection("users").document("Info").set(user);
        startActivity(new Intent(OTP_verificatiion.this,Login_page.class));
      finish();
    }
}
