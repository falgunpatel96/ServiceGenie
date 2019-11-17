package com.dal.mc.servicegenie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class AddNewService extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_service);

        Intent intent = getIntent();
        String serviceName = intent.getStringExtra("SERVICE_NAME");
        textView = findViewById(R.id.textView2);
        textView.setText(String.format("Hie hello there!!%s", serviceName));

    }
}

