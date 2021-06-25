package com.example.reachus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class allServices extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore fStore;

    TextView ServiceName, ServiceDescription,ServicePrice,ServiceType,secondaryServiceType,noServices;

    FirestoreRecyclerAdapter Adapter;
    FirebaseAuth mAuth;
    String value,mainValue,userId;
    Query query;
    Bundle extras;
    ProgressDialog pg1;
    View cardView;


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

        noServices=findViewById(R.id.noServices);
        //------------------------------


        pg1 = new ProgressDialog(allServices.this);
        pg1.setMessage("Loading wait...");
        pg1.setCancelable(true);
        pg1.setIndeterminate(true);
        pg1.show();

        recyclerView=findViewById(R.id.recyclerView);
        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        userId=mAuth.getCurrentUser().getUid().toString();

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
        }else if(mainValue.equals("Sanitizing")){
            if(value.equals("homeSanitizing")){
                query = fStore.collection("Services").whereEqualTo("secondaryJob", "HomeSanitizing");
            }
            else if(value.equals("vehicleSanitizing")){
                query = fStore.collection("Services").whereEqualTo("secondaryJob", "vehicleSanitizing");
            }
        }else if(mainValue.equals("home")) {
            query = fStore.collection("Services").whereEqualTo("mainJob", "Home Tutor");
        }else if(mainValue.equals("paste")) {
            query = fStore.collection("Services").whereEqualTo("mainJob", "Paste Control");
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
            protected void onBindViewHolder(@NonNull allServices.ServicesViewHolder holder, int position, @NonNull Services model) {
                    holder.initializeValues(model.getStoreName(),model.getMainJob(), model.getSecondaryJob(),model.getDescription(),model.getPrice(),model.getUserID(),model.getAddress_1(),model.getAddress_2(),model.getPincode(),model.getCity(),model.getDistrict(),model.getisIdVerified());
            }
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(getItemCount()==0){
                    pg1.dismiss();
                    recyclerView.setVisibility(View.GONE);
                    noServices.setVisibility(View.VISIBLE);
                }
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(Adapter);
    }

    private class ServicesViewHolder extends RecyclerView.ViewHolder {
        private View view;
        TextView ServiceName, ServiceDescription,ServicePrice;
        View cardView;
        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        void initializeValues(String StoreName,String mainJob,String secondaryjob,String Description,String Price,String provideruserID,String addr_1,String addr_2,String pincode,String city,String district,boolean isIdVerified) {
                if (!provideruserID.equals(userId)) {
                    if (isIdVerified) {
                        ServiceName = (TextView) view.findViewById(R.id.serviceName);
                        ServicePrice = (TextView) view.findViewById(R.id.servicePrice);
                        ServiceDescription = (TextView) view.findViewById(R.id.serviceDescription);
                        ServiceType = view.findViewById(R.id.serviceType);
                        secondaryServiceType = view.findViewById(R.id.secondaryServiceType);
                        cardView = view.findViewById(R.id.cardView);

                        ServiceType.setText(mainJob);
                        secondaryServiceType.setText(secondaryjob);
                        ServiceName.setText(StoreName);
                        ServiceDescription.setText(Description);
                        ServicePrice.setText("RS" + Price);
                        pg1.dismiss();

                        cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), BookService.class);
                                intent.putExtra("provideruserId", provideruserID);
                                startActivity(intent);
                            }
                        });
                    }else {
                        cardView = view.findViewById(R.id.cardView);
                        cardView.setVisibility(View.GONE);
                    }
                } else {
                    cardView = view.findViewById(R.id.cardView);
                    cardView.setVisibility(View.GONE);
                }
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