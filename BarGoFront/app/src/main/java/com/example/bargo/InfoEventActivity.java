package com.example.bargo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class InfoEventActivity extends AppCompatActivity {

    Button mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoevent);

        mapa = (Button) findViewById(R.id.vermapa);

        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(InfoEventActivity.this, //);
                //startActivity(intent);
            }
        });
    }
}
