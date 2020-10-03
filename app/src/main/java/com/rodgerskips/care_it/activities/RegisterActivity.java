package com.rodgerskips.care_it.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.rodgerskips.care_it.Models.User;
import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.constants.Contants;
import com.rodgerskips.care_it.constants.PrefManager;
import com.rodgerskips.care_it.constants.RequestHandler;
import com.rodgerskips.care_it.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    TextView already_have_an_account, header;
    Button btnRegister, btn_t, btn_c;
    EditText editTextEmail, editTextName, editTextPhone, editTextPassword;
    String dbName = "customer";
    public static int confirmation = 0;
    ProgressDialog progressDialog;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Utils.SetStatusBar(this);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.statusbar));

        header = findViewById(R.id.register_header);
        btn_c = findViewById(R.id.btn_customer);
        btn_t = findViewById(R.id.btn_technician);


        editTextEmail = findViewById(R.id.editTextEmail);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);

        already_have_an_account = findViewById(R.id.already_have_an_account);
        btnRegister = findViewById(R.id.btnRegister);


        findViewById(R.id.already_have_an_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
            }
        });


    }


    public void registertechnician(View view) {
        dbName = "technician";
        btnRegister.setText("Register as technician");
        header.setText("Technician registration");
        btn_c.setVisibility(View.VISIBLE);
        btn_t.setVisibility(View.GONE);


    }

    public void customerregister(View view) {
        dbName = "customer";
        btnRegister.setText("Register as customer");
        header.setText("Customer registration");
        btn_c.setVisibility(View.GONE);
        btn_t.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void Signup(View view) {
        final String email = editTextEmail.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        //validations
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please Enter Your Email");
            editTextEmail.requestFocus();

            return;
        }
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Please Enter Your Name");
            editTextName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            editTextPhone.setError("Please Enter Your Phone Number");
            editTextPhone.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        }

        ValidateCredentials(dbName, email, name, phone, password);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ValidateCredentials(final String dbName, final String email, final String name, final String phone, final String password) {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Contants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        //JSONObject jsonObject = new JSONObject(response);
                        Log.d("TAG", "onResponsejson: " + response);

                        if (response.equals("registered successfully!")) {
                            Utils.ShowToast(RegisterActivity.this, name+", your account was created succesfully");
                            startActivity(new Intent(RegisterActivity.this, LogInActivity.class));

                        } else {
                            Utils.ShowToast(RegisterActivity.this, response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Log.d("TAG", "onResponsejsoneror: " + error.getMessage());
                        Utils.ShowToast(RegisterActivity.this, String.valueOf(error.getMessage()));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("table", dbName);
                params.put("username", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);


    }

}