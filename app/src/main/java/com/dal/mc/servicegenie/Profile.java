package com.dal.mc.servicegenie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    private TextView nameView,phoneView,addressView,emailView,passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String name = getIntent().getExtras().getString("name");
        String email = getIntent().getExtras().getString("email");
        String phone = getIntent().getExtras().getString("phone");

        nameView=findViewById(R.id.profileName);
        phoneView=findViewById(R.id.profilePhone);
        addressView=findViewById(R.id.profileAddress);
        emailView=findViewById(R.id.profileEmail);
        nameView.setText(name);
        phoneView.setText(phone);
        emailView.setText(email);

    }
    @Override
    public void onBackPressed() {
        Intent main = new Intent(Profile.this , MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(main);
//        super.onBackPressed();
    }

}
