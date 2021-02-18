package com.example.bargo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MisReservasActivity extends AppCompatActivity {

    private ListView reservasListView;
    private ArrayList<Integer> barsListImages;
    private ArrayList<String> barsListNames;
    private ArrayList<String> barsListAdresses;
    private ArrayList<String> barsListReservationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reservas);

        barsListImages = MisReservasInfo.getInstance().getImages();
        barsListNames = MisReservasInfo.getInstance().getBars();
        barsListAdresses = MisReservasInfo.getInstance().getAdresses();
        barsListReservationInfo = MisReservasInfo.getInstance().getInfoReserva();

        reservasListView = findViewById(R.id.mis_reservasList);
        AdaptadorMisReservasList adaptador =  new AdaptadorMisReservasList();
        reservasListView.setAdapter(adaptador);



    }

    class AdaptadorMisReservasList extends BaseAdapter {

        @Override
        public int getCount() {
            return barsListImages.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.reserva_item_list, null);

            ImageView barImage = view.findViewById(R.id.barImage);
            TextView barName = view.findViewById(R.id.barNameText);
            TextView barAdress = view.findViewById(R.id.adressText);
            TextView barReservationInfo = view.findViewById(R.id.reservaText);
            Button cancelButton = view.findViewById(R.id.cancelButton);

            barImage.setImageResource(barsListImages.get(position));
            barName.setText(barsListNames.get(position));
            barAdress.setText(barsListAdresses.get(position));
            barReservationInfo.setText(barsListReservationInfo.get(position));

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MisReservasInfo.getInstance().cancelReserva(position);
                    Toast.makeText(MisReservasActivity.this, "Se ha cancelado la reserva", Toast.LENGTH_SHORT).show();
                    Intent mis_reservas_activity = new Intent(MisReservasActivity.this, MisReservasActivity.class);
                    startActivity(mis_reservas_activity);
                    finish();
                }
            });

            return view;
        }
    }
}