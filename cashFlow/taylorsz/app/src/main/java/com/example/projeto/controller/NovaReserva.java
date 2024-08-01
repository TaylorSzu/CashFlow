package com.example.projeto.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class NovaReserva extends AppCompatActivity {

    private EditText Nome;
    private EditText Valor;
    private EditText Descricao;
    private Button Salvar;
    private Button Cancelar;
    private TextView Entrada;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nova_reserva);

        db = FirebaseFirestore.getInstance();

        Nome = findViewById(R.id.nomeNovaReserva_txt);
        Valor = findViewById(R.id.valorNovaReserva_txt);
        Descricao = findViewById(R.id.descricaoNovaReserva_txt);
        Salvar = findViewById(R.id.salvarNovaReserva_btn);
        Cancelar = findViewById(R.id.cancelarNovaReserva_btn);

        verificarUsuarioLogado();
        salvar();
        Cancelar();
    }

    private void salvar(){
        Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarReserva();
            }
        });
    }

    private void Cancelar(){
        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NovaReserva.this, Menu_tela.class);
                startActivity(intent);
            }
        });
    }

    private void verificarUsuarioLogado() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "Usuário não está logado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void RegistrarReserva(){
        String nome = Nome.getText().toString();
        String valor = Valor.getText().toString();
        String descricao =  Descricao.getText().toString();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(descricao.isEmpty() || valor.isEmpty() || nome.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> reserva = new HashMap<>();
        reserva.put("Nome", nome);
        reserva.put("Valor", valor);
        reserva.put("Descrição", descricao);
        reserva.put("userId", userId);

        db.collection("Reservas").add(reserva).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Nome.setText("");
                    Valor.setText("");
                    Descricao.setText("");
                    Toast.makeText(NovaReserva.this, "Reserva registrada com sucesso", Toast.LENGTH_SHORT).show();
                    calcularTotalEntradas();
                }else {
                    Toast.makeText(NovaReserva.this, "Erro ao registrar a Reserva", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void calcularTotalEntradas() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("Reservas")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            double totalEntradas = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                String valorStr = document.getString("Valor");
                                if (valorStr != null) {
                                    double valor = Double.parseDouble(valorStr);
                                    totalEntradas += valor;
                                }
                            }
                            Toast.makeText(NovaReserva.this, "Total de entradas: " + totalEntradas, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NovaReserva.this, "Erro ao calcular o total de entradas", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
