package com.example.reachus;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    Button logout,completeProfile;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userId,s_name,s_email;
    TextView fullName,Email;
    ProgressDialog progress;
    boolean isServiceProvider;
    private static final String TAG = "EmailPassword";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        completeProfile = findViewById(R.id.completeProfile);

        fullName=findViewById(R.id.fullName);
        Email=findViewById(R.id.Email);
        logout = findViewById(R.id.logout);

        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login_page.class));
                finish();
            }
        });

        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        userId=mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.collection("users").document("Info")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                        DocumentSnapshot document=task.getResult();
                            if(document.exists()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            fullName.setText(document.getString("fullName"));
                            Email.setText(document.getString("Email"));
                            progress.dismiss();
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document=task.getResult();
                    if (document.exists()) {
                        Log.d("Data", "DocumentSnapshot data: " + document.getData());
                        if(!document.getData().isEmpty())
                            isServiceProvider=(boolean)document.get("isServiceProvider");
                    } else {
                        Log.d("data", "No such document");
                    }
                }
            }
        });

        completeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),complete_profile.class);
                intent.putExtra("isServiceProvider", isServiceProvider);
                startActivity(intent);
            }
        });
        //Botton Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationbar);
        //Selected Default as Home Navigation
        bottomNavigationView.setSelectedItemId(R.id.Profilee);

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
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }


    }