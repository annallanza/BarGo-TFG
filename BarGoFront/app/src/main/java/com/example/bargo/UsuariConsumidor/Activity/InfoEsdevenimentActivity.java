package com.example.bargo.UsuariConsumidor.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
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
import com.example.bargo.UsuariConsumidor.Model.EsdevenimentInfo;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InfoEsdevenimentActivity extends AppCompatActivity {

    private TextView nomEsdeveniment;
    private TextView nomEstabliment;
    private TextView direccio;
    private TextView fecha;
    private TextView hora;

    private ProgressDialog progressDialog;
    private final Consumidor consumidor = Consumidor.getInstance();
    private final EsdevenimentInfo esdevenimentInfo = EsdevenimentInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_esdeveniment);

        nomEsdeveniment = findViewById(R.id.nomEsdeveniment);
        nomEstabliment = findViewById(R.id.NomEstabliment);
        direccio = findViewById(R.id.direccion);
        fecha = findViewById(R.id.fechaEvento);
        hora = findViewById(R.id.horaEvento);

        progressDialog = new ProgressDialog(InfoEsdevenimentActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
    }

    public void onResume() {
        super.onResume();
        GetInfoEstablimentRequest();
    }

    private void refrescarDadesEsdeveniment() {

        nomEsdeveniment.setText(esdevenimentInfo.getNom());
        nomEstabliment.setText(esdevenimentInfo.getNomEstabliment());
        direccio.setText(esdevenimentInfo.getDireccio());
        fecha.setText(esdevenimentInfo.getDia());
        hora.setText(esdevenimentInfo.getHora());
    }

    private void GetInfoEstablimentRequest() {
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "esdeveniments/" + esdevenimentInfo.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SimpleDateFormat")
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            long id = response.getLong("id");
                            String nom = response.getString("nom");
                            String nomEstabliment = response.getString("nomEstabliment");
                            String direccio = response.getString("direccio");
                            String dia = response.getString("dia");
                            String hora = response.getString("hora");

                            esdevenimentInfo.setId(id);
                            esdevenimentInfo.setNom(nom);
                            esdevenimentInfo.setNomEstabliment(nomEstabliment);
                            esdevenimentInfo.setDireccio(direccio);

                            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(dia);
                            dia = new SimpleDateFormat("dd/MM/yyyy").format(fecha);

                            esdevenimentInfo.setDia(dia);

                            hora = hora.substring(0, hora.length() - 3);
                            esdevenimentInfo.setHora(hora);

                            refrescarDadesEsdeveniment();

                        } catch (JSONException | ParseException e) {
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
