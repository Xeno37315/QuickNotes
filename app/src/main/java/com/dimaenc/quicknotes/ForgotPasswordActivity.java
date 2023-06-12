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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText edit_mail;
    private TextView tw_goto_main_screen;
    private Button btn_recover;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edit_mail = findViewById(R.id.edit_mail);
        tw_goto_main_screen = findViewById(R.id.tw_goto_main_screen);
        btn_recover = findViewById(R.id.btn_recover);

        firebaseAuth = FirebaseAuth.getInstance();

        tw_goto_main_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
            }
        });

        btn_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = edit_mail.getText().toString().trim();
                if(mail.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "You need to enter your recovery mail!", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Recovery password was sent successfully!", Toast.LENGTH_SHORT);
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Failed to recover password! check if you typed your mail correctly.", Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
            }
        });
    }
}