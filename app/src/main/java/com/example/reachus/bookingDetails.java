package com.example.reachus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class bookingDetails extends AppCompatActivity {

    TextView storeName, consumerAddress, providerAddress,deliverPrice, mainJob,dateTime,Phone,Email;
    Bundle extras;
    String BookingId,storename, consumeraddress,provideraddress,deliveryprice,mainjob,datetime,providerUserId,consumerUserId,bDate,bTime;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    Button cancleService,report;
    ImageView phoneCall;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        extras = getIntent().getExtras();
        if(extras!=null){
            BookingId=extras.getString("BookingId");
            storename=extras.getString("Storename");
            mainjob=extras.getString("mainJob");
            datetime=extras.getString("bookingdate")+extras.getString("bookingtime");
            bDate=extras.getString("bookingdate");
            bTime=extras.getString("bookingtime");
            providerUserId=extras.getString("providerUserId");
        }

        phoneCall=findViewById(R.id.phoneCall);

        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        consumerUserId=mAuth.getCurrentUser().getUid();

        storeName=findViewById(R.id.storeName);
        consumerAddress=findViewById(R.id.consumerAddress);
        providerAddress=findViewById(R.id.providerAddress);
        deliverPrice=findViewById(R.id.deliveryCost);
        mainJob=findViewById(R.id.mainJob);
        dateTime=findViewById(R.id.dateTime);
        Phone=findViewById(R.id.Phone);
        Email=findViewById(R.id.Email);
        cancleService=findViewById(R.id.cancleService);
        report=findViewById(R.id.reportService);

        DocumentReference coRef= fStore.collection("Services").document("userId"+providerUserId);
        coRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        Log.d("Data", String.valueOf(document.getData()));
                        providerAddress.setText(document.getString("Address_1")+document.getString("Address_2")+document.getString("pincode")+document.getString("City")+
                        document.getString("District")+" Maharashtra"+" India");
                        deliverPrice.setText(document.getString("Price"));
                        Phone.setText(document.getString("Phone"));
                        Email.setText(document.getString("Email"));
                    }
                    else {
                        Log.d("Data","Does not exist");
                    }
                }
            }
        });

        DocumentReference docRef = fStore.collection("users").document(consumerUserId).collection("User Address").document("Address");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        Log.d("Data", String.valueOf(document.getData()));
                        consumerAddress.setText(document.getString("userAddr1")+document.getString("userAddr2")+document.getString("userPincode")+document.getString("userCity")+
                                document.getString("userDistrict")+" Maharashtra"+" India");
                    }
                    else {
                        Log.d("Data","Does not exist");
                    }
                }
            }
        });


        storeName.setText(storename);
        mainJob.setText(mainjob);
        dateTime.setText(datetime);
        deliverPrice.setText(deliveryprice);

        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
        cancleService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query coRef =fStore.collection("ServicesBookedByUser").document("userId"+consumerUserId).collection("Bookings").whereEqualTo("bookingId",BookingId);
                Query coRe =fStore.collection("Services").document("userId"+providerUserId).collection("Bookings").whereEqualTo("bookingId",BookingId);
                coRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Data", document.getId() + " => " + document.getData());
                                document.getReference().delete();
                            }
                        } else {
                            Log.d("Data", "Error getting documents: ", task.getException());
                        }
                    }
                });
                coRe.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Data", document.getId() + " => " + document.getData());
                                document.getReference().delete();
                            }
                        } else {
                            Log.d("Data", "Error getting documents: ", task.getException());
                        }
                    }
                });
                Toast.makeText(bookingDetails.this,"Booking Deleted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),reportService.class);
                intent.putExtra("providerId",providerUserId);
                startActivity(intent);
            }
        });
    }
    public void makePhoneCall(){
        String phoneNumber = Phone.getText().toString();
        if (phoneNumber.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(bookingDetails.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(bookingDetails.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + phoneNumber;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(bookingDetails.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
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