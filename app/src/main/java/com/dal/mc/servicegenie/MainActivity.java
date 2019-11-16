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

public class MainActivity extends AppCompatActivity {

    private Button signout;
    private Button profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        final FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            TextView view = findViewById(R.id.testTxt);
            view.setText(("Hello " + user.getDisplayName()));
        }
        profile=findViewById(R.id.profileBtn);
        signout = findViewById(R.id.testBtn);

        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
                profileIntent.putExtra("name",user.getDisplayName());
                profileIntent.putExtra("email",user.getEmail());
                profileIntent.putExtra("phone",user.getPhoneNumber());
                startActivity(profileIntent);
                finish();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit? You will be signed out!")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        signout.callOnClick();
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();

    }
}
