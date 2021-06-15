package com.example.reachus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class carTasks extends AppCompatActivity {

    CardView carRepairing,carWashing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_services);

        carRepairing=findViewById(R.id.carRepairing);
        carWashing=findViewById(R.id.carWashing);

        carRepairing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), allServices.class);
                intent.putExtra("mainKey", "Car");
                intent.putExtra("key", "carRepairing");
                startActivity(intent);
            }
        });
        carWashing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), allServices.class);
                intent.putExtra("mainKey", "Car");
                intent.putExtra("key", "carWashing");
                startActivity(intent);
            }
        });
    }
}