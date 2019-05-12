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

    private TextView carMake;
    private TextView carModel;
    private TextView carRegNum;
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
        carMake = view.findViewById(R.id.text_car_make_insurance_info_changable);
        carModel= view.findViewById(R.id.text_car_model_insurance_info_changeable);
        carRegNum= view.findViewById(R.id.text_car_reg_num_insurance_info_changeable);
        carList = view.findViewById(R.id.list_of_cars);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();
        databaseCars = FirebaseDatabase.getInstance().getReference("Car/" + userID);



        listofcars = new ArrayList<>();



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
                for (DataSnapshot sn: dataSnapshot.getChildren()) {
                    UserCar car = sn.getValue(UserCar.class);
                    Log.e(TAG, "whatever getchildren() does: " + sn.toString());
                    listofcars.add(car);
                    Log.e(TAG, "What it's adding to the list" + car.getRegistration());

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
