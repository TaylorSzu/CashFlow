package com.example.projeto.controller;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import com.example.projeto.recursos.HistoricoAdapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.R;
import com.example.projeto.entidades.Receitas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Historico extends AppCompatActivity {

    private ListView listaHistorico;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historico);

        db = FirebaseFirestore.getInstance();

        listaHistorico = findViewById(R.id.listaHistorico);

        verificarUsuarioLogado();
        carregarListaDeHistorico();
    }

    private void verificarUsuarioLogado() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "Usuário não está logado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void carregarListaDeHistorico() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("Receitas")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Receitas> receitas = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String descricao = document.getString("Descrição");
                                String tipo = document.getString("Tipo");
                                String valor = document.getString("Valor");

                                Receitas receita = new Receitas(id, descricao, tipo, valor);
                                receitas.add(receita);
                            }

                            HistoricoAdapter adapter = new HistoricoAdapter(Historico.this, receitas);
                            listaHistorico.setAdapter(adapter);
                        } else {
                            Toast.makeText(Historico.this, "Erro ao listar Receitas: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
