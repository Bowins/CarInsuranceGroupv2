package com.group2.carinsuranceapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group2.databaseclasses.UserCar;

import java.util.ArrayList;
import java.util.List;

public class Fragment_MyInsuranceInfo extends Fragment {


    private Button addCar;
    private ListView carList;

    DatabaseReference databaseCars;
    private FirebaseAuth mAuth;
    private String userID;
    FirebaseUser firebaseUser;
    private static final String TAG = "DEBUGGING - ";
    List<UserCar> listofcars;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_insurance_info,null);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialise layout widgets and database stuff
        carList = view.findViewById(R.id.list_of_cars);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();
        databaseCars = FirebaseDatabase.getInstance().getReference("Car/" + userID);



        listofcars = new ArrayList<>();

        carList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                UserCar us = listofcars.get(i);
                String lastPath = us.getRegistration();
                FirebaseDatabase.getInstance().getReference("Car/" + userID + "/" + lastPath).removeValue();
                return false;
            }
        });


        view.findViewById(R.id.b_add_car).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment_AddCar();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.screen,fragment);
                fragmentTransaction.commit();
            }
        });


        databaseCars.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "This exists  " + dataSnapshot.getChildrenCount());
                listofcars.removeAll(listofcars);
                for (DataSnapshot sn: dataSnapshot.getChildren()) {
                    UserCar car = sn.getValue(UserCar.class);
                    listofcars.add(car);

                }

                CarsList adapter = new CarsList(getActivity(), listofcars);
                carList.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
