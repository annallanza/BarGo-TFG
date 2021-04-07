package com.example.bargo.UsuariConsumidor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bargo.Consumidor;
import com.example.bargo.LoginActivity;
import com.example.bargo.Propietari;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;
import com.example.bargo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;

public class ConfiguracioUsuariActivity extends AppCompatActivity {

    private EditText nomUsuariEditText;
    private EditText contrasenyaActualEditText;
    private EditText contrasenyaNovaEditText;
    private CheckBox veureContrasenya;
    private Button guardarCanvis;
    private TextView logout;
    private TextView eliminarCompte;
    private ProgressDialog progressDialog;
    private Consumidor consumidor = Consumidor.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracio_usuari);

        nomUsuariEditText = findViewById(R.id.editTextUsername2);
        contrasenyaActualEditText = findViewById(R.id.editTextContraActual);
        contrasenyaNovaEditText = findViewById(R.id.editTextContraNova);
        veureContrasenya = findViewById(R.id.checkBoxContraseña3);
        guardarCanvis = findViewById(R.id.buttonAcceder2);
        logout = findViewById(R.id.textViewLogout);
        eliminarCompte = findViewById(R.id.textViewEliminarCuenta);

        progressDialog = new ProgressDialog(ConfiguracioUsuariActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        String textlogout = "¿Quieres cerrar la sesión?";
        SpannableString ssLogout = new SpannableString(textlogout);

        ClickableSpan clickableSpanLogout = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                showConfirmacio("Cerrar sesión", "¿Estás seguro de querer cerrar la sesión?", "logout");
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.colorBarGo));
                ds.setUnderlineText(false);
            }
        };
        ssLogout.setSpan(clickableSpanLogout, 9, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        logout.setText(ssLogout);
        logout.setMovementMethod(LinkMovementMethod.getInstance());

        String text = "¿Quieres eliminar tu cuenta?";
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpanDelete = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                showConfirmacio("Eliminar cuenta", "¿Estás seguro de querer eliminar la cuenta?", "delete");
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.colorBarGo));
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpanDelete, 9, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        eliminarCompte.setText(ss);
        eliminarCompte.setMovementMethod(LinkMovementMethod.getInstance());

        veureContrasenya.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    contrasenyaActualEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    contrasenyaNovaEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    contrasenyaActualEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    contrasenyaNovaEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        guardarCanvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCanvis.setEnabled(false);

                String nomUsuari = nomUsuariEditText.getText().toString();
                String contrasenyaActual = contrasenyaActualEditText.getText().toString();
                String contrasenyaNova = contrasenyaNovaEditText.getText().toString();

                if(contrasenyaActual.equals(consumidor.getContrasenya())) {
                    progressDialog.show();
                    PutInfoConsumidorRequest(nomUsuari, contrasenyaNova);
                }
                else {
                    Toast.makeText(getApplicationContext(), "La contraseña actual no es correcta", Toast.LENGTH_LONG).show();
                    guardarCanvis.setEnabled(true);
                }
            }
        });

    }

    public void onResume() {
        GetInfoConsumidorRequest();
        super.onResume();
    }

    private void showConfirmacio(String titol, String missatge, final String opcio){
        final AlertDialog alertdialog = new AlertDialog.Builder(ConfiguracioUsuariActivity.this)
                .setTitle(titol)
                .setMessage(missatge)
                .setPositiveButton("Si", null)
                .setNegativeButton("No", null)
                .show();
        Button positiveButton = alertdialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog.cancel();
                if(opcio.equals("logout")) {
                    consumidor.setConsumidorNull();
                    obrirLoginActivity();
                }
                else {
                    DeleteConsumidorRequest();
                }
            }
        });
    }

    private void obrirLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void refrescarDadesConsumidor(){
        nomUsuariEditText.setText(consumidor.getNom());
        contrasenyaActualEditText.setText("");
        contrasenyaNovaEditText.setText("");
    }

    private void GetInfoConsumidorRequest() {
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "usuaris/" + consumidor.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        long id = response.getLong("id");
                        String nomUsuari = response.getString("nomUsuari");
                        String imatge = response.getString("imatge");

                        consumidor.setId(id);
                        consumidor.setNom(nomUsuari);
                        if(!imatge.equals("null")){
                            byte[] bytesimatge = imatge.getBytes();
                            consumidor.setImatge(bytesimatge);
                        }
                        else consumidor.setImatge(null);

                        refrescarDadesConsumidor();
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
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void PutInfoConsumidorRequest(final String nomUsuari, final String contrasenyaNova){
        String url = VariablesGlobals.getUrlAPI() + "usuaris/";

        JSONObject postData = new JSONObject();
        try {
            postData.put("id", consumidor.getId());
            postData.put("nomUsuari", nomUsuari);
            postData.put("contrasenya", contrasenyaNova);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loginRequest(nomUsuari, contrasenyaNova);
                        try {
                            String missatge = response.getString("missatge");

                            Toast.makeText(getApplicationContext(), missatge, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                            Toast.makeText(getApplicationContext(), "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();

                        guardarCanvis.setEnabled(true);
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

    private void DeleteConsumidorRequest(){
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "consumidors/" + consumidor.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        consumidor.setConsumidorNull();
                        progressDialog.dismiss();
                        obrirLoginActivity();
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
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void loginRequest(final String nomUsuari, final String contrasenya){

        String url = VariablesGlobals.getUrlAPI() + "usuaris/auth/login";

        JSONObject postData = new JSONObject();
        try {
            postData.put("nomUsuari", nomUsuari);
            postData.put("contrasenya", contrasenya);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String token = response.getString("token");

                        long id = Jwts.parser().setSigningKey(VariablesGlobals.getSecret().getBytes()).parseClaimsJws(token).getBody().get("id", Long.class);
                        ArrayList rols = Jwts.parser().setSigningKey(VariablesGlobals.getSecret().getBytes()).parseClaimsJws(token).getBody().get("rols", ArrayList.class);

                        String rol_usuari = (String) rols.get(0);

                        if(rol_usuari.equals("ROL_CONSUMIDOR")){
                            Consumidor consumidor = Consumidor.getInstance();
                            consumidor.setAll(id,nomUsuari,contrasenya,token,null,0);
                        }
                        else if(rol_usuari.equals("ROL_PROPIETARI")){
                            Propietari propietari = Propietari.getInstance();
                            propietari.setAll(id,nomUsuari,contrasenya,token,null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    guardarCanvis.setEnabled(true);
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error.networkResponse.statusCode == 400) {
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
                        Toast.makeText(getApplicationContext(), "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();

                    guardarCanvis.setEnabled(true);
                    progressDialog.dismiss();
                }
            }
        );

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}