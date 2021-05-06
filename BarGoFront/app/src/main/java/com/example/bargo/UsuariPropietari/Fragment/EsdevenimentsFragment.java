package com.example.bargo.UsuariPropietari.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bargo.Propietari;
import com.example.bargo.R;
import com.example.bargo.RegisterActivity;
import com.example.bargo.UsuariConsumidor.Activity.ConfiguracioConsumidorActivity;
import com.example.bargo.UsuariPropietari.Activity.NouEsdevenimentActivity;
import com.example.bargo.UsuariPropietari.Model.EsdevenimentListInfo;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EsdevenimentsFragment extends Fragment {
    private ListView esdevenimentListView;
    private List<Long> esdevenimentListIds;
    private List<String> esdevenimentListNoms;
    private List<String> esdevenimentListDies;
    private List<String> esdevenimentListHores;
    private FloatingActionButton addEvent;
    View view;

    private long id = -1;
    private ProgressDialog progressDialog;
    private final Propietari propietari = Propietari.getInstance();
    private final EsdevenimentListInfo esdevenimentListInfo = EsdevenimentListInfo.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_esdeveniments, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        addEvent = view.findViewById(R.id.addEvent);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NouEsdevenimentActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void onResume() {
        super.onResume();
        GetListEsdeveniments();
    }

    private void showConfirmacio(String titol, String missatge){
        final AlertDialog alertdialog = new AlertDialog.Builder(getContext())
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
                DeleteEsdeveniment();
            }
        });
    }

    private void GetListEsdeveniments() {
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "esdeveniments/propietari/" + propietari.getId();

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
                                String dia = jsonObject.getString("dia");
                                String hora = jsonObject.getString("hora");

                                esdevenimentListInfo.addEsdeveniment(id,nom, dia, hora);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        esdevenimentListIds = esdevenimentListInfo.getIds();
                        esdevenimentListNoms = esdevenimentListInfo.getNoms();
                        esdevenimentListDies = esdevenimentListInfo.getDies();
                        esdevenimentListHores = esdevenimentListInfo.getHores();

                        esdevenimentListView = view.findViewById(R.id.esdevenimentsListPropietari);
                        EsdevenimentsFragment.AdaptadorEsdeveniments adaptador = new EsdevenimentsFragment.AdaptadorEsdeveniments();
                        esdevenimentListView.setAdapter(adaptador);

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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(JsonArrayRequest);
    }

    private void DeleteEsdeveniment(){
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "esdeveniments/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String missatge = response.getString("missatge");

                            Toast.makeText(getActivity(), missatge, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();

                        GetListEsdeveniments();
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
                    Toast.makeText(getActivity(), "No se indica el token o no es válido o el id asociado al evento no pertenece al establecimiento asociado al token del usuario", Toast.LENGTH_LONG).show();

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

    public class AdaptadorEsdeveniments extends BaseAdapter {

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

        @SuppressLint("SimpleDateFormat")
        @Override
        public View getView(final int position, View covertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.esdeveniment_item_list_propietari, null);
            TextView nomEsdeveniment = view.findViewById(R.id.nomEsdeveniment2);
            TextView dia = view.findViewById(R.id.dataEsdeveniment);
            Button deleteButton = view.findViewById(R.id.deleteButton);

            nomEsdeveniment.setText(esdevenimentListNoms.get(position));

            Date fecha = null;
            try {
                fecha = new SimpleDateFormat("yyyy-MM-dd").parse(esdevenimentListDies.get(position));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String data = "El ";
            data += new SimpleDateFormat("dd/MM/yyyy").format(fecha);

            String hora = esdevenimentListHores.get(position);

            if(hora.startsWith("01"))
                data += " a la " + hora.substring(0, hora.length() - 3);
            else
                data += " a las " + hora.substring(0, hora.length() - 3);

            dia.setText(data);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = esdevenimentListIds.get(position);
                    showConfirmacio("Eliminar evento", "¿Estás seguro de querer eliminar el evento?");
                }
            });

            return view;
        }
    }
}