package com.example.bargo.UsuariConsumidor.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bargo.Consumidor;
import com.example.bargo.R;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CodeFragment extends Fragment {

    EditText codi;
    Button bescanviar;

    private ProgressDialog progressDialog;
    private final Consumidor consumidor = Consumidor.getInstance();
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_code, container, false);

        codi = view.findViewById(R.id.EditTextCodi);
        bescanviar = view.findViewById(R.id.ButtonCanjear);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        bescanviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bescanviar.setEnabled(false);
                progressDialog.show();

                String code = codi.getText().toString();

                BescanviarCodi(code);
            }
        });

        return view;
    }

    public void BescanviarCodi(final String codi) {

        String url = VariablesGlobals.getUrlAPI() + "codis/bescanviar";

        JSONObject postData = new JSONObject();
        try {
            postData.put("id", consumidor.getId());
            postData.put("codi", codi);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String missatge = response.getString("missatge");

                        Toast.makeText(getContext(), missatge, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bescanviar.setEnabled(true);
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 400 || error.networkResponse.statusCode == 404 || error.networkResponse.statusCode == 409) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        String missatgeError = data.getString("missatge");

                        Toast.makeText(getContext(), missatgeError, Toast.LENGTH_LONG).show();

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(error.networkResponse.statusCode == 401)
                    Toast.makeText(getContext(), "No se indica el token o no es válido o el id no es el asociado al token", Toast.LENGTH_LONG).show();

                bescanviar.setEnabled(true);
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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }
}
