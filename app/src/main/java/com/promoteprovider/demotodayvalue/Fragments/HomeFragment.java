package com.promoteprovider.demotodayvalue.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.promoteprovider.demotodayvalue.MainActivity;
import com.promoteprovider.demotodayvalue.R;
import com.promoteprovider.demotodayvalue.StoryActivity;
import com.promoteprovider.demotodayvalue.StoryMember;
import com.promoteprovider.demotodayvalue.StoryViewHolder;
import com.promoteprovider.demotodayvalue.UploadActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    ConstraintLayout mainLayout,Add_story;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    DocumentReference documentReference;
    CircleImageView profile_image;
    ImageView storyImage,profileStory;
    String userId;
    private static int PICIMAGE = 1;
    Uri imageUri;

    //story
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    RecyclerView recyclerViewStory;
    DatabaseReference  storyRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViewStory = view.findViewById(R.id.rv_storyf4);
        mainLayout = view.findViewById(R.id.mainLayout);
        Add_story = view.findViewById(R.id.Add_story);
        profile_image = view.findViewById(R.id.profile_image);
        profileStory = view.findViewById(R.id.profileStory);
        storyImage = view.findViewById(R.id.storyImage);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();
        //set post layout profile image
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
                                Picasso.get().load(uri).into(profileStory);

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

        //story
        storyRef = database.getReference("All story");
        recyclerViewStory.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewStory.setLayoutManager(linearLayoutManager);
        recyclerViewStory.setItemAnimator(new DefaultItemAnimator());

        // recyclerStoryAdapter
        FirebaseRecyclerOptions<StoryMember> options
                = new FirebaseRecyclerOptions.Builder<StoryMember>()
                .setQuery(storyRef,StoryMember.class)
                .build();

        FirebaseRecyclerAdapter<StoryMember, StoryViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<StoryMember, StoryViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull StoryViewHolder holder, int i, @NonNull StoryMember model) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String currentUserId = user.getUid();
                        holder.setStory(getActivity(),model.getPostUri(),model.getName(),model.getTimeEnd(),model.getTimeUpload(),model.getType(),
                                model.getCaption(),model.getUrl(),model.getUid());


                    }

                    @NonNull
                    @Override
                    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view1 = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.story_layout,parent,false);
                        return new StoryViewHolder(view1);
                    }
                };
                firebaseRecyclerAdapter.startListening();
                recyclerViewStory.setAdapter(firebaseRecyclerAdapter);
        //set post layout click listener
        mainLayout.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(getContext(), UploadActivity.class);
                     startActivity(intent);
                 }
             });
        // add story
        Add_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,PICIMAGE);

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICIMAGE || resultCode == Activity.RESULT_OK || data != null || data.getData() != null)
            {
                imageUri = data.getData();
                String url = imageUri.toString();
                Intent intent = new Intent(getActivity(), StoryActivity.class);
                intent.putExtra("u",url);
                startActivity(intent);
            }
            else {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e){
            Toast.makeText(getActivity(), "error"+e, Toast.LENGTH_SHORT).show();
        }
    }
}