package com.example.bargo.UsuariConsumidor.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.bargo.Consumidor;
import com.example.bargo.UsuariConsumidor.Activity.InfoEstablimentActivity;
import com.example.bargo.UsuariConsumidor.Model.EstablimentInfo;
import com.example.bargo.UsuariConsumidor.Model.EstablimentListInfo;
import com.example.bargo.R;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListEstablimentsFragment extends Fragment {

    private ImageView buscar;
    private ImageView filtres;
    private EditText buscador;
    String itemSeleccionat = "null";
    String[] opcions = new String[] {"Nombre establecimiento", "Dirección"};
    private ListView establimentListView;
    private List<Long> establimentListIds;
    private List<String> establimentListNoms;
    private List<String> establimentListImatges;
    private List<String> establimentListDireccions;
    private List<Boolean> establimentListVisitats;
    View view;

    private ProgressDialog progressDialog;
    private final Consumidor consumidor = Consumidor.getInstance();
    private final EstablimentListInfo establimentListInfo = EstablimentListInfo.getInstance();
    private final EstablimentInfo establimentInfo = EstablimentInfo.getInstance();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_establiment, container, false);

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

                    String nomEstabliment = null;
                    String direccio = null;
                    if(itemSeleccionat.equals("Nombre establecimiento"))
                        nomEstabliment = buscador.getText().toString();
                    else if(itemSeleccionat.equals("Dirección"))
                        direccio = buscador.getText().toString();

                    itemSeleccionat = "null";
                    buscador.setText("");

                    GetListEstabliments(nomEstabliment, direccio);
                }
            }
        });

        return  view;
    }

    public void onResume() {
        super.onResume();
        buscador.setText("");
        GetListEstabliments(null, null);
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

    private void GetListEstabliments(final String nomEstabliment, final String direccio) {
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "establiments/all/" + consumidor.getId();

        if(nomEstabliment != null)
            url += "?nomEstabliment=" + nomEstabliment;
        else if(direccio != null)
            url += "?direccio=" + direccio;

        JsonArrayRequest JsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONArray response) {
                        establimentListInfo.resetAll();
                        for(int i = 0; i < response.length(); ++i){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                //Info establiment
                                long id = jsonObject.getLong("id");
                                String nom = jsonObject.getString("nom");
                                String imatge = jsonObject.getString("imatge");
                                String direccio = jsonObject.getString("direccio");
                                Boolean visitat = jsonObject.getBoolean("visitat");

                                establimentListInfo.addEstabliment(id,nom,imatge,direccio,visitat);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        establimentListIds = establimentListInfo.getIds();
                        establimentListNoms = establimentListInfo.getNoms();
                        establimentListImatges = establimentListInfo.getImatges();
                        establimentListDireccions = establimentListInfo.getDireccions();
                        establimentListVisitats = establimentListInfo.getVisitats();

                        establimentListView = view.findViewById(R.id.barsList);
                        AdaptadorListBar adaptador = new AdaptadorListBar();
                        establimentListView.setAdapter(adaptador);

                        buscar.setEnabled(true);
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

    class AdaptadorListBar extends BaseAdapter {

        @Override
        public int getCount() {
            return establimentListImatges.size();
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
            View view = getLayoutInflater().inflate(R.layout.establiment_item_list, null);
            TextView nomEstabliment = view.findViewById(R.id.nomEstablimentText);
            ImageView imatgeEstabliment = view.findViewById(R.id.barImage);
            TextView direccioEstabliment = view.findViewById(R.id.direccioText);
            CheckBox visitatEstabliment = view.findViewById(R.id.visitedCheckBox);
            Button infoButton = view.findViewById(R.id.infoButton);

            nomEstabliment.setText(establimentListNoms.get(position));
            direccioEstabliment.setText(establimentListDireccions.get(position));
            visitatEstabliment.setChecked(establimentListVisitats.get(position));

            String imatge = establimentListImatges.get(position);
            if(!imatge.equals("null")){
                byte[] bytesimatge = Base64.getDecoder().decode(imatge);
                Bitmap bmp = BitmapFactory.decodeByteArray(bytesimatge, 0, bytesimatge.length);
                imatgeEstabliment.setImageBitmap(bmp);
            }
            else
                imatgeEstabliment.setImageResource(R.drawable.logo);

            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    establimentInfo.setId(establimentListIds.get(position));

                    Intent intent = new Intent(getActivity(), InfoEstablimentActivity.class);
                    startActivity(intent);
                }
            });

            return view;
        }
    }

}

