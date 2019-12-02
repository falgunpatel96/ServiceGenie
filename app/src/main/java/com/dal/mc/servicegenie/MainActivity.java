package com.dal.mc.servicegenie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button signout;
    private Button profile;
    private GridView gridView;
    private Button addserviceBtn;

    int[] images = {
            R.drawable.cleaning, R.drawable.plumbing, R.drawable.electrician,
            R.drawable.painting, R.drawable.salon, R.drawable.movers,
            R.drawable.pest_control, R.drawable.carpenter, R.drawable.party
    };

    String[] values = {"Home Cleaning", "Plumber", "Electrician", "Painting", "Beauty & Salon", "Movers and Packers",
            "Pest Control", "Carpenter", "Party Planning"
    };

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        final FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            TextView view = findViewById(R.id.testTxt);
            view.setText(("Hello " + user.getDisplayName()));
        } else {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
        profile = findViewById(R.id.profileBtn);
        signout = findViewById(R.id.testBtn);
        addserviceBtn = findViewById(R.id.addMoreServices);
        gridView = (GridView) findViewById(R.id.grid);

        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
                startActivity(profileIntent);
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

        // display grid view of services
        gridView.setAdapter(new GridAdaptor(this, images, values));

        // Go to booking section
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                /*switch(position) {
                    default:
                        break;
                }*/
                intent = new Intent(getApplicationContext(), AddNewService.class); // [change name to booking page]
                intent.putExtra("SERVICE_NAME", values[position]);
                startActivity(intent);
            }
        });

        // Go to addMoreServices page on 'addMoreServices' button click
        addserviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddNewService.class));
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