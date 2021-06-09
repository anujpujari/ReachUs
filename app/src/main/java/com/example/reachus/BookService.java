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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class BookService extends AppCompatActivity {

    TextView bookStoreName,bookProviderName,bookServiceDescription,bookServiceAddress,bookServicePrice,bookService;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String value;
    Bundle extras;

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
        bookProviderName=findViewById(R.id.bookProviderName);
        bookServiceDescription=findViewById(R.id.bookServiceDescription);
        bookServiceAddress=findViewById(R.id.bookServiceAddress);
        bookServicePrice=findViewById(R.id.bookServicePrice);
        bookService=findViewById(R.id.bookService);

        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        fStore.collection("Services").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getString("userID").equals(value)){
                            bookStoreName.setText(document.getString("StoreName"));
                            bookServiceDescription.setText(document.getString("Description"));
                            bookServiceAddress.setText(document.getString("Address_1")+","+" "+document.getString("Address_2")+","+" "+
                                    document.getString("City")+"-"+document.getString("pincode")+","+" "+document.getString("District")+","+" Maharashtra"+","+" India");
                            bookServicePrice.setText("RS"+document.getString("Price"));
                        }
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
                startActivity(intent);
            }
        });
    }
}