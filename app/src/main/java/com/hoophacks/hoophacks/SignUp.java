package com.hoophacks.hoophacks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    // Shared components
    private TextView tvStatus;
    private String TAG = "Login";

    private EditText etEmail;
    private EditText etPassword;
    private Button bCreateUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Shared components
        tvStatus = (TextView) findViewById(R.id.tvStatus);

        // Email and Password Login
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bCreateUser = (Button) findViewById(R.id.bCreateUser);
        bCreateUser.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent myIntent = new Intent(SignUp.this, UserFeed.class);
                    SignUp.this.startActivity(myIntent);
                } else {
                    tvStatus.setText("Please log in.");
                }
            }
        };

        bCreateUser = (Button) findViewById(R.id.bCreateUser);
    }

    // Shared components - onClick
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bCreateUser:
                createUser(etEmail.getText().toString(), etPassword.getText().toString());
                break;
}
    }


    // Shared components - onStart
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // Shared components - onStop
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // Email and Password - createUser
    private void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent myIntent = new Intent(SignUp.this, Login.class);
                            SignUp.this.startActivity(myIntent);
                        } else {
                            tvStatus.setText("Authentication failed.");
                        }
                    }
                });
    }}
