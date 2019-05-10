package com.group2.carinsuranceapp;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    Button btnSignup;
    TextView btnLogin,btnForgotPass;
    EditText input_email,input_pass;
    RelativeLayout activity_sign_up;
    private String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private String passwordRegex ="^.{6,}";
    private FirebaseAuth auth;
    Snackbar snackbar;

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
        //TODO add error checks
        else if(view.getId() == R.id.signup_btn_register) {
            if(input_email.getText().toString().equals("") || input_pass.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(),"Input fields empty",Toast.LENGTH_SHORT).show();
            } else if(!input_email.getText().toString().matches(emailRegex)) {
                Toast.makeText(getApplicationContext(),"Email not acceptable",Toast.LENGTH_SHORT).show();
            } else if(!input_pass.getText().toString().matches(passwordRegex)){
                Toast.makeText(getApplicationContext(),"Password is too short",Toast.LENGTH_SHORT).show();
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
                            snackbar = Snackbar.make(findViewById(R.id.activity_sign_up),"Error: "+task.getException(),Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        else{
                            snackbar = Snackbar.make(findViewById(R.id.activity_sign_up),"Registration Successful: "+task.getException(),Snackbar.LENGTH_SHORT);
                            snackbar.show();


                            startActivity(new Intent(SignUp.this,LoggedInMainActivity.class));
                        }
                    }
                });
    }
}
