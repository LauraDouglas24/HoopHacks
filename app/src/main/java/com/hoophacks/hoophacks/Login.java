package com.hoophacks.hoophacks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    // Shared components
    private TextView tvStatus;
    private String TAG = "Login";

    // Email and Password Login
    private EditText etEmail;
    private EditText etPassword;
//    private Button bCreateUser;
    private Button bLogIn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Google Login
    private SignInButton bGoogleLogIn;
    GoogleApiClient mGoogleApiClient;

    // Shared components - onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Shared components
        tvStatus = (TextView) findViewById(R.id.tvStatus);

        // Email and Password Login
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        // bCreateUser = (Button) findViewById(R.id.bCreateUser);
//        bCreateUser.setOnClickListener(this);
        bLogIn = (Button) findViewById(R.id.bLogIn);
        bLogIn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent myIntent = new Intent(Login.this, UserFeed.class);
                    Login.this.startActivity(myIntent);
                } else {
                    tvStatus.setText("Please log in.");
                }
            }
        };

        // Google Login
        bGoogleLogIn = (SignInButton) findViewById(R.id.bGoogleLogIn);
        bGoogleLogIn.setOnClickListener(this);

        GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions).build();
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

    // Shared components - onClick
    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.bCreateUser:
//                createUser(etEmail.getText().toString(), etPassword.getText().toString());
            case R.id.bLogIn:
                logIn(etEmail.getText().toString(), etPassword.getText().toString());
                break;
            case R.id.bGoogleLogIn:
                googleLogIn();
                break;
        }
    }

    // Email and Password - createUser
//    private void createUser(String email, String password){
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                            Intent myIntent = new Intent(Login.this, UserFeed.class);
//                            Login.this.startActivity(myIntent);
//                        } else {
//                            tvStatus.setText("Authentication failed.");
//                        }
//                    }
//                });
//    }

    // Email and Password - logIn
    private void logIn(String email, String password){
        Log.d(TAG, "logIn");
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            tvStatus.setText("Email or Password musn't be empty.");
        } else {
            Log.d(TAG, "logIn ELSE");
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "logIn onComplete");
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                Intent myIntent = new Intent(Login.this, UserFeed.class);
                                Login.this.startActivity(myIntent);
                            } else {
                                tvStatus.setText("Authentication failed.");
                            }
                        }
                    });
        }
    }

    // Google Login - googleLogIn
    private void googleLogIn(){
        Intent logInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(logInIntent, 9001);
    }

    // Google Login - onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 9001){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    // Google Login - handleSignInResult
    private void handleSignInResult(GoogleSignInResult result)
    {
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            tvStatus.setText("Hello " + account.getDisplayName());

            Intent myIntent = new Intent(Login.this, UserFeed.class);
            Login.this.startActivity(myIntent);
        }
        else{

        }
    }

    // Google Login - onConnectionFailed
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }

}
