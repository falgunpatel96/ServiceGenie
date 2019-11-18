package com.dal.mc.servicegenie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private static final FirebaseAuth FIREBASE_AUTH = FirebaseAuth.getInstance();
    private TextView nameView, phoneView, addressView, emailView, passwordView;
    private CircleImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        loadUserElements();
        setUserElements();

    }

    private void loadUserElements() {
        nameView = findViewById(R.id.profileName);
        phoneView = findViewById(R.id.profilePhone);
        addressView = findViewById(R.id.profileAddress);
        emailView = findViewById(R.id.profileEmail);
        profilePic = findViewById(R.id.profile_profilePic);
    }

    private static Bitmap decodeImage(String base64EncodedImage) {
        byte[] decodedBytes = Base64.decode(base64EncodedImage, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private static String getPhoneNumber(String phone) {
        phone = phone.replaceAll(" ", "");
        return phone.substring(0, 2) + " " + phone.substring(2, 5) + " " + phone.substring(5, 8) + " " + phone.substring(8);
    }

    private void setUserElements() {
        final ProgressDialog dialog = new ProgressDialog(Profile.this);
        dialog.setMessage("Loading profile...");
        dialog.setCancelable(false);
        dialog.show();
        final FirebaseUser user = FIREBASE_AUTH.getCurrentUser();
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
        nameView.setText(user.getDisplayName());
        phoneView.setText(getPhoneNumber(user.getPhoneNumber()));
        emailView.setText(user.getEmail());

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userDetails = dataSnapshot.child(user.getUid()).getValue(User.class);
                StringBuilder address = new StringBuilder();
                if (userDetails.getAddress2() != null && !userDetails.getAddress2().isEmpty()) {
                    address.append(userDetails.getAddress2() + ", ");
                }
                address.append(userDetails.getStreetAddress()).append(", ").append(userDetails.getCity()).append(", ").append(userDetails.getProvince()).append(", ").append(userDetails.getPostalCode());
                addressView.setText(address.toString());
                if (userDetails.getProfilePicEncoded() != null && !userDetails.getProfilePicEncoded().isEmpty()) {
                    try {
                        Bitmap profilePicImage = decodeImage(userDetails.getProfilePicEncoded());
                        profilePic.setImageBitmap(profilePicImage);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent main = new Intent(Profile.this, MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(main);
//        super.onBackPressed();
    }

}
