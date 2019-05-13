package com.group2.carinsuranceapp;

import android.content.Intent;
import android.os.Bundle;
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

public class EditPersonalInformationActivity extends AppCompatActivity {

    private EditText firstNameField;
    private EditText surnameField;
    private EditText dateOfBirthField;
    private RadioGroup sexRadioButton;
    private RadioButton selectedSex;
    private Button submitButton;

    // database variables
    private static final String TAG = "AddToDatabase";
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);


        // initialise database variables
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        //widgets
        firstNameField = findViewById(R.id.edit_first_name_field);
        surnameField = findViewById(R.id.edit_last_name_field);
        dateOfBirthField = findViewById(R.id.edit_birth_date_field);
        sexRadioButton = findViewById(R.id.edit_sex_group);
        selectedSex = (RadioButton) findViewById(R.id.radio_edit_female);
        submitButton = findViewById(R.id.button_edit_personal_submit);


        submitButton.setOnClickListener(new View.OnClickListener() {
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
                        startActivity(new Intent(getApplicationContext(), LoggedInMainActivity.class));

                    } catch (Exception e2) {
                        Toast.makeText(getApplicationContext(), "Adding data was unsuccessful", Toast.LENGTH_LONG);

                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Saving your data was unsuccessful", Toast.LENGTH_LONG);

                }
            }
        });
    }

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
