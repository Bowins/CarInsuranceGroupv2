package com.group2.carinsuranceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseInputPersonalInfoActivity extends AppCompatActivity {

    private EditText firstNameField;
    private EditText surnameField;
    private EditText dateOfBirthField;
    private RadioGroup sexRadioButton;

    // puts in database
    private Button nextButton;

    // database variables
    private static final String TAG = "AddToDatabase";
    FirebaseDatabase mFirebaseDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_database_personal_info);


        // initialise database variables
        //mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        /*
        Alright so this right here is the line that is the line gives me errors
        Every time I try to reference the database it gives me a weird error and it crashes
        This is literally the end of the sample example the Firebase gives me which is appending a
        message: message > Hello, World!
         */

        DatabaseReference myRef = mFirebaseDatabase.getReference("message");
        myRef.setValue("Hello, World!");

        //widgets
        firstNameField = findViewById(R.id.field_enter_forename);
        surnameField = findViewById(R.id.field_enter_surname);
        dateOfBirthField = findViewById(R.id.field_enter_date_of_birth);
        sexRadioButton = findViewById(R.id.radio_button_group_sex);

        //TODO change onClick to update database
        nextButton =(Button) findViewById(R.id.b_signup_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatabaseInputPersonalInfoActivity.this, DatabaseInputInsuranceInfoActivity.class));
            }
        });
    }




}
