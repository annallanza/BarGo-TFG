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

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText nomUsuari;
    private EditText contrasenya;
    private TextView registrate;
    private CheckBox veureContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        nomUsuari = findViewById(R.id.editTextUsername);
        contrasenya = findViewById(R.id.editTextContra);
        loginButton = findViewById(R.id.buttonAcceder);
        veureContraseña = findViewById(R.id.checkBoxContraseña);

        veureContraseña.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

                String nomUsuari = LoginActivity.this.nomUsuari.getText().toString();
                String contrasenya = LoginActivity.this.contrasenya.getText().toString();
                loginRequest(nomUsuari, contrasenya);
            }
        });
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void loginRequest(final String nomUsuari, final String contrasenya){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = VariablesGlobals.getUrlAPI() + "usuaris/auth/login"; //localhost  192.168.1.13

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
                        usuari.setName(nomUsuari);
                        usuari.setPassword(contrasenya);
                        usuari.setToken(token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    openMainActivity();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error.networkResponse.statusCode == 401)
                        Toast.makeText(getApplicationContext(), "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();

                    loginButton.setEnabled(true);
                }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    /* JSONARRAYREQUEST
    public void loginRequest(String nomUsuari, String contrasenya){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.135:8080/usuaris/auth/login"; //localhost  192.168.1.13

        System.out.println("FEM LA PETICIO JSON");
        // Request a string response from the provided URL.
        JsonArrayRequest JsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("Response is: "+ response.toString());
                        openMainActivity();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("2.That didn't work!");
                System.out.println(error);
                Toast.makeText(getApplicationContext(), "Introduzca unas credenciales válidas", Toast.LENGTH_LONG).show();
                loginButton.setEnabled(true);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(JsonArrayRequest);

    }

    public void requestHTTP(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.135:8080/products/hello"; //localhost  192.168.1.135
        //String url ="https://enigmatic-forest-20507.herokuapp.com/";

        System.out.println("FEM LA PETICIO STRING");
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("Response is: "+ response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("1.That didn't work!");
                System.out.println(error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
     */
}