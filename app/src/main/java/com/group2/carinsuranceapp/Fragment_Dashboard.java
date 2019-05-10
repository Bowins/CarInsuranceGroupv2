package com.group2.carinsuranceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Fragment_Dashboard extends Fragment implements View.OnClickListener {

    private TextView txtWelcome;
    private EditText input_new_password;
    private Button btnChangePass, btnLogout;
    private RelativeLayout activity_dashboard;
    private Button btnDatabase;

    private FirebaseAuth auth;

    //TODO change to frangment

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_dashboard,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //View
        txtWelcome = view.findViewById(R.id.dashboard_welcome);
        input_new_password = view.findViewById(R.id.dashboard_new_password);
        btnChangePass = view.findViewById(R.id.dashboard_btn_change_pass);
        btnLogout = view.findViewById(R.id.dashboard_btn_logout);
        activity_dashboard = view.findViewById(R.id.activity_dash_board);
        btnDatabase = view.findViewById(R.id.dashboard_btn_database);

        btnDatabase.setOnClickListener(this);
        btnChangePass.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        //init Firebase

        auth = FirebaseAuth.getInstance();

        //Session Check
        if(auth.getCurrentUser() != null)
            txtWelcome.setText("Welcome, "+auth.getCurrentUser().getEmail());
    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.dashboard_btn_change_pass)
            changePassword(input_new_password.getText().toString());
        else if(view.getId() == R.id.dashboard_btn_logout)
            logoutUser();
        else if (view.getId() == R.id.dashboard_btn_database)
        {

            Fragment fragment = new Fragment_Database();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction incidentTransaction = fragmentManager.beginTransaction();

            incidentTransaction.replace(R.id.screen,fragment);
            incidentTransaction.commit();

        }
    }

    private void logoutUser() {
        auth.signOut();
        if(auth.getCurrentUser() == null){
            startActivity(new Intent(this.getActivity(),MainActivity.class));

        }
    }

    private void changePassword(String newPassword){
        FirebaseUser user = auth.getCurrentUser();
        user.updatePassword(newPassword).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(R.id.activity_dash_board),"Password changed", Snackbar.LENGTH_SHORT);
                    snackBar.show();
                }
            }
        });
    }
}
//  findViewById(R.id.activity_dash_board)