package com.example.bargo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class PoliticaPrivacitatActivity extends AppCompatActivity {

    private TextView politicaPrivacitat;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_privacitat);

        politicaPrivacitat = findViewById(R.id.textViewPoliticaPrivacitat);

        politicaPrivacitat.setText(
                "RESPONSABLE DEL TRATAMIENTO:\n\n" +
                "BarGo\n" +
                "Barcelona\n" +
                "\n" +
                "Si tiene cualquier duda o petición sobre el tratamiento que realizamos de sus datos personales, contacte con nuestro Delegado de Datos en:\ntfgbargoapp@gmail.com\n" +
                "\n\n" +
                "FINALIDAD:\n\n" +
                "BarGo tratará la información que nos proporcionan las personas interesadas con las siguientes finalidades:\n" +
                "\n" +
                "- Gestionar cualquier tipo de solicitud, sugerencia o petición sobre nuestros servicios\n" +
                "- Envio de información por medio electrónico\n" +
                "\n\n" +
                "LEGITIMACIÓN:\n\n" +
                "\n" +
                "Sus datos se conservan mientras sean necesarios para la finalidad para la cual fueron recabados. En cualquier momento podrá retirar el consentimiento, eliminando su cuenta.\n" +
                "\n\n" +
                "PROCEDENCIA:\n\n" +
                "\n" +
                "BarGo obtiene sus datos con la información que usted nos facilita cuando se registra en nuestra aplicación. Estos datos son de carácter identificativo y/o datos referentes a las características del establecimento.\n" +
                "\n\n" +
                "DESTINATARIOS:\n\n" +
                "BarGo no cederá sus datos personales a terceros, salvo que así lo autorice.\n" +
                "\n\n" +
                "DERECHOS:\n\n" +
                "El usuario tiene los siguientes derechos:\n\n" +
                "Derecho de acceso a sus datos personales que se estan tratando.\n\n" +
                "Derecho a la rectificación de los datos personales.\n\n" +
                "Derecho a la supresión de los datos cuando, entre otros motivos, los datos ya no sean necesarios para la finalidad por los que fueron recogidos.\n\n" +
                "Derecho a solicitar la limitación del tratamiento de sus datos.\n\n" +
                "Derecho de oponerse al tratamiento de sus datos personales.\n\n" +
                "Derecho a la portabilidad de sus datos personales.\n\n" +
                "Derecho a retirar el consentimiento prestado.\n\n" +
                "Derecho a reclamar ante la Agencia Española de Protección de Datos.\n\n" +
                "\n" +
                "El usuario puede ejercitar los derechos anteriores, a través de la comunicación mediante correo eletrónico a la dirección arriba indicada.");
    }
}