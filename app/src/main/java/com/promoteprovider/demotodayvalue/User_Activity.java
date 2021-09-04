package com.promoteprovider.demotodayvalue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.promoteprovider.demotodayvalue.databinding.ActivityUserBinding;
import com.squareup.picasso.Picasso;


public class User_Activity extends AppCompatActivity {

    private ActivityUserBinding binding;
    //firebase
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    StorageReference storageReference;
    String userId;
    @SuppressWarnings("MoveFieldAssignmentToInitializer")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();
        if (auth.getCurrentUser() != null) {
// get user Data
            storageReference = FirebaseStorage.getInstance().getReference("Profile_Images");
            DocumentReference documentReference = firestore.collection("users").document(userId);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    binding.profileNameChat.setText(documentSnapshot.getString("FirstName") + " " + documentSnapshot.getString("LastName"));


                }
            });
            // get image
            documentReference.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                String uri = task.getResult().getString("Profile_Image");
                                Picasso.get().load(uri).into(binding.profileImage);
                            } else {
                                Toast.makeText(User_Activity.this, "No Profile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
            setListener();
        }
        else
        {
            Intent intent = new Intent(User_Activity.this,MainActivity.class);
            startActivity(intent);
        }

    }

   private void setListener(){
        binding.imageBack.setOnClickListener(view -> onBackPressed());
   }
}