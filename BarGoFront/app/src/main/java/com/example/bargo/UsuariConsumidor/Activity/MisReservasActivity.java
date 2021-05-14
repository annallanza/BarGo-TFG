package com.example.bargo.UsuariConsumidor.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
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
import com.example.bargo.Consumidor;
import com.example.bargo.UsuariConsumidor.Model.MisReservasListInfo;
import com.example.bargo.R;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MisReservasActivity extends AppCompatActivity {

    private ListView reservasListView;
    private List<Long> reservaListIds;
    private List<String> reservaListNoms;
    private List<String> reservaListDireccions;
    private List<String> reservaListInformacions;
    private List<String> reservaListImatges;
    View view;

    private long id = -1;
    private ProgressDialog progressDialog;
    private final Consumidor consumidor = Consumidor.getInstance();
    private final MisReservasListInfo misReservasListInfo = MisReservasListInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reservas);

        progressDialog = new ProgressDialog(MisReservasActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
    }

    public void onResume() {
        super.onResume();
        GetListReserves();
    }

    private void showConfirmacio(String titol, String missatge){
        final AlertDialog alertdialog = new AlertDialog.Builder(MisReservasActivity.this)
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

        String url = VariablesGlobals.getUrlAPI() + "reserves/consumidor/" + consumidor.getId();

        JsonArrayRequest JsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onResponse(JSONArray response) {
                        misReservasListInfo.resetAll();
                        for(int i = 0; i < response.length(); ++i){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                //Info reserva
                                long id = jsonObject.getLong("id");
                                String nom = jsonObject.getString("nomEstabliment");
                                String imatge = jsonObject.getString("imatge");
                                String direccio = jsonObject.getString("direccio");
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

                                misReservasListInfo.addReserva(id, nom, direccio, informacio, imatge);
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        reservaListIds = misReservasListInfo.getIds();
                        reservaListNoms = misReservasListInfo.getNoms();
                        reservaListDireccions = misReservasListInfo.getDireccions();
                        reservaListInformacions = misReservasListInfo.getInformacions();
                        reservaListImatges = misReservasListInfo.getImatges();

                        reservasListView = findViewById(R.id.mis_reservasList);
                        MisReservasActivity.AdaptadorListMisReservas adaptador = new MisReservasActivity.AdaptadorListMisReservas();
                        reservasListView.setAdapter(adaptador);

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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(JsonArrayRequest);
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

                            Toast.makeText(getApplicationContext(), missatge, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();

                        GetListReserves();
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
                    Toast.makeText(getApplicationContext(), "No se indica el token o no es válido o el id asociado a la reserva no pertenece al usuario asociado al token", Toast.LENGTH_LONG).show();

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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    class AdaptadorListMisReservas extends BaseAdapter {

        @Override
        public int getCount() {
            return reservaListImatges.size();
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
            View view = getLayoutInflater().inflate(R.layout.reserva_item_list, null);
            
            TextView nomEstabliment = view.findViewById(R.id.nomEstablimentText);
            TextView direccioEstabliment = view.findViewById(R.id.direccioText);
            TextView barReservationInfo = view.findViewById(R.id.infoText);
            CircleImageView imatgeEstabliment = view.findViewById(R.id.circleImageView3);
            Button cancelar = view.findViewById(R.id.cancelButton);

            nomEstabliment.setText(reservaListNoms.get(position));
            direccioEstabliment.setText(reservaListDireccions.get(position));
            barReservationInfo.setText(reservaListInformacions.get(position));

            String imatge = reservaListImatges.get(position);
            if(!imatge.equals("null")){
                byte[] bytesimatge = Base64.getDecoder().decode(imatge);
                Bitmap bmp = BitmapFactory.decodeByteArray(bytesimatge, 0, bytesimatge.length);
                imatgeEstabliment.setImageBitmap(bmp);
            }
            else
                imatgeEstabliment.setImageResource(R.drawable.logo);

            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = reservaListIds.get(position);

                    showConfirmacio("Cancelar reserva", "¿Estás seguro de querer cancelar la reserva?");
                }
            });

            return view;
        }
    }
}