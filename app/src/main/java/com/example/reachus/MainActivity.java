package com.example.reachus;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class MainActivity extends AppCompatActivity {
    SliderView sliderView;
    int[] images = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four};
    ImageView cleaning,repairing,maid,carServices;
    TextView cleaning_text,repairing_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cleaning=findViewById(R.id.cleaning_image);
        cleaning_text=findViewById(R.id.cleaning_text);

        repairing=findViewById(R.id.repair_image);
        repairing_text=findViewById(R.id.repair_text);

        carServices=findViewById(R.id.car_image);

        maid=findViewById(R.id.maid_image);

        maid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, maidTask.class));
            }
        });
        carServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, carTasks.class));
            }
        });
        cleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, cleaningTask.class));
            }
        });
        repairing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,repairingTask.class));
            }
        });

        //Botton Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationbar);
        //Selected Default as Home Navigation
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelected Listner
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
                switch (menuitem.getItemId())
                {
                    case  R.id.Dashboard:
                        startActivity(new Intent(getApplicationContext(), userBookings.class));
                        overridePendingTransition(0,0);
                        return true;
                    case  R.id.Profilee:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });

        sliderView=findViewById(R.id.imageSlider);

        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }
}
