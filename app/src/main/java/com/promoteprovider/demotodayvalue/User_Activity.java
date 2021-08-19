package com.promoteprovider.demotodayvalue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.promoteprovider.demotodayvalue.adapters.UserAdapter;
import com.promoteprovider.demotodayvalue.databinding.ActivityUserBinding;
import com.promoteprovider.demotodayvalue.models.User;
import com.promoteprovider.demotodayvalue.utils.Constants;
import com.promoteprovider.demotodayvalue.utils.PreferenceManager;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;


public class User_Activity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private PreferenceManager preferenceManager;

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
        preferenceManager = new PreferenceManager(getApplicationContext());
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();
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
                        if (task.getResult().exists()){
                            String uri = task.getResult().getString("Profile_Image");
                            Picasso.get().load(uri).into(binding.profileImage);
                        }
                        else
                        {
                            Toast.makeText(User_Activity.this, "No Profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            getToken();
            getUser();
            setListener();
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_Activity.this,Profile.class);
                startActivity(intent);
            }
        });

    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token)
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // [START transactions]
        final DocumentReference sfDocRef = firestore.collection(Constants.KEY_COLLECTION_USERS).document(userId);

        firestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);

//
                transaction.update(sfDocRef,Constants.KEY_FCM_TOKEN, token);

//

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(User_Activity.this, "Update success!", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(User_Activity.this, "Update fail", Toast.LENGTH_SHORT).show();

                    }
                });
        // [END transactions]

    }
   private void getUser(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                   String currentUserId = preferenceManager.getString(Constants.KEY_UserId);
                   if (task.isSuccessful() && task.getResult() != null)
                   {
                       List<User> users = new ArrayList<>();
                       for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                       {
                           if (currentUserId.equals(queryDocumentSnapshot.getId()))
                           {
                               continue;
                           }
                           User user = new User();
                           user.name = queryDocumentSnapshot.getString(Constants.KEY_FirstName);
                           user.name1 = queryDocumentSnapshot.getString(Constants.KEY_LastName);
                           user.email = queryDocumentSnapshot.getString(Constants.KEY_Email);
                           user.image = queryDocumentSnapshot.getString(Constants.KEY_Image);
                           user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                           users.add(user);
                       }
                       if (users.size() > 0)
                       {
                           UserAdapter userAdapter = new UserAdapter(users);
                           binding.userRecyclerView.setAdapter(userAdapter);
                           binding.userRecyclerView.setVisibility(View.VISIBLE);
                       }
                       else
                       {
                           Toast.makeText(User_Activity.this, "User not found", Toast.LENGTH_SHORT).show();
                       }
                   }
                   else
                   {
                       Toast.makeText(User_Activity.this, "User not found", Toast.LENGTH_SHORT).show();
                   }
                });
   }
   private void setListener(){
        binding.imageBack.setOnClickListener(view -> onBackPressed());
   }
}