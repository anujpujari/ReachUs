package com.example.reachus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class repairingTask extends AppCompatActivity {

    CardView carpainter,electrician,plumber;
    String whichService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairing_task);

        carpainter=findViewById(R.id.carpainter);
        carpainter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichService="carpainterService";
                Intent intent = new Intent(getApplicationContext(), allServices.class);
                intent.putExtra("mainKey", "Repairing");
                intent.putExtra("key", "carpainterService");
                startActivity(intent);
            }
        });
        plumber=findViewById(R.id.plumber);
        plumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichService="plumberService";
                Intent intent = new Intent(getApplicationContext(), allServices.class);
                intent.putExtra("mainKey", "Repairing");
                intent.putExtra("key", "plumberService");
                startActivity(intent);
            }
        });
        electrician=findViewById(R.id.electrician);
        electrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichService="plumberService";
                Intent intent = new Intent(getApplicationContext(), allServices.class);
                intent.putExtra("mainKey", "Repairing");
                intent.putExtra("key", "electricianService");
                startActivity(intent);
            }
        });
    }
}