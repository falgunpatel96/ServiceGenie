package com.dal.mc.servicegenie;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class Signup extends AppCompatActivity {

    private TextView signinTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        signinTxtView = findViewById(R.id.signup_signInTxt);

        signinTxtView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, Login.class));
            }

        });

        signinTxtView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        signinTxtView.setBackgroundColor(getResources().getColor(R.color.colorGreyTxtBackground));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        signinTxtView.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });

    }

}
