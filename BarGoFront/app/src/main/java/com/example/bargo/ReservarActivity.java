package com.example.bargo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ReservarActivity extends AppCompatActivity implements View.OnClickListener{
    Button boton1, boton2, boton3, boton4, boton5, boton6, boton7, boton8, boton9, boton10, boton11, boton12, boton13, boton14, reservar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservar_activity);

        boton1 = findViewById(R.id.button1);
        boton2 = findViewById(R.id.button2);
        boton3 = findViewById(R.id.button3);
        boton4 = findViewById(R.id.button4);
        boton5 = findViewById(R.id.button5);
        boton6 = findViewById(R.id.button6);
        boton7 = findViewById(R.id.button7);
        boton8 = findViewById(R.id.button8);
        boton9 = findViewById(R.id.button9);
        boton10 = findViewById(R.id.button10);
        boton11 = findViewById(R.id.button11);
        boton12 = findViewById(R.id.button12);
        boton13 = findViewById(R.id.button13);
        boton14 = findViewById(R.id.button14);
        reservar = findViewById(R.id.buttonReservar);

        boton1.setOnClickListener(this);
        boton2.setOnClickListener(this);
        boton3.setOnClickListener(this);
        boton4.setOnClickListener(this);
        boton5.setOnClickListener(this);
        boton6.setOnClickListener(this);
        boton7.setOnClickListener(this);
        boton8.setOnClickListener(this);
        boton9.setOnClickListener(this);
        boton10.setOnClickListener(this);
        boton11.setOnClickListener(this);
        boton12.setOnClickListener(this);
        boton13.setOnClickListener(this);
        boton14.setOnClickListener(this);
        reservar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int color1 = boton1.getTextColors().getDefaultColor();
        int color2 = boton2.getTextColors().getDefaultColor();
        int color3 = boton3.getTextColors().getDefaultColor();
        int color4 = boton4.getTextColors().getDefaultColor();
        int color5 = boton5.getTextColors().getDefaultColor();
        int color6 = boton6.getTextColors().getDefaultColor();
        int color7 = boton7.getTextColors().getDefaultColor();
        int color8 = boton8.getTextColors().getDefaultColor();
        int color9 = boton9.getTextColors().getDefaultColor();
        int color10 = boton10.getTextColors().getDefaultColor();
        int color11 = boton11.getTextColors().getDefaultColor();
        int color12 = boton12.getTextColors().getDefaultColor();
        int color13 = boton13.getTextColors().getDefaultColor();
        int color14 = boton14.getTextColors().getDefaultColor();

        switch (v.getId()){
            case R.id.button1:
                if (color1 == Color.WHITE){
                    boton1.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    boton1.setTextColor(Color.BLACK);
                } else {
                    boton1.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
                    boton1.setTextColor(Color.WHITE);
                    if(color2 == Color.WHITE){
                        boton2.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton2.setTextColor(Color.BLACK);
                    }
                    else if (color3 == Color.WHITE) {
                        boton3.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton3.setTextColor(Color.BLACK);
                    }
                    else if (color4 == Color.WHITE){
                        boton4.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton4.setTextColor(Color.BLACK);
                    }
                }
                break;
            case R.id.button2:
                if (color2 == Color.WHITE){
                    boton2.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    boton2.setTextColor(Color.BLACK);
                } else {
                    boton2.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
                    boton2.setTextColor(Color.WHITE);
                    if(color1 == Color.WHITE){
                        boton1.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton1.setTextColor(Color.BLACK);
                    }
                    else if (color3 == Color.WHITE) {
                        boton3.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton3.setTextColor(Color.BLACK);
                    }
                    else if (color4 == Color.WHITE){
                        boton4.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton4.setTextColor(Color.BLACK);
                    }
                }
                break;
            case R.id.button3:
                if (color3 == Color.WHITE){
                    boton3.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    boton3.setTextColor(Color.BLACK);
                } else {
                    boton3.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
                    boton3.setTextColor(Color.WHITE);
                    if(color2 == Color.WHITE){
                        boton2.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton2.setTextColor(Color.BLACK);
                    }
                    else if (color1 == Color.WHITE) {
                        boton1.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton1.setTextColor(Color.BLACK);
                    }
                    else if (color4 == Color.WHITE){
                        boton4.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton4.setTextColor(Color.BLACK);
                    }
                }
                break;
            case R.id.button4:
                if (color4 == Color.WHITE){
                    boton4.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    boton4.setTextColor(Color.BLACK);
                } else {
                    boton4.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
                    boton4.setTextColor(Color.WHITE);
                    if(color2 == Color.WHITE){
                        boton2.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton2.setTextColor(Color.BLACK);
                    }
                    else if (color3 == Color.WHITE) {
                        boton3.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton3.setTextColor(Color.BLACK);
                    }
                    else if (color1 == Color.WHITE){
                        boton1.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton1.setTextColor(Color.BLACK);
                    }
                }
                break;
            case R.id.button5:
                if (color5 == Color.WHITE){
                    boton5.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    boton5.setTextColor(Color.BLACK);
                } else {
                    boton5.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
                    boton5.setTextColor(Color.WHITE);
                    if(color6 == Color.WHITE){
                        boton6.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton6.setTextColor(Color.BLACK);
                    }
                    else if (color7 == Color.WHITE) {
                        boton7.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton7.setTextColor(Color.BLACK);
                    }
                    else if (color8 == Color.WHITE){
                        boton8.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton8.setTextColor(Color.BLACK);
                    }
                }
                break;
            case R.id.button6:
                if (color6 == Color.WHITE){
                    boton6.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    boton6.setTextColor(Color.BLACK);
                } else {
                    boton6.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
                    boton6.setTextColor(Color.WHITE);
                    if(color5 == Color.WHITE){
                        boton5.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton5.setTextColor(Color.BLACK);
                    }
                    else if (color7 == Color.WHITE) {
                        boton7.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton7.setTextColor(Color.BLACK);
                    }
                    else if (color8 == Color.WHITE){
                        boton8.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton8.setTextColor(Color.BLACK);
                    }
                }
                break;
            case R.id.button7:
                if (color7 == Color.WHITE){
                    boton7.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    boton7.setTextColor(Color.BLACK);
                } else {
                    boton7.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
                    boton7.setTextColor(Color.WHITE);
                    if(color6 == Color.WHITE){
                        boton6.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton6.setTextColor(Color.BLACK);
                    }
                    else if (color5 == Color.WHITE) {
                        boton5.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton5.setTextColor(Color.BLACK);
                    }
                    else if (color8 == Color.WHITE){
                        boton8.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton8.setTextColor(Color.BLACK);
                    }
                }
                break;
            case R.id.button8:
                if (color8 == Color.WHITE){
                    boton8.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    boton8.setTextColor(Color.BLACK);
                } else {
                    boton8.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
                    boton8.setTextColor(Color.WHITE);
                    if(color6 == Color.WHITE){
                        boton6.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton6.setTextColor(Color.BLACK);
                    }
                    else if (color7 == Color.WHITE) {
                        boton7.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton7.setTextColor(Color.BLACK);
                    }
                    else if (color5 == Color.WHITE){
                        boton5.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton5.setTextColor(Color.BLACK);
                    }
                }
                break;
            case R.id.button9:
                if (color9 == Color.WHITE){
                    boton9.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    boton9.setTextColor(Color.BLACK);
                } else {
                    boton9.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
                    boton9.setTextColor(Color.WHITE);
                    if(color10 == Color.WHITE){
                        boton10.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton10.setTextColor(Color.BLACK);
                    }
                    else if (color11 == Color.WHITE) {
                        boton11.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton11.setTextColor(Color.BLACK);
                    }
                    else if (color12 == Color.WHITE){
                        boton12.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton12.setTextColor(Color.BLACK);
                    }
                }
                break;
            case R.id.button10:
                if (color10 == Color.WHITE){
                    boton10.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    boton10.setTextColor(Color.BLACK);
                } else {
                    boton10.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
                    boton10.setTextColor(Color.WHITE);
                    if(color9 == Color.WHITE){
                        boton9.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton9.setTextColor(Color.BLACK);
                    }
                    else if (color11 == Color.WHITE) {
                        boton11.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton11.setTextColor(Color.BLACK);
                    }
                    else if (color12 == Color.WHITE){
                        boton12.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton12.setTextColor(Color.BLACK);
                    }
                }
                break;
            case R.id.button11:
                if (color11 == Color.WHITE){
                    boton11.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    boton11.setTextColor(Color.BLACK);
                } else {
                    boton11.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
                    boton11.setTextColor(Color.WHITE);
                    if(color10 == Color.WHITE){
                        boton10.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton10.setTextColor(Color.BLACK);
                    }
                    else if (color9 == Color.WHITE) {
                        boton9.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton9.setTextColor(Color.BLACK);
                    }
                    else if (color12 == Color.WHITE){
                        boton12.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton12.setTextColor(Color.BLACK);
                    }
                }
                break;
            case R.id.button12:
                if (color12 == Color.WHITE){
                    boton12.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    boton12.setTextColor(Color.BLACK);
                } else {
                    boton12.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
                    boton12.setTextColor(Color.WHITE);
                    if(color10 == Color.WHITE){
                        boton10.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton10.setTextColor(Color.BLACK);
                    }
                    else if (color11 == Color.WHITE) {
                        boton11.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton11.setTextColor(Color.BLACK);
                    }
                    else if (color9 == Color.WHITE){
                        boton9.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton9.setTextColor(Color.BLACK);
                    }
                }
                break;
            case R.id.button13:
                if (color13 == Color.WHITE){
                    boton13.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    boton13.setTextColor(Color.BLACK);
                } else {
                    boton13.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
                    boton13.setTextColor(Color.WHITE);
                    if(color14 == Color.WHITE){
                        boton14.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton14.setTextColor(Color.BLACK);
                    }
                }
                break;
            case R.id.button14:
                if (color14 == Color.WHITE){
                    boton14.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    boton14.setTextColor(Color.BLACK);
                } else {
                    boton14.setBackgroundColor(getResources().getColor(R.color.colorBarGo));
                    boton14.setTextColor(Color.WHITE);
                    if(color13 == Color.WHITE){
                        boton13.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        boton13.setTextColor(Color.BLACK);
                    }
                }
                break;
            case R.id.buttonReservar:
                if((color1 == Color.WHITE || color2 == Color.WHITE || color3 == Color.WHITE || color4 == Color.WHITE) &&
                        (color5 == Color.WHITE || color6 == Color.WHITE || color7 == Color.WHITE || color8 == Color.WHITE) &&
                        (color9 == Color.WHITE || color10 == Color.WHITE || color11 == Color.WHITE || color12 == Color.WHITE) &&
                        (color13 == Color.WHITE || color14 == Color.WHITE)){
                    String dia = null, hora = null, pers = null, interior = null;
                    if (color1 == Color.WHITE){
                        dia = "Miércoles 25 nov";
                    } if (color2 == Color.WHITE){
                        dia = "Jueves 26 nov";
                    } if (color3 == Color.WHITE){
                        dia = "Viernes 27 nov";
                    } if (color4 == Color.WHITE){
                        dia = "Sábado 28 nov";
                    } if (color5 == Color.WHITE){
                        hora = "13.00h";
                    } if (color6 == Color.WHITE){
                        hora = "13.30h";
                    } if (color7 == Color.WHITE){
                        hora = "14.00h";
                    } if (color8 == Color.WHITE){
                        hora = "14.30h";
                    } if (color9 == Color.WHITE){
                        pers = "1 persona";
                    } if (color10 == Color.WHITE){
                        pers = "2 personas";
                    } if (color11 == Color.WHITE){
                        pers = "3 personas";
                    } if (color12 == Color.WHITE){
                        pers = "4 personas";
                    } if (color13 == Color.WHITE){
                        interior = "el interior";
                    } if (color14 == Color.WHITE){
                        interior = "la terraza";
                    }
                    MisReservasInfo.getInstance().addReserva(dia, hora, pers, interior);
                    new AlertDialog.Builder(this)
                         .setTitle("Reserva realizada")
                         .setMessage("Su reserva para el Bar Casa Pepe ha sido realizada correctamente.\n \nEn caso de no poder acudir, por favor no se olvide de cancelar su reserva.")
                         .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 openListBar();
                             }
                         })
                         .show();
                } else{
                    Toast.makeText(getApplicationContext(), "Seleccione una opción para cada campo", Toast.LENGTH_LONG).show();
                }
        }
    }

    private void openListBar() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}