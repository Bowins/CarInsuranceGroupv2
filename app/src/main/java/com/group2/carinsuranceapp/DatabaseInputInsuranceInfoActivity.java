package com.group2.carinsuranceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DatabaseInputInsuranceInfoActivity extends AppCompatActivity {

    private Button submitButton;
    private EditText carModelField;
    private EditText carMakeField;
    private EditText carRegNumField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_database_insurance_info);

        //widgets
        carMakeField = findViewById(R.id.field_input_car_make);
        carModelField = findViewById(R.id.field_input_car_model);
        carRegNumField= findViewById(R.id.field_input_car_reg_num);




        //TODO change onClick to update database
        submitButton =(Button) findViewById(R.id.button_signup_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatabaseInputInsuranceInfoActivity.this,LoggedInMainActivity.class));
            }
        });

    }

}
