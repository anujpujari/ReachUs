package com.example.reachus;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class userBookings extends AppCompatActivity {

    FirebaseFirestore fStore;
    String userId;
    String bookingId;
    FirebaseAuth mAuth;
    FirestoreRecyclerAdapter Adapter;
    RecyclerView recyclerView;
    TextView bookingDate,bookingTime,StoreName, mainJob,secondaryJob,bookingDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bookings);
        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.recyclerViewBookings);
        userId=mAuth.getCurrentUser().getUid();

        Log.d("UserId", userId.substring(0,7));

        Query query = fStore.collection("ServicesBookedByUser").document("userId"+userId).collection("Bookings").whereEqualTo("userId", userId);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value==null){
                    Log.d("Query is empty", value+"");
                }
                else{
                    Log.d("Query is", value+""+error);
                }
            }
        });
        FirestoreRecyclerOptions<userBookingAttributes> options=new FirestoreRecyclerOptions.Builder<userBookingAttributes>().setQuery(query,userBookingAttributes.class).build();

        Adapter = new FirestoreRecyclerAdapter<userBookingAttributes, userBookings.ServicesViewHolder>(options){
            @NonNull
            @Override
            public userBookings.ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_bookings,parent,false);
                return new userBookings.ServicesViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull userBookings.ServicesViewHolder holder, int position, @NonNull userBookingAttributes model) {
                    holder.initializeValue(model.getBookingId(),model.getProvideruserId(),model.getBookingDate(),model.getBookingTime(),model.getStoreName(),model.getMainJob(),model.getSecondaryJob());
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
        void initializeValue(String BookingId, String providerUserId,String bookingdate,String bookingtime,String Storename,String mainjob,String secondaryjob){
                StoreName=view.findViewById(R.id.StoreNameBooking);
                mainJob=view.findViewById(R.id.mainJobBooking);
                bookingDateTime=view.findViewById(R.id.bookingDateTime);

                StoreName.setText(Storename);
                mainJob.setText(mainjob);
                bookingDateTime.setText(bookingdate+" "+bookingtime);
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