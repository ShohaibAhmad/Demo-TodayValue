package com.promoteprovider.demotodayvalue.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.promoteprovider.demotodayvalue.MainActivity;
import com.promoteprovider.demotodayvalue.R;
import com.promoteprovider.demotodayvalue.UploadActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    ConstraintLayout mainLayout;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    DocumentReference documentReference;
    CircleImageView profile_image;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mainLayout = view.findViewById(R.id.mainLayout);
        profile_image = view.findViewById(R.id.profile_image);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();

        if (auth.getCurrentUser() != null) {
            DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);
            // get image
            documentReference.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                String uri = task.getResult().getString("Profile_Image");
                                Picasso.get().load(uri).into(profile_image);

                            } else {
                                Toast.makeText(getContext(), "No Profile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }


        mainLayout.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(getContext(), UploadActivity.class);
                     startActivity(intent);
                 }
             });

        return view;
    }
}