package com.example.bargo.UsuariPropietari.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bargo.Propietari;
import com.example.bargo.R;
import com.example.bargo.UsuariConsumidor.Activity.MainActivity;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NouEsdevenimentActivity extends AppCompatActivity {

    private EditText nom;
    private EditText dia;
    private EditText hora;
    private Button crear;

    private ProgressDialog progressDialog;
    private final Propietari propietari = Propietari.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nou_esdeveniment);

        progressDialog = new ProgressDialog(NouEsdevenimentActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        nom = findViewById(R.id.nomNouEsdeveniment);
        dia = findViewById(R.id.diaNouEsdeveniment);
        hora = findViewById(R.id.horaNouEsdeveniment);
        crear = findViewById(R.id.crearNouEsdeveniment);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crear.setEnabled(false);
                progressDialog.show();

                String horaEsdeveniment = hora.getText().toString();
                if(horaEsdeveniment.length() <= 5)
                    horaEsdeveniment += ":00";

                CreateEsdeveniment(nom.getText().toString(), dia.getText().toString(), horaEsdeveniment);
            }
        });
    }

    public void onDataClicked(View view){
        final EditText dataEditTextSegonsID = findViewById(view.getId());

        final Calendar calendar = Calendar.getInstance();

        final int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int any = calendar.get(Calendar.YEAR);

        System.out.println("MES: " + mes);

        DatePickerDialog datePickerDialog = new DatePickerDialog(NouEsdevenimentActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ++monthOfYear;
                if(dayOfMonth < 10) {
                    if(monthOfYear < 10)
                        dataEditTextSegonsID.setText("0" + dayOfMonth + "/0" + monthOfYear + "/" + year);
                    else
                        dataEditTextSegonsID.setText("0" + dayOfMonth + "/" + monthOfYear + "/" + year);
                }
                else if(monthOfYear < 10)
                    dataEditTextSegonsID.setText(dayOfMonth + "/0" + monthOfYear + "/" + year);
                else
                    dataEditTextSegonsID.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }
        }, dia, mes, any);

        datePickerDialog.updateDate(any,mes,dia);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    public void onHorariClicked(View view){
        final EditText horariEditTextSegonsID = findViewById(view.getId());

        final Calendar calendar = Calendar.getInstance();

        final int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuts = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(NouEsdevenimentActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay < 10) {
                    if(minute < 10)
                        horariEditTextSegonsID.setText("0" + hourOfDay + ":0" + minute);
                    else
                        horariEditTextSegonsID.setText("0" + hourOfDay + ":" + minute);
                }
                else {
                    if(minute < 10)
                        horariEditTextSegonsID.setText(hourOfDay + ":0" + minute);
                    else
                        horariEditTextSegonsID.setText(hourOfDay + ":" + minute);
                }
            }
        }, hora, minuts, false);

        timePickerDialog.updateTime(hora,minuts);
        timePickerDialog.show();
    }

    public void CreateEsdeveniment(final String nomEsdeveniment, final String diaEsdeveniment, final String horaEsdeveniment){
        String url = VariablesGlobals.getUrlAPI() + "esdeveniments/";

        JSONObject postData = new JSONObject();
        try {
            postData.put("id", propietari.getId());
            postData.put("nom", nomEsdeveniment);
            postData.put("dia", diaEsdeveniment);
            postData.put("hora", horaEsdeveniment);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String missatge = response.getString("missatge");

                        Toast.makeText(getApplicationContext(), missatge, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    crear.setEnabled(true);
                    progressDialog.dismiss();

                    finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error.networkResponse.statusCode == 400 || error.networkResponse.statusCode == 404 || error.networkResponse.statusCode == 409) {
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

                    crear.setEnabled(true);
                    progressDialog.dismiss();
                }
            }
        ){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + propietari.getToken());
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}