package com.dal.mc.servicegenie;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    private TextView signinTxtView;
    private EditText firstName, lastName, phone, emailId, password, confirmPassword;
    private Button signUpBtn;
    private static final Pattern UPPERCASE_REGEX = Pattern.compile("[A-Z]+");
    private static final Pattern LOWERCASE_REGEX = Pattern.compile("[a-z]+");
    private static final Pattern NUMBER_REGEX = Pattern.compile("[0-9]+");
    private static final Pattern SPECIAL_CHAR_REGEX = Pattern.compile("[!@#$%*&^-_=+]+");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadLayoutElements();
    }

    private void loadLayoutElements() {
        signinTxtView = findViewById(R.id.signup_signInTxt);
        firstName = findViewById(R.id.signup_firstName);
        lastName = findViewById(R.id.signup_lastName);
        phone = findViewById(R.id.signup_phone);
        emailId = findViewById(R.id.signup_emailId);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirmPassword);
        signUpBtn = findViewById(R.id.signup_signUpBtn);

        setupPhoneEditText();
        setupEmailEditText();
        setupPasswordEditText();
        setupConfirmPasswordEditText();

        setupSignupTextView();
        setupSignupBtn();
    }

    private void validateForm() {
        boolean valid = true;
        if (firstName.getText().toString().isEmpty()) {
            firstName.setError("Enter first name");
            valid = false;
        }
        if (lastName.getText().toString().isEmpty()) {
            lastName.setError("Enter last name");
            valid = false;
        }
        valid = validatePhone() && valid;
        valid = validateEmail() && valid;
        valid = validatePassword(password) && valid;
        valid = validateConfirmPassword() && valid;
        if (valid) {
            Toast.makeText(getApplicationContext(), "Valid", Toast.LENGTH_SHORT).show();
        }
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
        } else if (emailAddress.lastIndexOf(".", emailAddress.indexOf("@")) == -1) {
                emailId.setError("Enter valid email ID");
                valid = false;
        } else {
            String[] split = emailAddress.split("@");
            if (split.length > 1) {
                String domainName = split[1];
                if(!"dal.ca".equalsIgnoreCase(domainName)) {
                    emailId.setError("Please use dal.ca email");
                    valid = false;
                }
            }
        }

        return valid;
    }

    private boolean validatePhone() {
        boolean valid = true;
        String phoneNumber = phone.getText().toString();
        if(phoneNumber.isEmpty()) {
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
                validatePhone();
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
                validateForm();
            }
        });
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
                switch(event.getAction()) {
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
