package com.example.projeto.entidades;

public class Reservas {
    private String Nome;
    private String Valor;
    private String Id;

    public Reservas() {
        // necessário para o Firebase
    }

    public Reservas(String nome, String valor, String id) {
        Nome = nome;
        Valor = valor;
        Id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
