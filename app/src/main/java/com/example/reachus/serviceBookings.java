package com.example.reachus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class serviceBookings extends AppCompatActivity {

    FirebaseFirestore fStore;
    String userId;
    String bookingId;
    FirebaseAuth mAuth;
    FirestoreRecyclerAdapter Adapter;
    RecyclerView recyclerView;
    TextView bookingDate,bookingTime,serviceuserName,bookingDateTime;
    View cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_bookings);

        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.recyclerViewServiceBookings);
        userId=mAuth.getCurrentUser().getUid();
        Query query = fStore.collection("Services").document("userId"+userId).collection("Bookings").whereEqualTo("providerUserId", userId);
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
        FirestoreRecyclerOptions<serviceOrderAttributes> options=new FirestoreRecyclerOptions.Builder<serviceOrderAttributes>().setQuery(query,serviceOrderAttributes.class).build();

        Adapter = new FirestoreRecyclerAdapter<serviceOrderAttributes, serviceBookings.ServicesViewHolder>(options){
            @NonNull
            @Override
            public serviceBookings.ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_servicebooking,parent,false);
                return new serviceBookings.ServicesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull serviceBookings.ServicesViewHolder holder, int position, @NonNull serviceOrderAttributes model) {
                holder.initializeValue(model.getBookingId(),model.getProviderUserId(),model.getBookingDate(),model.getBookingTime(),model.getStoreName(),model.getBookingUserId());
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(Adapter);
    }
    public class ServicesViewHolder extends RecyclerView.ViewHolder{
        private View view;
        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        void initializeValue(String BookingId, String providerUserId,String bookingdate,String bookingtime,String Storename,String BookingUserId){
            serviceuserName=view.findViewById(R.id.userBooking);
            bookingDateTime=view.findViewById(R.id.serviceDateTime);

            bookingDateTime.setText(bookingdate+" "+bookingtime);
            cardView=view.findViewById(R.id.cardView);

            DocumentReference documentReference = fStore.collection("users").document(BookingUserId);
            documentReference.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            serviceuserName.setText(document.getString("fullName"));
                        }
                    } else {
                    }
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), orderBookingDetails.class);
                    intent.putExtra("BookingId",BookingId);
                    intent.putExtra("BookingUserId", BookingUserId);
                    intent.putExtra("Storename",Storename);
                    intent.putExtra("bookingdate",bookingdate);
                    intent.putExtra("bookingtime",bookingtime);
                    intent.putExtra("providerUserId",providerUserId);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Redireting", Toast.LENGTH_LONG).show();
                }
            });
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