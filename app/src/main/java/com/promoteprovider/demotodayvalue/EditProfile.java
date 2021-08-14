package com.promoteprovider.demotodayvalue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    //var
    EditText bio;
    ImageView cover;
    CircleImageView profile_image;
    TextView updateBtn;
    //request
    private int request_code = 100;
    // save data
    private Uri pUri;
    //fiebase
    private FirebaseAuth auth;
    private CollectionReference collectionReference;
    private StorageReference storageReference;
    private String mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        //firebase
        auth = FirebaseAuth.getInstance();
        collectionReference = FirebaseFirestore.getInstance().collection("users");
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile_Image");
        mUserId = auth.getUid();
        //find
        bio = findViewById(R.id.bio);
        cover = findViewById(R.id.cover);
        profile_image = findViewById(R.id.profile_image);
        updateBtn = findViewById(R.id.updateBtn);

        //update profile
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,request_code);
            }
        });

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,request_code);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDataAndSave();
            }
        });
    }

    private void checkDataAndSave() {
        String about  = bio.getText().toString().trim();
        if (pUri == null){
            Toast.makeText(getApplicationContext(), "Add Profile Image", Toast.LENGTH_SHORT).show();
            return;
        }
        if (about.length()<10){
            Toast.makeText(getApplicationContext(), "bio too short", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            uploadImage(about);
        }
    }

    private void uploadImage(String about) {
        UploadTask task = storageReference.child(mUserId).putFile(pUri);
        task.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            saveData(about,imageUrl);
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveData(String about, String imageUrl) {

        Map<String,Object> map = new HashMap<>();
        map.put("Profile_Image",imageUrl);
        map.put("About",about);
        collectionReference.document(mUserId).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Profile Info Saved", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_code && resultCode == RESULT_OK){
            CropImage.activity(data.getData())
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .setOutputCompressQuality(50)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (result != null ){
                pUri = result.getUri();
                profile_image.setImageURI(pUri);
                cover.setImageURI(pUri);
            }
        }
    }

}