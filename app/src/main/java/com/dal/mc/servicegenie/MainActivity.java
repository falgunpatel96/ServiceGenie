package com.dal.mc.servicegenie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            TextView view = findViewById(R.id.testTxt);
            view.setText(("Hello " + user.getDisplayName()));
        }

        signout = findViewById(R.id.testBtn);

        signout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
        });
    }
}
