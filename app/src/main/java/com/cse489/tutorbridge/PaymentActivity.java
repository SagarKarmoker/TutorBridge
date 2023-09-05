package com.cse489.tutorbridge;

import android.os.Bundle;

import com.cse489.tutorbridge.chat.ChatMainActivity;
import com.cse489.tutorbridge.modal.HistoryClass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cse489.tutorbridge.modal.OrderModal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener{
    private static final String TAG = PaymentActivity.class.getSimpleName();
    private AlertDialog.Builder alertDialogBuilder;
    double salary = 0;
    String mentorId="";
    String userId, orderId, category = "";
    TextView orderIdPayment, orderValue;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();

        salary = i.getDoubleExtra("mentorSalary", 0);
        mentorId = i.getStringExtra("mentorId");
        userId = i.getStringExtra("userId");
        orderId = i.getStringExtra("orderId");
        category = i.getStringExtra("mentorCategory");

        orderIdPayment = findViewById(R.id.orderIdPayment);
        orderValue = findViewById(R.id.orderValue);

        orderIdPayment.setText("Order #"+ orderId);
        orderValue.setText("BDT "+ salary);


        //HistoryClass history = (HistoryClass) getIntent().getSerializableExtra("order");
        //assert history != null;
        //System.out.println(history.toString());


        /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());

        // Payment button created by you in XML layout
        Button button = (Button) findViewById(R.id.btn_pay);

        alertDialogBuilder = new AlertDialog.Builder(PaymentActivity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            //do nothing
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });

        /*TextView privacyPolicy = (TextView) findViewById(R.id.txt_privacy_policy);

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse("https://razorpay.com/sample-application/"));
                startActivity(httpIntent);
            }
        });*/

    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        //rzp_test_jz5wiS63wA0WlE api
        EditText etApiKey = findViewById(R.id.et_api_key);
        etApiKey.setText("rzp_test_jz5wiS63wA0WlE");
        if (!TextUtils.isEmpty(etApiKey.getText().toString())){
            co.setKeyID("rzp_test_jz5wiS63wA0WlE");
        }
        EditText etCustomOptions = findViewById(R.id.et_custom_options);
        if (!TextUtils.isEmpty(etCustomOptions.getText().toString())){
            try{
                JSONObject options = new JSONObject(etCustomOptions.getText().toString());
                co.open(activity, options);
            }catch (JSONException e){
                Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                e.printStackTrace();
            }
        }else{
            try {
                JSONObject options = new JSONObject();
                options.put("name", "TutorBridge Payment");
                options.put("description", "Demoing Charges");
                options.put("send_sms_hash",true);
                options.put("allow_rotation", true);
                //You can omit the image option to fetch the image from dashboard
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                options.put("currency", "INR");
                String val = String.valueOf(salary*100);
                options.put("amount",val);

                JSONObject preFill = new JSONObject();
                preFill.put("email", "test@razorpay.com");
                preFill.put("contact", "9876543210");

                options.put("prefill", preFill);

                co.open(activity, options);
            } catch (Exception e) {
                Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                e.printStackTrace();
            }
        }


    }

    public void onExternalWalletSelected(String s, PaymentData paymentData) {
        try{
            alertDialogBuilder.setMessage("External Wallet Selected:\nPayment Data: "+paymentData.getData());
            alertDialogBuilder.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try{
            //alertDialogBuilder.setMessage("Payment Successful :\nPayment ID: "+s+"\nPayment Data: "+paymentData.getData());
            //alertDialogBuilder.show();


            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date = sdf.format(calendar.getTime());

            OrderModal order = new OrderModal(mentorId, userId, orderId, "Paid", date, "Visa/MasterCard", category, String.valueOf(salary));


            order.toString();
            CollectionReference ordersCollection = db.collection("doubt_history"); // Replace "orders" with your collection name
            ordersCollection.add(order)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(PaymentActivity.this, "Your Payment Is Done", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

            Intent i = new Intent(PaymentActivity.this, ChatMainActivity.class);
            i.putExtra("mentorid", mentorId);
            i.putExtra("userid", userId);
            i.putExtra("orderId", orderId);
            startActivity(i);

            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try{
            alertDialogBuilder.setMessage("Payment Failed:\nPayment Data: "+paymentData.getData());
            alertDialogBuilder.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}