package com.example.bargo.UsuariConsumidor.Activity;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.bargo.UsuariConsumidor.Fragment.CodeFragment;
import com.example.bargo.UsuariConsumidor.Fragment.ListEstablimentsFragment;
import com.example.bargo.UsuariConsumidor.Fragment.ListEventsFragment;
import com.example.bargo.UsuariConsumidor.Fragment.profileFragment;
import com.example.bargo.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {
    Fragment profile = new profileFragment();
    Fragment code = new CodeFragment();
    Fragment bars = new ListEstablimentsFragment();
    Fragment events = new ListEventsFragment();
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_layout_propietari, bars);
        ft.commit();
        BottomBar bottomBar = findViewById(R.id.bottomBar_propietari);
        bottomBar.setDefaultTab(R.id.tab_code);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                if(tabId == R.id.tab_code) {
                    ft2.replace(R.id.fragment_layout_propietari, code);
                }
                else if(tabId == R.id.tab_profile) {
                    ft2.replace(R.id.fragment_layout_propietari, profile);
                }
                else if(tabId == R.id.tab_bars) {
                    ft2.replace(R.id.fragment_layout_propietari, bars);
                }
                else if(tabId == R.id.tab_events) {
                    ft2.replace(R.id.fragment_layout_propietari, events);
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

