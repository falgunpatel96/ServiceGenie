package com.dal.mc.servicegenie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.TimeUnit;

public class AuthenticationUtil {

    private static final long SLEEP_TIME_BETWEEN_EMAIL_VALIDATION = 5000;
    private static final int PHONE_VERIFICATION_TIMEOUT = 60;

    static boolean createUser(final String email, final String password, final String name, final String phoneNumber) {
        final int[] success = {0};
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        Task<AuthResult> task = auth.createUserWithEmailAndPassword(email, password);

        while (!task.isComplete()) {
            System.out.println("Create user " + task.isComplete());
        }
        if (task.isSuccessful()) {
            FirebaseUser user = auth.getCurrentUser();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                    .Builder().setDisplayName(name).build();
            Task<Void> updateTask = user.updateProfile(profileUpdates);
            while (updateTask.isComplete()) {
                System.out.println(updateTask.isComplete());
            }
            if (updateTask.isSuccessful()) {
                return true;
            } else {
                return false;
            }

        } else {
            Exception e = task.getException();
            if (e != null) {
                e.printStackTrace();
            }
            return false;
        }

    }

    private static boolean verifyPhone(Context context, String phoneNumber, final FirebaseUser user, ProgressDialog dialog) {
        final int[] verify = {0};
        dialog.setMessage("Sending verification code to verify phone number");
        dialog.show();
        final String[] codeSent = new String[1];
        final PhoneAuthProvider auth = PhoneAuthProvider.getInstance();
        auth.verifyPhoneNumber(phoneNumber, PHONE_VERIFICATION_TIMEOUT, TimeUnit.SECONDS, (Activity) context, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeSent[0] = s;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }


        });
        dialog.dismiss();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Verify Phone");

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String otp = input.getText().toString();
                if (codeSent[0].equals(otp)) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent[0], otp);
                    user.updatePhoneNumber(credential);
                } else {
                    verify[0] = 2;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                verify[0] = 2;
            }
        });

        builder.show();
        while (verify[0] == 0) ;
        return verify[1] == 1;
    }

    private static void showError(String message, Context context) {
        new AlertDialog.Builder(context).setMessage(message).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create().show();
    }

}
