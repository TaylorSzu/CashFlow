package com.example.projeto.recursos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.projeto.R;
import com.example.projeto.entidades.Reservas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReservaAdapter extends ArrayAdapter<Reservas> {

    private List<Reservas> reservasList;
    private int layoutResource;
    private Context mContext;
    private FirebaseFirestore db;

    public ReservaAdapter(@NonNull Context context, int resource, @NonNull List<Reservas> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.layoutResource = resource;
        this.reservasList = objects;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(layoutResource, null);
            holder = new ViewHolder();
            holder.textViewNome = view.findViewById(R.id.textViewNome);
            holder.textViewValor = view.findViewById(R.id.textViewValor);
            holder.btnExcluir = view.findViewById(R.id.btnExcluir);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Reservas reserva = reservasList.get(position);
        holder.textViewNome.setText(reserva.getNome());
        holder.textViewValor.setText(String.valueOf(reserva.getValor()));

        holder.btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirReserva(reserva.getId());
            }
        });

        return view;
    }

    private static class ViewHolder {
        TextView textViewNome;
        TextView textViewValor;
        Button btnExcluir;
    }

    private void excluirReserva(String id) {
        db.collection("Reservas").document(id).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "Reserva excluída com sucesso", Toast.LENGTH_SHORT).show();
                            // Se necessário, pode-se adicionar uma interface para notificar a atividade após a exclusão
                        } else {
                            Toast.makeText(mContext, "Erro ao excluir reserva" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

