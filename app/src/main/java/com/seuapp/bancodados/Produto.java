package com.seuapp.bancodados;

import java.util.Locale;

public class Produto {

    private long id;
    private String nome;
    private double preco;

    public Produto(long id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public long getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }

    @Override
    public String toString() {
        // Ex.: "Caneta Azul - R$ 2,50"
        return nome + " - R$ " + String.format(new Locale("pt", "BR"), "%.2f", preco);
    }
}
