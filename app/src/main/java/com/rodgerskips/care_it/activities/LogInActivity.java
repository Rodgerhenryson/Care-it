package com.rodgerskips.care_it.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rodgerskips.care_it.Admin.home.AdminHomeActivity;
import com.rodgerskips.care_it.Models.User;
import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.constants.Contants;
import com.rodgerskips.care_it.constants.PrefManager;
import com.rodgerskips.care_it.constants.RequestHandler;
import com.rodgerskips.care_it.constants.URLS;
import com.rodgerskips.care_it.customer.home.CustomerHomeActivity;
import com.rodgerskips.care_it.technician.home.TechnicianHomeActivity;
import com.rodgerskips.care_it.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class LogInActivity extends AppCompatActivity {
    private static final String TAG = "LogInActivity";
    EditText editTextEmail, editTextPassword;
    TextView tv;
    Button btn_login,btn_login_tech,btn_admin,btn_login_1,btn_login_2;
    String dbName = "customer";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.login));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.login));

       PrefManager prefManager = PrefManager.getInstance(LogInActivity.this);

        tv= findViewById(R.id.header_login_tv);
        btn_login= findViewById(R.id.logIn_button);
        btn_login_1= findViewById(R.id.btn_customer_1);
        btn_login_2= findViewById(R.id.btn_customer_2);
        btn_login_tech= findViewById(R.id.btn_technician_login);
        btn_admin= findViewById(R.id.btn_admin);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

    }





    public void technicianLogin(View view) {
        dbName = "technician";
        tv.setText("Login as technician");
        btn_login.setText("technician login");
        btn_login_1.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);

    }

    public void AdminLogin(View view) {
        dbName = "admin";
        tv.setText("Login as Admin");
        btn_login.setText("Admin login");
        btn_admin.setVisibility(View.GONE);
        btn_login_2.setVisibility(View.VISIBLE);
    }

    public void LoginMainFuntion(View view) {

        //first getting the values
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        //validating inputs
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter email");
            editTextEmail.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter password");
            editTextPassword.requestFocus();
            return;
        } else {
            if (dbName.equals("customer")){
               // Utils.ShowToast(LogInActivity.this,"Account Type: customer");
                userLogin(dbName,email,password);
            } else if (dbName.equals("technician")){
                //Utils.ShowToast(LogInActivity.this,"Account Type: technician");
                userLogin(dbName,email,password);
            }else if (dbName.equals("admin")){
               // Utils.ShowToast(LogInActivity.this,"Account Type: admin");
                userLogin(dbName,email,password);
            }
        }
    }

    public void CustomerLogin(View view) {

        if (view.getId()==R.id.btn_customer_2){
            view.setVisibility(View.GONE);
            dbName = "customer";
            tv.setText("Login as Customer");
            btn_login.setText("Customer login");
            btn_admin.setVisibility(View.VISIBLE);

        }

        if (view.getId()==R.id.btn_customer_1){
            view.setVisibility(View.GONE);
            dbName = "customer";
            tv.setText("Login as Customer");
            btn_login.setText("Customer login");
            btn_login_tech.setVisibility(View.VISIBLE);

        }
    }

    public void Toregister(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }

    private void userLogin(String AccType,String email,String password) {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging in as "+AccType);
        progressDialog.setMessage("please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Contants.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    if(!obj.getBoolean("error")){
                        /*SharedPrefManager.getInstance(getApplicationContext())
                                .userLogin(
                                        obj.getInt("id"),
                                        obj.getString("username"),
                                        obj.getString("email")
                                );

                         */

                        User user= new User(email,"","",password);
                        PrefManager.getInstance(LogInActivity.this).isLoggedIn();
                        PrefManager.getInstance(LogInActivity.this).setAccountType(AccType);
                        PrefManager.getInstance(LogInActivity.this).setUserLogin(user);


                        if (dbName.equals("customer")){
                            startActivity(new Intent(getApplicationContext(), CustomerHomeActivity.class));
                        } else if (dbName.equals("technician")){
                            startActivity(new Intent(getApplicationContext(), TechnicianHomeActivity.class));
                        }else if (dbName.equals("admin")){
                            startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));
                        }


                        //startActivity(new Intent(getApplicationContext(), Te.class));
                      //  Utils.ShowToast(LogInActivity.this,user.getEmail());
                        //finish();
                    }else{
                        Toast.makeText(
                                getApplicationContext(),
                                obj.getString("message"),
                                Toast.LENGTH_LONG
                        ).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(
                        getApplicationContext(),
                        error.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
               params.put("email", email);
                params.put("password", password);
                params.put("type", AccType);


                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }
}
