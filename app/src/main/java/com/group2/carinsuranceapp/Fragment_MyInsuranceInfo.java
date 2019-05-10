package com.group2.carinsuranceapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Fragment_MyInsuranceInfo extends Fragment {

    private TextView carMake;
    private TextView carModel;
    private TextView carRegNum;
    private Button addCar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_insurance_info,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        carMake = view.findViewById(R.id.text_car_make_insurance_info_changable);
        carModel= view.findViewById(R.id.text_car_model_insurance_info_changeable);
        carRegNum= view.findViewById(R.id.text_car_reg_num_insurance_info_changeable);












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
    }
}
