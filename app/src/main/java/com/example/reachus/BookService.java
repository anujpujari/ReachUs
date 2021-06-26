package com.example.reachus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookService extends AppCompatActivity {

    TextView bookStoreName,bookProviderName,bookServiceDescription,bookServiceAddress,bookServicePrice,bookService;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String value;
    Bundle extras;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_service);
        extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("provideruserId");
            Log.d("UserId is", value);
        }

        bookStoreName=findViewById(R.id.bookStoreName);
        bookProviderName=findViewById(R .id.bookProviderName);
        bookServiceDescription=findViewById(R.id.bookServiceDescription);
        bookServiceAddress=findViewById(R.id.bookServiceAddress);
        bookServicePrice=findViewById(R.id.bookServicePrice);
        bookService=findViewById(R.id.bookService);

        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        fStore.collection("Services").document("userId"+value).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            bookStoreName.setText(document.getString("StoreName"));
                            bookServiceDescription.setText(document.getString("Description"));
                            bookServiceAddress.setText(document.getString("Address_1")+","+" "+document.getString("Address_2")+","+" "+
                                    document.getString("City")+"-"+document.getString("pincode")+","+" "+document.getString("District")+","+" Maharashtra"+","+" India");
                            price=document.getString("Price");
                            bookServicePrice.setText("RS"+price);
                        }else
                        {
                            Log.d("Data", "document dont exist ");
                    }
                } else {
                    Log.d("Data", "Error getting documents: ", task.getException());
                }
            }
        });

        bookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), timeForService.class);
                intent.putExtra("provideruserId", value);
                intent.putExtra("priceOfService", price);
                startActivity(intent);
            }
        });
    }
}