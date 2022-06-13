package com.example.chethan.industrain.paymentgateway;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chethan.industrain.R;
import com.razorpay.Checkout;
import com.razorpay.Payment;
import com.razorpay.PaymentResultListener;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class paymadakke extends AppCompatActivity implements PaymentResultListener {

    Button pay;
    int payamount;
    EditText value;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymadakke);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.blackk));
            getWindow().setStatusBarColor(getResources().getColor(R.color.blackk));
        }
        getSupportActionBar().hide();

        value=findViewById(R.id.input);
        pay=findViewById(R.id.razorpay);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Donate");

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String a=value.getText().toString().trim();
                if(a.length()>0)
                {
                    startpayment();

                }
                else {
                    Toast.makeText(paymadakke.this, "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void startpayment()
    {
        payamount=Integer.parseInt(value.getText().toString());
        Checkout checkout=new Checkout();
        checkout.setImage(R.mipmap.majorwithveg);
        final Activity activity=this;



        try{
            JSONObject options=new JSONObject();
            options.put("description","Thank you for Donating to us:)");
            options.put("currency","INR");
            options.put("amount",payamount*100);
            checkout.open(activity,options);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        //https://github.com/razorpay/razorpay-java
        RazorpayClient razorpay = null;
        try
        {
            razorpay = new RazorpayClient("rzp_test_CzgtB1VG2KvnMK", "8F8XmlNEIe82dFSyzWyuqf6P");
            JSONObject captureRequest = new JSONObject();
            captureRequest.put("amount", payamount*100); // Amount should be in paise
            Payment payment = razorpay.Payments.capture("pay_BBq6qiPrpfa8Lp", captureRequest);


        } catch (RazorpayException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
