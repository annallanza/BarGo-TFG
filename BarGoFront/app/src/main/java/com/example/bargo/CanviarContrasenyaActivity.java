package com.example.bargo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class CanviarContrasenyaActivity extends AppCompatActivity {

    private Button continuarButton;
    private EditText correuEditText;
    private TextView codiTextView;
    private EditText codiEditText;
    private TextView contrasenyaTextView;
    private EditText contrasenyaEditText;
    private TextView confirmarContrasenyaTextView;
    private EditText confirmarContrasenyaEditText;
    private CheckBox veureContrasenya;

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
        codiTextView = findViewById(R.id.textViewCodi);
        codiEditText = findViewById(R.id.editTextCodi);
        contrasenyaTextView = findViewById(R.id.textViewContrasenya);
        contrasenyaEditText = findViewById(R.id.editTextContrasenya);
        confirmarContrasenyaTextView = findViewById(R.id.textViewConfirmarContrasenya);
        confirmarContrasenyaEditText = findViewById(R.id.editTextConfirmarContrasenya);
        veureContrasenya = findViewById(R.id.checkBoxContraseña2);

        codiTextView.setVisibility(View.GONE);
        codiEditText.setVisibility(View.GONE);
        contrasenyaTextView.setVisibility(View.GONE);
        contrasenyaEditText.setVisibility(View.GONE);
        confirmarContrasenyaTextView.setVisibility(View.GONE);
        confirmarContrasenyaEditText.setVisibility(View.GONE);
        veureContrasenya.setVisibility(View.GONE);

        veureContrasenya.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    contrasenyaEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmarContrasenyaEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    contrasenyaEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmarContrasenyaEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        continuarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuarButton.setEnabled(false);

                progressDialog.show();

                String correu = correuEditText.getText().toString();

                if(codiEditText.getVisibility() == View.GONE)
                    solicitarCanviarConstrasenyaRequest(correu);
                else {
                    String codi = codiEditText.getText().toString();
                    String contrasenya = contrasenyaEditText.getText().toString();

                    if (!contrasenya.equals(confirmarContrasenyaEditText.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                        continuarButton.setEnabled(true);
                        progressDialog.dismiss();
                    }
                    else
                        canviarConstrasenyaRequest(correu, codi, contrasenya);
                }
            }
        });
    }

    public void solicitarCanviarConstrasenyaRequest(final String correu){
        String url = VariablesGlobals.getUrlAPI() + "usuaris/solicitarCanviarContrasenya";

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
                        Toast.makeText(getApplicationContext(), "Has recibido un correo electrónico con un código para cambiar la contraseña", Toast.LENGTH_LONG).show();

                        codiTextView.setVisibility(View.VISIBLE);
                        codiEditText.setVisibility(View.VISIBLE);
                        contrasenyaTextView.setVisibility(View.VISIBLE);
                        contrasenyaEditText.setVisibility(View.VISIBLE);
                        confirmarContrasenyaTextView.setVisibility(View.VISIBLE);
                        confirmarContrasenyaEditText.setVisibility(View.VISIBLE);
                        veureContrasenya.setVisibility(View.VISIBLE);

                        veureContrasenya.setChecked(false);

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

    public void canviarConstrasenyaRequest(final String correu, final String codi, final String contrasenya){
        String url = VariablesGlobals.getUrlAPI() + "usuaris/canviarContrasenya";

        JSONObject postData = new JSONObject();
        try {
            postData.put("correu", correu);
            postData.put("codi", codi);
            postData.put("contrasenya", contrasenya);

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

                        continuarButton.setEnabled(true);
                        progressDialog.dismiss();

                        finish();
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













































