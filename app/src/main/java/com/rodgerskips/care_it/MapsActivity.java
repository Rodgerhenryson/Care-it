package com.rodgerskips.care_it;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rodgerskips.care_it.constants.PrefManager;
import com.rodgerskips.care_it.utils.Utils;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    Switch aSwitch;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        aSwitch=findViewById(R.id.tch_switch_map);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Bitmap bitmap = Utils.getBitmapFromVectorDrawable(this,R.drawable.ic_marker);
        BitmapDescriptor descriptor =BitmapDescriptorFactory.fromBitmap(bitmap);


        Bitmap mybitmap = Utils.getBitmapFromVectorDrawable(this,R.drawable.ic_marker2);
        BitmapDescriptor mydescriptor =BitmapDescriptorFactory.fromBitmap(mybitmap);

        String status = getIntent().getStringExtra("from");
        if (status.equals("techh")){
            String lat = getIntent().getStringExtra("lat");
            String longi = getIntent().getStringExtra("long");

            Double vv1 = new Double(lat);
            Double vv2 = new Double(longi);
            // Add a marker in Sydney and move the camera
            LatLng myposition = new LatLng(Double.parseDouble(lat),
                    vv2);// Add a marker in Sydney and move the camera

            Marker customermarker;
            MarkerOptions mymarkerOptions = new MarkerOptions().position(myposition).title("My location");
            mymarkerOptions.icon(mydescriptor);
            mMap.addMarker(mymarkerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myposition, 11));
            aSwitch.setText("Customer's Location");

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(-0.547140, 36.945690))
                    .title("Customer's Location");
            Log.d(TAG, "onMapReady: "+status);
            final Marker[] marker = {null};

            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){

                        marker[0] = mMap.addMarker(markerOptions);
                        mMap.setIndoorEnabled(true);

                        markerOptions.icon(descriptor);
                    } else {
                        marker[0].remove();
                        Toast.makeText(MapsActivity.this, "offf", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } else{

        }
     /*   String lati = PrefManager.getInstance(this).getLat();
        String longi = PrefManager.getInstance(this).getLong();

        Double vv1 = new Double(lati);
        Double vv2 = new Double(longi);
        // Add a marker in Sydney and move the camera
        LatLng myposition = new LatLng(Double.parseDouble(lati),
                vv2);// Add a marker in Sydney and move the camera

      */









/*
        MarkerOptions mymarkerOptions = new MarkerOptions().position(myposition).title("My location");
        mymarkerOptions.icon(mydescriptor);
        mMap.addMarker(mymarkerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myposition, 11));

 */
    }


}