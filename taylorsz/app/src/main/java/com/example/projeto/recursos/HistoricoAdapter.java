package com.example.projeto.recursos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projeto.R;
import com.example.projeto.entidades.Receitas;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class HistoricoAdapter extends BaseAdapter {

    private Context context;
    private List<Receitas> receitas;
    private FirebaseFirestore db;

    public HistoricoAdapter(Context context, List<Receitas> receitas) {
        this.context = context;
        this.receitas = receitas;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public int getCount() {
        return receitas.size();
    }

    @Override
    public Object getItem(int position) {
        return receitas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
        }

        final Receitas receita = receitas.get(position);

        TextView descricao = convertView.findViewById(R.id.text_view_descricao);
        TextView tipo = convertView.findViewById(R.id.text_view_tipo);
        TextView valor = convertView.findViewById(R.id.text_view_valor);
        Button deleteButton = convertView.findViewById(R.id.button_delete);

        descricao.setText("Nome: " + "\n"+ receita.getDescricao());
        tipo.setText("Tipo: " + "\n"+ receita.getTipo());
        valor.setText("Valor: " + "\n"+ receita.getValor());

        deleteButton.setOnClickListener(v -> {
            db.collection("Receitas").document(receita.getId()).delete()
                    .addOnSuccessListener(aVoid -> {
                        receitas.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Receita excluída", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Erro ao excluir receita: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        return convertView;
    }
}
