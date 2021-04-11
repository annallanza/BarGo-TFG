package com.example.bargo.UsuariPropietari.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
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
    private EditText contrasenyaNovaEditText;
    private EditText confirmarContrasenyaNovaEditText;
    private EditText nomUsuariEditText;
    private Button guardarCanvis;
    private CheckBox veureContrasenya;
    private EditText nomEstablimentEditText;
    private EditText direccioEditText;
    private CheckBox exteriorCheckBox;
    private EditText numCadiresEditText;
    private EditText numTaulesEditText;
    private int contador;
    private TextView horariTextView;
    private Button horariMes;
    private Button horariMenys;
    private EditText horariEditText1;
    private TextView guioTextView;
    private EditText horariEditText2;
    private EditText horariEditText3;
    private TextView guioTextView2;
    private EditText horariEditText4;
    private EditText horariEditText5;
    private TextView guioTextView3;
    private EditText horariEditText6;
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
        contrasenyaNovaEditText = view.findViewById(R.id.editTextContraNova);
        confirmarContrasenyaNovaEditText = view.findViewById(R.id.editTextConfirmaContraNova);
        veureContrasenya = view.findViewById(R.id.checkBoxContraseña2);
        isConsumidor = view.findViewById(R.id.checkBox);
        isPropietari = view.findViewById(R.id.checkBox2);
        nomEstablimentEditText = view.findViewById(R.id.editTextNomEstabliment);
        direccioEditText = view.findViewById(R.id.editTextDireccio);
        exteriorCheckBox = view.findViewById(R.id.checkBoxExterior);
        numCadiresEditText = view.findViewById(R.id.editTextNumCadires);
        numTaulesEditText = view.findViewById(R.id.editTextNumTaules);
        contador = 1;
        horariTextView = view.findViewById(R.id.textViewHorari);
        horariMes = view.findViewById(R.id.buttonMES);
        horariMenys = view.findViewById(R.id.buttonMENYS);
        horariEditText1 = view.findViewById(R.id.editTextHorari);
        guioTextView = view.findViewById(R.id.textViewGuio);
        horariEditText2 = view.findViewById(R.id.editTextHorari2);
        horariEditText3 = view.findViewById(R.id.editTextHorari3);
        guioTextView2 = view.findViewById(R.id.textViewGuio2);
        horariEditText4 = view.findViewById(R.id.editTextHorari4);
        horariEditText5 = view.findViewById(R.id.editTextHorari5);
        guioTextView3 = view.findViewById(R.id.textViewGuio3);
        horariEditText6 = view.findViewById(R.id.editTextHorari6);
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
                    contrasenyaNovaEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmarContrasenyaNovaEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    contrasenyaNovaEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmarContrasenyaNovaEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        horariMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador == 1) {
                    ++contador;
                    horariEditText3.setVisibility(View.VISIBLE);
                    guioTextView2.setVisibility(View.VISIBLE);
                    horariEditText4.setVisibility(View.VISIBLE);
                }
                else if(contador == 2){
                    ++contador;
                    horariEditText5.setVisibility(View.VISIBLE);
                    guioTextView3.setVisibility(View.VISIBLE);
                    horariEditText6.setVisibility(View.VISIBLE);
                }
                else
                    Toast.makeText(getContext(), "No puedes añadir más de 3", Toast.LENGTH_LONG).show();
            }
        });

        horariMenys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador == 2) {
                    --contador;
                    horariEditText3.setText("");
                    horariEditText4.setText("");

                    horariEditText3.setVisibility(View.GONE);
                    guioTextView2.setVisibility(View.GONE);
                    horariEditText4.setVisibility(View.GONE);
                }
                else if(contador == 3){
                    --contador;
                    horariEditText5.setText("");
                    horariEditText6.setText("");

                    horariEditText5.setVisibility(View.GONE);
                    guioTextView3.setVisibility(View.GONE);
                    horariEditText6.setVisibility(View.GONE);
                }
                else
                    Toast.makeText(getContext(), "Has de indicar como mínimo un horario", Toast.LENGTH_LONG).show();
            }
        });

        horariEditText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHorariClicked(v);
            }
        });

        horariEditText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHorariClicked(v);
            }
        });

        horariEditText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHorariClicked(v);
            }
        });

        horariEditText4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHorariClicked(v);
            }
        });

        horariEditText5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHorariClicked(v);
            }
        });

        horariEditText6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHorariClicked(v);
            }
        });

        guardarCanvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCanvis.setEnabled(false);
                progressDialog.show();

                String nomUsuari = nomUsuariEditText.getText().toString();
                String contrasenyaNova = contrasenyaNovaEditText.getText().toString();
                String confirmarContrasenyaNova = confirmarContrasenyaNovaEditText.getText().toString();
                String nomEstabliment = nomEstablimentEditText.getText().toString();
                String direccioEstabliment = direccioEditText.getText().toString();
                Boolean exteriorEstabliment = exteriorCheckBox.isChecked();
                String numCadiresEstabliment = numCadiresEditText.getText().toString();
                String numTaulesEstabliment = numTaulesEditText.getText().toString();
                String horariEstabliment = horariEditText1.getText().toString() + "-" + horariEditText2.getText().toString();

                if(!horariEditText3.getText().toString().equals(""))
                    horariEstabliment += "y" + horariEditText3.getText().toString() + "-" + horariEditText4.getText().toString();
                if(!horariEditText5.getText().toString().equals(""))
                    horariEstabliment += "y" + horariEditText5.getText().toString() + "-" + horariEditText6.getText().toString();

                String descripcioEstabliment = descripcioEditText.getText().toString();
                String paginaWebEstabliment = paginaWebEditText.getText().toString();

                if(!paginaWebEstabliment.contains("https://"))
                    paginaWebEstabliment = "https://" + paginaWebEstabliment;

                if(!contrasenyaNova.equals(confirmarContrasenyaNova)) {
                    Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                    guardarCanvis.setEnabled(true);
                    progressDialog.dismiss();
                }
                else if(numCadiresEstabliment.equals("")){
                    Toast.makeText(getContext(), "Indica el número de sillas", Toast.LENGTH_LONG).show();
                    guardarCanvis.setEnabled(true);
                    progressDialog.dismiss();
                }
                else if(numTaulesEstabliment.equals("")){
                    Toast.makeText(getContext(), "Indica el número de mesas", Toast.LENGTH_LONG).show();
                    guardarCanvis.setEnabled(true);
                    progressDialog.dismiss();
                }
                else if(horariEditText1.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Indica el horario", Toast.LENGTH_LONG).show();
                    guardarCanvis.setEnabled(true);
                    progressDialog.dismiss();
                }
                else if(horariEditText2.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Indica el horario", Toast.LENGTH_LONG).show();
                    guardarCanvis.setEnabled(true);
                    progressDialog.dismiss();
                }
                else if(!horariEditText3.getText().toString().equals("") && horariEditText4.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Indica el horario", Toast.LENGTH_LONG).show();
                    guardarCanvis.setEnabled(true);
                    progressDialog.dismiss();
                }
                else if(!horariEditText5.getText().toString().equals("") && horariEditText6.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Indica el horario", Toast.LENGTH_LONG).show();
                    guardarCanvis.setEnabled(true);
                    progressDialog.dismiss();
                }
                else {
                    int numCadires = Integer.parseInt(numCadiresEditText.getText().toString());
                    int numTaules = Integer.parseInt(numTaulesEditText.getText().toString());

                    PutInfoConsumidorRequest(nomUsuari, contrasenyaNova, nomEstabliment, direccioEstabliment, exteriorEstabliment, numCadires, numTaules, horariEstabliment, descripcioEstabliment, paginaWebEstabliment);
                }
            }
        });

        return view;
    }

    private void guardarTokenASharedPreferences(String token){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sessio", getActivity().MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        editor.putString("token",token);

        editor.apply();
    }

    public void onResume() {
        GetInfoPropietariRequest();
        super.onResume();
    }

    public void onHorariClicked(View v){
        final EditText horariEditTextSegonsID = v.findViewById(v.getId());

        final Calendar calendar = Calendar.getInstance();

        final int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuts = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay < 10) {
                    if(minute < 10)
                        horariEditTextSegonsID.setText("0" + hourOfDay + ":0" + minute);
                    else
                        horariEditTextSegonsID.setText("0" + hourOfDay + ":" + minute);
                }
                else {
                    if(minute < 10)
                        horariEditTextSegonsID.setText(hourOfDay + ":0" + minute);
                    else
                        horariEditTextSegonsID.setText(hourOfDay + ":" + minute);
                }
            }
        }, hora, minuts, false);

        timePickerDialog.show();
    }

    private void refrescarDadesPopietari(){
        nomUsuariEditText.setText(propietari.getNom());
        contrasenyaNovaEditText.setText("");
        confirmarContrasenyaNovaEditText.setText("");

        nomEstablimentEditText.setText(propietari.getNomEstabliment());
        direccioEditText.setText(propietari.getDireccioEstabliment());
        exteriorCheckBox.setChecked(propietari.getExteriorEstabliment());
        numCadiresEditText.setText(Integer.toString(propietari.getNumCadiresEstabliment()));
        numTaulesEditText.setText(Integer.toString(propietari.getNumTaulesEstabliment()));

        String horari = propietari.getHorariEstabliment();
        String[] horarisy = horari.split("y");

        ArrayList<String> horarisDividits = new ArrayList<>();
        for(int i = 0; i < horarisy.length; ++i){
            String[] horaris_ = horarisy[i].split("-");

            horarisDividits.addAll(Arrays.asList(horaris_));
        }

        while(horarisDividits.size() < 6){
            horarisDividits.add("");
        }

        horariEditText1.setText(horarisDividits.get(0));
        horariEditText2.setText(horarisDividits.get(1));
        horariEditText3.setText(horarisDividits.get(2));
        horariEditText4.setText(horarisDividits.get(3));
        horariEditText5.setText(horarisDividits.get(4));
        horariEditText6.setText(horarisDividits.get(5));

        if(!horariEditText3.getText().toString().equals("")) {
            horariEditText3.setVisibility(View.VISIBLE);
            horariEditText4.setVisibility(View.VISIBLE);
            contador = 2;
        }
        if(!horariEditText5.getText().toString().equals("")) {
            horariEditText5.setVisibility(View.VISIBLE);
            horariEditText6.setVisibility(View.VISIBLE);
            contador = 3;
        }

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

                            refrescarDadesPopietari();
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

                        guardarTokenASharedPreferences(token);

                        long id = Jwts.parser().setSigningKey(VariablesGlobals.getSecret().getBytes()).parseClaimsJws(token).getBody().get("id", Long.class);
                        ArrayList rols = Jwts.parser().setSigningKey(VariablesGlobals.getSecret().getBytes()).parseClaimsJws(token).getBody().get("rols", ArrayList.class);

                        String rol_usuari = (String) rols.get(0);

                        if(rol_usuari.equals("ROL_CONSUMIDOR")){
                            Consumidor consumidor = Consumidor.getInstance();
                            consumidor.setAlmostAll(id,nomUsuari,token);
                        }
                        else if(rol_usuari.equals("ROL_PROPIETARI")){
                            Propietari propietari = Propietari.getInstance();
                            propietari.setAlmostAll(id,nomUsuari,token);
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