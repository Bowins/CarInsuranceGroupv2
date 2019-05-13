package com.group2.carinsuranceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group2.databaseclasses.UserData;

public class DatabaseInputPersonalInfoActivity extends AppCompatActivity {

    private EditText firstNameField;
    private EditText surnameField;
    private EditText dateOfBirthField;
    private RadioGroup sexRadioButton;
    private RadioButton selectedSex;

    // puts in database
    private Button nextButton;

    // database variables
    private static final String TAG = "AddToDatabase";
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_database_personal_info);


        // initialise database variables
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        //widgets
        firstNameField = findViewById(R.id.field_enter_forename);
        surnameField = findViewById(R.id.field_enter_surname);
        dateOfBirthField = findViewById(R.id.field_enter_date_of_birth);
        sexRadioButton = findViewById(R.id.radio_button_group_sex);
        selectedSex = (RadioButton)findViewById(R.id.radio_button_female);


        nextButton =(Button) findViewById(R.id.b_signup_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dateOfBirth = dateOfBirthField.getText().toString();
                Log.e(TAG, "Date of birth in string format: " + dateOfBirth);

                if ((inputHandle(firstNameField) == false) && (inputHandle(surnameField) == false) && !(dateOfBirth.equals(""))) {

                    FirebaseUser userAuth = mAuth.getCurrentUser();

                    //Creating User
                    String first = firstNameField.getText().toString();
                    String surname = surnameField.getText().toString();
                    String email = userAuth.getEmail();
                    String ID = userAuth.getUid();
                    String gender = "Male";

                    if (selectedSex.isChecked()) {
                        gender = "Female";
                    }

                    //Trying to input data in database
                    try {

                        //Define object, reference
                        UserData user = new UserData(email, first, surname, dateOfBirth, gender);
                        DatabaseReference myRef = mFirebaseDatabase.getReference("User");
                        //Setting value
                        myRef.child(ID).setValue(user);
                        startActivity(new Intent(DatabaseInputPersonalInfoActivity.this, DatabaseInputInsuranceInfoActivity.class));

                    } catch (Exception e2) {
                        Toast.makeText(getApplicationContext(), "Adding data was unsuccessful", Toast.LENGTH_LONG);

                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Saving your data was unsuccessful", Toast.LENGTH_LONG);

                }

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
