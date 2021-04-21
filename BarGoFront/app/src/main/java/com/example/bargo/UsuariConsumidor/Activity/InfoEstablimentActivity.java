package com.example.bargo.UsuariConsumidor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bargo.Consumidor;
import com.example.bargo.R;
import com.example.bargo.UsuariConsumidor.Model.EstablimentInfo;

public class InfoEstablimentActivity extends AppCompatActivity {

    Button reservar;

    private final EstablimentInfo establimentInfo = EstablimentInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_establiment);
        final int points = Consumidor.getInstance().getPuntuacio();
        reservar = (Button) findViewById(R.id.reservaButton);
        if(points < 0){
            reservar.setBackgroundColor(getResources().getColor(R.color.grey));
        }
        else{
            reservar.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
        }
        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(points < 0){
                    Toast.makeText(getBaseContext(), "No puede reservar, está penalizado.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(InfoEstablimentActivity.this, ReservarActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
