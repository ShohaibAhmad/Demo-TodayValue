package com.promoteprovider.demotodayvalue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Profile extends AppCompatActivity {
    LinearLayout goToWid;
    ImageView editProfile,settingBtn;
    TextView profile_name_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        goToWid = findViewById(R.id.goToWid);
        settingBtn = findViewById(R.id.settingBtn);
        editProfile = findViewById(R.id.editProfile);
        profile_name_main = findViewById(R.id.profile_name_main);


        goToWid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,Reward.class);
                startActivity(intent);
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,Setting.class);
                startActivity(intent);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,EditProfile.class);
                startActivity(intent);
            }
        });
    }
}