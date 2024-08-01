package com.example.projeto.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projeto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button login_btn;
    private Button naoPossuo;
    private Button esqueceuSenha;
    private TextView email_txt;
    private TextView password_txt;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        login_btn = findViewById(R.id.login_btn);
        email_txt = findViewById(R.id.email_txt);
        password_txt = findViewById(R.id.password_txt);
        naoPossuo = findViewById(R.id.naoPossuo_btn);
        esqueceuSenha = findViewById(R.id.esqueceuSenha_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        carregarCadastro();
        Login();
        EsqueceuSenha();

    }

    private void Login(){
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_txt.getText().toString().trim();
                String senha = password_txt.getText().toString().trim();

                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(login.this, "Digite o Email e Senha", Toast.LENGTH_LONG).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email, senha)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        verificarDocumentoUsuario();
                                    } else {
                                        if (task.getException() instanceof FirebaseAuthException) {
                                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                            Toast.makeText(login.this, "Falha no login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(login.this, "Falha no login", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    private void EsqueceuSenha(){
        esqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, RedefinirSenha.class);
                startActivity(intent);
            }
        });
    }

    private void carregarCadastro() {
        naoPossuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, Cadastro.class);
                startActivity(intent);
            }
        });
    }

    private void verificarDocumentoUsuario() {
        String userId = firebaseAuth.getCurrentUser().getUid();
        DocumentReference userRef = db.collection("usuario").document(userId);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Intent intent = new Intent(login.this, Menu_tela.class);
                        startActivity(intent);
                        finish();
                    } else {
                        criarDocumentoUsuario(userId);
                    }
                } else {
                    Toast.makeText(login.this, "Erro ao verificar usuário", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void criarDocumentoUsuario(String userId) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", firebaseAuth.getCurrentUser().getEmail());

        db.collection("usuario").document(userId)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(login.this, Menu_tela.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(login.this, "Erro ao criar usuário", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
