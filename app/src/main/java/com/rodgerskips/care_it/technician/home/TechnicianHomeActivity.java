package com.rodgerskips.care_it.technician.home;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.rodgerskips.care_it.R;
public class TechnicianHomeActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_home);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.techhome));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.techhome));

        Toolbar toolbar=findViewById(R.id.tech_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Technician Home");

        drawer= findViewById(R.id.tech_drawer_layout);
        NavigationView navigationView= findViewById(R.id.nav_view_tech);
        View headerView = navigationView.getHeaderView(0);

        TextView user= headerView.findViewById(R.id.nav_header_name_tech);
        TextView phone= headerView.findViewById(R.id.nav_header_phone_tech);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
}