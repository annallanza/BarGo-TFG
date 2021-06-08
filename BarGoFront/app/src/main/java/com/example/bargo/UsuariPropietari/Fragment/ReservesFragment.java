package com.example.bargo.UsuariPropietari.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bargo.Propietari;
import com.example.bargo.R;
import com.example.bargo.UsuariPropietari.Model.ReservesListInfo;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;

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


public class ReservesFragment extends Fragment {

    private ListView reservasListView;
    private List<Long> reservaListIds;
    private List<String> reservaListNoms;
    private List<String> reservaListInformacions;
    View view;

    private long id = -1;
    private boolean clickValidar = false;
    private ProgressDialog progressDialog;
    private final Propietari propietari = Propietari.getInstance();
    private final ReservesListInfo reservesListInfo = ReservesListInfo.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reserves, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        return view;
    }

    public void onResume() {
        super.onResume();
        GetListReserves();
    }

    private void showConfirmacio(String titol, String missatge){
        final AlertDialog alertdialog = new AlertDialog.Builder(getActivity())
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
                DeleteReserva();
            }
        });
    }

    private void GetListReserves() {
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "reserves/propietari/" + propietari.getId();

        JsonArrayRequest JsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onResponse(JSONArray response) {
                        reservesListInfo.resetAll();
                        for(int i = 0; i < response.length(); ++i){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                //Info reserva
                                long id = jsonObject.getLong("id");
                                String nom = jsonObject.getString("nomUsuari");
                                String dia = jsonObject.getString("dia");
                                String hora = jsonObject.getString("hora");
                                int numPersones = jsonObject.getInt("numPersones");
                                boolean exterior = jsonObject.getBoolean("exterior");

                                Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(dia);
                                dia = new SimpleDateFormat("dd/MM/yyyy").format(fecha);

                                String persones = " persona";
                                if(numPersones > 1)
                                    persones += "s";

                                String ubicacio = "interior";
                                if(exterior)
                                    ubicacio = "exterior";

                                String informacio = "El " + dia + " a las " + hora.substring(0,5) + " para " + numPersones + persones + " en el " + ubicacio;

                                reservesListInfo.addReserva(id, nom, informacio);
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        reservaListIds = reservesListInfo.getIds();
                        reservaListNoms = reservesListInfo.getNoms();
                        reservaListInformacions = reservesListInfo.getInformacions();

                        reservasListView = view.findViewById(R.id.ReservesList);
                        ReservesFragment.AdaptadorListMisReservas adaptador = new ReservesFragment.AdaptadorListMisReservas();
                        reservasListView.setAdapter(adaptador);

                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                    Toast.makeText(getContext(), "No se ha podido conectar con el servidor. Comprueba tu conexión a internet.", Toast.LENGTH_LONG).show();
                else if(error.networkResponse != null && error.networkResponse.statusCode == 404) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        String missatgeError = data.getString("missatge");

                        Toast.makeText(getActivity(), missatgeError, Toast.LENGTH_LONG).show();

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(error.networkResponse != null && error.networkResponse.statusCode == 401)
                    Toast.makeText(getActivity(), "No se indica el token o no es válido o el id no es el asociado al token", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(), "Se ha producido un error, vuélvelo a intentar más tarde.", Toast.LENGTH_LONG).show();

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

    private void DeleteReserva(){
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "reserves/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String missatge = response.getString("missatge");

                            if(clickValidar)
                                Toast.makeText(getActivity(), "Se ha validado la reserva", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getActivity(), missatge, Toast.LENGTH_LONG).show();

                            clickValidar = false;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();

                        GetListReserves();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                    Toast.makeText(getContext(), "No se ha podido conectar con el servidor. Comprueba tu conexión a internet.", Toast.LENGTH_LONG).show();
                else if(error.networkResponse != null && error.networkResponse.statusCode == 404) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        String missatgeError = data.getString("missatge");

                        Toast.makeText(getActivity(), missatgeError, Toast.LENGTH_LONG).show();

                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(error.networkResponse != null && error.networkResponse.statusCode == 401)
                    Toast.makeText(getActivity(), "No se indica el token o no es válido o el id asociado a la reserva no pertenece al usuario asociado al token", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(), "Se ha producido un error, vuélvelo a intentar más tarde.", Toast.LENGTH_LONG).show();

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

    class AdaptadorListMisReservas extends BaseAdapter {

        @Override
        public int getCount() {
            return reservaListIds.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.reserva_item_list_propietari, null);

            TextView nomUsuari = view.findViewById(R.id.nomUsuariReserva);
            TextView informacioReserva = view.findViewById(R.id.infoReserva);
            Button validar = view.findViewById(R.id.validarButton);
            Button eliminar = view.findViewById(R.id.eliminarButton);

            nomUsuari.setText(reservaListNoms.get(position));
            informacioReserva.setText(reservaListInformacions.get(position));

            validar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = reservaListIds.get(position);
                    clickValidar = true;

                    showConfirmacio("Validar reserva", "¿Estás seguro de querer validar la reserva?");
                }
            });

            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = reservaListIds.get(position);

                    showConfirmacio("Eliminar reserva", "¿Estás seguro de querer eliminar la reserva?");
                }
            });

            return view;
        }
    }
}