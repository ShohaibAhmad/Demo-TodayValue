package com.promoteprovider.demotodayvalue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class Profile extends AppCompatActivity {
    LinearLayout goToWid;
    ImageView editProfile,settingBtn,profile_image,Cover_Image;
    TextView profile_name_main,BIO;


    //firebase
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String userId;
    // save data
    private Uri imageUri;
    private static final int PICK_IMAGE = 1;
    UploadTask uploadTask;
    StorageReference storageReference;
    DocumentReference documentReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        goToWid = findViewById(R.id.goToWid);
        settingBtn = findViewById(R.id.settingBtn);
        editProfile = findViewById(R.id.editProfile);
        profile_name_main = findViewById(R.id.profile_name_main);
        Cover_Image = findViewById(R.id.Cover_Image);
        profile_image = findViewById(R.id.profile_image);
        BIO = findViewById(R.id.BIO);
        //firebase start
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userId = auth.getUid();
        //firebase end

        // get user Data
        storageReference = FirebaseStorage.getInstance().getReference("Profile_Images");
        DocumentReference documentReference = firestore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                profile_name_main.setText(documentSnapshot.getString("FirstName") + " " + documentSnapshot.getString("LastName"));
                if (auth.getCurrentUser() != null) {
                    BIO.setText(documentSnapshot.getString("About"));
                }

            }
        });
        // get image
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                            String uri = task.getResult().getString("Profile_Image");
                            String uri1 = task.getResult().getString("Cover_Image");
                            Picasso.get().load(uri).into(profile_image);
                            Picasso.get().load(uri1).into(Cover_Image);
                        }
                        else
                        {
                            Toast.makeText(Profile.this, "No Profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
                startActivity(intent);
            }
        });
        goToWid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Reward.class);
                startActivity(intent);
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Setting.class);
                startActivity(intent);
            }
        });


    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profile.this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

}