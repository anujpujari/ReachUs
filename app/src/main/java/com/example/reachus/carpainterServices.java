package com.example.reachus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class carpainterServices extends AppCompatActivity {

    TextView serviceName,serviceDescription,servicePrice;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    private static final String TAG = "Service Fetched";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpainter_services);

        serviceName=findViewById(R.id.serviceName);
        serviceDescription=findViewById(R.id.serviceDescription);
        servicePrice=findViewById(R.id.servicePrice);

        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        String userId=mAuth.getCurrentUser().getUid();
        DocumentReference ServiceReference = fStore.collection("provider").document(userId);

        ServiceReference.collection("ServiceCollection").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        if(document.getString("mainJob").equals("Repairing Service")){
                            if(document.getString("secondaryJob").equals("Carpainter")){
                                serviceName.setText(document.getString("mainJob"));
                                serviceDescription.setText(document.getString("Description"));
                                servicePrice.setText(document.getString("Price"));
                            }
                        }
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }
}