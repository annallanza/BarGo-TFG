package com.example.bargo.UsuariPropietari.Fragment;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.bargo.Consumidor;
import com.example.bargo.Propietari;
import com.example.bargo.R;
import com.example.bargo.UsuariPropietari.Activity.ConfiguracioPropietariActivity;
import com.example.bargo.VariablesGlobals;
import com.example.bargo.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.jsonwebtoken.Jwts;


public class PerfilFragment extends Fragment {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static final int STORAGE_PERMISSION_CODE = 21;
    private CircleImageView imatgeView;
    private ImageButton configuracio;
    private CheckBox isConsumidor;
    private CheckBox isPropietari;
    private EditText contrasenyaActualEditText;
    private EditText contrasenyaNovaEditText;
    private EditText nomUsuariEditText;
    private Button guardarCanvis;
    private CheckBox veureContrasenya;
    private EditText nomEstablimentEditText;
    private EditText direccioEditText;
    private CheckBox exteriorCheckBox;
    private EditText numCadiresEditText;
    private EditText numTaulesEditText;
    private EditText horariEditText;
    private EditText descripcioEditText;
    private EditText paginaWebEditText;
    private ProgressDialog progressDialog;
    private final Propietari propietari = Propietari.getInstance();
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        imatgeView = view.findViewById(R.id.circleImageView2);
        configuracio = view.findViewById(R.id.imageButtonConfiguracio2);
        nomUsuariEditText = view.findViewById(R.id.editTextNombreUsuario);
        contrasenyaActualEditText = view.findViewById(R.id.editTextContraActual);
        contrasenyaNovaEditText = view.findViewById(R.id.editTextContraNova);
        veureContrasenya = view.findViewById(R.id.checkBoxContraseña2);
        isConsumidor = view.findViewById(R.id.checkBox);
        isPropietari = view.findViewById(R.id.checkBox2);
        nomEstablimentEditText = view.findViewById(R.id.editTextNomEstabliment);
        direccioEditText = view.findViewById(R.id.editTextDireccio);
        exteriorCheckBox = view.findViewById(R.id.checkBoxExterior);
        numCadiresEditText = view.findViewById(R.id.editTextNumCadires);
        numTaulesEditText = view.findViewById(R.id.editTextNumTaules);
        horariEditText = view.findViewById(R.id.editTextHorari);
        descripcioEditText = view.findViewById(R.id.editTextDescripcio);
        paginaWebEditText = view.findViewById(R.id.editTextPaginaWeb);
        guardarCanvis = view.findViewById(R.id.buttonGuardarCanvis);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);

        imatgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmacio("Foto perfil establecimiento", "¿Quieres cambiar la foto de perfil de tu establecimiento?");
            }
        });

        configuracio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConfiguracioPropietariActivity.class);
                startActivity(intent);
            }
        });

        veureContrasenya.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    contrasenyaActualEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    contrasenyaNovaEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    contrasenyaActualEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    contrasenyaNovaEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        guardarCanvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCanvis.setEnabled(false);

                String nomUsuari = nomUsuariEditText.getText().toString();
                String contrasenyaActual = contrasenyaActualEditText.getText().toString();
                String contrasenyaNova = contrasenyaNovaEditText.getText().toString();
                String nomEstabliment = nomEstablimentEditText.getText().toString();
                String direccioEstabliment = direccioEditText.getText().toString();
                Boolean exteriorEstabliment = exteriorCheckBox.isChecked();
                int numCadiresEstabliment = Integer.parseInt(numCadiresEditText.getText().toString());
                int numTaulesEstabliment = Integer.parseInt(numTaulesEditText.getText().toString());
                String horariEstabliment = horariEditText.getText().toString();
                String descripcioEstabliment = descripcioEditText.getText().toString();
                String paginaWebEstabliment = paginaWebEditText.getText().toString();

                if(contrasenyaActual.equals(propietari.getContrasenya())) {
                    progressDialog.show();
                    PutInfoConsumidorRequest(nomUsuari, contrasenyaNova, nomEstabliment, direccioEstabliment, exteriorEstabliment, numCadiresEstabliment, numTaulesEstabliment, horariEstabliment, descripcioEstabliment, paginaWebEstabliment);
                }
                else {
                    Toast.makeText(getContext(), "La contraseña actual no es correcta", Toast.LENGTH_LONG).show();
                    guardarCanvis.setEnabled(true);
                }
            }
        });

        return view;
    }

    public void onResume() {
        GetInfoPropietariRequest();
        super.onResume();
    }

    private void refrescarDadesConsumidor(){
        nomUsuariEditText.setText(propietari.getNom());
        contrasenyaActualEditText.setText("");
        contrasenyaNovaEditText.setText("");

        nomEstablimentEditText.setText(propietari.getNomEstabliment());
        direccioEditText.setText(propietari.getDireccioEstabliment());
        exteriorCheckBox.setChecked(propietari.getExteriorEstabliment());
        numCadiresEditText.setText(Integer.toString(propietari.getNumCadiresEstabliment()));
        numTaulesEditText.setText(Integer.toString(propietari.getNumTaulesEstabliment()));
        horariEditText.setText(propietari.getHorariEstabliment());
        descripcioEditText.setText(propietari.getDescripcioEstabliment());
        paginaWebEditText.setText(propietari.getPaginaWebEstabliment());

        refrescarImatge();
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
                progressDialog.show();
                Uri imatgeUri = data.getData();

                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(imatgeUri);

                    Bitmap imatgeBitmap = BitmapFactory.decodeStream(inputStream);

                    imatgeBitmap = redimensionarImatgeBitmap(imatgeBitmap, 400, 600); //Si vull que la imatge ocupi menys espai, canviar parametres

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imatgeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imatgeByteArray = baos.toByteArray();
                    String imatgeBase64 = Base64.getEncoder().encodeToString(imatgeByteArray);

                    PutImatgePropietari(imatgeBase64, imatgeByteArray);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "No se ha podido abrir la imagen", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    imatgeView.setEnabled(true);
                }
            }
        }
    }

    private void refrescarImatge(){

        byte[] imatgeBytes = propietari.getImatge();
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
        if (requestCode == STORAGE_PERMISSION_CODE)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                imatgeView.setEnabled(false);
                obrirGaleriaFotos();
    }

    private void GetInfoPropietariRequest() {
        progressDialog.show();

        String url = VariablesGlobals.getUrlAPI() + "propietaris/" + propietari.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            long id = response.getLong("id");
                            String nomUsuari = response.getString("nomUsuari");
                            String imatge = response.getString("imatge");
                            JSONObject establiment = response.getJSONObject("establiment");

                            long idEstabliment = establiment.getLong("id");
                            String nomEstabliment = establiment.getString("nom");
                            String direccioEstabliment = establiment.getString("direccio");
                            Boolean exteriorEstabliment = establiment.getBoolean("exterior");
                            int numCadiresEstabliment = establiment.getInt("numCadires");
                            int numTaulesEstabliment = establiment.getInt("numTaules");
                            String horariEstabliment = establiment.getString("horari");
                            String descripcioEstabliment = establiment.getString("descripcio");
                            String paginaWebEstabliment = establiment.getString("paginaWeb");

                            propietari.setId(id);
                            propietari.setNom(nomUsuari);
                            if(!imatge.equals("null")){
                                byte[] bytesimatge = Base64.getDecoder().decode(imatge);
                                propietari.setImatge(bytesimatge);
                            }
                            else propietari.setImatge(null);

                            propietari.setAllEstabliment(idEstabliment,nomEstabliment,direccioEstabliment,exteriorEstabliment,numCadiresEstabliment,numTaulesEstabliment,horariEstabliment,descripcioEstabliment,paginaWebEstabliment);

                            refrescarDadesConsumidor();
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
                headers.put("Authorization", "Bearer " + propietari.getToken());
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void PutInfoConsumidorRequest(final String nomUsuari, final String contrasenyaNova, String nomEstabliment, String direccioEstabliment, Boolean exteriorEstabliment, int numCadiresEstabliment, int numTaulesEstabliment, String horariEstabliment, String descripcioEstabliment, String paginaWebEstabliment){
        String url = VariablesGlobals.getUrlAPI() + "propietaris/";

        JSONObject postData = new JSONObject();
        try {
            postData.put("id", propietari.getId());
            postData.put("nomUsuari", nomUsuari);
            postData.put("contrasenya", contrasenyaNova);
            postData.put("nomEstabliment", nomEstabliment);
            postData.put("direccio", direccioEstabliment);
            postData.put("exterior", exteriorEstabliment);
            postData.put("numCadires", numCadiresEstabliment);
            postData.put("numTaules", numTaulesEstabliment);
            postData.put("horari", horariEstabliment);
            postData.put("descripcio", descripcioEstabliment);
            postData.put("paginaWeb", paginaWebEstabliment);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, postData,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    loginRequest(nomUsuari, contrasenyaNova);
                    try {
                        String missatge = response.getString("missatge");

                        Toast.makeText(getActivity(), missatge, Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error.networkResponse.statusCode == 400 || error.networkResponse.statusCode == 404 || error.networkResponse.statusCode == 409) {
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
                        Toast.makeText(getActivity(), "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();

                    guardarCanvis.setEnabled(true);
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

    private void PutImatgePropietari(final String imatge, final byte[] imatgeByteArray) {

        String url = VariablesGlobals.getUrlAPI() + "usuaris/" + propietari.getId();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        propietari.setImatge(imatgeByteArray);
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
                headers.put("Authorization", "Bearer " + propietari.getToken());
                return headers;
            }

            @Override
            public Map<String, String> getParams () {
                HashMap<String, String> params = new HashMap<>();
                params.put("imatge", imatge);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    public void loginRequest(final String nomUsuari, final String contrasenya){

        String url = VariablesGlobals.getUrlAPI() + "usuaris/auth/login";

        JSONObject postData = new JSONObject();
        try {
            postData.put("nomUsuari", nomUsuari);
            postData.put("contrasenya", contrasenya);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String token = response.getString("token");

                        long id = Jwts.parser().setSigningKey(VariablesGlobals.getSecret().getBytes()).parseClaimsJws(token).getBody().get("id", Long.class);
                        ArrayList rols = Jwts.parser().setSigningKey(VariablesGlobals.getSecret().getBytes()).parseClaimsJws(token).getBody().get("rols", ArrayList.class);

                        String rol_usuari = (String) rols.get(0);

                        if(rol_usuari.equals("ROL_CONSUMIDOR")){
                            Consumidor consumidor = Consumidor.getInstance();
                            consumidor.setAlmostAll(id,nomUsuari,contrasenya,token);
                        }
                        else if(rol_usuari.equals("ROL_PROPIETARI")){
                            Propietari propietari = Propietari.getInstance();
                            propietari.setAlmostAll(id,nomUsuari,contrasenya,token);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    guardarCanvis.setEnabled(true);
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error.networkResponse.statusCode == 400) {
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
                        Toast.makeText(getActivity(), "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();

                    guardarCanvis.setEnabled(true);
                    progressDialog.dismiss();
                }
            }
        );

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

}