package com.example.reachus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class cleaningTask extends AppCompatActivity {
    CardView homesantz,vehiclesantz;
    String whichService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning);

        homesantz = findViewById(R.id.homesantizing);
        vehiclesantz = findViewById(R.id.vehiclesantizing);
         homesantz.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 whichService="homeSanitizing";
                 Intent intent = new Intent(getApplicationContext(), allServices.class);
                 intent.putExtra("mainKey", "Sanitizing");
                 intent.putExtra("key", "homeSanitizing");
                 startActivity(intent);
             }
         });

         vehiclesantz.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 whichService="homeSanitizing";
                 Intent intent = new Intent(getApplicationContext(), allServices.class);
                 intent.putExtra("mainKey", "Sanitizing");
                 intent.putExtra("key", "vehicleSanitizing");
                 startActivity(intent);
             }
         });

    }
}