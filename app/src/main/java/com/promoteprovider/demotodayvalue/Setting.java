package com.promoteprovider.demotodayvalue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.promoteprovider.demotodayvalue.utils.Util;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class Setting extends AppCompatActivity {
    //delete account
    LinearLayout dBtn,pdBtn;
    DocumentReference documentReference;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String userId;
    //alert
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        dialog = Util.getAlertDialog(this,"Update Loading...");
        pdBtn = findViewById(R.id.pdBtn);
        dBtn = findViewById(R.id.dBtn);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();

        //click to delete
        dBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdBtn.setVisibility(View.VISIBLE);
            }
        });

        pdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteProfile();

            }
        });
    }

    private void DeleteProfile() {
        ShowDialog();

    }

    private void ShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
        builder.setTitle("Delete Today'sValue Account Permanently");
        builder.setMessage("Are Sure to Delete Profile");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.show();
                firestore.collection("users").document(userId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                auth.getCurrentUser().delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Setting.this, "Profile Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Setting.this,MainActivity.class);
                                startActivity(intent);
                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Setting.this, "Fail to delete account", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}