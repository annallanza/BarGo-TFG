package com.example.bargo.UsuariConsumidor.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.bargo.Consumidor;
import com.example.bargo.UsuariConsumidor.Model.RepteListInfo;
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

public class ReptesActivity extends AppCompatActivity {
    private ListView repteListView;
    private List<Long> repteListIds;
    private List<String> repteListNoms;
    private List<Integer> repteListPuntuacions;
    private List<Boolean> repteListComplets;
    private List<String> repteListProgresos;

    private long idRepte = -1;
    private ProgressDialog progressDialog;
    private final Consumidor consumidor = Consumidor.getInstance();
    private final RepteListInfo repteListInfo = RepteListInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reptes);

        progressDialog = new ProgressDialog(ReptesActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
    }

    public void onResume() {
        super.onResume();
        GetListReptes();
    }

    private void GetListReptes() {
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "reptes/" + consumidor.getId();

        JsonArrayRequest JsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        repteListInfo.resetAll();
                        for(int i = 0; i < response.length(); ++i){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                //Info repte
                                long id = jsonObject.getLong("id");
                                String nom = jsonObject.getString("nom");
                                int puntuacio = jsonObject.getInt("puntuacio");
                                boolean complet = jsonObject.getBoolean("complet");
                                String progres = jsonObject.getString("progres");

                                repteListInfo.addRepte(id,nom, puntuacio, complet, progres);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        repteListIds = repteListInfo.getIds();
                        repteListNoms = repteListInfo.getNoms();
                        repteListPuntuacions = repteListInfo.getPuntuacions();
                        repteListComplets = repteListInfo.getComplets();
                        repteListProgresos = repteListInfo.getProgresos();

                        repteListView = findViewById(R.id.reptesList);
                        ReptesActivity.RepteListAdapter repteListAdapter = new ReptesActivity.RepteListAdapter();
                        repteListView.setAdapter(repteListAdapter);

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

                        Toast.makeText(getApplicationContext(), missatgeError, Toast.LENGTH_LONG).show();

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(error.networkResponse.statusCode == 401)
                    Toast.makeText(getApplicationContext(), "No se indica el token o no es válido o el id no es el asociado al token", Toast.LENGTH_LONG).show();

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

    public class RepteListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return repteListIds.size();
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
            View view = getLayoutInflater().inflate(R.layout.reptes_item_list, null);

            TextView nomRepte = view.findViewById(R.id.NomRepte);
            TextView progresTextRepte = view.findViewById(R.id.progresTextRepte);
            ProgressBar progressBarRepte = view.findViewById(R.id.progressBarRepte);
            TextView puntuacioRepte = view.findViewById(R.id.puntuacioRepte);

            nomRepte.setText(repteListNoms.get(position));

            progresTextRepte.setText(repteListProgresos.get(position));
            progressBarRepte.setMax(Integer.parseInt(repteListProgresos.get(position).substring(0,1)));
            progressBarRepte.setProgress(Integer.parseInt(repteListProgresos.get(position).substring(2,3)));

            if(repteListComplets.get(position))
                puntuacioRepte.setText(getString(R.string.yaObtenido));
            else
                puntuacioRepte.setText(repteListPuntuacions.get(position) + " " + getString(R.string.points));

            return view;
        }
    }
}
