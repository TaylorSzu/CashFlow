package com.example.projeto.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RedefinirSenha extends AppCompatActivity {

    private EditText emailRedefinirSenha;
    private Button redefinirBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redefinir_senha);

        emailRedefinirSenha = findViewById(R.id.emailRedefinirSenha_txt);
        redefinirBtn = findViewById(R.id.redefinir_btn);
        firebaseAuth = FirebaseAuth.getInstance();

        redefinirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailRedefinirSenha.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(RedefinirSenha.this, "Digite o email", Toast.LENGTH_LONG).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RedefinirSenha.this, "Email de redefinição enviado", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(RedefinirSenha.this, "Erro ao enviar email de redefinição", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
