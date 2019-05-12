package com.group2.carinsuranceapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group2.databaseclasses.UserData;

import org.w3c.dom.Text;

public class Fragment_MyInformation extends Fragment {

    private TextView name;
    private TextView surname;
    private TextView birthdate;
    private TextView sex;
    private Button editButton;

    // Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_information,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialise database variables
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("User");
        userID = mAuth.getCurrentUser().getUid();


        //Views
        name = view.findViewById(R.id.text_first_name_myinfo_changable);
        surname = view.findViewById(R.id.text_last_name_myinfo_changeable);
        birthdate = view.findViewById(R.id.text_birthdate_myinfo_changeable);
        sex = view.findViewById(R.id.text_sex_myinfo_changeable);
        editButton = view.findViewById(R.id.edit_button);


        //Put database info in views
        //String nameF = myRef.child(userID).child("gender")

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserData udata = dataSnapshot.getValue(UserData.class);
                name.setText(udata.getFirstName());
                surname.setText(udata.getLastName());
                birthdate.setText(udata.getDob());
                sex.setText(udata.getGender());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
