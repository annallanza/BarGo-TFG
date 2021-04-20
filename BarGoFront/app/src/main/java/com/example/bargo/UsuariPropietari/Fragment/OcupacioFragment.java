package com.example.bargo.UsuariPropietari.Fragment;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bargo.Propietari;
import com.example.bargo.R;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class OcupacioFragment extends Fragment {

    private RadioButton llenoInterior;
    private RadioButton medioLlenoInterior;
    private RadioButton vacioInterior;
    private TextView exteriorTextView;
    private RadioGroup exteriorRadioGroup;
    private RadioButton llenoExterior;
    private RadioButton medioLlenoExterior;
    private RadioButton vacioExterior;
    private Button guardarCanvis;
    private ProgressDialog progressDialog;
    private final Propietari propietari = Propietari.getInstance();
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ocupacio, container, false);

        llenoInterior = view.findViewById(R.id.radioButtonLlenoInterior);
        medioLlenoInterior = view.findViewById(R.id.radioButtonMedioLlenoInterior);
        vacioInterior = view.findViewById(R.id.radioButtonVacioInterior);
        exteriorTextView = view.findViewById(R.id.textViewExterior);
        exteriorRadioGroup = view.findViewById(R.id.radioGroupExterior);
        llenoExterior = view.findViewById(R.id.radioButtonLlenoExterior);
        medioLlenoExterior = view.findViewById(R.id.radioButtonMedioLlenoExterior);
        vacioExterior = view.findViewById(R.id.radioButtonVacioExterior);
        guardarCanvis = view.findViewById(R.id.buttonGuardarCanvis2);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        guardarCanvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCanvis.setEnabled(false);
                progressDialog.show();

                String ocupacioInterior;
                if(llenoInterior.isChecked())
                    ocupacioInterior = "Lleno";
                else if(medioLlenoInterior.isChecked())
                    ocupacioInterior = "Medio_lleno";
                else
                    ocupacioInterior = "Vacio";

                String ocupacioExterior = "null";
                if(exteriorTextView.getVisibility() == View.VISIBLE){
                    if(llenoExterior.isChecked())
                        ocupacioExterior = "Lleno";
                    else if(medioLlenoExterior.isChecked())
                        ocupacioExterior = "Medio_lleno";
                    else
                        ocupacioExterior = "Vacio";
                }

                PutOcupacioEstablimentPropietariRequest(ocupacioInterior, ocupacioExterior);
            }
        });

        return view;
    }

    public void onResume() {
        super.onResume();
        GetExteriorEstablimentPropietariRequest();
    }

    private void setVisibilityExterior(Boolean exterior){
        if(exterior){
            exteriorTextView.setVisibility(View.VISIBLE);
            exteriorRadioGroup.setVisibility(View.VISIBLE);
        }
        else{
            exteriorTextView.setVisibility(View.GONE);
            exteriorRadioGroup.setVisibility(View.GONE);
        }
    }

    private void setOcupacioRadioButton(Boolean exterior, String ocupacioInterior, String ocupacioExterior) {
        if(ocupacioInterior.equals("Lleno"))
            llenoInterior.setChecked(true);
        else if(ocupacioInterior.equals("Medio_lleno"))
            medioLlenoInterior.setChecked(true);
        else
            vacioInterior.setChecked(true);

        if(exterior){
            if(ocupacioExterior.equals("Lleno"))
                llenoExterior.setChecked(true);
            else if(ocupacioExterior.equals("Medio_lleno"))
                medioLlenoExterior.setChecked(true);
            else
                vacioExterior.setChecked(true);
        }
    }

    private void GetExteriorEstablimentPropietariRequest() {
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "establiments/exterior/" + propietari.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean exterior = response.getBoolean("exterior");
                            String ocupacioInterior = response.getString("ocupacioInterior");
                            String ocupacioExterior = response.getString("ocupacioExterior");

                            setVisibilityExterior(exterior);
                            setOcupacioRadioButton(exterior, ocupacioInterior, ocupacioExterior);
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

                        Toast.makeText(getActivity(), missatgeError, Toast.LENGTH_LONG).show();

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(error.networkResponse.statusCode == 401)
                    Toast.makeText(getActivity(), "No se indica el token o no es válido o el id no es el asociado al token", Toast.LENGTH_LONG).show();

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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void PutOcupacioEstablimentPropietariRequest(String ocupacioInterior, String ocupacioExterior) {
        String url = VariablesGlobals.getUrlAPI() + "establiments/";

        JSONObject postData = new JSONObject();
        try {
            postData.put("id", propietari.getId());
            postData.put("ocupacioInterior", ocupacioInterior);
            postData.put("ocupacioExterior", ocupacioExterior);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String missatge = response.getString("missatge");

                            Toast.makeText(getActivity(), missatge, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        guardarCanvis.setEnabled(true);
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 400 || error.networkResponse.statusCode == 404) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        String missatgeError = data.getString("missatge");

                        Toast.makeText(getActivity(), missatgeError, Toast.LENGTH_LONG).show();

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(error.networkResponse.statusCode == 401)
                    Toast.makeText(getActivity(), "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();

                guardarCanvis.setEnabled(true);
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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }

}