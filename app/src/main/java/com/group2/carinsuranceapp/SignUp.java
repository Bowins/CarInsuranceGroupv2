package com.group2.carinsuranceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    Button btnSignup;
    TextView btnLogin,btnForgotPass;
    EditText input_email,input_pass;
    RelativeLayout activity_sign_up;
    private String passwordRegex ="^.{6,}";
    private FirebaseAuth auth;
    Toast toast;
    private static final int FRAGMENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //View
        btnSignup = (Button)findViewById(R.id.signup_btn_register);
        btnLogin = (TextView)findViewById(R.id.signup_btn_login);
        btnForgotPass = (TextView)findViewById(R.id.signup_btn_forgot_pass);
        input_email = (EditText)findViewById(R.id.signup_email);
        input_pass = (EditText)findViewById(R.id.signup_password);
        activity_sign_up = (RelativeLayout)findViewById(R.id.activity_main);


        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);


        //init Firebase
        auth = FirebaseAuth.getInstance();

    }

    public void onClick(View view){
        if(view.getId() == R.id.signup_btn_login){
            startActivity(new Intent(SignUp.this,MainActivity.class));
            finish();
        }
        else if(view.getId() == R.id.signup_btn_forgot_pass) {
            startActivity(new Intent(SignUp.this, ForgotPassword.class));
            finish();
        }
        else if(view.getId() == R.id.signup_btn_register) {
            if(input_email.getText().toString().equals("") || input_pass.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(),"Input fields empty",Toast.LENGTH_LONG).show();
            } else if(!input_pass.getText().toString().matches(passwordRegex)){
                Toast.makeText(getApplicationContext(),"Password has to be 6 or more characters",Toast.LENGTH_LONG).show();
            }
            else{signUpUser(input_email.getText().toString(),input_pass.getText().toString());}

        }

    }

    private void signUpUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            toast = Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG);
                            toast.show();
                        }
                        else{
                            toast = Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG);
                            toast.show();

                            startActivity(new Intent(SignUp.this, DatabaseInputPersonalInfoActivity.class));
                        }
                    }
                });
    }
}
