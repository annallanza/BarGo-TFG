package com.example.bargo.UsuariPropietari.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bargo.LoginActivity;
import com.example.bargo.PoliticaPrivacitatActivity;
import com.example.bargo.Propietari;
import com.example.bargo.R;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ConfiguracioPropietariActivity extends AppCompatActivity {

    private TextView logout;
    private TextView eliminarCompte;
    private TextView politicaPrivacitat;

    private ProgressDialog progressDialog;
    private Propietari propietari = Propietari.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracio_propietari);

        logout = findViewById(R.id.textViewLogout2);
        eliminarCompte = findViewById(R.id.textViewEliminarCuenta2);
        politicaPrivacitat = findViewById(R.id.textViewPoliticaPrivacitat);

        progressDialog = new ProgressDialog(ConfiguracioPropietariActivity.this);
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

        politicaPrivacitat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPoliticaPrivacitatActivity();
            }
        });
    }

    private void eliminarTokenDeSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("sessio", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        editor.apply();
    }

    private void obrirLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void showConfirmacio(String titol, String missatge, final String opcio){
        final AlertDialog alertdialog = new AlertDialog.Builder(ConfiguracioPropietariActivity.this)
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
                    propietari.setPropietariNull();
                    eliminarTokenDeSharedPreferences();
                    obrirLoginActivity();
                }
                else {
                    DeletePropietariRequest();
                }
            }
        });
    }

    private void openPoliticaPrivacitatActivity() {
        Intent intent = new Intent(this, PoliticaPrivacitatActivity.class);
        startActivity(intent);
    }

    private void DeletePropietariRequest(){
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "propietaris/" + propietari.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        propietari.setPropietariNull();
                        eliminarTokenDeSharedPreferences();
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
                headers.put("Authorization", "Bearer " + propietari.getToken());
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}