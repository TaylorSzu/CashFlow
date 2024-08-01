package com.example.projeto.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Cadastro extends AppCompatActivity {

    private EditText nomeTxt, emailTxt, senhaTxt;
    private Button cadastrarBtn;
    private Button possuo;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);

        mAuth = FirebaseAuth.getInstance();

        nomeTxt = findViewById(R.id.usuario_txt);
        emailTxt = findViewById(R.id.email_txt);
        senhaTxt = findViewById(R.id.password_txt);
        cadastrarBtn = findViewById(R.id.cadastrar_btn);
        possuo = findViewById(R.id.Possuo_btn);

        Cadastar();
        PossuoConta();
    }

    private void Cadastar() {
        cadastrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarNovoUsuario();
            }
        });
    }

    private void PossuoConta() {
        possuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cadastro.this, login.class);
                startActivity(intent);
            }
        });
    }

    private void cadastrarNovoUsuario() {
        final String nome = nomeTxt.getText().toString().trim();
        final String email = emailTxt.getText().toString().trim();
        final String senha = senhaTxt.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                user.updateProfile(new UserProfileChangeRequest.Builder()
                                                .setDisplayName(nome)
                                                .build())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    user.sendEmailVerification()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(Cadastro.this, "Cadastro realizado com sucesso! Verifique seu email para confirmar.", Toast.LENGTH_SHORT).show();

                                                                    } else {
                                                                        Toast.makeText(Cadastro.this, "Erro ao enviar email de verificação.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    Toast.makeText(Cadastro.this, "Erro ao atualizar perfil do usuário.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.w("CadastroActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Cadastro.this, "Erro ao cadastrar. Verifique os dados e tente novamente.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
