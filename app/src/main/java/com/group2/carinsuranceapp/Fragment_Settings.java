package com.group2.carinsuranceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Fragment_Settings extends Fragment implements View.OnClickListener {

    private TextView txtWelcome;
    private EditText input_new_password;
    private Button btnChangePass, btnLogout;
    private RelativeLayout activity_dashboard;
    private Button btnDatabase;
    private FirebaseAuth auth;
    private EditText input_old_password;
    FirebaseUser user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings,null);



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
        input_old_password = view.findViewById(R.id.dashboard_old_password);


        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

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
        if(view.getId() == R.id.dashboard_btn_change_pass) {

            Log.d("Password says", input_new_password.getText().toString());

            if (input_new_password.getText().equals(null) )
            {
                Toast.makeText(getActivity(), "Please nput new password", Toast.LENGTH_LONG);
            }
            else if (input_old_password.getText().equals(null) )
            {
                Toast.makeText(getActivity(), "Please input old password", Toast.LENGTH_LONG);
            } else if(input_new_password.getText().toString().length() < 6){
                Toast.makeText(getActivity(), "New password has to be 6 or more characters", Toast.LENGTH_LONG);
            }
            else
                {
                changePassword(input_new_password.getText().toString(),input_old_password.getText().toString());
            }
        }
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

    private void changePassword(final String newPassword, String oldPassword){
        user = auth.getCurrentUser();
        Log.d("IN Change", "hello");
        String email = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email,oldPassword);

        user.reauthenticate(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(newPassword).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("IN success", "hi");
                                Toast toast = Toast.makeText(getContext(), "Password changed", Toast.LENGTH_LONG);
                                toast.show();
                            }else{
                                Log.d("PASSWORD CHANGE","failed");
                            }
                        }
                    });
                } else {
                    Log.d("REAUTHORISE","failed");
                    Toast toast = Toast.makeText(getContext(), "ERROR: Old password is not correct!", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });

    }


}
