package com.example.reachus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class maidTask extends AppCompatActivity {

    CardView utensils, clothes,homecleaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_service);

        utensils=findViewById(R.id.utensilsCleaning);
        clothes=findViewById(R.id.clothesCleaning);
        homecleaning=findViewById(R.id.homeCleaning);

        utensils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), allServices.class);
                intent.putExtra("mainKey", "Maid");
                intent.putExtra("key", "utensilsCleaning");
                startActivity(intent);
            }
        });
        clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), allServices.class);
                intent.putExtra("mainKey", "Maid");
                intent.putExtra("key", "clothesCleaning");
                startActivity(intent);
            }
        });
        homecleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), allServices.class);
                intent.putExtra("mainKey", "Maid");
                intent.putExtra("key", "homeCleaning");
                startActivity(intent);
            }
        });
    }
}