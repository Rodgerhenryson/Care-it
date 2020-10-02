package com.rodgerskips.care_it.customer.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rodgerskips.care_it.R;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rodgerskips.care_it.constants.Contants;
import com.rodgerskips.care_it.constants.PrefManager;
import com.rodgerskips.care_it.constants.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Kiduyu klaus
 * on 16/09/2020 09:30 2020
 */
public class UploadFragment extends Fragment implements OnMapReadyCallback {
    private Button buttonChoose;
    private Button buttonUpload;
    private FusedLocationProviderClient mFusedLocationClient;
    private ImageView imageView;
    private TextView choose;

    private EditText editTextName, locationedt;
    private GoogleMap mMap;
    private Bitmap bitmap;
    double v1, v2;
    private int PICK_IMAGE_REQUEST = 1;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "description";
    private String KEY_LOCATION = "location";
    private String KEY_LAT = "latitude";
    private String KEY_LONG = "longitude";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        Button button = view.findViewById(R.id.upload_button);

        imageView = view.findViewById(R.id.post_image);
        editTextName = view.findViewById(R.id.description_upload);
        choose = view.findViewById(R.id.choose_image_text);
        locationedt = view.findViewById(R.id.location_upload);
        button.setOnClickListener(v -> UploadPhoto());

        choose.setOnClickListener(v -> ChoosePhoto());
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map_upload);
        mapFragment.getMapAsync(this);
        //getLocationPermission();

        GetCurrentlocation();
        //PrefManager.Toast(getActivity(), PrefManager.getInstance(getActivity()).getLong());
        TextView lattv = view.findViewById(R.id.upload_lat);
        lattv.setText("Latitude: " + PrefManager.getInstance(getActivity()).getLat());
        TextView longtv = view.findViewById(R.id.upload_long);
        longtv.setText("Longitude: " + PrefManager.getInstance(getActivity()).getLong());

        return view;
    }

    private void ChoosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void UploadPhoto() {
        String name = editTextName.getText().toString().trim();
        String locationtxt = locationedt.getText().toString().trim();


        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(locationtxt)) {
            PrefManager.ErrorToast(getActivity(), "Description or Location is empty");
            return;
        } else {

            //Showing the progress dialog
            final ProgressDialog loading = ProgressDialog.show(getActivity(), "Uploading...", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Contants.URL_UPLOAD,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //Disimissing the progress dialog
                            loading.dismiss();
                            //Showing toast message of the response
                            //Toast.makeText(MainActivity.this, s , Toast.LENGTH_LONG).show();
                            PrefManager.Toast(getActivity(), "Upload Added Successfully");
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new HistoryFragment()).commit();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Dismissing the progress dialog
                            loading.dismiss();
                            Log.d("TAG", "onResponse: "+String.valueOf(volleyError.getMessage()));
                            //Showing toast
                            PrefManager.Toast(getActivity(), String.valueOf(volleyError.getMessage()));
                            //Toast.makeText(MainActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    String image = getStringImage(bitmap);

                    String lati="";
                    String longi="";
                    if(PrefManager.getInstance(getActivity()).getLat()==null || PrefManager.getInstance(getActivity()).getLong()==null) {
                        lati = "00000";
                        longi = "00000";
                    } else {
                        longi = PrefManager.getInstance(getActivity()).getLong();
                        lati = PrefManager.getInstance(getActivity()).getLat();
                    }
                    String username = PrefManager.getInstance(getActivity()).getUser().getEmail();
                    //Creating parameters
                    Map<String, String> params = new Hashtable<String, String>();

                    //Adding parameters
                    params.put(KEY_IMAGE, image);
                    params.put(KEY_NAME, name);
                    params.put("user", username);
                    params.put(KEY_LOCATION, locationtxt);
                    params.put(KEY_LAT, lati);
                    params.put(KEY_LONG, longi);

                    //returning parameters
                    return params;
                }
            };

            //Creating a Request Queue
            RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
        }

    }

    private void getLocationPermission() {

        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                123);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetCurrentlocation();
                } else {
                    Toast.makeText(getActivity(), "Please Grant permission", Toast.LENGTH_SHORT).show();
                }
                break;


        }

    }

    private void GetCurrentlocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getLocationPermission();
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {


                            double lat = location.getLatitude();
                            v1 = location.getLatitude();
                            double longitude = location.getLongitude();
                            v2 = location.getLongitude();

                            PrefManager.getInstance(getActivity()).setLatLong(lat, longitude);

                            //PrefManager.Toast(getActivity(),String.valueOf(lat+"  "+longitude));
                            Log.d("TAG", "onSuccess: " + lat + "   " + longitude);


                        }
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String lati="";
        String longi="";
        if(PrefManager.getInstance(getActivity()).getLat()==null || PrefManager.getInstance(getActivity()).getLong()==null) {
            lati = "00000";
            longi = "00000";
        } else {
            longi = PrefManager.getInstance(getActivity()).getLong();
            lati = PrefManager.getInstance(getActivity()).getLat();
        }
      // Double vv1 = new Double(lati);
       // Double vv2 = new Double(longi);


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.parseDouble(lati),
                Double.parseDouble(longi));
        PrefManager.Toast(getActivity(), String.valueOf(lati + "  " + longi));
        Log.d("TAG", "onMapReady: " + v1);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 11));

    }
}
