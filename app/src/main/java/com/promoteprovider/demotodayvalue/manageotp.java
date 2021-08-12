package com.promoteprovider.demotodayvalue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class manageotp extends AppCompatActivity
{
    EditText t2;
    TextView b2;
    String phonenumber;
    String otpid;
    FirebaseAuth mAuth;
    ProgressBar otpV;
    TextView timer;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageotp);

        phonenumber=getIntent().getStringExtra("phone").toString();
        t2=findViewById(R.id.otpCode);
        b2= findViewById(R.id.otpSend);
        otpV= findViewById(R.id.otpV);
        timer= findViewById(R.id.timer);
        mAuth=FirebaseAuth.getInstance();

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Wait For OTP Code: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timer.setText("done!");
            }
        }.start();

        initiateotp();

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(t2.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Blank Field can not be processed", Toast.LENGTH_LONG).show();
                    otpV.setVisibility(View.GONE);
                }
                else if(t2.getText().toString().length()!=6) {
                    Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_LONG).show();
                    otpV.setVisibility(View.GONE);
                }
                else
                {
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpid,t2.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                    otpV.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void initiateotp()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
                    {
                        otpid=s;
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
                    {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });        // OnVerificationStateChangedCallbacks

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(manageotp.this,MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(),"Signing Code Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        otpV.setVisibility(View.GONE);
        super.onBackPressed();
    }
}