package com.example.reachus;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class reportService extends AppCompatActivity {

    RadioGroup grp;
    RadioButton button;
    EditText description;
    Button submit;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    int selectedId;
    Bundle extras;
    String providerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_service);

        extras = getIntent().getExtras();
        if(extras!=null){
            providerId=extras.getString("providerId");
        }

        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        grp=findViewById(R.id.reportValue);
        description=findViewById(R.id.problemDescription);
        submit=findViewById(R.id.submitProblem);

        selectedId=grp.getCheckedRadioButtonId();
        button=findViewById(selectedId);

        DocumentReference documentReference=fStore.collection("Services").document("userId"+providerId).collection("Reports").document("Report");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> report = new HashMap<>();
                report.put("Problem", button.getText().toString());
                report.put("problemDescription",description.getText().toString());
                documentReference.set(report);
            }
        });
    }
}