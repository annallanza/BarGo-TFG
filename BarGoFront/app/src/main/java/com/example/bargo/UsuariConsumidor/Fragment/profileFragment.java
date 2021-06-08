package com.example.bargo.UsuariConsumidor.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.bargo.Consumidor;
import com.example.bargo.UsuariConsumidor.Activity.ConfiguracioConsumidorActivity;
import com.example.bargo.UsuariConsumidor.Activity.PremisActivity;
import com.example.bargo.UsuariConsumidor.Activity.MisReservasActivity;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;
import com.example.bargo.R;
import com.example.bargo.UsuariConsumidor.Activity.ReptesActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileFragment extends Fragment {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static final int STORAGE_PERMISSION_CODE = 21;
    private CircleImageView imatgeView;
    private Button reserves;
    private ImageButton configuracio;
    private Button productes;
    private Button reptes;
    private TextView nomUsuariTextView;
    private TextView puntuacio;

    private ProgressDialog progressDialog;
    private final Consumidor consumidor = Consumidor.getInstance();
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        imatgeView = view.findViewById(R.id.circleImageView);
        reserves = view.findViewById(R.id.misReservas_button);
        configuracio = view.findViewById(R.id.imageButtonConfiguracio);
        nomUsuariTextView = view.findViewById(R.id.user_name_profile);
        productes = view.findViewById(R.id.exchangeBttn);
        reptes = view.findViewById(R.id.makeChallengeBttn);
        puntuacio = view.findViewById(R.id.points);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        nomUsuariTextView.setText(consumidor.getNom());

        refreshPoints();

        imatgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmacio("Foto perfil", "¿Quieres cambiar la foto de perfil?");
            }
        });

        reserves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mis_reservas_activity = new Intent(getActivity(), MisReservasActivity.class);
                startActivity(mis_reservas_activity);
            }
        });

        configuracio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConfiguracioConsumidorActivity.class);
                startActivity(intent);
            }
        });


        productes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent list_products_activity = new Intent(getActivity(), PremisActivity.class);
                startActivity(list_products_activity);
            }
        });

        reptes.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent retos_activity = new Intent(getActivity(), ReptesActivity.class);
                startActivity(retos_activity);
            }
        });

        return view;
    }

    public void onResume() {
        GetPuntuacioConsumidor();
        GetInfoConsumidorRequest();
        super.onResume();
    }

    private void obrirGaleriaFotos() {
        Intent intentFotoGaleria = new Intent(Intent.ACTION_PICK);

        File directoriFoto = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pathDirectoriFotos = directoriFoto.getPath();

        Uri data = Uri.parse(pathDirectoriFotos);

        intentFotoGaleria.setDataAndType(data, "image/*");

        startActivityForResult(intentFotoGaleria, IMAGE_GALLERY_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.O) //Minima versio d'Android = OREO 8.0
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getActivity();
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == IMAGE_GALLERY_REQUEST){
                Uri imatgeUri = data.getData();

                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(imatgeUri);

                    Bitmap imatgeBitmap = BitmapFactory.decodeStream(inputStream);

                    int ampleActual = imatgeBitmap.getWidth();
                    int altActual = imatgeBitmap.getHeight();

                    if(ampleActual > altActual)
                        imatgeBitmap = redimensionarImatgeBitmap(imatgeBitmap, 600, 400); //Si vull que la imatge ocupi menys espai, canviar parametres
                    else if(ampleActual < altActual)
                        imatgeBitmap = redimensionarImatgeBitmap(imatgeBitmap, 400, 600); //Si vull que la imatge ocupi menys espai, canviar parametres
                    else
                        imatgeBitmap = redimensionarImatgeBitmap(imatgeBitmap, 400, 400); //Si vull que la imatge ocupi menys espai, canviar parametres

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imatgeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imatgeByteArray = baos.toByteArray();
                    String imatgeBase64 = Base64.getEncoder().encodeToString(imatgeByteArray);

                    PutImatgeConsumidor(imatgeBase64, imatgeByteArray);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "No se ha podido abrir la imagen", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    imatgeView.setEnabled(true);
                }
            }
        }
    }

    private void refreshPoints(){
        int pts = consumidor.getPuntuacio();
        if (pts < 0) pts = 0;
        String p = String.valueOf(pts);
        puntuacio.setText(p + " " + getString(R.string.points));
    }

    private void refrescarImatge(){

        byte[] imatgeBytes = consumidor.getImatge();
        if(imatgeBytes != null){
            Bitmap bmp = BitmapFactory.decodeByteArray(imatgeBytes, 0, imatgeBytes.length);
            imatgeView.setImageBitmap(bmp);
        }
        else
            imatgeView.setImageResource(R.drawable.logo);
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

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    imatgeView.setEnabled(false);
                    obrirGaleriaFotos();
                }
                else
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            }
        });
    }

    private Bitmap redimensionarImatgeBitmap(Bitmap imatgeBitmap, float ampleNou, float altNou){
        int ampleActual = imatgeBitmap.getWidth();
        int altActual = imatgeBitmap.getHeight();

        if(ampleActual > ampleNou || altActual > altNou){
            float escalaAmple = ampleNou/ampleActual;
            float escalaAlt = altNou/altActual;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAmple,escalaAlt);

            return Bitmap.createBitmap(imatgeBitmap, 0, 0, ampleActual, altActual, matrix, false);
        }
        else return imatgeBitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imatgeView.setEnabled(false);
                obrirGaleriaFotos();
            }
        }
    }

    private void GetPuntuacioConsumidor() {
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "consumidors/" + consumidor.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int puntuacio = Integer.parseInt(response.getString("puntuacio"));

                        consumidor.setPuntuacio(puntuacio);

                        refreshPoints();
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
                headers.put("Authorization", "Bearer " + consumidor.getToken());
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void GetInfoConsumidorRequest() {
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "usuaris/" + consumidor.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        long id = response.getLong("id");
                        String nomUsuari = response.getString("nomUsuari");
                        String correu = response.getString("correu");
                        String imatge = response.getString("imatge");

                        consumidor.setId(id);
                        consumidor.setNom(nomUsuari);
                        consumidor.setCorreu(correu);
                        if(!imatge.equals("null")){
                            byte[] bytesimatge = Base64.getDecoder().decode(imatge);
                            consumidor.setImatge(bytesimatge);
                        }
                        else consumidor.setImatge(null);

                        nomUsuariTextView.setText(nomUsuari);
                        refrescarImatge();

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
                headers.put("Authorization", "Bearer " + consumidor.getToken());
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void PutImatgeConsumidor(final String imatge, final byte[] imatgeByteArray) {
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "usuaris/" + consumidor.getId();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    consumidor.setImatge(imatgeByteArray);
                    refrescarImatge();
                    imatgeView.setEnabled(true);
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error.networkResponse.statusCode == 404 || error.networkResponse.statusCode == 401) {
                        try {
                            String missatgeError = new String(error.networkResponse.data, "utf-8");

                            Toast.makeText(getContext(), missatgeError, Toast.LENGTH_LONG).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    imatgeView.setEnabled(true);
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

            @Override
            public Map<String, String> getParams () {
                HashMap<String, String> params = new HashMap<>();
                params.put("imatge", imatge);
                return params;
            }
        };

        int timeout = 15000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
