package com.example.bargo.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bargo.Activity.ConfiguracioUsuariActivity;
import com.example.bargo.Activity.ListProductActivity;
import com.example.bargo.Activity.MisReservasActivity;
import com.example.bargo.Model.User;
import com.example.bargo.Model.VariablesGlobals;
import com.example.bargo.R;
import com.example.bargo.Activity.RetosActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
    private final User usuari = User.getInstance();
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

        nomUsuariTextView.setText(User.getInstance().getNom());

        refreshPoints();

        imatgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmacio("Foto perfil establecimiento", "¿Quieres cambiar la foto de perfil de tu establecimiento?");
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
                Intent intent = new Intent(getActivity(), ConfiguracioUsuariActivity.class);
                startActivity(intent);
            }
        });


        productes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent list_products_activity = new Intent(getActivity(), ListProductActivity.class);
                startActivity(list_products_activity);
            }
        });

        reptes.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent retos_activity = new Intent(getActivity(), RetosActivity.class);
                startActivity(retos_activity);
            }
        });

        return view;
    }

    public void onResume() {
        GetPuntuacioConsumidor();
        //GetInfoConsumidorRequest();
        refrescarImatge();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getActivity();
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == IMAGE_GALLERY_REQUEST){
                Uri imatgeUri = data.getData();

                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(imatgeUri);

                    Bitmap imatgebitmap = BitmapFactory.decodeStream(inputStream);

                    imatgeView.setImageBitmap(imatgebitmap);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imatgebitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
                    byte[] imatgeByteArray = baos.toByteArray();

                    usuari.setImatge(imatgeByteArray);

                    refrescarImatge();
                    PutImatgeConsumidor();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "No se ha podido abrir la imagen", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void refreshPoints(){
        int pts = User.getInstance().getPuntuacio();
        if (pts < 0) pts = 0;
        String p = String.valueOf(pts);
        puntuacio.setText(p + " " + getString(R.string.points));
    }

    private void refrescarImatge(){

        byte[] imatgeBytes = usuari.getImatge();
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

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    obrirGaleriaFotos();
                else
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                obrirGaleriaFotos();
    }

    private void GetPuntuacioConsumidor() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = VariablesGlobals.getUrlAPI() + "consumidors/" + usuari.getId();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int puntuacio = Integer.parseInt(response.getString("puntuacio"));

                            User usuari = User.getInstance();
                            usuari.setPuntuacio(puntuacio);

                            refreshPoints();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + usuari.getToken());
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private void GetInfoConsumidorRequest() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = VariablesGlobals.getUrlAPI() + "usuaris/" + usuari.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response);
                            long id = response.getLong("id");
                            String nomUsuari = response.getString("nomUsuari");
                            String imatge = response.getString("imatge");

                            usuari.setId(id);
                            usuari.setNom(nomUsuari);
                            if(!imatge.equals("null")){
                                byte[] bytesimatge = imatge.getBytes();
                                usuari.setImatge(bytesimatge);
                            }
                            else usuari.setImatge(null);

                            refrescarImatge();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + usuari.getToken());
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private void PutImatgeConsumidor() { //TODO: fer put imatge consumidor
    }

    //TODO: mirar si fa falta
    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    //TODO: PERMISOS PER GUARDAR LA IMATGE
    //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

}
