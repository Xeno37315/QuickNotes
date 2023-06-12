package com.dimaenc.quicknotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailedNoteActivity extends AppCompatActivity {

    private TextView detailed_title_text, detailed_note_content;
    private FloatingActionButton detailed_btn_edit, detailed_btn_delete, detailed_btn_back;

    private FirebaseFirestore firestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_note);

        detailed_title_text = findViewById(R.id.detailed_title_text);
        detailed_note_content = findViewById(R.id.detailed_note_content);
        detailed_btn_edit = findViewById(R.id.detailed_btn_edit);
        detailed_btn_delete = findViewById(R.id.detailed_btn_delete);
        detailed_btn_back = findViewById(R.id.detailed_btn_back);

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent data = getIntent();

        detailed_btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditNoteActivity.class);
                intent.putExtra("title", data.getStringExtra("title"));
                intent.putExtra("content", data.getStringExtra("content"));
                intent.putExtra("note_id", data.getStringExtra("note_id"));

                finish();
                startActivity(intent);
            }
        });

        detailed_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String document_id = data.getStringExtra("note_id");
                DocumentReference documentReference = firestore.collection("notes").document(firebaseUser.getUid()).collection("UserNotes").document(document_id);
                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Note was succefully removed!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to remove note!", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
                startActivity(new Intent(DetailedNoteActivity.this, NotepadActivity.class));
            }
        });

        detailed_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(DetailedNoteActivity.this, NotepadActivity.class));
            }
        });

        detailed_title_text.setText(data.getStringExtra("title"));
        detailed_note_content.setText(data.getStringExtra("content"));

    }
}