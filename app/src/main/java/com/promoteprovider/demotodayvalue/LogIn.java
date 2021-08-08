package com.promoteprovider.demotodayvalue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        TextView login=findViewById(R.id.login);
        TextView NewAccount=findViewById(R.id.NewAccount);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(LogIn.this,MainActivity.class);
                startActivity(intent);
            }
        });



        NewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(LogIn.this,Welcome.class);
                startActivity(intent);
            }
        });




    }
}