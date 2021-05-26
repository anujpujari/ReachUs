package com.example.reachus;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash_Screen extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser fuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                mAuth = FirebaseAuth.getInstance();
                fuser = mAuth.getCurrentUser();
        if(mAuth.getCurrentUser()==null)
        {
            startActivity(new Intent(Splash_Screen.this,Login_page.class) );
            finish();
        }
        else
        {
            startActivity(new Intent(Splash_Screen.this,MainActivity.class) );
            finish();
        }
            /*    if(currentUser!=null)
      {
          Intent i = new Intent(Splash_Screen.this, MainActivity.class);
          startActivity(i);
          finish();
       } else
           {
                    Intent i = new Intent(Splash_Screen.this, Login_page.class);
                    startActivity(i);
                    finish();
           }*/

            }
        }, 5000);


    }
}