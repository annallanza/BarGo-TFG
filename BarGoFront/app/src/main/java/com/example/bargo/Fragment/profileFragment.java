package com.example.bargo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.bargo.Activity.ListProductActivity;
import com.example.bargo.Activity.MisReservasActivity;
import com.example.bargo.Model.User;
import com.example.bargo.R;
import com.example.bargo.Activity.RetosActivity;

public class profileFragment extends Fragment {

    private TextView points;
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button mis_reservas = (Button) view.findViewById(R.id.misReservas_button);

        TextView userName = (TextView) view.findViewById(R.id.user_name_profile);
        userName.setText(User.getInstance().getName());

        refreshPoints();

        mis_reservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mis_reservas_activity = new Intent(getActivity(), MisReservasActivity.class);
                startActivity(mis_reservas_activity);
            }
        });

        Button list_events = (Button) view.findViewById(R.id.exchangeBttn);
        list_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent list_products_activity = new Intent(getActivity(), ListProductActivity.class);
                startActivity(list_products_activity);
            }
        });

        Button retos = (Button) view.findViewById(R.id.makeChallengeBttn);
        retos.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent retos_activity = new Intent(getActivity(), RetosActivity.class);
                startActivity(retos_activity);
            }
        });

        return view;
    }

    private void refreshPoints(){
        TextView points = (TextView) view.findViewById(R.id.points);
        int pts = User.getInstance().getPoints();
        if (pts < 0) pts = 0;
        String p = String.valueOf(pts);
        points.setText(p + " " + getString(R.string.points));
    }

    public void onResume() {
        refreshPoints();
        super.onResume();
    }
    
}
