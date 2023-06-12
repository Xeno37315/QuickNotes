package com.dimaenc.quicknotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private TextView tw_have_account;
    private Button btn_finish_signup;
    private EditText edit_username;
    private EditText edit_mail;
    private EditText edit_password_first;
    private EditText edit_password_second;
    private int pass_min_length = 7;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        tw_have_account = findViewById(R.id.tw_have_account);

        tw_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });

        edit_username = findViewById(R.id.edit_username);
        edit_mail = findViewById(R.id.edit_mail);
        edit_password_first = findViewById(R.id.edit_password_first);
        edit_password_second = findViewById(R.id.edit_password_second);

        btn_finish_signup = findViewById(R.id.btn_finish_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_finish_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edit_username.getText().toString().trim();
                String mail = edit_mail.getText().toString().trim();
                String password_first = edit_password_first.getText().toString().trim();
                String password_second = edit_password_second.getText().toString().trim();

                if(username.isEmpty() || mail.isEmpty() || password_first.isEmpty() || password_second.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields are required to be filled", Toast.LENGTH_SHORT).show();
                }
                else if(!password_first.equals(password_second)) {
                    Toast.makeText(getApplicationContext(), "Passwords have to match!", Toast.LENGTH_SHORT).show();
                }
                else if(password_first.length() < pass_min_length || password_second.length() < pass_min_length) {
                    Toast.makeText(getApplicationContext(), "Password needs at least 7 characters", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.createUserWithEmailAndPassword(mail, password_first).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), username + " is now registered!", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Failed to register!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null) {
            Toast.makeText(getApplicationContext(), "Failed to send verification email!", Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String mail = edit_mail.getText().toString().trim();
                    Toast.makeText(getApplicationContext(), "Verification email was sent to " + mail, Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();

                    finish();
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                }
            });
        }
    }
}