package com.promoteprovider.demotodayvalue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.promoteprovider.demotodayvalue.Storages.MySharedPreferences;
import com.promoteprovider.demotodayvalue.utils.Util;

import java.util.HashMap;
import java.util.Map;


public class SignUp_part_1 extends AppCompatActivity {


    //auth
    EditText LastName,FirstName,dBirth,email,pass;
    String namef,namel,birth,emails,passw;
    TextView dataSend;

    //firebase
    private FirebaseAuth auth;
    private CollectionReference collectionReference;

    //alert
    private AlertDialog dialog;
    private MySharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_part1);
        //alert
        dialog = Util.getAlertDialog(this,"SignUp Loading...");
        sp =  MySharedPreferences.getInstance(this);
        //firebase init
        auth = FirebaseAuth.getInstance();
        collectionReference = FirebaseFirestore.getInstance().collection("users");

        //find
            FirstName =findViewById(R.id.fName);
            LastName = findViewById(R.id.lName);
            dBirth = findViewById(R.id.dBirth);
            email = findViewById(R.id.email);
            pass = findViewById(R.id.pass);
            //dataSend
            dataSend = findViewById(R.id.dataSend);

            //Already Logged

            //click to send user data
        dataSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDataAndLogin();
            }
        });





    }
        //check Data And Login
    private void checkDataAndLogin() {
        namef = FirstName.getText().toString();
        namel = LastName.getText().toString();
        birth = dBirth.getText().toString();
        emails = email.getText().toString().trim();
        passw = pass.getText().toString().trim();

        if (namef.isEmpty() || namef.equals("") ||
            namel.isEmpty() || namel.equals("") ||
            birth.isEmpty() || birth.equals("") ||
            emails.isEmpty() || emails.equals("") ||
            passw.isEmpty() || passw.equals("") || passw.length()<6)
        {
            Toast.makeText(getApplicationContext(), "Fill Full Form", Toast.LENGTH_SHORT).show();
        }

        else
            {
                dialog.show();
                auth.createUserWithEmailAndPassword(emails,passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                        }
                        else
                            {
                                saveUser();
                            }
                    }
                });
            }

    }
        //save User Data
    private void saveUser() {
        Map<String,String> map = new HashMap<>();
        map.put("FirstName",namef);
        map.put("LastName",namel);
        map.put("dBirth",birth);
        map.put("Email",emails);
        map.put("Password",passw);
        map.put("userId",auth.getUid());
        //add data
        collectionReference.document(auth.getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                if (!task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sp.setLogin("2");
                    sp.setUserId(auth.getUid());
                    Toast.makeText(getApplicationContext(), "SignUp Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp_part_1.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

    }

}