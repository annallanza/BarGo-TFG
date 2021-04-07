package com.example.bargo.UsuariPropietari.Activity;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.bargo.UsuariPropietari.Fragment.EsdevenimentsFragment;
import com.example.bargo.UsuariPropietari.Fragment.OcupacioFragment;
import com.example.bargo.UsuariPropietari.Fragment.PerfilFragment;
import com.example.bargo.UsuariPropietari.Fragment.ReservesFragment;
import com.example.bargo.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainPropietariActivity extends AppCompatActivity {
    Fragment perfil = new PerfilFragment();
    Fragment ocupacio = new OcupacioFragment();
    Fragment reserves = new ReservesFragment();
    Fragment esdeveniments = new EsdevenimentsFragment();
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_propietari);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_layout_propietari, ocupacio);
        ft.commit();
        BottomBar bottomBar = findViewById(R.id.bottomBar_propietari);
        bottomBar.setDefaultTab(R.id.tab_ocupacio);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                if(tabId == R.id.tab_ocupacio) {
                    ft2.replace(R.id.fragment_layout_propietari, ocupacio);
                }
                else if(tabId == R.id.tab_perfil) {
                    ft2.replace(R.id.fragment_layout_propietari, perfil);
                }
                else if(tabId == R.id.tab_reserves) {
                    ft2.replace(R.id.fragment_layout_propietari, reserves);
                }
                else if(tabId == R.id.tab_esdeveniments) {
                    ft2.replace(R.id.fragment_layout_propietari, esdeveniments);
                }
                ft2.commit();
            }
        });
    }

    //press back to back mainpage
    @Override
    public void onBackPressed() {
        if(backPressedTime+2000 > System.currentTimeMillis()){ //compare first click time with second
            finish();
        }
        else Toast.makeText( getBaseContext(), "Press back again", Toast.LENGTH_SHORT).show();

        backPressedTime = System.currentTimeMillis();

    }
}