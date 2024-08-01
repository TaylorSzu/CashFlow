package com.example.projeto.entidades;

public class Receitas {

    private String id;
    private String descricao;
    private String tipo;
    private String valor;

    public Receitas(String id, String descricao, String tipo, String valor) {
        this.id = id;
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;
    }

    public Receitas(String descricao, String tipo, String valor) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }
}
