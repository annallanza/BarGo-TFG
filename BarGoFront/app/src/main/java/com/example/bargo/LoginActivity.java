package com.example.bargo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText email;
    private EditText passw;
    private TextView registrate;

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

        email = findViewById(R.id.editTextEmail);
        passw = findViewById(R.id.editTextContra);
        loginButton = findViewById(R.id.buttonAcceder);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correu = email.getText().toString();
                String contra = passw.getText().toString();
                User usuari = User.getInstance();
                //requestHTTP();
                JSONrequestHTTP();
                if (correu.equals(usuari.getEmail()) && contra.equals(usuari.getPassword())) openMainActivity();
                else Toast.makeText(getApplicationContext(), "Introduzca unas credenciales válidas", Toast.LENGTH_LONG).show();
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

    public void JSONrequestHTTP(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        //String url ="http://192.168.1.135:8080/products"; //localhost  192.168.1.13
        String url ="https://radiant-river-14336.herokuapp.com/products";

        System.out.println("FEM LA PETICIO JSON");
        // Request a string response from the provided URL.
        JsonArrayRequest JsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("Response is: "+ response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("2.That didn't work!");
                System.out.println(error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(JsonArrayRequest);

    }
}