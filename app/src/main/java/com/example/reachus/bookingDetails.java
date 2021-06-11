package com.example.reachus;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class bookingDetails extends AppCompatActivity {

    TextView storeName, consumerAddress, providerAddress,deliverPrice, mainJob,dateTime,Phone,Email;
    Bundle extras;
    String BookingId,storename, consumeraddress,provideraddress,deliveryprice,mainjob,datetime,providerUserId,consumerUserId;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

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
            providerUserId=extras.getString("providerUserId");
        }

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
    }
}