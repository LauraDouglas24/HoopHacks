package com.hoophacks.hoophacks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.common.api.GoogleApiClient;

public class UserFeed extends AppCompatActivity implements View.OnClickListener{

    // Shared Components
    private Button bLogOut;
    private Button bProfile;

    // Email and Password Login
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Google Login
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        // Shared Components
        bLogOut = (Button) findViewById(R.id.bLogOut);
        bLogOut.setOnClickListener(this);
        bProfile = (Button) findViewById(R.id.bProfile);
        bProfile.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent myIntent = new Intent(UserFeed.this, Login.class);
                    UserFeed.this.startActivity(myIntent);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // Shared components - onClick
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogOut:
                logOut();
                break;
            case R.id.bProfile:
                Intent myIntent = new Intent(UserFeed.this, UserProfile.class);
                UserFeed.this.startActivity(myIntent);
        }
    }

    // Shared components - logOut
    private void logOut(){
        //Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        mAuth.signOut();

        Intent myIntent = new Intent(UserFeed.this, Login.class);
        UserFeed.this.startActivity(myIntent);
    }
}
