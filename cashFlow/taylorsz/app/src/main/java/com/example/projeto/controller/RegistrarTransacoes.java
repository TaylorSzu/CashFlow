package com.example.projeto.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegistrarTransacoes extends AppCompatActivity {

    private EditText Descricao;
    private EditText Valor;
    private Spinner Tipo;
    private Button Salvar;
    private Button Cancelar;
    private String[] valores = {"Entrada", "Saída"};
    private TextView TotalView;
    private ArrayAdapter<String> adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_transacao);

        db = FirebaseFirestore.getInstance();

        Descricao = findViewById(R.id.descricaoRegistrarTransacao_txt);
        Valor = findViewById(R.id.valorRegistrarTransacao_txt);
        Tipo = findViewById(R.id.tipoRegistrarTransacao_spinner);
        Salvar = findViewById(R.id.salvarRegistrarTransacoes_btn);
        Cancelar = findViewById(R.id.cancelarRegistrarTransacao_btn);
        TotalView = findViewById(R.id.totalRegistrarTransacoes_label);

        verificarUsuarioLogado();
        mostrarTotal();
        carregarSpinner();
        configurarSalvar();
        configurarCancelar();
    }

    private void configurarSalvar(){
        Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarTransacao();
            }
        });
    }

    private void configurarCancelar(){
        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarTransacoes.this, Menu_tela.class);
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

    private void registrarTransacao(){
        String descricao = Descricao.getText().toString();
        String valor = Valor.getText().toString();
        String tipo = Tipo.getSelectedItem().toString();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(descricao.isEmpty() || valor.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> transacoes = new HashMap<>();
        transacoes.put("Descrição", descricao);
        transacoes.put("Tipo", tipo);
        transacoes.put("Valor", valor);
        transacoes.put("userId", userId);

        db.collection("Receitas").add(transacoes).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Descricao.setText("");
                    Valor.setText("");
                    Tipo.setSelection(0);
                    mostrarTotal();
                    Toast.makeText(RegistrarTransacoes.this, "Transação registrada com sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegistrarTransacoes.this, "Erro ao registrar a transação", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void mostrarTotal() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("Receitas")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            double total = 0;
                            for(DocumentSnapshot document : task.getResult()){
                                String valorStr = document.getString("Valor");
                                String tipo = document.getString("Tipo");
                                if(valorStr != null && tipo != null){
                                    double valor = Double.parseDouble(valorStr);
                                    if(tipo.equals("Entrada")){
                                        total += valor;
                                    } else if(tipo.equals("Saída")){
                                        total -= valor;
                                    }
                                }
                            }
                            TotalView.setText("Total: " + String.format("%.2f", total));
                        } else {
                            Toast.makeText(RegistrarTransacoes.this, "Erro ao obter o total das transações", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void carregarSpinner(){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Tipo.setAdapter(adapter);
        Tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ItemSelecionado = parent.getItemAtPosition(position).toString();
                if(!ItemSelecionado.equals("Entrada") && !ItemSelecionado.equals("Saída")){
                    Toast.makeText(RegistrarTransacoes.this, "Selecione uma opção válida: Entrada ou Saída", Toast.LENGTH_SHORT).show();
                    Tipo.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
