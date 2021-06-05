package com.example.bargo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.bargo.UsuariConsumidor.Activity.MainActivity;
import com.example.bargo.UsuariPropietari.Activity.MainPropietariActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText nomUsuari;
    private EditText contrasenya;
    private TextView registrate;
    private TextView canviarContrasenya;
    private CheckBox veureContrasenya;

    private ProgressDialog progressDialog;
    int usuariExisteix = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicialitzarProgressDialog();

        registrate = findViewById(R.id.textViewRegistrate);
        String text = "¿Aún no tienes tu cuenta? Regístrate";
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                openRegisterActivity();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.colorBarGo));
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan1, 26, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registrate.setText(ss);
        registrate.setMovementMethod(LinkMovementMethod.getInstance());

        canviarContrasenya = findViewById(R.id.textViewCanviarContrasenya);
        nomUsuari = findViewById(R.id.editTextUsername);
        contrasenya = findViewById(R.id.editTextContra2);
        loginButton = findViewById(R.id.buttonAcceder);
        veureContrasenya = findViewById(R.id.checkBoxContraseña);

        canviarContrasenya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCanviarContrasenyaActivity();
            }
        });

        veureContrasenya.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    contrasenya.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else contrasenya.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);

                progressDialog.show();

                String nomUsuari = LoginActivity.this.nomUsuari.getText().toString();
                String contrasenya = LoginActivity.this.contrasenya.getText().toString();
                loginRequest(nomUsuari, contrasenya);
            }
        });
    }

    private void inicialitzarProgressDialog() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(VariablesGlobals.getSecret().getBytes()).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("token mal formado");
        } catch (UnsupportedJwtException e) {
            System.out.println("token no soportado");
        } catch (ExpiredJwtException e) {
            System.out.println("token expirado");
            PostRefreshTokenRequest(token);
        } catch (IllegalArgumentException e) {
            System.out.println("token vacío");
        } catch (SignatureException e) {
            System.out.println("fail en la firma");
        }
        return false;
    }

    private void comprovarSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("sessio", MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);

        if (token != null && validateToken(token)) {
            if(usuariExisteix == -1)
                UsuariExisteixRequest(token);

            else if(usuariExisteix == 1) {
                long id = Jwts.parser().setSigningKey(VariablesGlobals.getSecret().getBytes()).parseClaimsJws(token).getBody().get("id", Long.class);
                String nomUsuari = Jwts.parser().setSigningKey(VariablesGlobals.getSecret().getBytes()).parseClaimsJws(token).getBody().getSubject();
                ArrayList rols = Jwts.parser().setSigningKey(VariablesGlobals.getSecret().getBytes()).parseClaimsJws(token).getBody().get("rols", ArrayList.class);

                String rol_usuari = (String) rols.get(0);

                if (rol_usuari.equals("ROL_CONSUMIDOR")) {
                    Consumidor consumidor = Consumidor.getInstance();
                    consumidor.setId(id);
                    consumidor.setNom(nomUsuari);
                    consumidor.setToken(token);
                } else if (rol_usuari.equals("ROL_PROPIETARI")) {
                    Propietari propietari = Propietari.getInstance();
                    propietari.setId(id);
                    propietari.setNom(nomUsuari);
                    propietari.setToken(token);
                }
                openMainActivity(rol_usuari);
            }
        }
    }

    private void guardarTokenASharedPreferences(String token){
        SharedPreferences sharedPreferences = getSharedPreferences("sessio", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        editor.putString("token",token);

        editor.apply();
    }

    private void eliminarTokenDeSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("sessio", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        comprovarSharedPreferences();
    }

    private void openCanviarContrasenyaActivity() {
        Intent intent = new Intent(this, CanviarContrasenyaActivity.class);
        startActivity(intent);
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void openMainActivity(String rol_usuari){
        Intent intent;
        if(rol_usuari.equals("ROL_CONSUMIDOR"))
            intent = new Intent(this, MainActivity.class);
        else intent = new Intent(this, MainPropietariActivity.class);

        startActivity(intent);
        finish();
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

                        guardarTokenASharedPreferences(token);

                        long id = Jwts.parser().setSigningKey(VariablesGlobals.getSecret().getBytes()).parseClaimsJws(token).getBody().get("id", Long.class);
                        ArrayList rols = Jwts.parser().setSigningKey(VariablesGlobals.getSecret().getBytes()).parseClaimsJws(token).getBody().get("rols", ArrayList.class);

                        String rol_usuari = (String) rols.get(0);

                        if(rol_usuari.equals("ROL_CONSUMIDOR")){
                            Consumidor consumidor = Consumidor.getInstance();
                            consumidor.setAlmostAll(id,nomUsuari,token);
                        }
                        else if(rol_usuari.equals("ROL_PROPIETARI")){
                            Propietari propietari = Propietari.getInstance();
                            propietari.setAlmostAll(id,nomUsuari,token);
                        }
                        progressDialog.dismiss();
                        openMainActivity(rol_usuari);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
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

                    loginButton.setEnabled(true);
                    progressDialog.dismiss();
                }
            }
        );

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void PostRefreshTokenRequest(String token){
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "usuaris/auth/refresh";

        JSONObject postData = new JSONObject();
        try {
            postData.put("token", token);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String token = response.getString("token");

                        guardarTokenASharedPreferences(token);
                        progressDialog.dismiss();
                        comprovarSharedPreferences();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
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
                progressDialog.dismiss();
            }
        }
        );

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void UsuariExisteixRequest(String token){
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "usuaris/auth/exists/";

        JSONObject postData = new JSONObject();
        try {
            postData.put("token", token);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean existeix = response.getBoolean("existeix");

                        if(existeix) {
                            usuariExisteix = 1;
                            comprovarSharedPreferences();
                        }
                        else {
                            usuariExisteix = 0;
                            eliminarTokenDeSharedPreferences();
                        }

                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        usuariExisteix = 0;
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                usuariExisteix = 0;
                progressDialog.dismiss();
            }
        }
        );

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}