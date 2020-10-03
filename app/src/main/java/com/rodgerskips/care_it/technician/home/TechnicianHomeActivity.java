package com.rodgerskips.care_it.technician.home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.rodgerskips.care_it.Admin.fragment.HomeFragmentAdmin;
import com.rodgerskips.care_it.Admin.home.AdminHomeActivity;
import com.rodgerskips.care_it.BuildConfig;
import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.constants.PrefManager;
import com.rodgerskips.care_it.customer.fragments.HistoryFragment;
import com.rodgerskips.care_it.customer.fragments.HomeFragment;
import com.rodgerskips.care_it.customer.fragments.ProfileFragment;
import com.rodgerskips.care_it.customer.fragments.UploadFragment;
import com.rodgerskips.care_it.customer.home.CustomerHomeActivity;
import com.rodgerskips.care_it.technician.fragments.TechCompleteFragment;
import com.rodgerskips.care_it.technician.fragments.TechPendingFragment;
import com.rodgerskips.care_it.utils.Utils;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

public class TechnicianHomeActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_home);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.techhome));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.techhome));

        toolbar=findViewById(R.id.tech_toolbar);
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
                switch (Item.getItemId()) {
                    case R.id.tech_nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.tech_fragment_container,
                                new HomeFragment()).commit();
                        break;
                    case R.id.tech_nav_pendingjobs:
                        getSupportFragmentManager().beginTransaction().replace(R.id.tech_fragment_container,
                                new TechPendingFragment()).commit();
                        break;
                    case R.id.tech_nav_completed:
                        getSupportFragmentManager().beginTransaction().replace(R.id.tech_fragment_container,
                                new TechCompleteFragment()).commit();

                        break;


                    case R.id.customer_nav_signout:
                        Logout(navigationView);

                        break;

                    case R.id.customer_nav_share:


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

    private void Logout(NavigationView navigationView) {
        new FancyAlertDialog.Builder(this)
                .setTitle("Log out")
                .setBackgroundColor(Color.parseColor("#303F9F"))
                .setMessage("Are you sure you want to log out of the sytem?")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))
                .setNegativeBtnText("No")
                .setPositiveBtnBackground(Color.parseColor("#FF4081"))
                .setPositiveBtnText("Yes")
                .setAnimation(Animation.POP)
                .isCancellable(false)
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Utils.ShowToast(TechnicianHomeActivity.this,"Logout Canceled");
                        toolbar.setTitle("Home");
                        navigationView.setCheckedItem(R.id.admin_nav_home);
                        getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container,
                                new HomeFragmentAdmin()).commit();
                    }
                })
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        PrefManager.getInstance(TechnicianHomeActivity.this).logout();

                    }
                })
                .setIcon(R.drawable.ic_logout, Icon.Visible)
                .build();
    }

    private void SendApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String shareMessage = "\nLet me recommend you this Care-It application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }

    }
}