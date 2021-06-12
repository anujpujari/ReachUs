package com.example.reachus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class payForService extends AppCompatActivity implements PaymentResultListener {

    Button serviceBooked;
    String orderCount;
    CollectionReference BookingInformation,UserServices;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userId,bookingId,providerUserId,BookingDate,BookingTime,StoreName,mainJob,secondaryJob,priceOfService;
    Bundle extras;
    Checkout ch;

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

        int amount=Math.round(Float.parseFloat(priceOfService)*100);
        Checkout ch=new Checkout();
        ch.setKeyID("rzp_test_rsw1fzVDAecUPY");

        BookingInformation = fStore.collection("Services").document("userId"+providerUserId).collection("Bookings");
        UserServices = fStore.collection("ServicesBookedByUser").document("userId"+userId).collection("Bookings");
        serviceBooked.setOnClickListener(v -> {

            JSONObject obj=new JSONObject();
            try {
                obj.put("Name", "ReachUs");
                obj.put("description", "Test");
                obj.put("currency", "INR");
                obj.put("amount", amount);
                obj.put("prefill.contact", "8408042265");
                obj.put("prefill.email", "sangaveaditya2003@gmail.com");
                ch.open(payForService.this, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void onPaymentSuccess(String s) {
        AlertDialog.Builder dialogue = new AlertDialog.Builder(this);
        dialogue.setMessage(s);
        dialogue.setTitle("Payment id");

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

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG);
    }
}