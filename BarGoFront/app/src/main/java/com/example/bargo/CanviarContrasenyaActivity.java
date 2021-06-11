package com.example.bargo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import io.jsonwebtoken.Jwts;

public class CanviarContrasenyaActivity extends AppCompatActivity {

    private Button continuarButton;
    private EditText correuEditText;
    private TextView respostaTextView;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canviar_contrasenya);

        progressDialog = new ProgressDialog(CanviarContrasenyaActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        correuEditText = findViewById(R.id.editTextCorreu);
        continuarButton = findViewById(R.id.buttonContinuar);
        respostaTextView = findViewById(R.id.textViewRespuesta);

        continuarButton.setVisibility(View.VISIBLE);
        respostaTextView.setVisibility(View.GONE);

        continuarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuarButton.setEnabled(false);

                progressDialog.show();

                String correu = correuEditText.getText().toString();
                canviarConstrasenyaRequest(correu);
            }
        });
    }

    public void canviarConstrasenyaRequest(final String correu){
        String url = VariablesGlobals.getUrlAPI() + "usuaris/canviarContrasenya";

        JSONObject postData = new JSONObject();
        try {
            postData.put("correu", correu);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        respostaTextView.setText("Has recibido un correo electrónico con una nueva contraseña");
                        respostaTextView.setVisibility(View.VISIBLE);
                        continuarButton.setVisibility(View.GONE);

                        continuarButton.setEnabled(true);
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                    Toast.makeText(getApplicationContext(), "No se ha podido conectar con el servidor. Comprueba tu conexión a internet.", Toast.LENGTH_LONG).show();
                else if(error.networkResponse != null && (error.networkResponse.statusCode == 400 || error.networkResponse.statusCode == 404)) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        String missatgeError = data.getString("missatge");

                        Toast.makeText(getApplicationContext(), missatgeError, Toast.LENGTH_LONG).show();

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "Se ha producido un error, vuélvelo a intentar más tarde.", Toast.LENGTH_LONG).show();

                continuarButton.setEnabled(true);
                progressDialog.dismiss();
            }
        }
        );

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}













































