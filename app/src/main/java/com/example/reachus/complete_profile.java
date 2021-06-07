package com.example.reachus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class complete_profile extends AppCompatActivity {

    private TextView beInsider,logout;
    String becomeinsider ;
    private static final String TAG = "Storing data";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        // Redirection to be Insider/service provider
        beInsider=findViewById(R.id.be_insider);

        becomeinsider = getIntent().getStringExtra("BEP");
        String status="success";


        beInsider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((becomeinsider.equals(status)))
                {

                    Toast.makeText(complete_profile.this, "You are Already a Service Provider", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(),Service_Provider_Info.class));


                }

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
