package com.dimaenc.quicknotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNoteActivity extends AppCompatActivity {

    private EditText editing_title_text, editing_note_content;
    private FloatingActionButton editing_btn_save, editing_btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        editing_title_text = findViewById(R.id.editing_title_text);
        editing_note_content = findViewById(R.id.editing_note_content);
        editing_btn_save = findViewById(R.id.editing_btn_save);
        editing_btn_back = findViewById(R.id.editing_btn_back);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent data = getIntent();

        editing_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edited_title = editing_title_text.getText().toString().trim();
                String edited_content = editing_note_content.getText().toString().trim();

                if(edited_title.isEmpty() || edited_content.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Can't save note with empty title or content!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String document_id = data.getStringExtra("note_id");
                    DocumentReference documentReference = firestore.collection("notes").document(firebaseUser.getUid()).collection("UserNotes").document(document_id);
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", edited_title);
                    note.put("content", edited_content);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Note was successfully updated!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Note has failed to update!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                    startActivity(new Intent(EditNoteActivity.this, NotepadActivity.class));

                }
            }
        });

        editing_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(EditNoteActivity.this, NotepadActivity.class));
            }
        });

        editing_title_text.setText(data.getStringExtra("title"));
        editing_note_content.setText(data.getStringExtra("content"));
    }
}