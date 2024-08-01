package com.example.projeto.controller;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.R;
import com.example.projeto.entidades.Reservas;
import com.example.projeto.recursos.ReservaAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class VerReserva extends AppCompatActivity {

    private ListView ListaReservas;
    private FirebaseFirestore db;
    private ReservaAdapter reservaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_reservas);

        db = FirebaseFirestore.getInstance();

        ListaReservas = findViewById(R.id.listReservas);

        verificarUsuarioLogado();
        carregarListaDeReservas();
    }

    private void verificarUsuarioLogado() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "Usuário não está logado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void carregarListaDeReservas() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("Reservas")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Reservas> reservas = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nome = document.getString("Nome");
                                String valor = document.getString("Valor");
                                String id = document.getId();

                                Reservas reserva = new Reservas(nome, valor, id);
                                reservas.add(reserva);
                            }

                            reservaAdapter = new ReservaAdapter(VerReserva.this, R.layout.ver_reserva_item, reservas);
                            ListaReservas.setAdapter(reservaAdapter);
                        } else {
                            Toast.makeText(VerReserva.this, "Erro ao listar Reservas" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
