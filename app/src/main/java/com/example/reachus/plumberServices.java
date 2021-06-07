package com.example.reachus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class plumberServices extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore fStore;

    TextView ServiceName, ServiceDescription,ServicePrice;
    FirestoreRecyclerAdapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plumber_services);

        ServiceName=findViewById(R.id.serviceName);
        ServiceDescription=findViewById(R.id.serviceDescription);
        ServicePrice=findViewById(R.id.servicePrice);

        recyclerView=findViewById(R.id.recyclerView);

        fStore=FirebaseFirestore.getInstance();

        Query query = fStore.collection("Services").whereEqualTo("secondaryJob", "Plumber");

        FirestoreRecyclerOptions<Services> options=new FirestoreRecyclerOptions.Builder<Services>().setQuery(query,Services.class).build();

        Adapter = new FirestoreRecyclerAdapter<Services, ServicesViewHolder>(options) {
            @NonNull
            @Override
            public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_design,parent,false);
                return new ServicesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ServicesViewHolder holder, int position, @NonNull Services model) {
                holder.initializeValues(model.getMainJob(), model.getSecondaryJob(),model.getDescription(),model.getPrice());
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
        void initializeValues(String mainJob,String secondaryjob,String Description,String Price){
            ServiceName=(TextView) view.findViewById(R.id.serviceName);
            ServicePrice=(TextView) view.findViewById(R.id.servicePrice);
            ServiceDescription=(TextView) view.findViewById(R.id.serviceDescription);

            ServiceName.setText(mainJob);
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