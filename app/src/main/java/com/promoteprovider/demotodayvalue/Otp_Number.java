package com.promoteprovider.demotodayvalue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class Otp_Number extends AppCompatActivity {
   EditText phone_number;
   ProgressBar otppNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_number);

        TextView verify=findViewById(R.id.verify);
        phone_number = findViewById(R.id.phone_number);
        otppNumber = findViewById(R.id.otppNumber);


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if (phone_number.getText().toString().isEmpty()){
                  otppNumber.setVisibility(View.GONE);
                  Toast.makeText(Otp_Number.this, "Enter Your Number", Toast.LENGTH_SHORT).show();
              }
              else {
                  Intent intent = new Intent(Otp_Number.this, manageotp.class);
                  intent.putExtra("phone", phone_number.getText().toString().replace(" ", ""));
                  startActivity(intent);
                  otppNumber.setVisibility(View.VISIBLE);
              }
            }
        });


    }
    @Override
    public void onBackPressed() {
        otppNumber.setVisibility(View.GONE);
        super.onBackPressed();
    }
    }
