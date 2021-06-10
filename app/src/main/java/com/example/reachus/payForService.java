package com.example.reachus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class    payForService extends AppCompatActivity {

    int Order=1;
    int min=0,max=500;
    Button serviceBooked;
    String orderCount;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userId,bookingId,providerUserId,BookingDate,BookingTime,StoreName,mainJob,secondaryJob;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_for_service);

        extras = getIntent().getExtras();
        if (extras != null) {
            providerUserId= extras.getString("providerUserId");
            BookingDate= extras.getString("BookingDate");
            BookingTime = extras.getString("BookingTime");
            StoreName= extras.getString("StoreName");
            mainJob= extras.getString("mainJob");
            secondaryJob= extras.getString("secondaryJob");
            Log.d("Values", bookingId+" "+providerUserId);
        }

        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        serviceBooked=findViewById(R.id.payForService);

        userId=mAuth.getCurrentUser().getUid();

        bookingId=userId.substring(0,7)+BookingTime.replace(":", "").replace("PM", "").replace("AM","");

        CollectionReference BookingInformation = fStore.collection("Services").document("userId"+providerUserId).collection("Bookings");
        CollectionReference UserServices = fStore.collection("ServicesBookedByUser").document("userId"+userId).collection("Bookings");
        serviceBooked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> BookingInfo = new HashMap<>();
                BookingInfo.put("BookingUserId", userId);
                BookingInfo.put("providerUserId", providerUserId);
                BookingInfo.put("BookingDate", BookingDate);
                BookingInfo.put("BookingTime", BookingTime);
                BookingInfo.put("bookingId", bookingId);
                BookingInfo.put("StoreName", StoreName);
                BookingInfo.put("mainJob", mainJob);
                BookingInfo.put("secondaryJob", secondaryJob);

                BookingInformation.add(BookingInfo);

                Map<String,Object> ServiceBookedInfo = new HashMap<>();
                ServiceBookedInfo.put("userId", userId);
                ServiceBookedInfo.put("bookingId", bookingId);
                ServiceBookedInfo.put("provideruserId", providerUserId);
                ServiceBookedInfo.put("BookingDate", BookingDate);
                ServiceBookedInfo.put("BookingTime", BookingTime);
                ServiceBookedInfo.put("StoreName", StoreName);
                ServiceBookedInfo.put("mainJob", mainJob);
                ServiceBookedInfo.put("secondaryJob", secondaryJob);

                UserServices.add(ServiceBookedInfo);

                Toast.makeText(payForService.this,"ServiceBooked", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

    }

}