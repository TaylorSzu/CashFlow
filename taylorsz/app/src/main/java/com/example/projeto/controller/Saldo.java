package com.example.projeto.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Saldo extends AppCompatActivity {

    private TextView Entrada;
    private TextView Saida;
    private TextView Total;

    private Button Inicio;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saldo);

        db = FirebaseFirestore.getInstance();

        Entrada = findViewById(R.id.Entrda_label);
        Saida = findViewById(R.id.Saida_label);
        Total = findViewById(R.id.Total_label);
        Inicio = findViewById(R.id.InicioSaldo_btn);

        carregarInicio();
        mostrarSaldo();
    }

    private void mostrarSaldo() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("Receitas")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            double totalEntrada = 0;
                            double totalSaida = 0;

                            for (DocumentSnapshot document : task.getResult()) {
                                String valorStr = document.getString("Valor");
                                String tipo = document.getString("Tipo");

                                if (valorStr != null && tipo != null) {
                                    double valor = Double.parseDouble(valorStr);
                                    if (tipo.equals("Entrada")) {
                                        totalEntrada += valor;
                                    } else if (tipo.equals("Saída")) {
                                        totalSaida += valor;
                                    }
                                }
                            }

                            double total = totalEntrada - totalSaida;

                            Entrada.setText("Entrada: R$ " + String.format("%.2f", totalEntrada));
                            Saida.setText("Saída: R$ " + String.format("%.2f", totalSaida));
                            Total.setText("Total: R$ " + String.format("%.2f", total));
                        } else {
                            Toast.makeText(Saldo.this, "Erro ao obter o saldo", Toast.LENGTH_SHORT).show();
                            Log.e("Saldo", "Erro ao obter o saldo", task.getException());
                        }
                    }
                });
    }


    private void carregarInicio(){
        Inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Saldo.this, Menu_tela.class);
                startActivity(intent);
            }
        });
    }
}
