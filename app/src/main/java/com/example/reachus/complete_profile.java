package com.example.reachus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class complete_profile extends AppCompatActivity {

    private TextView beInsider,logout,youraddresses;
    String becomeinsider;
    private static final String TAG = "Storing data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        beInsider=findViewById(R.id.be_insider);
        youraddresses=findViewById(R.id.yourAddresses);
        beInsider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Service_Provider_Info.class));
            }
        });
        youraddresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), inputUserAddress.class));
                Toast.makeText(getApplicationContext(),"your addresses here", Toast.LENGTH_LONG);
            }
        });
        //Logging out
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login_page.class));
                finish();
            }
        });
    }
}
