package com.example.bargo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
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
import java.util.Calendar;

import io.jsonwebtoken.Jwts;

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
    private int contador;
    private TextView horariTextView;
    private Button horariMes;
    private Button horariMenys;
    private EditText horariEditText;
    private TextView guioTextView;
    private EditText horariEditText2;
    private EditText horariEditText3;
    private TextView guioTextView2;
    private EditText horariEditText4;
    private EditText horariEditText5;
    private TextView guioTextView3;
    private EditText horariEditText6;
    private EditText descripcioEditText;
    private EditText paginaWebEditText;
    private ProgressDialog progressDialog;
    private Consumidor consumidor = Consumidor.getInstance();

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
        contrasenyaEditText = findViewById(R.id.editTextContraNova);
        veureContraseña = findViewById(R.id.checkBoxContraseña2);
        isConsumidor = findViewById(R.id.checkBox);
        isPropietari = findViewById(R.id.checkBox2);
        nomEstablimentEditText = findViewById(R.id.editTextNomEstabliment);
        direccioEditText = findViewById(R.id.editTextDireccio);
        exteriorCheckBox = findViewById(R.id.checkBoxExterior);
        numCadiresEditText = findViewById(R.id.editTextNumCadires);
        numTaulesEditText = findViewById(R.id.editTextNumTaules);
        contador = 1;
        horariTextView = findViewById(R.id.textViewHorari);
        horariMes = findViewById(R.id.buttonMES);
        horariMenys = findViewById(R.id.buttonMENYS);
        horariEditText = findViewById(R.id.editTextHorari);
        guioTextView = findViewById(R.id.textViewGuio);
        horariEditText2 = findViewById(R.id.editTextHorari2);
        horariEditText3 = findViewById(R.id.editTextHorari3);
        guioTextView2 = findViewById(R.id.textViewGuio2);
        horariEditText4 = findViewById(R.id.editTextHorari4);
        horariEditText5 = findViewById(R.id.editTextHorari5);
        guioTextView3 = findViewById(R.id.textViewGuio3);
        horariEditText6 = findViewById(R.id.editTextHorari6);
        descripcioEditText = findViewById(R.id.editTextDescripcio);
        paginaWebEditText = findViewById(R.id.editTextPaginaWeb);
        signupButton = findViewById(R.id.buttonAcceder);

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        veureContraseña.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    contrasenyaEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else contrasenyaEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        horariMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador == 1) {
                    ++contador;
                    horariEditText3.setVisibility(View.VISIBLE);
                    guioTextView2.setVisibility(View.VISIBLE);
                    horariEditText4.setVisibility(View.VISIBLE);
                }
                else if(contador == 2){
                    ++contador;
                    horariEditText5.setVisibility(View.VISIBLE);
                    guioTextView3.setVisibility(View.VISIBLE);
                    horariEditText6.setVisibility(View.VISIBLE);
                }
                else
                    Toast.makeText(getApplicationContext(), "No puedes añadir más de 3", Toast.LENGTH_LONG).show();
            }
        });

        horariMenys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador == 2) {
                    --contador;
                    horariEditText3.setText("");
                    horariEditText4.setText("");

                    horariEditText3.setVisibility(View.GONE);
                    guioTextView2.setVisibility(View.GONE);
                    horariEditText4.setVisibility(View.GONE);
                }
                else if(contador == 3){
                    --contador;
                    horariEditText5.setText("");
                    horariEditText6.setText("");

                    horariEditText5.setVisibility(View.GONE);
                    guioTextView3.setVisibility(View.GONE);
                    horariEditText6.setVisibility(View.GONE);
                }
                else
                    Toast.makeText(getApplicationContext(), "Has de indicar como mínimo un horario", Toast.LENGTH_LONG).show();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signupButton.setEnabled(false);
                progressDialog.show();

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
                    String horari = horariEditText.getText().toString() + "-" + horariEditText2.getText().toString();

                    if(!horariEditText3.getText().toString().equals(""))
                        horari += "y" + horariEditText3.getText().toString() + "-" + horariEditText4.getText().toString();
                    if(!horariEditText5.getText().toString().equals(""))
                        horari += "y" + horariEditText5.getText().toString() + "-" + horariEditText6.getText().toString();

                    String descripcio = descripcioEditText.getText().toString();
                    String paginaWeb = paginaWebEditText.getText().toString();

                    if(!paginaWeb.contains("https://"))
                        paginaWeb = "https://" + paginaWeb;

                    if(numCadires_string.equals("")){
                        Toast.makeText(getApplicationContext(), "Indica el número de sillas", Toast.LENGTH_LONG).show();
                        signupButton.setEnabled(true);
                        progressDialog.dismiss();
                    }
                    else if(numTaules_string.equals("")){
                        Toast.makeText(getApplicationContext(), "Indica el número de mesas", Toast.LENGTH_LONG).show();
                        signupButton.setEnabled(true);
                        progressDialog.dismiss();
                    }
                    else if(horariEditText.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Indica el horario", Toast.LENGTH_LONG).show();
                        signupButton.setEnabled(true);
                        progressDialog.dismiss();
                    }
                    else if(horariEditText2.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Indica el horario", Toast.LENGTH_LONG).show();
                        signupButton.setEnabled(true);
                        progressDialog.dismiss();
                    }
                    else if(!horariEditText3.getText().toString().equals("") && horariEditText4.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Indica el horario", Toast.LENGTH_LONG).show();
                        signupButton.setEnabled(true);
                        progressDialog.dismiss();
                    }
                    else if(!horariEditText5.getText().toString().equals("") && horariEditText6.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Indica el horario", Toast.LENGTH_LONG).show();
                        signupButton.setEnabled(true);
                        progressDialog.dismiss();
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
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void guardarTokenASharedPreferences(String token){
        SharedPreferences sharedPreferences = getSharedPreferences("sessio", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        editor.putString("token",token);

        editor.apply();
    }

    public void openMainActivity(String rol_usuari){
        Intent intent;
        if(rol_usuari.equals("ROL_CONSUMIDOR"))
            intent = new Intent(this, MainActivity.class);
        else intent = new Intent(this, MainPropietariActivity.class);

        startActivity(intent);
        finish();
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void onHorariClicked(View view){
        final EditText horariEditTextSegonsID = findViewById(view.getId());

        final Calendar calendar = Calendar.getInstance();

        final int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuts = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(RegisterActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

        timePickerDialog.show();
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
                    horariTextView.setVisibility(View.GONE);
                    horariMes.setVisibility(View.GONE);
                    horariMenys.setVisibility(View.GONE);
                    horariEditText.setVisibility(View.GONE);
                    guioTextView.setVisibility(View.GONE);
                    horariEditText2.setVisibility(View.GONE);
                    horariEditText3.setVisibility(View.GONE);
                    guioTextView2.setVisibility(View.GONE);
                    horariEditText4.setVisibility(View.GONE);
                    horariEditText5.setVisibility(View.GONE);
                    guioTextView3.setVisibility(View.GONE);
                    horariEditText6.setVisibility(View.GONE);
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
                    horariTextView.setVisibility(View.VISIBLE);
                    horariMes.setVisibility(View.VISIBLE);
                    horariMenys.setVisibility(View.VISIBLE);
                    horariEditText.setVisibility(View.VISIBLE);
                    guioTextView.setVisibility(View.VISIBLE);
                    horariEditText2.setVisibility(View.VISIBLE);
                    descripcioEditText.setVisibility(View.VISIBLE);
                    paginaWebEditText.setVisibility(View.VISIBLE);
                }
                else {
                    nomEstablimentEditText.setVisibility(View.GONE);
                    direccioEditText.setVisibility(View.GONE);
                    exteriorCheckBox.setVisibility(View.GONE);
                    numCadiresEditText.setVisibility(View.GONE);
                    numTaulesEditText.setVisibility(View.GONE);
                    horariTextView.setVisibility(View.GONE);
                    horariMes.setVisibility(View.GONE);
                    horariMenys.setVisibility(View.GONE);
                    horariEditText.setVisibility(View.GONE);
                    guioTextView.setVisibility(View.GONE);
                    horariEditText2.setVisibility(View.GONE);
                    horariEditText3.setVisibility(View.GONE);
                    guioTextView2.setVisibility(View.GONE);
                    horariEditText4.setVisibility(View.GONE);
                    horariEditText5.setVisibility(View.GONE);
                    guioTextView3.setVisibility(View.GONE);
                    horariEditText6.setVisibility(View.GONE);
                    descripcioEditText.setVisibility(View.GONE);
                    paginaWebEditText.setVisibility(View.GONE);
                }
        }
    }

    public void SignupRequestConsumidor(final String nomUsuari, final String contrasenya){
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
                    progressDialog.dismiss();
                }
            }
        );

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void SignupRequestPropietari(final String nomUsuari, final String contrasenya, String nomEstabliment, String direccio, Boolean exterior, int numCadires, int numTaules, String horari, String descripcio, String paginaWeb){
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
                    progressDialog.dismiss();
                }
            }
        );

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
                        openMainActivity(rol_usuari);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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

                    signupButton.setEnabled(true);
                    progressDialog.dismiss();
                }
            }
        );

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

}