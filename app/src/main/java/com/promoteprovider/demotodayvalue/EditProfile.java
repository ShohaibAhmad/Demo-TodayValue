package com.promoteprovider.demotodayvalue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.promoteprovider.demotodayvalue.utils.Util;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    //var
    EditText bio,LastName,FirstName,dBirth,email,pass;;
    ImageView cover;
    CircleImageView profile_image;
    TextView updateBtn;
    //request
    private int request_code = 100;
    // save data
    private Uri imageUri;
    private static final int PICK_IMAGE = 1;
    UploadTask uploadTask;

    //fiebase
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    String userId;
    //alert
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        //alert
        dialog = Util.getAlertDialog(this,"Update Loading...");
        //firebase
        userId = auth.getUid();
            documentReference = firebaseFirestore.collection("users").document(userId);
            storageReference = FirebaseStorage.getInstance().getReference("Profile_Images");


        //find
        bio = findViewById(R.id.bio);
        FirstName = findViewById(R.id.fName);
        LastName = findViewById(R.id.lName);
        dBirth = findViewById(R.id.dBirth);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        cover = findViewById(R.id.cover);
        profile_image = findViewById(R.id.profile_image);
        updateBtn = findViewById(R.id.updateBtn);


        //change profile
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoseImage();
            }
        });
        //change cover
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoseImage();
            }
        });


            //update profile
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadData();
            }
        });
    }

    public void ChoseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE || resultCode == RESULT_OK || data != null || data.getData() != null)
        {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(profile_image);
            Picasso.get().load(imageUri).into(cover);
        }
    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void UploadData() {
        String fName = FirstName.getText().toString();
        String lName = LastName.getText().toString();
        String Birth = dBirth.getText().toString();
        String bios = bio.getText().toString();
        String emails = email.getText().toString();
        String passW = pass.getText().toString();
        if (!TextUtils.isEmpty(fName) || !TextUtils.isEmpty(lName) ||
                !TextUtils.isEmpty(Birth) || !TextUtils.isEmpty(bios) ||
                !TextUtils.isEmpty(emails) || !TextUtils.isEmpty(passW) || imageUri != null)
        {
            dialog.show();
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(imageUri));
            uploadTask = reference.putFile(imageUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                Uri downloadUri = task.getResult();
                                Map<String,String> profile = new HashMap<>();
                                profile.put("FirstName",fName);
                                profile.put("LastName",lName);
                                profile.put("About",bios);
                                profile.put("Email",emails);
                                profile.put("Password",passW);
                                profile.put("dBirth",Birth);
                                profile.put("Profile_Image",downloadUri.toString());
                                profile.put("Cover_Image",downloadUri.toString());
                                documentReference.set(profile)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.show();
                                                Toast.makeText(EditProfile.this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(EditProfile.this,Profile.class);
                                                startActivity(intent);
                                            }
                                        })

                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(EditProfile.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
        else
        {
            Toast.makeText(EditProfile.this, "Fill Full Form", Toast.LENGTH_SHORT).show();
        }

    }
}