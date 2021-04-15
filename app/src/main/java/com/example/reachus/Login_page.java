package com.example.reachus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login_page extends AppCompatActivity {
    private static final String TAG = "Email Password";
    EditText email,password;
 String s_email,s_pass;
 Button login,forgot_pass,signup;
    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(Login_page.this,MainActivity.class) );
            finish();
        }
        setContentView(R.layout.activity_login_page);


        email = findViewById(R.id.l_email);
        password = findViewById(R.id.l_password);
        login = findViewById(R.id.login_button);
        forgot_pass = findViewById(R.id.forgotpasword_button);
        signup = findViewById(R.id.Sign_up_button);

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_page.this,Forgot_password.class);
                startActivity(i);
                finish();

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirects the user to the registration Page
                Intent i = new Intent(Login_page.this,OTP_verificatiion.class);
                startActivity(i);
                finish();
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
            }
        });
    }

    private void signIn() {

        Log.d(TAG,"signin"+email);
        if (!validateform())
        {
            return;
        }

        s_email = email.getText().toString();
        s_pass = password.getText().toString();

        mAuth.signInWithEmailAndPassword(s_email,s_pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(Login_page.this,"Sign-In Sucessfull",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Login_page.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Log.w(TAG,"Failed to Sign-in",task.getException());
                            Toast.makeText(Login_page.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    private boolean validateform() {
        boolean valid = true;
        String em = email.getText().toString();
        if(TextUtils.isEmpty(em)){
            email.setError("Field Empty");
            valid = false;
        }

        else
        {
            email.setError(null);
        }

        String pw = password.getText().toString();
        if(TextUtils.isEmpty(pw)){
            password.setError("Field Empty");
            valid = false;
        }
        else
        {
            password.setError(null);
        }
        return valid;

    }


}
