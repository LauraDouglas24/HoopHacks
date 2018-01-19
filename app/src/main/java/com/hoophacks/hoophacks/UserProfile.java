package com.hoophacks.hoophacks;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    private EditText etWeight, etHeight, etAge;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private Button bUpdate;

    private double weight, height, age;
    private String gender;

    private static final String TAG = "UserProfile";
    private DocumentReference dr = FirebaseFirestore.getInstance().document("HoopHacks/User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        etWeight = (EditText)findViewById(R.id.etWeight);
        etHeight = (EditText)findViewById(R.id.etHeight);
        etAge = (EditText)findViewById(R.id.etAge);
        rgGender = (RadioGroup)findViewById(R.id.rgGender);
        rbMale = (RadioButton)findViewById(R.id.rbMale);
        rbFemale = (RadioButton)findViewById(R.id.rbFemale);
        bUpdate = (Button)findViewById(R.id.bUpdate);
        bUpdate.setOnClickListener(this);

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                switch (checkedId){
                    case R.id.rbMale:
                        gender = "Male";
                        break;
                    case R.id.rbFemale:
                        gender = "Female";
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bUpdate:
                weight = Double.parseDouble(etWeight.getText().toString());
                height = Double.parseDouble(etHeight.getText().toString());
                age = Double.parseDouble(etAge.getText().toString());

                Log.d(TAG, "Weight: " + weight);
                Log.d(TAG, "Height: " + height);
                Log.d(TAG, "Age: " + age);
                Log.d(TAG, "Gender: " + gender);

                Map<String, Object> dateToSave = new HashMap<String, Object>();
                dateToSave.put("Weight", weight);
                dateToSave.put("Height", height);
                dateToSave.put("Age", age);
                dateToSave.put("Gender", gender);
                dr.set(dateToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Database update Complete");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Database update not Complete");
                    }
                });

        }
    }

}
