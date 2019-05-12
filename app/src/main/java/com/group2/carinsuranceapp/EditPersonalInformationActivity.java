package com.group2.carinsuranceapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class EditPersonalInformationActivity extends AppCompatActivity {

    private EditText firstNameField;
    private EditText surnameField;
    private EditText dateOfBirthField;
    private RadioGroup sexRadioButton;
    private RadioButton selectedSex;
    private Button submitButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);


        //widgets
        firstNameField = findViewById(R.id.edit_first_name_field);
        surnameField = findViewById(R.id.edit_last_name_field);
        dateOfBirthField = findViewById(R.id.edit_birth_date_field);
        sexRadioButton = findViewById(R.id.edit_sex_group);
        selectedSex = (RadioButton) findViewById(R.id.radio_edit_female);
        submitButton = findViewById(R.id.button_edit_personal_submit);



    }


}
