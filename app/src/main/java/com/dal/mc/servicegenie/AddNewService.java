package com.dal.mc.servicegenie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddNewService extends AppCompatActivity {
    private EditText userNameTV, emailIdTV, serviceNameTV, addServiceComment;
    private Button addServiceBtn;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_service);

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        final String userName = user.getDisplayName();
        final String emailId = user.getEmail();
        //Intent intent = getIntent();
        //String serviceName = intent.getStringExtra("SERVICE_NAME");
        //textView.setText(String.format("Hie hello there!!%s", serviceName));
        userNameTV = findViewById(R.id.add_service_userNameVal);
        emailIdTV = findViewById(R.id.add_service_userEmailID);
        serviceNameTV = findViewById(R.id.add_service_ServiceNm);
        addServiceComment = findViewById(R.id.add_service_commentVal);
        addServiceBtn = findViewById(R.id.addServiceBtn);

        userNameTV.setText(userName);
        emailIdTV.setText(emailId);



        addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceName = serviceNameTV.getText().toString();
                String comment = addServiceComment.getText().toString();
                System.out.println(serviceName +": comment: "+comment +"user: "+userName +": "+emailId);
                if(serviceName.equalsIgnoreCase(" ")){
                    serviceNameTV.setError("Please enter a Service Name you want to request");
                } else {
                    // save data to database
                    databaseReference = FirebaseDatabase.getInstance().getReference();

                    serviceRequest request = new serviceRequest(serviceName, userName, emailId, comment);
                    databaseReference.child("ServiceRequest").setValue(request);
                    startActivity(new Intent(AddNewService.this, activity_popup.class));
                }
            }
        });
    }
}