package com.example.reachus;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class timeForService extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button pickDate,timePikcer, bookService;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String bookingDate, bookingTime;
    TextView Date,Time;
    String bookingId,providerUserId,storeName,mainJob,secondaryJob,priceOfService;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_for_service);

        extras = getIntent().getExtras();
        if (extras != null) {
            providerUserId= extras.getString("provideruserId");
            priceOfService=extras.getString("priceOfService");
            Log.d("Values", bookingId+" "+providerUserId);
        }

        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        DocumentReference ref= fStore.collection("Services").document("userId"+providerUserId);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Data", "DocumentSnapshot data: " + document.getData());
                        storeName=document.getString("StoreName");
                        mainJob=document.getString("mainJob");
                        secondaryJob=document.getString("secondaryJob");
                    } else {
                        Log.d("Data", "No such document");
                    }
                } else {
                    Log.d("Data", "get failed with ", task.getException());
                }
            }
        });

        Date=findViewById(R.id.textView);
        Time=findViewById(R.id.time);
        Button pickDate = (Button) findViewById(R.id.button);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        timePikcer=findViewById(R.id.timePicker);
        timePikcer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        bookService=findViewById(R.id.bookService);
        bookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(timeForService.this, "Time to pay", Toast.LENGTH_SHORT).show();
                bookingDate=Date.getText().toString();
                bookingTime=Time.getText().toString();

                String userId = mAuth.getCurrentUser().getUid();

                Intent intent=new Intent(getApplicationContext(), payForService.class);
                intent.putExtra("providerUserId", providerUserId);
                intent.putExtra("StoreName", storeName);
                intent.putExtra("mainJob", mainJob);
                intent.putExtra("secondaryJob", secondaryJob);
                intent.putExtra("BookingDate", bookingDate);
                intent.putExtra("BookingTime", bookingTime);
                intent.putExtra("priceOfService", priceOfService);
                startActivity(intent);
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        LocalDate date = LocalDate.now();
        int currentDay = date.getDayOfMonth();
        int currentMonth = date.getMonthValue();
        int currentYear = date.getYear();
        Log.d("Values",month+""+currentMonth);
        if(year>=currentYear){
            if(month+1>=currentMonth){
                if(dayOfMonth>=currentDay){
                    Calendar c = Calendar.getInstance();
                    Calendar currentDate = Calendar.getInstance();
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String currentDateString = DateFormat.getDateInstance().format(c.getTime());

                    Date = (TextView) findViewById(R.id.textView);
                    Date.setText(currentDateString);
                }else{
                    Toast.makeText(timeForService.this,"Choose correct day",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(timeForService.this,"Choose correct month",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(timeForService.this,"Choose correct year",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onTimeSet(TimePicker view, int hour, int minutes) {
        String timeSet = "";
        String hr ="";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12){
            timeSet = "PM";
        }else{
            timeSet = "AM";
        }

        String min = "";
        if (minutes < 10)
            min = "0" + minutes ;
        else
            min = String.valueOf(minutes);
        hr = String.valueOf(hour);
        Log.d("length", hr.length()+"");
        if(hr.length()==1){
            hr ="0"+hour;
        }
        Time = (TextView) findViewById(R.id.time);
        Time.setText(hr + ":" + min+" "+timeSet);
    }
}