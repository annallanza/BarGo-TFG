package com.example.bargo.UsuariConsumidor.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bargo.Consumidor;
import com.example.bargo.UsuariConsumidor.Model.PremiListInfo;
import com.example.bargo.R;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PremisActivity extends AppCompatActivity {

    private ListView premiListView;
    private List<Long> premiListIds;
    private List<String> premiListNoms;
    private List<Integer> premiListPuntuacions;

    private long idPremi = -1;
    private ProgressDialog progressDialog;
    private final Consumidor consumidor = Consumidor.getInstance();
    private final PremiListInfo premiListInfo = PremiListInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_premi);

        progressDialog = new ProgressDialog(PremisActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
    }

    public void onResume() {
        super.onResume();
        GetListPremis();
    }

    private void showConfirmacio(String titol, String missatge){
        final AlertDialog alertdialog = new AlertDialog.Builder(PremisActivity.this)
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
                IntercanviarPremi();
            }
        });
    }

    private void GetListPremis() {
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "premis/";

        JsonArrayRequest JsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    premiListInfo.resetAll();
                    for(int i = 0; i < response.length(); ++i){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);

                            //Info premi
                            long id = jsonObject.getLong("id");
                            String nom = jsonObject.getString("nom");
                            int puntuacio = jsonObject.getInt("puntuacio");

                            premiListInfo.addPremi(id,nom, puntuacio);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    premiListIds = premiListInfo.getIds();
                    premiListNoms = premiListInfo.getNoms();
                    premiListPuntuacions = premiListInfo.getPuntuacions();

                    premiListView = findViewById(R.id.PremisList);
                    ProductListAdapter productListAdapter = new ProductListAdapter();
                    premiListView.setAdapter(productListAdapter);

                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                    Toast.makeText(getApplicationContext(), "No se ha podido conectar con el servidor. Comprueba tu conexión a internet.", Toast.LENGTH_LONG).show();
                else if(error.networkResponse != null && error.networkResponse.statusCode == 401)
                    Toast.makeText(getApplicationContext(), "No se indica el token o no es válido", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Se ha producido un error, vuélvelo a intentar más tarde.", Toast.LENGTH_LONG).show();

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
        VolleySingleton.getInstance(this).addToRequestQueue(JsonArrayRequest);
    }

    public class ProductListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return premiListIds.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View covertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.premi_item_list, null);
            ImageView mImageView = view.findViewById(R.id.ImatgePremi);
            TextView mNameView = view.findViewById(R.id.NomPremi);
            TextView mPointView = view.findViewById(R.id.PuntsPremi);
            Button obtenerButton = view.findViewById(R.id.ButtonObtener);

            String nomPremi = premiListNoms.get(position);
            int imatge = R.drawable.premi_samarreta;
            switch (nomPremi) {
                case "Camiseta":
                    imatge = R.drawable.premi_samarreta;
                    break;
                case "Pendrive 64GB":
                    imatge = R.drawable.premi_pendrive;
                    break;
                case "Entradas de cine":
                    imatge = R.drawable.premi_entrades_cine;
                    break;
                case "Entradas del museo Picasso":
                    imatge = R.drawable.premi_entrades_picasso;
                    break;
                case "Entradas al aquarium":
                    imatge = R.drawable.premi_entrades_aquarium;
                    break;
            }

            mImageView.setImageResource(imatge);
            mNameView.setText(nomPremi);
            String puntuacio = premiListPuntuacions.get(position) + " puntos";
            mPointView.setText(puntuacio);

            obtenerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idPremi = premiListIds.get(position);

                    showConfirmacio("Intercanviar premio", "¿Estás seguro de que deseas intercanviar este premio?");
                }
            });
            return view;
        }
    }

    public void IntercanviarPremi() {

        String url = VariablesGlobals.getUrlAPI() + "premis/intercanviar";

        JSONObject postData = new JSONObject();
        try {
            postData.put("id", consumidor.getId());
            postData.put("idPremi", idPremi);

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

                    progressDialog.dismiss();
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
                else if(error.networkResponse != null && error.networkResponse.statusCode == 401)
                    Toast.makeText(getApplicationContext(), "No se indica el token o no es válido o el id no es el asociado al token", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Se ha producido un error, vuélvelo a intentar más tarde.", Toast.LENGTH_LONG).show();

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
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}