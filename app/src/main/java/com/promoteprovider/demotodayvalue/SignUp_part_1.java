package com.promoteprovider.demotodayvalue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUp_part_1 extends AppCompatActivity {


    //auth
    EditText LastName,FirstName,dBirth,email,pass;
    ProgressBar progressBar2;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    TextView dataSend;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_part1);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        //auth
            progressBar2 = findViewById(R.id.progressBar2);
            FirstName =findViewById(R.id.FirstName);
            LastName = findViewById(R.id.LastName);
            dBirth = findViewById(R.id.dBirth);
            email = findViewById(R.id.email);
            pass = findViewById(R.id.pass);
            //dataSend
            dataSend = findViewById(R.id.dataSend);

            //Already Logged
        if (auth.getCurrentUser() != null){
            Intent intent = new Intent(SignUp_part_1.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

            //click to send user data
        dataSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int
                String fName = FirstName.getText().toString();
                String lName = LastName.getText().toString();
                String dBarth = dBirth.getText().toString();
                String emailF = email.getText().toString();
                String passF = pass.getText().toString();

                //Auth
                if (TextUtils.isEmpty(fName)){
                    FirstName.setError("First Name is required!");
                    return;
                }

                if (TextUtils.isEmpty(lName)){
                    LastName.setError("Last Name is required!");
                    return;
                }

                if (TextUtils.isEmpty(dBarth)){
                    dBirth.setError("Date Of Birth is required!");
                    return;
                }

                if (TextUtils.isEmpty(emailF)){
                    email.setError("Email is required!");
                    return;
                }

                if (TextUtils.isEmpty(passF)){
                    pass.setError("Password is required!");
                    return;
                }

                if (passF.length() < 6){
                    pass.setError("Password must be >= 6 Character!");
                    return;
                }
                progressBar2.setVisibility(View.VISIBLE);


                //map
                Map<String,Object> data = new HashMap<>();
                data.put("fName",fName);
                data.put("lName",lName);
                data.put("dBarth",dBarth);
                data.put("emailF",emailF);
                data.put("passF",passF);



                firestore.collection("Users").document().set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(SignUp_part_1.this, "Registration Successfully!", Toast.LENGTH_SHORT).show();
                                auth.createUserWithEmailAndPassword(emailF,passF).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            Intent intent = new Intent(SignUp_part_1.this,Otp_Number.class);
                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(SignUp_part_1.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            progressBar2.setVisibility(View.GONE);
                                        }
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUp_part_1.this, "User Created Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });





    }

}