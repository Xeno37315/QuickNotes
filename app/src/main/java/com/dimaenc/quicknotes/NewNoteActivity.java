package com.dimaenc.quicknotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewNoteActivity extends AppCompatActivity {

    private EditText title_text, note_content;
    private FloatingActionButton btn_back;
    private FloatingActionButton btn_save;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        title_text = findViewById(R.id.title_text);
        note_content = findViewById(R.id.note_content);
        btn_back = findViewById(R.id.btn_back);
        btn_save = findViewById(R.id.btn_save);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(NewNoteActivity.this, NotepadActivity.class));
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = title_text.getText().toString().trim();
                String content = note_content.getText().toString().trim();

                if(title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Note must containt title and text!", Toast.LENGTH_SHORT).show();
                }
                else {
                    DocumentReference documentReference = firestore.collection("notes").document(firebaseUser.getUid()).collection("UserNotes").document();
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", title);
                    note.put("content", content);
                    documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Note was created successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(NewNoteActivity.this, NotepadActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to create note!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}