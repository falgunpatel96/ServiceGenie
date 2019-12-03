package com.dal.mc.servicegenie;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyBookings extends AppCompatActivity {

    RecyclerView recyclerView;
    RViewAdapter rviewAdapter;
    ArrayList<Booking> bookings;
    private Runnable runnable;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_bookings);

        bookings = new ArrayList<Booking>();

        runnable = new Runnable() {
            @Override
            public void run() {
                getJokes();
            }
        };

        //retrieve data on separate thread
        Thread thread = new Thread(null, runnable, "background");
        thread.start();

        rviewAdapter = new RViewAdapter(bookings);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rviewAdapter);

        //notifying RecyclerViewAdapter for change in data
        rviewAdapter.notifyDataSetChanged();

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

    public void getJokes() {
        //final String url = "http://api.icndb.com/jokes/random/10";


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
        Calendar tmp = Calendar.getInstance();


        Booking booking = new Booking();
        booking.setServiceName("Car Cleaner");
        booking.setCost(new Float(200));
        booking.setProfInfo("Kaycereous");
        booking.setStatus("Completed");
        booking.setTimeNDate(tmp);

        for (int i=0;i<10;i++)
            bookings.add(booking);

        //notifying RecyclerViewAdapter for change in data
        rviewAdapter.notifyDataSetChanged();
        //adding request in queue
        //RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }
}
