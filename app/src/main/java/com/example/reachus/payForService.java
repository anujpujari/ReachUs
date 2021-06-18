package com.example.reachus;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class payForService extends AppCompatActivity{

    Button serviceBooked;
    String orderCount;
    CollectionReference BookingInformation,UserServices;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userId,bookingId,providerUserId,BookingDate,BookingTime,StoreName,mainJob,secondaryJob,priceOfService;
    Bundle extras;
    Checkout ch;
    EditText amt,accHolderName,upiID,message;
    final int UPI_PAYMENT=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_for_service);

        message=findViewById(R.id.Message);
        upiID=findViewById(R.id.upiID);
        accHolderName=findViewById(R.id.accHolderName);
        amt=findViewById(R.id.amount);
        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        extras = getIntent().getExtras();
        if (extras != null) {
            providerUserId= extras.getString("providerUserId");
            BookingDate= extras.getString("BookingDate");
            BookingTime = extras.getString("BookingTime");
            StoreName= extras.getString("StoreName");
            mainJob= extras.getString("mainJob");
            secondaryJob= extras.getString("secondaryJob");
            priceOfService=extras.getString("priceOfService");
            Log.d("Values", bookingId+" "+providerUserId);
        }

        Task<DocumentSnapshot> dRef = fStore.collection("provider").document("userId"+providerUserId).
                collection("Bank Details").document("BankDetails").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    upiID.setText(document.getString("AccountNumber"));
                    accHolderName.setText(document.getString("AccountName"));
                }
            }
        });
        serviceBooked=findViewById(R.id.payForService);

        userId=mAuth.getCurrentUser().getUid();

        bookingId=userId.substring(0,7)+BookingTime.replace(":", "").replace("PM", "").replace("AM","");

        int amount=Math.round(Float.parseFloat(priceOfService));
        amt.setText(String.valueOf(amount));

        BookingInformation = fStore.collection("Services").document("userId"+providerUserId).collection("Bookings");
        UserServices = fStore.collection("ServicesBookedByUser").document("userId"+userId).collection("Bookings");


        serviceBooked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upiIdText = upiID.getText().toString();
                String nameText = accHolderName.getText().toString();
                String messageText = message.getText().toString();
                String amountRs =amt.getText().toString();
                payUsingUpi(amountRs,upiIdText,nameText,messageText);
            }
        });
    }
    void payUsingUpi(String amount, String upiId, String name, String note) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(payForService.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(payForService.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(payForService.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);
                Map<String,Object> BookingInfo = new HashMap<>();
                BookingInfo.put("BookingUserId", userId);
                BookingInfo.put("providerUserId", providerUserId);
                BookingInfo.put("BookingDate", BookingDate);
                BookingInfo.put("BookingTime", BookingTime);
                BookingInfo.put("bookingId", bookingId);
                BookingInfo.put("StoreName", StoreName);
                BookingInfo.put("mainJob", mainJob);
                BookingInfo.put("secondaryJob", secondaryJob);

                BookingInformation.add(BookingInfo);

                Map<String,Object> ServiceBookedInfo = new HashMap<>();
                ServiceBookedInfo.put("userId", userId);
                ServiceBookedInfo.put("bookingId", bookingId);
                ServiceBookedInfo.put("provideruserId", providerUserId);
                ServiceBookedInfo.put("BookingDate", BookingDate);
                ServiceBookedInfo.put("BookingTime", BookingTime);
                ServiceBookedInfo.put("StoreName", StoreName);
                ServiceBookedInfo.put("mainJob", mainJob);
                ServiceBookedInfo.put("secondaryJob", secondaryJob);

                UserServices.add(ServiceBookedInfo);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(payForService.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            else {
                Toast.makeText(payForService.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        } else {
            Toast.makeText(payForService.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}