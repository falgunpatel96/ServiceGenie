package com.dal.mc.servicegenie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MyBookings extends AppCompatActivity implements RViewAdapter.onClickListener {

    RecyclerView recyclerView;
    RViewAdapter rviewAdapter;
    ArrayList<Booking> bookings;
    private Runnable runnable;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_bookings);

        bookings = new ArrayList<Booking>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        /*runnable = new Runnable() {
            @Override
            public void run() {
                getBookings();
            }
        };*/

        getBookings();

        /*//retrieve data on separate thread
        Thread thread = new Thread(null, runnable, "background");
        thread.start();*/

//        rviewAdapter = new RViewAdapter(bookings);
        //thread.start();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_bookings);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;

                        case R.id.nav_bookings:
                            selectedFragment = new BookingsFragment();
                            break;

                        case R.id.nav_help:
                            selectedFragment = new HelpFragment();
                            break;

                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;

                    }
//                    bottomNavigationView.setSelectedItemId(menuItem.getItemId());
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();

                    return true;
                }
            };

        /*rviewAdapter = new RViewAdapter(bookings);


//        recyclerView.setAdapter(rviewAdapter);

        //notifying RecyclerViewAdapter for change in data
//        rviewAdapter.notifyDataSetChanged();


    }

    public void getAllBookingsByUser() {
        //final TaskCompletionSource<String> task = new TaskCompletionSource<>();
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();

    /***************************************************************************************
     *    Title: JsonObjectRequest setup in getJokes() function
     *    Author: Bola Okesanjo, Archanaapriya Nallasivan & Deepan Shankar
     *    Date: 2019
     *    Code version: 1.0
     *    Availability: CSCI5708-CurrencyApp_Fall2019 [Slide 31], https://dal.brightspace.com/d2l/le/content/100143/viewContent/1488863/View?ou=100143
     ***************************************************************************************/

    /****************************************************************************************
     *    Title: chuck_norris_icon.png image
     *    Author: Bola Okesanjo, Archanaapriya Nallasivan & Deepan Shankar
     *    Date: 2019
     *    Code version: 1.0
     *    Availability: chuck_norris_icon.png from code.zip/code , https://dal.brightspace.com/d2l/le/content/100143/viewContent/1506439/View
     ***************************************************************************************/

    public void getBookings() {

        //get current users email ID
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser theUser = mAuth.getCurrentUser();

        final DatabaseReference services = FirebaseDatabase.getInstance().getReference("ServiceRequest");
        services.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Booking booking = data.getValue(Booking.class);
                    if(theUser.getEmail().toString().equalsIgnoreCase(booking.getRequestedByEmailId())){
                        // set front layout

                        //add to recycler view
                        bookings.add(booking);
                        Log.e("bookings","bookings Size:"+bookings.size());

                        if(rviewAdapter == null )
                        {
                            rviewAdapter = new RViewAdapter(MyBookings.this,bookings,MyBookings.this);
                            recyclerView.setAdapter(rviewAdapter);
                        }
                        if(bookings!=null && !bookings.isEmpty())
                        {
                            rviewAdapter.doRefresh(bookings);
                        }
                    }
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.e("Booking","Bookings out of listener:"+bookings.size());

        //notifying RecyclerViewAdapter for change in data
       /* if(rviewAdapter == null )
        {
            rviewAdapter = new RViewAdapter(bookings);
        }
        if(bookings!=null && !bookings.isEmpty())
        {
            rviewAdapter.doRefresh(bookings);
        }
        else
        {
            Toast.makeText(this, "bookins is blank", Toast.LENGTH_SHORT).show();
        }*/
//        rviewAdapter.notifyDataSetChanged();


//        //build the request
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//
//                    if (response.get("type").toString().equals("success")) {
//
//                        //we got status as success in response
//                        JSONArray jsonArray = response.getJSONArray("value");
//
//                        jokes.clear();
//
//                        JSONObject jo = new JSONObject();
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            jo = jsonArray.getJSONObject(i);
//
//
//                            //setting the jokeText and jokeImage from response on POJO class
//                            // and then adding class on ArrayList that was there on RecyclerViewAdapter
//                            Joke joke = new Joke();
//                            joke.setImage(R.drawable.chuck_norris_icon);
//                            joke.setJoke(jo.get("joke").toString());
//                            jokes.add(joke);
//
//
//                        }
//                        //notifying RecyclerViewAdapter for change in data
//                        rviewAdapter.notifyDataSetChanged();
//
//
//                    } else if (!response.get("type").toString().equals("success")) {
//                        //if response of api is failure
//                        Toast.makeText(getApplicationContext(), "Error while fetching data from API!", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//
//                Toast.makeText(getApplicationContext(), "Error retriving data", Toast.LENGTH_SHORT).show();
//            }
//        }
//        );
        //Calendar tmp = Calendar.getInstance();


//        Booking booking = new Booking();
//        booking.setServiceName("Car Cleaner");
//        booking.setCost(new Float(200));
//        booking.setProfInfo("Kaycereous");
//        booking.setStatus("Completed");
//        booking.setTimeNDate(tmp);

//        for (int i=0;i<bookings.size();i++)
//            bookings.add(booking);
//
//        //notifying RecyclerViewAdapter for change in data
//        rviewAdapter.notifyDataSetChanged();

        //adding request in queue
        //RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    public void onItemClickListener(int position, Booking booking)
    {
        Intent intent = new Intent(this, activity_help.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        Intent intent = new Intent(MyBookings.this, MainActivity.class);
        startActivity(intent);

    }
}
