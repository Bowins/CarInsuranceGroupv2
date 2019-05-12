package com.group2.carinsuranceapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group2.databaseclasses.UserCar;

public class Fragment_AddCar extends Fragment {

    // View fields
    private EditText registration;
    private EditText model;
    private EditText make;
    private Button submit;

    // database variables
    private static final String TAG = "AddToDatabase";
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_car,null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        registration = view.findViewById(R.id.car_registration_label);
        model = view.findViewById(R.id.car_model_label);
        make = view.findViewById(R.id.car_make_label);
        submit = view.findViewById(R.id.b_submit);


        // initialise database variables
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((inputHandle(registration) == false) && (inputHandle(model) == false) && (inputHandle(make) == false)) {

                    String reg = registration.getText().toString();
                    String mod = model.getText().toString();
                    String mak = make.getText().toString();

                    UserCar car = new UserCar(reg, mod, mak);
                    Log.e(TAG, "All input is checked and not empty: Reg - " + reg + ", Mod - " + mod + ", Mak - " + mak);

                    try {

                        //Try to get current user ID and the reference
                        FirebaseUser userAuth = mAuth.getCurrentUser();
                        DatabaseReference myRef = mFirebaseDatabase.getReference("Car");

                        //Setting value
                        //Path is Car -> Current user ID -> Car ID
                        myRef.child(userAuth.getUid()).child(reg).setValue(car);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "Something went wrong input to db");

                    }


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
