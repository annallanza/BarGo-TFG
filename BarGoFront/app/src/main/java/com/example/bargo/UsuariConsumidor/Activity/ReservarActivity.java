package com.example.bargo.UsuariConsumidor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReservarActivity extends AppCompatActivity {

    private TextView nomEstabliment;
    private CircleImageView imatge;
    private EditText dia;
    private EditText hora;
    private EditText numPersones;
    private TextView textViewExterior;
    private CheckBox interior;
    private CheckBox exterior;
    private Button reservar;

    private ProgressDialog progressDialog;
    private final Consumidor consumidor = Consumidor.getInstance();
    private final EstablimentInfo establimentInfo = EstablimentInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservar_activity);

        progressDialog = new ProgressDialog(ReservarActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        nomEstabliment = findViewById(R.id.nomEstabliment2);
        imatge = findViewById(R.id.circleImageView3);
        dia = findViewById(R.id.diaNovaReserva);
        hora = findViewById(R.id.horaNovaReserva);
        numPersones = findViewById(R.id.NumPersonesNovaReserva);
        textViewExterior = findViewById(R.id.textViewExterior);
        interior = findViewById(R.id.checkBoxInterior);
        exterior = findViewById(R.id.checkBoxExterior);
        reservar = findViewById(R.id.reservaButton2);

        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservar.setEnabled(false);
                progressDialog.show();

                if(numPersones.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Indica el número de personas", Toast.LENGTH_LONG).show();
                    reservar.setEnabled(true);
                    progressDialog.dismiss();
                }
                else if(establimentInfo.getExterior() && !interior.isChecked() && !exterior.isChecked()){
                    Toast.makeText(getApplicationContext(), "Indica la ubicación de la mesa de la reserva", Toast.LENGTH_LONG).show();
                    reservar.setEnabled(true);
                    progressDialog.dismiss();
                }
                else {
                    int num_persones = Integer.parseInt(numPersones.getText().toString());
                    boolean isExterior = exterior.isChecked();
                    String horaReserva = hora.getText().toString();

                    if(horaReserva.length() <= 5)
                        horaReserva += ":00";

                    CreateReserva(dia.getText().toString(), horaReserva, num_persones, isExterior);
                }
            }
        });

    }

    public void onResume() {
        super.onResume();
        refrescarDades();
    }

    public void refrescarDades(){

        nomEstabliment.setText(establimentInfo.getNom());
        refrescarImatge();

        if(establimentInfo.getExterior()){
            textViewExterior.setVisibility(View.VISIBLE);
            interior.setVisibility(View.VISIBLE);
            exterior.setVisibility(View.VISIBLE);
        }
        else {
            textViewExterior.setVisibility(View.GONE);
            interior.setVisibility(View.GONE);
            exterior.setVisibility(View.GONE);
        }
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

    public void onDataClicked(View view){
        final EditText dataEditTextSegonsID = findViewById(view.getId());

        final Calendar calendar = Calendar.getInstance();

        final int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int any = calendar.get(Calendar.YEAR);

        System.out.println("MES: " + mes);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ReservarActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(ReservarActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.checkBoxInterior:
                if(checked)
                    exterior.setChecked(false);
                break;
            case R.id.checkBoxExterior:
                if(checked)
                    interior.setChecked(false);
                break;
        }
    }

    public void Alerta(String diaEsdeveniment, String horaEsdeveniment, int numPersones, boolean exterior) {
        horaEsdeveniment = horaEsdeveniment.substring(0, 5);

        String ubicacio = "interior";
        if(exterior)
            ubicacio = "exterior";

        String persona = " persona";
        if(numPersones > 1)
            persona += "s";

        new AlertDialog.Builder(this)
                .setTitle("Reserva realizada")
                .setMessage("Su reserva en " + establimentInfo.getNom() +  " para el " + diaEsdeveniment + " a las " + horaEsdeveniment + " para " + numPersones + persona + " en el " + ubicacio +
                        " ha sido realizada correctamente.\n \nEn caso de no poder acudir, por favor no se olvide de cancelar su reserva.")
                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    public void CreateReserva(final String diaEsdeveniment, final String horaEsdeveniment, final int numPersones, final boolean exterior){
        String url = VariablesGlobals.getUrlAPI() + "reserves/";

        JSONObject postData = new JSONObject();
        try {
            postData.put("id", consumidor.getId());
            postData.put("idEstabliment", establimentInfo.getId());
            postData.put("dia", diaEsdeveniment);
            postData.put("hora", horaEsdeveniment);
            postData.put("numPersones", numPersones);
            postData.put("exterior", exterior);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    reservar.setEnabled(true);
                    progressDialog.dismiss();

                    Alerta(diaEsdeveniment, horaEsdeveniment, numPersones, exterior);
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                    Toast.makeText(getApplicationContext(), "No se ha podido conectar con el servidor. Comprueba tu conexión a internet.", Toast.LENGTH_LONG).show();
                else if(error.networkResponse != null && (error.networkResponse.statusCode == 400 || error.networkResponse.statusCode == 404 || error.networkResponse.statusCode == 409)) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        String missatgeError = data.getString("missatge");

                        Toast.makeText(getApplicationContext(), missatgeError, Toast.LENGTH_LONG).show();

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(error.networkResponse != null && error.networkResponse.statusCode == 401)
                    Toast.makeText(getApplicationContext(), "No se indica el token o no es válido o el id no es el asociado al token", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Se ha producido un error, vuélvelo a intentar más tarde.", Toast.LENGTH_LONG).show();

                reservar.setEnabled(true);
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
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}