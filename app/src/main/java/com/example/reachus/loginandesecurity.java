package com.example.reachus;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class loginandesecurity extends AppCompatActivity {
    private static final String TAG = "Data Retrieval";
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String s_email,userId,pass,newemail;
    Button updateemail,done;
    EditText nmail;
    LinearLayout nwmail;
    ProgressDialog pg;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_security);
        TextView email = findViewById(R.id.LS_Email);
        updateemail = findViewById(R.id.updateemail);
        nmail = findViewById(R.id.newmail);
        done  = findViewById(R.id.done);
        nwmail = findViewById(R.id.newmaillayout);
        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        getdata(email);

        pg = new ProgressDialog(loginandesecurity.this);
        pg.setMessage("Changing Your Email..");
        pg.setIndeterminate(true);
        pg.setCancelable(false);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pg.show();
                newemail = nmail.getText().toString();
                updateemailwithpass(newemail);
            }
        });
        updateemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: innnnnnnnnnnnnnnn");
                AlertDialog.Builder builder = new AlertDialog.Builder(loginandesecurity.this);
                builder.setTitle("Do you want to update your email");
                builder.setMessage("Enter Password if you want to change your email");
                final EditText input = new EditText(loginandesecurity.this);
                input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                builder.setView(input);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing but close the dialog
                        pass= input.getText().toString();

                        Log.d(TAG, "onClick: "+pass);
                        nwmail.setVisibility(View.VISIBLE);
                        dialog.dismiss();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(loginandesecurity.this, "", Toast.LENGTH_SHORT).show();
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });



    }

    private void updateemailwithpass(String newemail) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider
                .getCredential(s_email, pass);

        // Current Login Credentials \\
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");
                        //Now change your email address \\
                        //----------------Code for Changing Email Address----------\\
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(loginandesecurity.this.newemail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            DocumentReference docRef = fStore.collection("users").document(userId).collection("users").document("Info");
                                            Map<String, Object> updatedEmail=new HashMap<>();
                                            updatedEmail.put("Email",nmail.getText().toString());
                                            docRef.update(updatedEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(loginandesecurity.this,"Email updated sucessfully and verification mail has been sent",Toast.LENGTH_LONG).show();
                                                    user.sendEmailVerification();
                                                    pg.dismiss();
                                                }
                                            });

//                                            Map<String,Object> users = new HashMap<>();
//                                            users.put("Email", s_email);
//                                            Task<Void> reference;
//                                            reference = fStore.collection("users")
//                                                    .document(userId).collection("users").document("Info").update(users).addOnFailureListener(new OnFailureListener() {
//                                                        @Override
//                                                        public void onFailure(@NonNull Exception e) {
//                                                            Toast.makeText(loginandesecurity.this, "Failed", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    });
//                                            Log.d(TAG, "You can now sign in with a new Email");
//                                            Toast.makeText(loginandesecurity.this, "Email Address Updated", Toast.LENGTH_SHORT).show();
//                                            nwmail.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });


                        //----------------------------------------------------------\\
                    }
                });
    }



    private void getdata(TextView email) {
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.collection("users").document("Info")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document=task.getResult();
                    if(document.exists()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        s_email=document.getString("Email");
                        email.setText(s_email);
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }
}