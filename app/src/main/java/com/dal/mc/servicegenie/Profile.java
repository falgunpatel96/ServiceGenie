package com.dal.mc.servicegenie;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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
    private MaterialButton signOut, deleteAccount, changePassword;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = FIREBASE_AUTH.getCurrentUser();
        if (user == null) {
            finish();
        }

        loadUserElements();
        setUserElements();

    }

    private void loadUserElements() {
        nameView = findViewById(R.id.profileName);
        phoneView = findViewById(R.id.profilePhone);
        addressView = findViewById(R.id.profileAddress);
        emailView = findViewById(R.id.profileEmail);
        profilePic = findViewById(R.id.profile_profilePic);
        signOut = findViewById(R.id.profile_signOut);
        deleteAccount = findViewById(R.id.profile_deleteAccount);
        changePassword = findViewById(R.id.profile_changePassword);

        setupSignoutBtn();
        setupDeleteAccountBtn();
        setupChangePasswordBtn();
    }

    private void setupChangePasswordBtn() {
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, ChangePassword.class));
            }
        });
    }

    private void deleteUserDetailsInDatabase(String uid) {
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
        users.child(uid).removeValue();
    }

    private void deleteUser() {
        final ProgressDialog dialog = new ProgressDialog(Profile.this);
        dialog.setMessage("Deleting account...");
        dialog.setCancelable(false);
        dialog.show();
        final String uid = user.getUid();
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                if (task.isSuccessful()) {
                    deleteUserDetailsInDatabase(uid);
                    startActivity(new Intent(Profile.this, Login.class));
                    finish();
                } else {
                    Exception e = task.getException();
                    if (e != null) {
                        e.printStackTrace();
                    }
                    new AlertDialog.Builder(Profile.this).setMessage("Failed to delete user data. Please try again later.").setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            }
        });
    }

    private void setupDeleteAccountBtn() {
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Profile.this).setMessage("Are you sure you want to delete your account ? ").setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.dismiss();
                        deleteUser();
                    }
                }).create().show();

            }
        });
    }

    private void setupSignoutBtn() {
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Profile.this).setMessage("Are you sure you want to sign out" +
                        "? ").setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.dismiss();
                        FIREBASE_AUTH.signOut();
                        startActivity(new Intent(Profile.this, Login.class));
                        finish();
                    }
                }).create().show();
            }
        });
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
                    } catch (Exception e) {
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
