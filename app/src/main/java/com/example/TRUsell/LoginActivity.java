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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText emailET, passwET;


    private ProgressDialog progress;

    private Button createAccountLinkButton;

    private FirebaseAuth firebaseAuth;
    private ImageView googleIV, phoneIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAccountLinkButton = findViewById(R.id.login_register_button);
        emailET = findViewById(R.id.login_email_ET);
        passwET = findViewById(R.id.login_pass_ET);
        loginButton = findViewById(R.id.login_account_button);
        progress = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        createAccountLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSignUpActivity();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttemptLogin();
            }
        });

    }

    private void AttemptLogin() {
        String email = emailET.getText().toString();
        String password = passwET.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progress.setTitle("Login");
            progress.setMessage("Please wait, while we attempt to login");
            progress.setCanceledOnTouchOutside(true);
            progress.show();


            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                SendToMainActivity();

                                Toast.makeText(LoginActivity.this, "Logged In successfully!", Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        }
                    });
        }
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
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void sendToSignUpActivity() {

        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(loginIntent);



    }
}
