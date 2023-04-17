package com.fypvpreventor.VpreventorFYP;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class schedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        setContentView(R.layout.activity_schedule);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Button now  = findViewById(R.id.button2);
        Button ten  = findViewById(R.id.button7);
        Button thirty  = findViewById(R.id.button6);
        Button ff  = findViewById(R.id.button8);
        if (actionBar != null) {
            actionBar.hide();
        }

        now.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FakeCall.sche = 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Intent i = new Intent(schedule.this,call.class);
                        startActivity(i);
                        finish();
                    }
                },500);
            }
        });
        thirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FakeCall.sche = 1;
                Toast.makeText(schedule.this, "CALL WILL RECEIVE AFTER 30 SECONDS",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Intent i = new Intent(schedule.this,call.class);
                        startActivity(i);
                        finish();
                    }
                },30000);
            }
        });
        ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FakeCall.sche = 1;
                Toast.makeText(schedule.this, "CALL WILL RECEIVE AFTER 45 SECONDS",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Intent i = new Intent(schedule.this,call.class);
                        startActivity(i);
                        finish();
                    }
                },45000);
            }
        });
        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FakeCall.sche = 1;
                Toast.makeText(schedule.this, "CALL WILL RECEIVE AFTER 10 SECONDS",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Intent i = new Intent(schedule.this,call.class);
                        startActivity(i);
                        finish();
                    }
                },10000);
            }
        });

    }


}