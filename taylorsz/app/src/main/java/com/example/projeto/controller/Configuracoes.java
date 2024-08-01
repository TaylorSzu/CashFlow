package com.example.projeto.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto.R;

public class Configuracoes extends AppCompatActivity {

    private Button suporte_btn;
    private Button enviar_btn;
    private RatingBar ratingBarFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracoes);

        ratingBarFeedback = findViewById(R.id.ratingBarFeedback);
        suporte_btn = findViewById(R.id.buttonSupport);
        enviar_btn = findViewById(R.id.enviar_btn);

        Suporte();
        avaliar();
    }

    private void Suporte(){
        suporte_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://forms.gle/FsfU98ogZx2EHJBE7";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private void avaliar(){
        enviar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBarFeedback.getRating();
                enviarFeedback(rating);
            }
        });
    }

    private void enviarFeedback(float rating){
        String subject = "Feedback do App";
        String message = "Avaliação do usuário: " + rating;

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:taylorsz2006@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar feedback..."));
            Toast.makeText(this, "Escolha um cliente de e-mail para enviar o feedback.", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Não há clientes de e-mail instalados.", Toast.LENGTH_SHORT).show();
        }
    }
}
