package com.group2.carinsuranceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DatabaseInputPersonalInfoActivity extends AppCompatActivity {

    private EditText firstNameField;
    private EditText surnameField;
    private EditText dateOfBirthField;
    private RadioGroup sexRadioButton;


    private Button nextButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_database_personal_info);

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
