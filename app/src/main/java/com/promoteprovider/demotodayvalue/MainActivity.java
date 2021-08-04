package com.promoteprovider.demotodayvalue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
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
    // start navigation Id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (model.getId()){
                    case HOME_ID:
                        transaction.replace(R.id.Main_Container,new HomeFragment());
                        break;

                    case VIDEO_ID:
                        transaction.replace(R.id.Main_Container,new VideoFragment());
                        break;

                    case MESSAGE_ID:
                        transaction.replace(R.id.Main_Container,new MessageFragment());
                        break;

                    case PODCAST_ID:
                        transaction.replace(R.id.Main_Container,new PodcastFragment());
                        break;

                    case SHORT_ID:
                        transaction.replace(R.id.Main_Container,new Short_VideoFragment());
                        break;
                }
                transaction.commit();
                return null;
            }
        });
        // End navigation All Code do not delete any code




    }
}