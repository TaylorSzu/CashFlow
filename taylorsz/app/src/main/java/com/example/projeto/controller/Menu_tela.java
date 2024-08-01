package com.example.projeto.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.projeto.R;

public class Menu_tela extends AppCompatActivity {

    private ImageButton btn_registrarTransacao;
    private ImageButton btn_verReserva;
    private ImageButton btn_historico;
    private ImageButton btn_atualizarReserva;
    private ImageButton btn_novaReserva;
    private ImageButton btn_saldo;
    private ImageButton btn_trocarConta;
    private ImageButton btn_config;
    private ImageButton btn_sair;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_tela);

        btn_registrarTransacao = findViewById(R.id.registrarTransacao_btn);
        btn_verReserva = findViewById(R.id.verReserva_btn);
        btn_historico = findViewById(R.id.historico_btn);
        btn_atualizarReserva = findViewById(R.id.Atualiza_btn);
        btn_novaReserva = findViewById(R.id.novaReserva_btn);
        btn_saldo = findViewById(R.id.saldo_btn);
        btn_sair = findViewById(R.id.Sair_btn);
        btn_trocarConta = findViewById(R.id.trocarConta_btn);
        btn_config = findViewById(R.id.config_btn);

        carregarRegistrarTransacao();
        carregarVerReservas();
        carregarHistorico();
        carregarAtualizarReserva();
        carregarNovaReserva();
        carregarSaldo();
        carregarTrocarConta();
        carregarConfig();
        Sair();
    }

    private void carregarRegistrarTransacao(){
        btn_registrarTransacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_tela.this, RegistrarTransacoes.class);
                startActivity(intent);
            }
        });
    }

    private void carregarVerReservas(){
        btn_verReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_tela.this, VerReserva.class);
                startActivity(intent);
            }
        });
    }

    private void carregarHistorico(){
        btn_historico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Menu_tela.this, Historico.class);
                startActivity(intent);
            }
        });
    }

    private void carregarAtualizarReserva(){
        btn_atualizarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Menu_tela.this, Atualizar.class);
                startActivity(intent);
            }
        });
    }

    private void carregarNovaReserva(){
        btn_novaReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_tela.this, NovaReserva.class);
                startActivity(intent);
            }
        });
    }

    private void carregarSaldo(){
        btn_saldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_tela.this, Saldo.class);
                startActivity(intent);
            }
        });
    }

    private void carregarTrocarConta(){
        btn_trocarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_tela.this, login.class);
                startActivity(intent);
            }
        });
    }
    private void carregarConfig(){
        btn_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_tela.this, Configuracoes.class);
                startActivity(intent);
            }
        });
    }

    private void Sair() {
        btn_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });
    }
}