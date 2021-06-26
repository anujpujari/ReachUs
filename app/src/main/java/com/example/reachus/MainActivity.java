package com.example.reachus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class MainActivity extends AppCompatActivity {
    SliderView sliderView;
    int[] images = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four};
    ImageView cleaning,repairing,maid,carServices;
    TextView cleaning_text,repairing_text;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userId;
    CardView homeTutor,pasteControl,millServices,laundryServices;
    boolean userAddress,personalInfo=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();

        cleaning=findViewById(R.id.cleaning_image);
        cleaning_text=findViewById(R.id.cleaning_text);

        repairing=findViewById(R.id.repair_image);
        repairing_text=findViewById(R.id.repair_text);

        carServices=findViewById(R.id.car_image);

        maid=findViewById(R.id.maid_image);

        homeTutor=findViewById(R.id.homeTutor);
        pasteControl=findViewById(R.id.pasteControl);

        millServices=findViewById(R.id.millView);
        laundryServices=findViewById(R.id.clothingView);

        DocumentReference docRef=fStore.collection("users").document(userId);
        docRef.collection("User Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Data", document.getId() + " => " + document.getData());
                        Log.d("Address", document.get("userAddress")+"");
                        userAddress= (boolean) document.get("userAddress");
                    }
                } else {
                    Log.w("Data", "Error getting documents.", task.getException());
                }
            }
        });
        DocumentReference docRe=fStore.collection("users").document(userId);
        docRe.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Data", document.getId() + " => " + document.getData());
                        Log.d("Address", document.get("personalInfoAdded")+"");
                        if(document.get("personalInfoAdded")!=null)
                            personalInfo= (boolean) document.get("personalInfoAdded");
                    }
                } else {
                    Log.w("Data", "Error getting documents.", task.getException());
                }
            }
        });
        //
        maid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userAddress && personalInfo)
                    startActivity(new Intent(MainActivity.this, maidTask.class));
                else
                    Toast.makeText(getApplicationContext(), "Please Fill Your Address First and personal Info First", Toast.LENGTH_LONG).show();
            }
        });
        carServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userAddress && personalInfo)
                    startActivity(new Intent(MainActivity.this, carTasks.class));
                else
                    Toast.makeText(getApplicationContext(), "Please Fill Your Address First and personal Info first", Toast.LENGTH_LONG).show();

            }
        });
        cleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userAddress && personalInfo)
                    startActivity(new Intent(MainActivity.this, cleaningTask.class));
                else
                    Toast.makeText(getApplicationContext(), "Please Fill Your Address First and personal Info first", Toast.LENGTH_LONG).show();

            }
        });
        repairing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userAddress && personalInfo)
                    startActivity(new Intent(MainActivity.this,repairingTask.class));
                else
                    Toast.makeText(getApplicationContext(), "Please Fill Your Address First and personal Info first", Toast.LENGTH_LONG).show();

            }
        });
        homeTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userAddress && personalInfo) {
                    Intent i = new Intent(MainActivity.this, allServices.class);
                    i.putExtra("mainKey","home");
                    i.putExtra("key", "home");
                    startActivity(i);
                }else
                    Toast.makeText(getApplicationContext(), "Please Fill Your Address First and personal Info first", Toast.LENGTH_LONG).show();
            }
        });
        pasteControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userAddress && personalInfo) {
                    Intent i = new Intent(MainActivity.this, allServices.class);
                    i.putExtra("mainKey","paste");
                    i.putExtra("key", "paste");
                    startActivity(i);
                }else
                    Toast.makeText(getApplicationContext(), "Please Fill Your Address First and personal Info first", Toast.LENGTH_LONG).show();
            }
        });
        millServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userAddress && personalInfo) {
                    Intent i = new Intent(MainActivity.this, millServices.class);
                    startActivity(i);
                }else
                    Toast.makeText(getApplicationContext(), "Please Fill Your Address First and personal Info first", Toast.LENGTH_LONG).show();
            }
        });
        laundryServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userAddress && personalInfo) {
                    Intent i = new Intent(MainActivity.this, LaundryServices.class);
                    startActivity(i);
                }else
                    Toast.makeText(getApplicationContext(), "Please Fill Your Address First and personal Info first", Toast.LENGTH_LONG).show();
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
