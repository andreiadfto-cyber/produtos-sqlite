package com.seuapp.bancodados;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editPreco;
    private ListView listaProdutos;
    private ProdutoDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = findViewById(R.id.editNome);
        editPreco = findViewById(R.id.editPreco);
        listaProdutos = findViewById(R.id.listaProdutos);
        Button btnSalvar = findViewById(R.id.btnSalvar);

        dbHelper = new ProdutoDbHelper(this);

        btnSalvar.setOnClickListener(v -> salvarProduto());

        // Carrega o que já está no banco: é isso que comprova a persistência
        atualizarLista();
    }

    private void salvarProduto() {
        String nome = editNome.getText().toString().trim();
        String precoStr = editPreco.getText().toString().trim().replace(',', '.');

        // Regra 1: nome obrigatório, com pelo menos 3 caracteres
        if (nome.length() < 3) {
            Toast.makeText(this, "Nome inválido. Mínimo de 3 caracteres.", Toast.LENGTH_SHORT).show();
            editNome.requestFocus();
            return;
        }

        // Regra 2: preço obrigatório, numérico e maior que zero
        if (precoStr.isEmpty()) {
            Toast.makeText(this, "Preço inválido. Informe um valor maior que zero.", Toast.LENGTH_SHORT).show();
            editPreco.requestFocus();
            return;
        }
        double preco;
        try {
            preco = Double.parseDouble(precoStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Preço inválido. Digite um número.", Toast.LENGTH_SHORT).show();
            editPreco.requestFocus();
            return;
        }
        if (preco <= 0) {
            Toast.makeText(this, "Preço inválido. Informe um valor maior que zero.", Toast.LENGTH_SHORT).show();
            editPreco.requestFocus();
            return;
        }

        // Dados válidos: grava no SQLite e atualiza a lista
        boolean sucesso = dbHelper.inserirProduto(nome, preco);
        if (sucesso) {
            Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show();
            editNome.setText("");
            editPreco.setText("");
            editNome.requestFocus();
            atualizarLista();
        } else {
            Toast.makeText(this, "Erro ao salvar produto.", Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarLista() {
        List<Produto> produtos = dbHelper.listarProdutos();
        ArrayAdapter<Produto> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, produtos);
        listaProdutos.setAdapter(adapter);
    }
}
