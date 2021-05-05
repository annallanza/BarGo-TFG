package com.example.bargo.UsuariConsumidor.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bargo.Consumidor;
import com.example.bargo.R;
import com.example.bargo.UsuariConsumidor.Model.EstablimentInfo;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoEstablimentActivity extends AppCompatActivity {

    private TextView nomEstabliment;
    private CircleImageView imatge;
    private TextView direccio;
    private TextView descripcio;
    private TextView horari;
    private TextView paginaWeb;
    private TextView paginaWeb_;
    private TextView ocupacioInterior;
    private TextView ocupacioExterior;
    private TextView ocupacioExterior_;
    Button reservar;

    private ProgressDialog progressDialog;
    private final Consumidor consumidor = Consumidor.getInstance();
    private final EstablimentInfo establimentInfo = EstablimentInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_establiment);

        nomEstabliment = findViewById(R.id.nomEstabliment);
        imatge = findViewById(R.id.imatge);
        direccio = findViewById(R.id.direccio);
        descripcio = findViewById(R.id.descripcio);
        horari = findViewById(R.id.horario2);
        paginaWeb = findViewById(R.id.paginaWeb);
        paginaWeb_ = findViewById(R.id.paginaweb);
        ocupacioInterior = findViewById(R.id.ocupacioInterior);
        ocupacioExterior = findViewById(R.id.ocupacioExterior);
        ocupacioExterior_ = findViewById(R.id.exterior);
        reservar = findViewById(R.id.reservaButton);

        progressDialog = new ProgressDialog(InfoEstablimentActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        paginaWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri link = Uri.parse(paginaWeb.getText().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, link);
                startActivity(intent);
            }
        });

        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoEstablimentActivity.this, ReservarActivity.class);
                startActivity(intent);
                //TODO: cal fer finish??
            }
        });
    }

    public void onResume() {
        super.onResume();
        GetInfoEstablimentRequest();
    }

    private void refrescarImatge() {
        byte[] imatgeBytes = establimentInfo.getImatge();
        if(imatgeBytes != null){
            Bitmap bmp = BitmapFactory.decodeByteArray(imatgeBytes, 0, imatgeBytes.length);
            imatge.setImageBitmap(bmp);
        }
        else
            imatge.setImageResource(R.drawable.logo);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refrescarDadesEstabliment() {

        nomEstabliment.setText(establimentInfo.getNom());
        direccio.setText(establimentInfo.getDireccio());
        refrescarImatge();

        descripcio.setText(establimentInfo.getDescripcio());
        horari.setText(establimentInfo.getHorari());

        if(establimentInfo.getPaginaWeb() != null) {
            paginaWeb.setVisibility(View.VISIBLE);
            paginaWeb_.setVisibility(View.VISIBLE);
            paginaWeb.setText(establimentInfo.getPaginaWeb());
        }
        else {
            paginaWeb.setVisibility(View.GONE);
            paginaWeb_.setVisibility(View.GONE);
        }

        String ocuInterior = establimentInfo.getOcupacioInterior();
        if(ocuInterior.equals("Lleno")){
            ocupacioInterior.setText(ocuInterior);
            ocupacioInterior.setTextColor(Color.parseColor("#cb3234"));
        }
        else if(ocuInterior.equals("Medio_lleno")){
            ocupacioInterior.setText("Medio lleno");
            ocupacioInterior.setTextColor(Color.parseColor("#e5be01"));
        }
        else{
            ocupacioInterior.setText("Vacío");
            ocupacioInterior.setTextColor(Color.parseColor("#317f43"));
        }

        if(establimentInfo.getExterior()){
            ocupacioExterior.setVisibility(View.VISIBLE);
            ocupacioExterior_.setVisibility(View.VISIBLE);
            String ocuExterior = establimentInfo.getOcupacioExterior();
            if (ocuExterior.equals("Lleno")) {
                ocupacioExterior.setText(ocuExterior);
                ocupacioExterior.setTextColor(Color.parseColor("#cb3234"));
            } else if (ocuExterior.equals("Medio_lleno")) {
                ocupacioExterior.setText("Medio lleno");
                ocupacioExterior.setTextColor(Color.parseColor("#e5be01"));
            } else {
                ocupacioExterior.setText("Vacío");
                ocupacioExterior.setTextColor(Color.parseColor("#317f43"));
            }
        }
        else {
            ocupacioExterior.setVisibility(View.GONE);
            ocupacioExterior_.setVisibility(View.GONE);
        }
    }

    private void GetInfoEstablimentRequest() {
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "establiments/" + establimentInfo.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            long id = response.getLong("id");
                            String nom = response.getString("nom");
                            String direccio = response.getString("direccio");
                            String imatge = response.getString("imatge");
                            String descripcio = response.getString("descripcio");
                            String horari = response.getString("horari");
                            String paginaWeb = response.getString("paginaWeb");
                            Boolean exterior = response.getBoolean("exterior");
                            String ocupacioInterior = response.getString("ocupacioInterior");
                            String ocupacioExterior = response.getString("ocupacioExterior");

                            establimentInfo.setId(id);
                            establimentInfo.setNom(nom);
                            establimentInfo.setDireccio(direccio);
                            if(!imatge.equals("null")){
                                byte[] bytesimatge = Base64.getDecoder().decode(imatge);
                                establimentInfo.setImatge(bytesimatge);
                            }
                            else establimentInfo.setImatge(null);

                            establimentInfo.setDescripcio(descripcio);
                            establimentInfo.setHorari(horari);

                            if(paginaWeb.equals("null"))
                                paginaWeb = null;

                            establimentInfo.setPaginaWeb(paginaWeb);
                            establimentInfo.setExterior(exterior);
                            establimentInfo.setOcupacioInterior(ocupacioInterior);
                            establimentInfo.setOcupacioExterior(ocupacioExterior);

                            refrescarDadesEstabliment();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 404) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        String missatgeError = data.getString("missatge");

                        Toast.makeText(getApplicationContext(), missatgeError, Toast.LENGTH_LONG).show();

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(error.networkResponse.statusCode == 401)
                    Toast.makeText(getApplicationContext(), "No se indica el token o no es válido o el id no es el asociado al token", Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + consumidor.getToken());
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}
