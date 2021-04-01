package com.example.bargo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bargo.Activity.ListProductActivity;
import com.example.bargo.Activity.MisReservasActivity;
import com.example.bargo.Model.User;
import com.example.bargo.Model.VariablesGlobals;
import com.example.bargo.R;
import com.example.bargo.Activity.RetosActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class profileFragment extends Fragment {

    private Button reserves;
    private Button llista_productes;
    private Button reptes;
    private TextView nomUsuariTextView;
    private TextView puntuacio;
    private final User usuari = User.getInstance();
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        reserves = view.findViewById(R.id.misReservas_button);
        nomUsuariTextView = view.findViewById(R.id.user_name_profile);
        llista_productes = view.findViewById(R.id.exchangeBttn);
        reptes = view.findViewById(R.id.makeChallengeBttn);
        puntuacio = view.findViewById(R.id.points);

        nomUsuariTextView.setText(User.getInstance().getNom());

        refreshPoints();

        reserves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mis_reservas_activity = new Intent(getActivity(), MisReservasActivity.class);
                startActivity(mis_reservas_activity);
            }
        });


        llista_productes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent list_products_activity = new Intent(getActivity(), ListProductActivity.class);
                startActivity(list_products_activity);
            }
        });

        reptes.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent retos_activity = new Intent(getActivity(), RetosActivity.class);
                startActivity(retos_activity);
            }
        });

        return view;
    }

    private void refreshPoints(){
        int pts = User.getInstance().getPuntuacio();
        if (pts < 0) pts = 0;
        String p = String.valueOf(pts);
        puntuacio.setText(p + " " + getString(R.string.points));
    }

    public void onResume() {
        GetPuntuacioConsumidor();
        super.onResume();
    }

    private void GetPuntuacioConsumidor() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = VariablesGlobals.getUrlAPI() + "consumidors/" + usuari.getId();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int puntuacio = Integer.parseInt(response.getString("puntuacio"));

                            User usuari = User.getInstance();
                            usuari.setPuntuacio(puntuacio);

                            refreshPoints();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse.statusCode == 404) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                String missatgeError = data.getString("missatge");

                                Toast.makeText(getActivity(), missatgeError, Toast.LENGTH_LONG).show();

                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(error.networkResponse.statusCode == 401)
                            Toast.makeText(getActivity(), "No se indica el token o no es válido o el id no es el asociado al token", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + usuari.getToken());
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

}
