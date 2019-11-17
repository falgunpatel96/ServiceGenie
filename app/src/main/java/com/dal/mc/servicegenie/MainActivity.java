package com.dal.mc.servicegenie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button signout;
    private Button profile;
    GridView gridView;
    Button addserviceBtn;

    int[] images = {
            R.drawable.cleaning, R.drawable.plumbing, R.drawable.electrician,
            R.drawable.painting, R.drawable.salon, R.drawable.movers,
            R.drawable.pest_control, R.drawable.carpenter, R.drawable.party
    };

    String[] values = {"Home Cleaning", "Plumber", "Electrician", "Painting", "Beauty & Salon", "Movers and Packers",
            "Pest Control", "Carpenter", "Party Planning"
    };

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
        profile = findViewById(R.id.profileBtn);
        signout = findViewById(R.id.testBtn);
        addserviceBtn = findViewById(R.id.addMoreServices);
        gridView = (GridView) findViewById(R.id.grid);

        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
                profileIntent.putExtra("name", user.getDisplayName());
                profileIntent.putExtra("email", user.getEmail());
                profileIntent.putExtra("phone", user.getPhoneNumber());
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
                intent =  new Intent(getApplicationContext(), AddNewService.class); // [change name to booking page]
                intent.putExtra("SERVICE_NAME", values[position]);
                startActivity(intent);
            }
        });

        // Go to addMoreServices page on "addMoreServices' button click
        addserviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddNewService.class));
            }
        });
    }
    // for search

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    //TODO: Reset your views
                    return false;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false; //do the default
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    //NOTE: doing anything here is optional, onNewIntent is the important bit
                    if (s.length() > 1) { //2 chars or more
                        //TODO: filter/return results
                    } else if (s.length() == 0) {
                        //TODO: reset the displayed data
                    }
                    return false;
                }

            });
        }
        return true;
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
