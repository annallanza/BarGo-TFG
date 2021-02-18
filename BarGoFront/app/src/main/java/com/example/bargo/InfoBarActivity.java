package com.example.bargo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InfoBarActivity extends AppCompatActivity {

    Button reservar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infobar);
        final int points = User.getInstance().getPoints();
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
                    Intent intent = new Intent(InfoBarActivity.this, ReservarActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
