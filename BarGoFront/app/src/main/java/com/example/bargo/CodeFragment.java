package com.example.bargo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class CodeFragment extends Fragment {
    EditText introducedCode;
    Button button;
    Button btnScanner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_code, container, false);

        introducedCode = (EditText)rootView.findViewById(R.id.code);
        button = (Button)rootView.findViewById(R.id.button);
        btnScanner = rootView.findViewById(R.id.scan);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = introducedCode.getText().toString();
                if (TextUtils.isEmpty(code)) Toast.makeText(getActivity().getBaseContext(), "Introduzca un código valido", Toast.LENGTH_SHORT).show();
                else {
                    if (code.contains("BAR")) { //en caso de ser un código de un bar
                        Toast.makeText(getActivity().getBaseContext(), "Se ha añadido el bar en su lista de correbares", Toast.LENGTH_SHORT).show();
                        RetosInfo.getInstance().setContext(getActivity().getBaseContext());
                        RetosInfo.getInstance().changeChallenges("BAR");
                        // Añadir bar no se a que lista...
                    }
                    else if (code.contains("PROD")) { //en caso de producto de bar
                        Toast.makeText(getActivity().getBaseContext(), "¡Canjeado! ¡+100 puntos!", Toast.LENGTH_SHORT).show();
                        User.getInstance().addPoints(100);
                        RetosInfo.getInstance().setContext(getActivity().getBaseContext());
                        RetosInfo.getInstance().changeChallenges("BEER");
                    }
                    else Toast.makeText(getActivity().getBaseContext(), "Introduzca un código valido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escanear();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public void escanear() {
        IntentIntegrator intent = IntentIntegrator.forSupportFragment(CodeFragment.this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intent.setPrompt("ESCANEAR CODIGO");
        intent.setCameraId(0);
        intent.setBeepEnabled(false);
        intent.setBarcodeImageEnabled(false);
        intent.setOrientationLocked(true);
        intent.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getActivity().getBaseContext(), "Cancelaste el escaneo", Toast.LENGTH_SHORT).show();
            }else{
                introducedCode.setText(result.getContents().toString());
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


}
