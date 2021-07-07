package com.example.reachus;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class userBookings extends AppCompatActivity {

    FirebaseFirestore fStore;
    String userId;
    String bookingId;
    FirebaseAuth mAuth;
    FirestoreRecyclerAdapter Adapter;
    RecyclerView recyclerView;
    TextView bookingDate,bookingTime,StoreName, mainJob,secondaryJob,bookingDateTime,noServices;
    View cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bookings);
        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        noServices=findViewById(R.id.noServices);

        recyclerView=findViewById(R.id.recyclerViewBookings);
        userId=mAuth.getCurrentUser().getUid();

        Log.d("UserId", userId.substring(0,7));

        Query query = fStore.collection("ServicesBookedByUser").document("userId"+userId).collection("Bookings").whereEqualTo("userId", userId);
//        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if(error==null){
//                    Log.d("Query is empty", value+"");
//                }
//                else{
//                    Log.d("Query is", value+"");
//                }
//            }
//        });
        FirestoreRecyclerOptions<userBookingAttributes> options=new FirestoreRecyclerOptions.Builder<userBookingAttributes>().setQuery(query,userBookingAttributes.class).build();

        Adapter = new FirestoreRecyclerAdapter<userBookingAttributes, userBookings.ServicesViewHolder>(options){
            @NonNull
            @Override
            public userBookings.ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_bookings,parent,false);
                return new userBookings.ServicesViewHolder(view);
            }
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onBindViewHolder(@NonNull userBookings.ServicesViewHolder holder, int position, @NonNull userBookingAttributes model) {
                    Log.d("Service", Adapter.getItemCount()+"Initialized");
                    holder.initializeValue(model.getBookingId(),model.getProvideruserId(),model.getBookingDate(),model.getBookingTime(),model.getStoreName(),model.getMainJob(),model.getSecondaryJob());
            }
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(getItemCount()==0){
                    recyclerView.setVisibility(View.GONE);
                    noServices.setVisibility(View.VISIBLE);
                }
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(Adapter);
    }

    public class ServicesViewHolder extends RecyclerView.ViewHolder {
        private View view;
        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        void initializeValue(String BookingId, String providerUserId, String bookingdate, String bookingtime, String Storename, String mainjob, String secondaryjob){
            StoreName=view.findViewById(R.id.StoreNameBooking);
            mainJob=view.findViewById(R.id.mainJobBooking);
            bookingDateTime=view.findViewById(R.id.bookingDateTime);
            cardView=view.findViewById(R.id.cardView);

            LocalDate date = LocalDate.now();
            int currentDay = date.getDayOfMonth();
            int currentMonth = date.getMonthValue();
            int currentYear = date.getYear();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String currentTime = dtf.format(now).substring(11, 19);
            int currenthour = Integer.parseInt(currentTime.substring(0, 2));
            int currentmin = Integer.parseInt(currentTime.substring(3, 5));

            Log.d("Current Time", currenthour+" "+currentmin);
            Log.d("Time", bookingtime+"");
            int inputHour = Integer.parseInt(bookingtime.substring(0,2));
            int inputMin = Integer.parseInt(bookingtime.substring(3,6).replace(" ",""));

            Log.d("Input Time", inputHour+" "+inputMin);


            Log.d("current Date", currentDay+"");
            Log.d("Input Day", bookingdate.substring(0,2)+"");
            int inputDay = Integer.parseInt(bookingdate.substring(0,2));
            if(currentDay > inputDay){
                Log.d("day false","");
                cardView.setVisibility(View.GONE);
            }
            else if(currentDay==inputDay){
                if(currenthour>inputHour){
                    Log.d("hour", "");
                    cardView.setVisibility(View.GONE);
                }else if(inputHour==currenthour){
                    if(currentmin>inputMin){
                        Log.d("min", "");
                        cardView.setVisibility(View.GONE);
                    }
                    else{
                        Log.d("Data Initializing", Storename+" "+mainjob);
                        StoreName.setText(Storename);
                        mainJob.setText(mainjob);
                        bookingDateTime.setText(bookingdate+" "+bookingtime);
                    }
                }else{
                    Log.d("Data Initializing", Storename+" "+mainjob);
                    StoreName.setText(Storename);
                    mainJob.setText(mainjob);
                    bookingDateTime.setText(bookingdate+" "+bookingtime);
                }
            }
            else{
                Log.d("Data Initializing", Storename+" "+mainjob);
                StoreName.setText(Storename);
                mainJob.setText(mainjob);
                bookingDateTime.setText(bookingdate+" "+bookingtime);
            }
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), bookingDetails.class);
                    intent.putExtra("BookingId",BookingId);
                    intent.putExtra("Storename",Storename);
                    intent.putExtra("mainJob", mainjob);
                    intent.putExtra("bookingdate",bookingdate);
                    intent.putExtra("bookingtime",bookingtime);
                    intent.putExtra("providerUserId",providerUserId);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Redireting", Toast.LENGTH_LONG).show();
                }
            });
        }
        void noBookings(){
            TextView noBookings = view.findViewById(R.id.noBookings);
            noBookings.setVisibility(View.VISIBLE);
            noBookings.setText("Please Book Some Services");
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Adapter.stopListening();
    }
}