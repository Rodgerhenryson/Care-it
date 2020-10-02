package com.rodgerskips.care_it.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rodgerskips.care_it.Admin.home.AdminHomeActivity;
import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.constants.PrefManager;
import com.rodgerskips.care_it.customer.home.CustomerHomeActivity;
import com.rodgerskips.care_it.technician.home.TechnicianHomeActivity;
import com.rodgerskips.care_it.utils.Utils;

public class InitialActivity extends AppCompatActivity {
    Button customer_button, technician_button;

    private FusedLocationProviderClient mFusedLocationClient;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        Utils.SetStatusBar(this);


        GetCurrentlocation();

        if (PrefManager.getInstance(InitialActivity.this).isLoggedIn()){

            if (PrefManager.getInstance(InitialActivity.this).getAccoutntType().equals("customer")){
                startActivity(new Intent(getApplicationContext(), CustomerHomeActivity.class));
            } else if (PrefManager.getInstance(InitialActivity.this).getAccoutntType().equals("technician")){
                startActivity(new Intent(getApplicationContext(), TechnicianHomeActivity.class));
            }else if (PrefManager.getInstance(InitialActivity.this).getAccoutntType().equals("admin")){
                startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));
            }


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocationPermission() {

        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                123);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetCurrentlocation();
                } else {
                    Toast.makeText(this, "Please Grant permission", Toast.LENGTH_SHORT).show();
                }
                break;


        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void GetCurrentlocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getLocationPermission();
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double lat = location.getLatitude();
                            double longitude = location.getLongitude();

                            PrefManager.getInstance(InitialActivity.this).setLatLong(lat, longitude);

                            //PrefManager.Toast(getActivity(),String.valueOf(lat+"  "+longitude));
                            //Log.d("TAG", "onSuccess: " + lat + "   " + longitude);




                        }
                        else {
                            Toast.makeText(InitialActivity.this, "No location make sure power mode is off", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void Register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void Login(View view) {
        startActivity(new Intent(this, LogInActivity.class));
    }
}