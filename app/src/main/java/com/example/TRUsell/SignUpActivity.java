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
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailET, passwordET, confirmPasswordET;
    private Button register_button;

    private ProgressDialog progress;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.register_email_ET);
        passwordET = findViewById(R.id.register_pass_ET);
        confirmPasswordET = findViewById(R.id.register_confirm_pass_ET);

        register_button =(Button) findViewById(R.id.register_account_button);
        progress = new ProgressDialog(this);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount() {
    String email = emailET.getText().toString();
    String password = passwordET.getText().toString();
    String confpassword = confirmPasswordET.getText().toString();


    if(TextUtils.isEmpty(email)){
        Toast.makeText(this, "Please write your email", Toast.LENGTH_SHORT).show();

    }else if(TextUtils.isEmpty(password)){
        Toast.makeText(this, "Please write your password", Toast.LENGTH_SHORT).show();

    }if(TextUtils.isEmpty(confpassword)){
            Toast.makeText(this, "Please confirm the password", Toast.LENGTH_SHORT).show();

        }else if(!password.equals(confpassword)){
            Toast.makeText(this, "Your password does not match the confirmation", Toast.LENGTH_SHORT).show();

        }else {

        progress.setTitle("Signing you up!");
        progress.setMessage("Please Wait!");
        progress.show();
        progress.setCanceledOnTouchOutside(true);


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    SendToSetupActivity();

                    Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }else{
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(SignUpActivity.this, "Error:"+errorMessage, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        });

        }
    }

    private void SendToSetupActivity() {
    Intent setupIntent = new Intent(SignUpActivity.this, SetupActivity.class);
    setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(setupIntent);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null) {
            SendToMainActivity();

        }
    }

    private void SendToMainActivity() {
        Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
