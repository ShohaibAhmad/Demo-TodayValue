package com.promoteprovider.demotodayvalue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;
import com.promoteprovider.demotodayvalue.Fragments.HomeFragment;
import com.promoteprovider.demotodayvalue.Fragments.MessageFragment;
import com.promoteprovider.demotodayvalue.Fragments.PodcastFragment;
import com.promoteprovider.demotodayvalue.Fragments.Short_VideoFragment;
import com.promoteprovider.demotodayvalue.Fragments.VideoFragment;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    // start navigation variable
        MeowBottomNavigation meowBottomNavigation;
        LinearLayout Main_Container;
    // end navigation variable

    // start navigation Id
    private final static int HOME_ID = 1;
        private final static int VIDEO_ID = 2;
        private final static int MESSAGE_ID = 3;
        private final static int PODCAST_ID = 4;
        private final static int SHORT_ID = 5;
        private final static int s = 6;
    // start navigation Id

    //DR Navigation
    private DrawerLayout DR_Main;
    private NavigationView DrNavigation;
    private Toolbar toolBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ConstraintLayout header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //toolBar
        toolBar = findViewById(R.id.toolBar);
        DrNavigation = findViewById(R.id.DrNavigation);
        DR_Main = findViewById(R.id.DR_Main);
        //support action bar
        setSupportActionBar(toolBar);
        //toggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,DR_Main,toolBar,R.string.open,R.string.close);
        DR_Main.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DR_Main.isDrawerOpen(Gravity.RIGHT)) {
                    DR_Main.closeDrawer(Gravity.RIGHT);
                } else {
                    DR_Main.openDrawer(Gravity.RIGHT);
                }
            }
        });

        DrNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                DR_Main.closeDrawer(GravityCompat.END);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.login_register:
                        Intent intent = new Intent(MainActivity.this,LogIn.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Login/Register", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.video:
                        transaction.replace(R.id.Main_Container,new VideoFragment());
                        Toast.makeText(MainActivity.this, "Videos", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.message:
                        transaction.replace(R.id.Main_Container,new MessageFragment());
                        Toast.makeText(MainActivity.this, "Message", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.podcast:
                        transaction.replace(R.id.Main_Container,new PodcastFragment());
                        Toast.makeText(MainActivity.this, "Podcast", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.short_video:
                        transaction.replace(R.id.Main_Container,new Short_VideoFragment());
                        Toast.makeText(MainActivity.this, "Short Videos", Toast.LENGTH_SHORT).show();
                        break;
                }
                transaction.commit();
                return true;
            }
        });
        //Change Header
        View view = DrNavigation.getHeaderView(0);

        header = view.findViewById(R.id.header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DR_Main.closeDrawer(GravityCompat.END);
              Intent intent = new Intent(MainActivity.this,Profile.class);
              startActivity(intent);
                meowBottomNavigation.show(s,true);
            }
        });


        // start navigation All Code do note delete any code
        // meow find id
        meowBottomNavigation = findViewById(R.id.meowBottomNavigation);
        Main_Container = findViewById(R.id.Main_Container);
        //meow add
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_baseline_home_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_baseline_ondemand_video_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.message));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.podcast));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.short_video));
        //Active Home Button
        meowBottomNavigation.show(HOME_ID,true);


        //fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.Main_Container,new HomeFragment());
        transaction.commit();
        // meow click
        meowBottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES
                return null;
            }
        });

        meowBottomNavigation.setOnReselectListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                return null;
            }
        });

        meowBottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                switch (model.getId()){
                    case HOME_ID:
                        transaction1.replace(R.id.Main_Container,new HomeFragment());
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;

                    case VIDEO_ID:
                        transaction1.replace(R.id.Main_Container,new VideoFragment());
                        Toast.makeText(MainActivity.this, "Videos", Toast.LENGTH_SHORT).show();
                        break;

                    case MESSAGE_ID:
                        transaction1.replace(R.id.Main_Container,new MessageFragment());
                        Toast.makeText(MainActivity.this, "Chat", Toast.LENGTH_SHORT).show();
                        break;

                    case PODCAST_ID:
                        transaction1.replace(R.id.Main_Container,new PodcastFragment());
                        Toast.makeText(MainActivity.this, "Podcast", Toast.LENGTH_SHORT).show();
                        break;

                    case SHORT_ID:
                        transaction1.replace(R.id.Main_Container,new Short_VideoFragment());
                        Toast.makeText(MainActivity.this, "Short Videos", Toast.LENGTH_SHORT).show();
                        break;
                }
                transaction1.commit();
                return null;
            }
        });
        // End navigation All Code do not delete any code




    }

    @Override
    public void onBackPressed() {
        if (DR_Main.isDrawerOpen(GravityCompat.END)){
                DR_Main.closeDrawer(GravityCompat.END);
        }
        else {
            super.onBackPressed();
        }
    }
}