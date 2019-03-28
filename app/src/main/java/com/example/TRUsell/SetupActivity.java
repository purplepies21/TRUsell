package com.example.TRUsell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {


    private EditText userNameET, fullNameET, cityET;
    private Button confirmButton;
    CircleImageView profilePicture;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progress;

    private DatabaseReference userReference;

    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        progress = new ProgressDialog(this);
        userNameET = findViewById(R.id.setup_username_ET);
        cityET = findViewById(R.id.setup_City_ET);
        fullNameET = findViewById(R.id.setup_fullname_ET);
        profilePicture = (CircleImageView)findViewById(R.id.setup_profile_picture);
        confirmButton = (Button) findViewById(R.id.setup_confirm_button);





        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountInformation();
            }
        });


    }
    private void SaveAccountInformation() {

        String username = userNameET.getText().toString();
        String fullname = fullNameET.getText().toString();
        String city = cityET.getText().toString();
        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(fullname)){
            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(city)){
            Toast.makeText(this, "Please enter your city name", Toast.LENGTH_SHORT).show();

        }else{

            progress.setTitle("Saving info!");
            progress.setMessage("Please wait, while we send your info to our database");
            progress.setCanceledOnTouchOutside(true);
            progress.show();

            HashMap userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("fullname",fullname);
            userMap.put("city",city);
            userMap.put("itemsbought", 0);
            userMap.put("itemssold",0);
            userMap.put("status","Computer Science");
            userMap.put("studyyear", 3);

            userReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful()) {
                        SendToMainActivity();
                        Toast.makeText(SetupActivity.this, "Account setup successfully!", Toast.LENGTH_SHORT).show();
                        progress.dismiss();


                    }else{
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error:"+ message, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                }
            });



        }

    }
    private void SendToMainActivity() {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
