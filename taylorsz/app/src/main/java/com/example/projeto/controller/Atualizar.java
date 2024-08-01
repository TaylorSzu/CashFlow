package com.example.projeto.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Atualizar extends AppCompatActivity {

    private Spinner selecionarReservaSpinner;
    private EditText valorAtual;
    private EditText valor;
    private Button salvar;
    private Button cancelar;
    private CheckBox adicionar;
    private CheckBox retirar;
    private List<String> reservaIds;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atualizar_reserva);

        db = FirebaseFirestore.getInstance();
        selecionarReservaSpinner = findViewById(R.id.selecionarReserva_spinner);
        valorAtual = findViewById(R.id.valorAtualAttReserva_txt);
        valor = findViewById(R.id.valorAttReserva_txt);
        adicionar = findViewById(R.id.Adicionar_checkBox);
        retirar = findViewById(R.id.Retirar_checkBox);
        salvar = findViewById(R.id.salvarAtualizaReserva_btn);
        cancelar = findViewById(R.id.cancelarAtualizaReserva_btn);
        reservaIds = new ArrayList<>();

        verificarUsuarioLogado();
        carregarSpinner();
        Salvar();
        Cancelar();
    }

    private void verificarUsuarioLogado() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "Usuário não está logado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void Salvar(){
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarReserva();
            }
        });
    }

    private void Cancelar(){
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Atualizar.this, Menu_tela.class);
                startActivity(intent);
            }
        });
    }

    private void atualizarReserva() {
        int selectedPosition = selecionarReservaSpinner.getSelectedItemPosition();
        if (selectedPosition != Spinner.INVALID_POSITION) {
            String reservaId = reservaIds.get(selectedPosition);
            String novoValorString = valor.getText().toString();
            if (!novoValorString.isEmpty()) {
                try {
                    if (adicionar.isChecked()) {
                        novoValorString = String.valueOf(Double.parseDouble(valorAtual.getText().toString()) + Double.parseDouble(novoValorString));
                    } else if (retirar.isChecked()) {
                        novoValorString = String.valueOf(Double.parseDouble(valorAtual.getText().toString()) - Double.parseDouble(novoValorString));
                    }
                    db.collection("Reservas").document(reservaId)
                            .update("Valor", novoValorString)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Atualizar.this, "Reserva atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Atualizar.this, "Erro ao atualizar reserva", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } catch (NumberFormatException e) {
                    Toast.makeText(Atualizar.this, "Valor inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Atualizar.this, "Por favor, insira um valor", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Atualizar.this, "Selecione uma reserva", Toast.LENGTH_SHORT).show();
        }
    }

    private void carregarSpinner() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("Reservas")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> reservas = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nomeReservas = document.getString("Nome");
                                if (nomeReservas != null) {
                                    reservas.add(nomeReservas);
                                    reservaIds.add(document.getId());
                                } else {
                                    Toast.makeText(Atualizar.this, "Campo 'Nome' não encontrado em um documento", Toast.LENGTH_SHORT).show();
                                }
                            }

                            if (!reservas.isEmpty()) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(Atualizar.this, android.R.layout.simple_spinner_item, reservas);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                selecionarReservaSpinner.setAdapter(adapter);
                                selecionarReservaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        String selectedReservaId = reservaIds.get(position);
                                        carregarValorReserva(selectedReservaId);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else {
                                Toast.makeText(Atualizar.this, "Nenhuma reserva encontrada", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Atualizar.this, "Erro ao carregar as reservas", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void carregarValorReserva(String reservaId) {
        db.collection("Reservas").document(reservaId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Object valorObj = document.get("Valor");
                        if (valorObj != null) {
                            String valorString = valorObj.toString();
                            double valor = Double.parseDouble(valorString);
                            DecimalFormat df = new DecimalFormat("#0.00");
                            valorString = df.format(valor);
                            valorAtual.setText(valorString);
                        } else {
                            valorAtual.setText("");
                            Toast.makeText(Atualizar.this, "Campo 'Valor' não encontrado no documento", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        valorAtual.setText("");
                        Toast.makeText(Atualizar.this, "Documento não encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Atualizar.this, "Erro ao carregar o valor da reserva", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
