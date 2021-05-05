package com.example.bargo.UsuariConsumidor.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.bargo.Consumidor;
import com.example.bargo.UsuariConsumidor.Activity.InfoEsdevenimentActivity;
import com.example.bargo.UsuariConsumidor.Model.EsdevenimentInfo;
import com.example.bargo.UsuariConsumidor.Model.EsdevenimentListInfo;
import com.example.bargo.R;
import com.example.bargo.UsuariConsumidor.Model.EstablimentInfo;
import com.example.bargo.UsuariConsumidor.Model.EstablimentListInfo;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListEsdevenimentsFragment extends Fragment {

    private ImageView buscar;
    private ImageView filtres;
    private EditText buscador;
    String itemSeleccionat = "null";
    String[] opcions = new String[] {"Nombre evento","Nombre establecimiento", "Dirección"};
    private ListView esdevenimentListView;
    private List<Long> esdevenimentListIds;
    private List<String> esdevenimentListNoms;
    private List<String> esdevenimentListNomsEstabliment;
    View view;

    private ProgressDialog progressDialog;
    private final Consumidor consumidor = Consumidor.getInstance();
    private final EsdevenimentListInfo esdevenimentListInfo = EsdevenimentListInfo.getInstance();
    private final EsdevenimentInfo esdevenimentInfo = EsdevenimentInfo.getInstance();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_esdeveniments, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        buscar = view.findViewById(R.id.search);
        filtres = view.findViewById(R.id.filtres);
        buscador = view.findViewById(R.id.buscador);

        filtres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSeleccionat = "null";
                showConfirmacio("Escoge la opción a filtrar:", opcions);
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!buscador.getText().toString().isEmpty() && itemSeleccionat.equals("null"))
                    Toast.makeText(getActivity(), "Escoge el filtro de búsqueda", Toast.LENGTH_LONG).show();
                else {
                    buscar.setEnabled(false);

                    String nomEsdeveniment = null;
                    String nomEstabliment = null;
                    String direccio = null;
                    switch (itemSeleccionat) {
                        case "Nombre evento":
                            nomEsdeveniment = buscador.getText().toString();
                            break;
                        case "Nombre establecimiento":
                            nomEstabliment = buscador.getText().toString();
                            break;
                        case "Dirección":
                            direccio = buscador.getText().toString();
                            break;
                    }

                    itemSeleccionat = "null";
                    buscador.setText("");

                    GetListEstabliments(nomEsdeveniment, nomEstabliment, direccio);
                }
            }
        });

        return  view;
    }

    public void onResume() {
        super.onResume();
        buscador.setText("");
        GetListEstabliments(null,null, null);
    }

    private void showConfirmacio(String titol, final String[] opcions){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(titol);
        alertDialogBuilder.setSingleChoiceItems(opcions, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                itemSeleccionat = opcions[which];
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancelar", null);
        alertDialogBuilder.setCancelable(false);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void GetListEstabliments(final String nomEsdeveniment, final String nomEstabliment, final String direccio) {
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "esdeveniments/";

        if(nomEsdeveniment != null)
            url += "?nomEsdeveniment=" + nomEsdeveniment;
        else if(nomEstabliment != null)
            url += "?nomEstabliment=" + nomEstabliment;
        else if(direccio != null)
            url += "?direccio=" + direccio;

        JsonArrayRequest JsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        esdevenimentListInfo.resetAll();
                        for(int i = 0; i < response.length(); ++i){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                //Info esdeveniment
                                long id = jsonObject.getLong("id");
                                String nom = jsonObject.getString("nom");
                                String nomEstabliment = jsonObject.getString("nomEstabliment");

                                esdevenimentListInfo.addEsdeveniment(id,nom, nomEstabliment);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        esdevenimentListIds = esdevenimentListInfo.getIds();
                        esdevenimentListNoms = esdevenimentListInfo.getNoms();
                        esdevenimentListNomsEstabliment = esdevenimentListInfo.getNomsEstabliment();

                        esdevenimentListView = view.findViewById(R.id.esdevenimentsList);
                        ListEsdevenimentsFragment.AdaptadorListEsdeveniments adaptador = new ListEsdevenimentsFragment.AdaptadorListEsdeveniments();
                        esdevenimentListView.setAdapter(adaptador);

                        buscar.setEnabled(true);
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 401)
                    Toast.makeText(getActivity(), "No se indica el token o no es válido o el id no es el asociado al token", Toast.LENGTH_LONG).show();

                buscar.setEnabled(true);
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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(JsonArrayRequest);
    }

    public class AdaptadorListEsdeveniments extends BaseAdapter {

        @Override
        public int getCount() {
            return esdevenimentListIds.size();
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
            View view = getLayoutInflater().inflate(R.layout.esdeveniment_item_list, null);
            TextView nomEsdeveniment = view.findViewById(R.id.eventName);
            TextView nomEstabliment = view.findViewById(R.id.barName);
            Button infoButton = view.findViewById(R.id.infoButton);

            nomEsdeveniment.setText(esdevenimentListNoms.get(position));
            nomEstabliment.setText(esdevenimentListNomsEstabliment.get(position));

            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    esdevenimentInfo.setId(esdevenimentListIds.get(position));

                    Intent intent = new Intent(getActivity(), InfoEsdevenimentActivity.class);
                    startActivity(intent);
                }
            });

            return view;
        }
    }

}
