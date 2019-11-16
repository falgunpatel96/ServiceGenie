package com.dal.mc.servicegenie;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    private TextView signinTxtView;
    private EditText firstName, lastName, phone, emailId, password, confirmPassword;
    private ImageView profilePic;
    private Button signUpBtn;
    private static final Pattern UPPERCASE_REGEX = Pattern.compile("[A-Z]+");
    private static final Pattern LOWERCASE_REGEX = Pattern.compile("[a-z]+");
    private static final Pattern NUMBER_REGEX = Pattern.compile("[0-9]+");
    private static final Pattern SPECIAL_CHAR_REGEX = Pattern.compile("[!@#$%*&^-_=+]+");
    static final String PHONE_NUMBER_KEY = "phoneNumber";

    private enum REQUEST_CODES {IMAGE_CAPTURE, CAMERA_ACCESS_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION}

    private static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadLayoutElements();
    }

    private void loadLayoutElements() {
        profilePic = findViewById(R.id.profilePic);
        signinTxtView = findViewById(R.id.signup_signInTxt);
        firstName = findViewById(R.id.signup_firstName);
        lastName = findViewById(R.id.signup_lastName);
        phone = findViewById(R.id.signup_phone);
        emailId = findViewById(R.id.signup_emailId);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirmPassword);
        signUpBtn = findViewById(R.id.signup_signUpBtn);

        setupPorfilePicElement();
        setupPhoneEditText();
        setupEmailEditText();
        setupPasswordEditText();
        setupConfirmPasswordEditText();

        setupSignupTextView();
        setupSignupBtn();
    }

    private boolean validateForm() {
        boolean valid = true;
        if (firstName.getText().toString().isEmpty()) {
            firstName.setError("Enter first name");
            valid = false;
        }
        if (lastName.getText().toString().isEmpty()) {
            lastName.setError("Enter last name");
            valid = false;
        }
        valid = validatePhone(phone) && valid;
        valid = validateEmail() && valid;
        valid = validatePassword(password) && valid;
        valid = validateConfirmPassword() && valid;
        return valid;
    }

    private boolean validateConfirmPassword() {
        boolean valid = validatePassword(confirmPassword);
        if (valid && !confirmPassword.getText().toString().equals(password.getText().toString())) {
            confirmPassword.setError("Passwords don't match");
            valid = false;
        }
        return valid;
    }

    private boolean validatePassword(EditText password) {
        boolean valid = true;
        String passwordTxt = password.getText().toString();
        if (passwordTxt.isEmpty()) {
            password.setError("Enter password");
            valid = false;
        } else {
            StringBuilder error = new StringBuilder("Password must contain:");
            if (!UPPERCASE_REGEX.matcher(passwordTxt).find()) {
                error.append("\n- 1 Uppercase letter");
                valid = false;
            }
            if (!LOWERCASE_REGEX.matcher(passwordTxt).find()) {
                error.append("\n- 1 Lowercase letter");
                valid = false;
            }
            if (!NUMBER_REGEX.matcher(passwordTxt).find()) {
                error.append("\n- 1 Number");
                valid = false;
            }
            if (!SPECIAL_CHAR_REGEX.matcher(passwordTxt).find()) {
                error.append("\n- 1 Special character");
                valid = false;
            }
            if (passwordTxt.length() < 8) {
                error.append("\n- 8 Characters");
                valid = false;
            }
            if (!valid) {
                password.setError(error.toString());
            }
        }
        return valid;
    }

    private boolean validateEmail() {
        boolean valid = true;
        String emailAddress = emailId.getText().toString();
        if (emailAddress.isEmpty()) {
            emailId.setError("Enter email ID");
            valid = false;
        } else if (!emailAddress.contains("@")) {
            emailId.setError("Enter valid email ID");
            valid = false;
        } else if (emailAddress.indexOf(".", emailAddress.indexOf("@")) == -1) {
            emailId.setError("Enter valid email ID");
            valid = false;
        } else {
            String[] split = emailAddress.split("@");
            if (split.length > 1) {
                String domainName = split[1];
                if (!"dal.ca".equalsIgnoreCase(domainName)) {
                    emailId.setError("Please use dal.ca email");
                    valid = false;
                }
            }
        }

        return valid;
    }

    static boolean validatePhone(EditText phone) {
        boolean valid = true;
        String phoneNumber = phone.getText().toString().replaceAll(" ", "");
        if (phoneNumber.isEmpty()) {
            phone.setError("Enter phone number");
            valid = false;
        }
        if (phoneNumber.length() != 10) {
            phone.setError("Enter 10 digit phone number");
            valid = false;
        }
        if (phoneNumber.contains("+") || phoneNumber.contains("-") || phoneNumber.contains("(") || phoneNumber.contains(")")) {
            phone.setError("Enter phone number without +/-/()");
            valid = false;
        }
        return valid;
    }

    private void setupConfirmPasswordEditText() {
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateConfirmPassword();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupPasswordEditText() {
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword(password);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupEmailEditText() {
        emailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupPhoneEditText() {
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePhone(phone);
                PhoneVerification.autoFormatPhoneNumberField(s,  phone);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupSignupBtn() {
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    final ProgressDialog dialog = new ProgressDialog(Signup.this);
                    dialog.setMessage("Signing up...");
                    dialog.setCancelable(false);
                    dialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(emailId.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final FirebaseUser user = firebaseAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                                        .Builder().setDisplayName(firstName.getText() + " " + lastName.getText()).build();
                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();
                                        if (task.isSuccessful()) {
                                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        new AlertDialog.Builder(Signup  .this).setMessage("User created. Please check email for verification.").setCancelable(false)
                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.dismiss();
                                                                        Intent emailVerifyIntent = new Intent(new Intent(Signup.this, EmailVerification.class));
                                                                        emailVerifyIntent.putExtra(Login.PASSWORD_KEY, password.getText().toString());
                                                                        emailVerifyIntent.putExtra(PHONE_NUMBER_KEY, phone.getText().toString());
                                                                        startActivity(emailVerifyIntent);
                                                                        finish();
                                                                    }
                                                                }).create().show();

                                                    } else {
                                                        new AlertDialog.Builder(Signup.this).setMessage("User created. Please sign in to continue registration").setCancelable(false)
                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        dialog.dismiss();
                                                                    }
                                                                }).create().show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error signing up", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Error signing up", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });
    }

    private void setupPorfilePicElement() {
        profilePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        REQUEST_CODES.WRITE_EXTERNAL_STORAGE_PERMISSION.ordinal()) &&
                        requestPermission(Manifest.permission.CAMERA,
                                REQUEST_CODES.CAMERA_ACCESS_PERMISSION.ordinal())) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(cameraIntent, REQUEST_CODES.IMAGE_CAPTURE.ordinal());
                    }
                }
            }
        });
    }

    private boolean requestPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Signup.this,
                    permission)) {
                Toast.makeText(getApplicationContext(),
                        "Allow access to choose profile picture", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(Signup.this, new String[]{permission},
                        requestCode);
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODES.IMAGE_CAPTURE.ordinal() && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profilePic.setImageBitmap(imageBitmap);

        }
    }

    private void setupSignupTextView() {

        signinTxtView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });

        signinTxtView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        signinTxtView.setBackgroundColor(getResources().getColor(R.color.colorGreyTxtBackground));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        signinTxtView.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });
    }

}
