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
import com.group2.databaseclasses.UserData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

                String dateOfBirth = dateOfBirthField.getText().toString();

                try {
                    Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(dateOfBirth);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Wrong Date Input", Toast.LENGTH_LONG);
                }

                if ((inputHandle(firstNameField) == false) && (inputHandle(surnameField) == false) && (dateOfBirthField.getText().toString().equals(""))) {

                    //Creating User
                    String first = firstNameField.getText().toString();
                    String surname = surnameField.getText().toString();
                   // UserData user = new UserData(first, surname, )
                }



                startActivity(new Intent(DatabaseInputPersonalInfoActivity.this, DatabaseInputInsuranceInfoActivity.class));
                DatabaseReference myRef = mFirebaseDatabase.getReference("message");
                myRef.setValue("Hello, World!");
                

            }
        });
    }



    /*
    Empty checking for fields
     */

    private boolean inputHandle(EditText editText) {

        boolean empty = false;

        if(editText.getText().toString().equals("")){
            empty = true;
        }
        else {
            empty = false;
        }

        return empty;

    }



}
