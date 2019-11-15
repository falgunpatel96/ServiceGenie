package com.dal.mc.servicegenie;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerification extends AppCompatActivity {

    private static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ProgressBar progressBar;
    private TextView sendEmailTxt;
    private Handler handler = new Handler();
    private static final long SLEEP_INTERVAL_BETWEEN_EMAIL_VERIFY = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sendEmailTxt = findViewById(R.id.emailverify_sendEmailTxt);
        progressBar = findViewById(R.id.emailverify_progressBar);

        setupSendEmailTxt();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                final Runnable runnable = this;
                if (user != null) {
                    if (user.isEmailVerified()) {
                        handler.removeCallbacks(this);
                        startActivity(new Intent(EmailVerification.this, MainActivity.class));
                        progressBar.setVisibility(View.GONE);
                        finish();
                    } else {
                        final String password = getIntent().getStringExtra(Login.PASSWORD_KEY);
                        final String email = user.getEmail();
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(email, password);
                        System.out.println(password);
                        user.reauthenticateAndRetrieveData(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = task.getResult().getUser();
                                    if (user != null) {
                                        if (user.isEmailVerified()) {
                                            handler.removeCallbacks(runnable);
                                            startActivity(new Intent(EmailVerification.this, MainActivity.class));
                                            progressBar.setVisibility(View.GONE);
                                            finish();
                                        } else {
                                            handler.postDelayed(runnable, SLEEP_INTERVAL_BETWEEN_EMAIL_VERIFY);
                                        }
                                    }
                                } else {
                                    System.out.println("Signin failed");
                                    startActivity(new Intent(EmailVerification.this, Login.class));
                                    finish();
                                }
                            }
                        });
                    }
                } else {
                    System.out.println("User null first");
                    startActivity(new Intent(EmailVerification.this, Login.class));
                    finish();
                }

            }
        }, SLEEP_INTERVAL_BETWEEN_EMAIL_VERIFY);

    }

    private void setupSendEmailTxt() {
        sendEmailTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(EmailVerification.this, Login.class));
                    finish();
                } else {
                    final ProgressDialog dialog = new ProgressDialog(EmailVerification.this);
                    dialog.setMessage("Sending email for verification...");
                    dialog.setCancelable(false);
                    dialog.show();
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Exception e = task.getException();
                                if (e != null) {
                                    e.printStackTrace();
                                }
                                new AlertDialog.Builder(EmailVerification.this).setMessage("Failed to send email, please try again after some time").setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        }).create().show();
                            }
                        }
                    });
                }
            }
        });

        sendEmailTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sendEmailTxt.setBackgroundColor(getResources().getColor(R.color.colorGreyTxtBackground));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        sendEmailTxt.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });
    }

}
