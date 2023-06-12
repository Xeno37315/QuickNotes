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

public class MainActivity extends AppCompatActivity {

    private EditText edit_mail, edit_password;
    private Button btn_signin;
    private Button btn_signup;
    private TextView tw_forgot_password;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_mail = findViewById(R.id.edit_mail);
        edit_password = findViewById(R.id.edit_password);
        btn_signin = findViewById(R.id.btn_signin);
        tw_forgot_password = findViewById(R.id.tw_forgot_password);
        btn_signup = findViewById(R.id.btn_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null) {
            finish();
            startActivity(new Intent(MainActivity.this, NotepadActivity.class));
        }

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = edit_mail.getText().toString().trim();
                String password = edit_password.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "To sign in input both mail and password", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                checkMailVerification();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Account does not exist!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        tw_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void checkMailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser.isEmailVerified()) {
            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, NotepadActivity.class));
        }
        else {
            Toast.makeText(getApplicationContext(), "Failed to login! Email is not verified.", Toast.LENGTH_SHORT).show();
        }
    }
}