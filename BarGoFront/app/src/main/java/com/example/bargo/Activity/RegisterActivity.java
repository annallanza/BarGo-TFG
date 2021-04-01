package com.example.bargo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bargo.Model.User;
import com.example.bargo.Model.VariablesGlobals;
import com.example.bargo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RegisterActivity extends AppCompatActivity {
    private TextView login;
    private CheckBox isConsumidor;
    private CheckBox isPropietari;
    private EditText contrasenyaEditText;
    private EditText nomUsuariEditText;
    private Button signupButton;
    private CheckBox veureContraseña;
    private EditText nomEstablimentEditText;
    private EditText direccioEditText;
    private CheckBox exteriorCheckBox;
    private EditText numCadiresEditText;
    private EditText numTaulesEditText;
    private EditText horariEditText;
    private EditText descripcioEditText;
    private EditText paginaWebEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login = findViewById(R.id.textViewRegistrate);
        String text = "¿Ya tienes tu cuenta? Inicia sesión";
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                openLoginActivity();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.colorBarGo));
                ds.setUnderlineText(false);
            }
        };

        ss.setSpan(clickableSpan1, 22, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        login.setText(ss);
        login.setMovementMethod(LinkMovementMethod.getInstance());

        nomUsuariEditText = findViewById(R.id.editTextNombreUsuario);
        contrasenyaEditText = findViewById(R.id.editTextContra);
        veureContraseña = findViewById(R.id.checkBoxContraseña2);
        isConsumidor = findViewById(R.id.checkBox);
        isPropietari = findViewById(R.id.checkBox2);
        nomEstablimentEditText = findViewById(R.id.editTextNomEstabliment);
        direccioEditText = findViewById(R.id.editTextDireccio);
        exteriorCheckBox = findViewById(R.id.checkBoxExterior);
        numCadiresEditText = findViewById(R.id.editTextNumCadires);
        numTaulesEditText = findViewById(R.id.editTextNumTaules);
        horariEditText = findViewById(R.id.editTextHorari);
        descripcioEditText = findViewById(R.id.editTextDescripcio);
        paginaWebEditText = findViewById(R.id.editTextPaginaWeb);
        signupButton = findViewById(R.id.buttonAcceder);

        veureContraseña.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    contrasenyaEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else contrasenyaEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signupButton.setEnabled(false);

                String nomUsuari = nomUsuariEditText.getText().toString();
                String contrasenya = contrasenyaEditText.getText().toString();

                if(isConsumidor.isChecked())
                    SignupRequestConsumidor(nomUsuari, contrasenya);
                else if(isPropietari.isChecked()) {
                    String nomEstabliment = nomEstablimentEditText.getText().toString();
                    String direccio = direccioEditText.getText().toString();
                    Boolean exterior = exteriorCheckBox.isChecked();
                    String numCadires_string = numCadiresEditText.getText().toString();
                    String numTaules_string = numTaulesEditText.getText().toString();
                    String horari = horariEditText.getText().toString();
                    String descripcio = descripcioEditText.getText().toString();
                    String paginaWeb = paginaWebEditText.getText().toString();

                    if(numCadires_string.equals("")){
                        Toast.makeText(getApplicationContext(), "Indica el número de sillas", Toast.LENGTH_LONG).show();
                        signupButton.setEnabled(true);
                    }
                    else if(numTaules_string.equals("")){
                        Toast.makeText(getApplicationContext(), "Indica el número de mesas", Toast.LENGTH_LONG).show();
                        signupButton.setEnabled(true);
                    }
                    else {
                        int numCadires = Integer.parseInt(numCadiresEditText.getText().toString());
                        int numTaules = Integer.parseInt(numTaulesEditText.getText().toString());

                        SignupRequestPropietari(nomUsuari, contrasenya, nomEstabliment, direccio, exterior, numCadires, numTaules, horari, descripcio, paginaWeb);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Indica el tipo de usuario", Toast.LENGTH_LONG).show();
                    signupButton.setEnabled(true);
                }
            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkBox:
                if (checked) {
                    isPropietari.setChecked(false);
                    nomEstablimentEditText.setVisibility(View.GONE);
                    direccioEditText.setVisibility(View.GONE);
                    exteriorCheckBox.setVisibility(View.GONE);
                    numCadiresEditText.setVisibility(View.GONE);
                    numTaulesEditText.setVisibility(View.GONE);
                    horariEditText.setVisibility(View.GONE);
                    descripcioEditText.setVisibility(View.GONE);
                    paginaWebEditText.setVisibility(View.GONE);
                }
                break;
            case R.id.checkBox2:
                if (checked){
                    isConsumidor.setChecked(false);
                    nomEstablimentEditText.setVisibility(View.VISIBLE);
                    direccioEditText.setVisibility(View.VISIBLE);
                    exteriorCheckBox.setVisibility(View.VISIBLE);
                    numCadiresEditText.setVisibility(View.VISIBLE);
                    numTaulesEditText.setVisibility(View.VISIBLE);
                    horariEditText.setVisibility(View.VISIBLE);
                    descripcioEditText.setVisibility(View.VISIBLE);
                    paginaWebEditText.setVisibility(View.VISIBLE);
                }
                else {
                    nomEstablimentEditText.setVisibility(View.GONE);
                    direccioEditText.setVisibility(View.GONE);
                    exteriorCheckBox.setVisibility(View.GONE);
                    numCadiresEditText.setVisibility(View.GONE);
                    numTaulesEditText.setVisibility(View.GONE);
                    horariEditText.setVisibility(View.GONE);
                    descripcioEditText.setVisibility(View.GONE);
                    paginaWebEditText.setVisibility(View.GONE);
                    break;
                }
        }
    }

    public void SignupRequestConsumidor(final String nomUsuari, final String contrasenya){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = VariablesGlobals.getUrlAPI() + "consumidors/auth/signup";

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
                        loginRequest(nomUsuari,contrasenya);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse.statusCode == 400 || error.networkResponse.statusCode == 409) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                String missatgeError = data.getString("missatge");

                                Toast.makeText(getApplicationContext(), missatgeError, Toast.LENGTH_LONG).show();

                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        signupButton.setEnabled(true);
                    }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void SignupRequestPropietari(final String nomUsuari, final String contrasenya, String nomEstabliment, String direccio, Boolean exterior, int numCadires, int numTaules, String horari, String descripcio, String paginaWeb){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = VariablesGlobals.getUrlAPI() + "propietaris/auth/signup";

        JSONObject postData = new JSONObject();
        try {
            postData.put("nomUsuari", nomUsuari);
            postData.put("contrasenya", contrasenya);
            postData.put("nomEstabliment", nomEstabliment);
            postData.put("direccio", direccio);
            postData.put("exterior", exterior);
            postData.put("numCadires", numCadires);
            postData.put("numTaules", numTaules);
            postData.put("horari", horari);
            postData.put("descripcio", descripcio);
            postData.put("paginaWeb", paginaWeb);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(postData);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loginRequest(nomUsuari,contrasenya);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 400 || error.networkResponse.statusCode == 409) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        String missatgeError = data.getString("missatge");

                        Toast.makeText(getApplicationContext(), missatgeError, Toast.LENGTH_LONG).show();

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                signupButton.setEnabled(true);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void loginRequest(final String nomUsuari, final String contrasenya){
        RequestQueue queue = Volley.newRequestQueue(this);
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

                            User usuari = User.getInstance();
                            usuari.setNom(nomUsuari);
                            usuari.setContrasenya(contrasenya);
                            usuari.setToken(token);

                            System.out.println(usuari.getNom() + " " + usuari.getContrasenya() + " " + usuari.getToken());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        openMainActivity();
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

                signupButton.setEnabled(true);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

}