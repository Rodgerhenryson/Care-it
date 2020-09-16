package com.rodgerskips.care_it.customer.home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.rodgerskips.care_it.BuildConfig;
import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.constants.PrefManager;
import com.rodgerskips.care_it.customer.fragments.HistoryFragment;
import com.rodgerskips.care_it.customer.fragments.HomeFragment;
import com.rodgerskips.care_it.customer.fragments.ProfileFragment;
import com.rodgerskips.care_it.customer.fragments.UploadFragment;

public class CustomerHomeActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Customer Home");


        drawer= findViewById(R.id.drawer_layout);
        NavigationView navigationView= findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView user= headerView.findViewById(R.id.nav_header_name);
        TextView phone= headerView.findViewById(R.id.nav_header_phone);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container, new UploadFragment())
                    .commit();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
                switch (Item.getItemId()) {
                    case R.id.user_nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HomeFragment()).commit();

                        break;
                    case R.id.user_nav_upload:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new UploadFragment()).commit();

                        break;
                    case R.id.user_nav_history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HistoryFragment()).commit();

                        break;

                    case R.id.user_nav_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ProfileFragment()).commit();

                        break;

                    case R.id.customer_nav_signout:

                        PrefManager.getInstance(CustomerHomeActivity.this).logout();

                        break;

                    case R.id.customer_nav_share:

                        PrefManager.Toast(getApplicationContext(),"Share App");
                        break;

                    case R.id.customer_nav_send:

                        SendApp();
                        break;

                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void SendApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String shareMessage= "\nLet me recommend you this Care-It application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }

    }
}