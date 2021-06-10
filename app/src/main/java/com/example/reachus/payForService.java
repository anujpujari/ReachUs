package com.example.reachus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class payForService extends AppCompatActivity implements PaymentResultListener {

    Button serviceBooked;
    String orderCount;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userId,bookingId,providerUserId,BookingDate,BookingTime,StoreName,mainJob,secondaryJob,priceOfService;
    Bundle extras;
    CollectionReference BookingInformation;
    CollectionReference UserServices;

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
            priceOfService=extras.getString("priceOfService");
            Log.d("Values", bookingId+" "+providerUserId);
        }

        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        serviceBooked=findViewById(R.id.payForService);
        userId=mAuth.getCurrentUser().getUid();
        bookingId=userId.substring(0,7)+BookingTime.replace(":", "").replace("PM", "").replace("AM","");


        //razor pay implementation
        int amount=Math.round(Float.parseFloat(priceOfService)*100);


        BookingInformation = fStore.collection("Services").document("userId"+providerUserId).collection("Bookings");
        UserServices = fStore.collection("ServicesBookedByUser").document("userId"+userId).collection("Bookings");
        serviceBooked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checkout checkout=new Checkout();

                checkout.setKeyID("rzp_test_rsw1fzVDAecUPY");

                JSONObject object=new JSONObject();

                try {
                    object.put("Name", "ReachUs");
                    object.put("description", "Test");
                    object.put("theme.color", "#0093DD");
                    object.put("currency", "INR");
                    object.put("amount", amount);
                    object.put("prefill.contact", "8408042265");
                    object.put("prefill.email", "adityasangave21@gmail.com");

                    checkout.open(payForService.this, object);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment Id");
        builder.setMessage(s);
        builder.show();

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

        Toast.makeText(payForService.this,"ServiceBooked", Toast.LENGTH_LONG);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), s,Toast.LENGTH_LONG).show();
    }
}