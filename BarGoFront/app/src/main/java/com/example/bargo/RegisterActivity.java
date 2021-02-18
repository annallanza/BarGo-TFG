package com.example.bargo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private TextView login;
    private CheckBox cons;
    private CheckBox prop;
    private EditText email;
    private EditText passw;
    private EditText name;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login = findViewById(R.id.textViewRegistrate);
        String text = "¿Ya tienes tu cuenta? Inicia sesión";
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                openLoginActivity();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.colorBarGo));
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan1, 22, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        login.setText(ss);
        login.setMovementMethod(LinkMovementMethod.getInstance());

        cons = findViewById(R.id.checkBox);
        prop = findViewById(R.id.checkBox2);
        email = findViewById(R.id.editTextEmail);
        passw = findViewById(R.id.editTextContra);
        name = findViewById(R.id.editTextNombre);

        signupButton = findViewById(R.id.buttonAcceder);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User usuari = User.getInstance();
                String correu = email.getText().toString();
                String contra = passw.getText().toString();
                String nom = name.getText().toString();
                if(correu.equals("") || contra.equals("") || nom.equals("")) {
                    Toast.makeText(getApplicationContext(), "Introduzca unas credenciales válidas", Toast.LENGTH_LONG).show();
                }
                else {
                    usuari.setEmail(correu);
                    usuari.setName(contra);
                    usuari.setPassword(nom);
                    openMainActivity();
                }
            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkBox:
                if (checked) prop.setChecked(false);
                break;
            case R.id.checkBox2:
                if (checked) cons.setChecked(false);
                break;
        }
    }


}