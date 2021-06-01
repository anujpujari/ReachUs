package com.example.reachus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class allServices extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore fStore;

    TextView ServiceName, ServiceDescription,ServicePrice;

    FirestoreRecyclerAdapter Adapter;
    String value,mainValue;
    Query query;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpainter_services);

        extras = getIntent().getExtras();
        if (extras != null) {
            mainValue=extras.getString("mainKey");
            value = extras.getString("key");
            Log.d("Values", mainValue+" "+value);
        }
        ServiceName=findViewById(R.id.serviceName);
        ServiceDescription=findViewById(R.id.serviceDescription);
        ServicePrice=findViewById(R.id.servicePrice);

        recyclerView=findViewById(R.id.recyclerView);

        fStore=FirebaseFirestore.getInstance();

        if(mainValue.equals("Repairing")){
            if(value.equals("carpainterService")){
                query = fStore.collection("Services").whereEqualTo("secondaryJob", "Carpainter");
            }
            else if(value.equals("plumberService")){
                query = fStore.collection("Services").whereEqualTo("secondaryJob", "Plumber");
            }
            else if(value.equals("electricianService")){
                query = fStore.collection("Services").whereEqualTo("secondaryJob", "Electrician");
            }
        }
        else if(mainValue.equals("Maid")){
            if(value.equals("utensilsCleaning")){
                query = fStore.collection("Services").whereEqualTo("secondaryJob", "UtensilsCleaning");
            }
            else if(value.equals("clothesCleaning")){
                query = fStore.collection("Services").whereEqualTo("secondaryJob", "ClothesCleaning");
            }
            else if(value.equals("homeCleaning")){
                query = fStore.collection("Services").whereEqualTo("secondaryJob", "HomeCleaning");
            }
        }
        else if(mainValue.equals("Car")){
            if(value.equals("carRepairing")){
                query = fStore.collection("Services").whereEqualTo("secondaryJob", "CarRepairing");
            }
            else if(value.equals("carWashing")){
                query = fStore.collection("Services").whereEqualTo("secondaryJob", "CarWashing");
            }
        }
        FirestoreRecyclerOptions<Services> options=new FirestoreRecyclerOptions.Builder<Services>().setQuery(query,Services.class).build();

        Adapter = new FirestoreRecyclerAdapter<Services, allServices.ServicesViewHolder>(options) {
            @NonNull
            @Override
            public allServices.ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_design,parent,false);
                return new allServices.ServicesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull allServices.ServicesViewHolder holder, int position, @NonNull Services model) {;
            holder.initializeValues(model.getMainJob(), model.getSecondaryJob(),model.getDescription(),model.getPrice(),model.getUserID());
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(Adapter);
    }

    private class ServicesViewHolder extends RecyclerView.ViewHolder {
        private View view;
        TextView ServiceName, ServiceDescription,ServicePrice;

        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        void initializeValues(String mainJob,String secondaryjob,String Description,String Price,String userID){
            ServiceName=(TextView) view.findViewById(R.id.serviceName);
            ServicePrice=(TextView) view.findViewById(R.id.servicePrice);
            ServiceDescription=(TextView) view.findViewById(R.id.serviceDescription);
            ServiceName.setText(mainJob);
            ServiceName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), BookService.class);
                    intent.putExtra("userId", userID);
                    startActivity(intent);
                }
            });
            ServiceDescription.setText(Description);
            ServicePrice.setText(Price);
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