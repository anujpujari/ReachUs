package com.example.reachus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;

import java.util.HashMap;
import java.util.Map;

public class payForService extends AppCompatActivity{

    Button serviceBooked;
    private static final int REQUEST_CALL = 1;
    String orderCount;
    ImageView call;
    CollectionReference BookingInformation,UserServices;
    FirebaseAuth mAuth;
    View layoutActivate;
    FirebaseFirestore fStore;
    String userId,bookingId,providerUserId,BookingDate,BookingTime,StoreName,mainJob,secondaryJob,priceOfService,Phone;
    Bundle extras;
    Checkout ch;
    EditText amt,accHolderName,upiID,message;
    Button bookService,dontBookService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_for_service);

//        message=findViewById(R.id.Message);
//        upiID=findViewById(R.id.upiID);
//        accHolderName=findViewById(R.id.accHolderName);
//        amt=findViewById(R.id.amount);
        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        extras = getIntent().getExtras();
        if (extras != null) {
            providerUserId= extras.getString("providerUserId");
            BookingDate= extras.getString("BookingDate");
            BookingTime = extras.getString("BookingTime");
            StoreName= extras.getString("StoreName");
            mainJob= extras.getString("mainJob");
            secondaryJob= extras.getString("secondaryJob");
            priceOfService=extras.getString("priceOfService");
            Log.d("Values", bookingId+" "+providerUserId);
        }

        DocumentReference dRef = fStore.collection("Services").document("userId"+providerUserId);
        dRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    Log.d("Data",document+"");
                    Phone = document.getString("Phone");
                    Log.d("data",document.getString("Phone")+"");
                }
            }
        });
        userId=mAuth.getCurrentUser().getUid();
        call=findViewById(R.id.call);
        layoutActivate=findViewById(R.id.actiavate);
        bookingId=userId.substring(0,7)+BookingTime.replace(":", "").replace("PM", "").replace("AM","");
        bookService=findViewById(R.id.bookService);
        dontBookService=findViewById(R.id.dontbookService);

        BookingInformation = fStore.collection("Services").document("userId"+providerUserId).collection("Bookings");
        UserServices = fStore.collection("ServicesBookedByUser").document("userId"+userId).collection("Bookings");

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutActivate.setVisibility(View.VISIBLE);
                makePhoneCall();
            }
        });
        bookService.setOnClickListener(new View.OnClickListener() {
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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        dontBookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(payForService.this,"Service not Booked",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }


    public void makePhoneCall(){
        String phoneNumber = Phone;
        if (phoneNumber.length() > 0) {
            if (ContextCompat.checkSelfPermission(payForService.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(payForService.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + phoneNumber;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(payForService.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


