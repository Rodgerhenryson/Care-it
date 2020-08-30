package com.rodgerskips.care_it.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
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

import com.rodgerskips.care_it.Models.User;
import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.constants.PrefManager;
import com.rodgerskips.care_it.constants.RequestHandler;
import com.rodgerskips.care_it.constants.URLS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    TextView already_have_an_account;
    Button btnRegister;
    EditText editTextEmail,editTextName,editTextPhone,editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.app_name);
        }

        editTextEmail=findViewById(R.id.editTextEmail);
        editTextName=findViewById(R.id.editTextName);
        editTextPhone=findViewById(R.id.editTextPhone);
        editTextPassword=findViewById(R.id.editTextPassword);

        already_have_an_account=findViewById(R.id.already_have_an_account);
        btnRegister=findViewById(R.id.btnRegister);

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        findViewById(R.id.already_have_an_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
            }
        });



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this, CustomerHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser() {
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

        //if it passes all the validations
        //executing the async task
        RegisterUser ru = new RegisterUser(email,name,phone,password);
        ru.execute();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    private class RegisterUser extends AsyncTask<Void, Void, String> {
        private ProgressBar progressBar;
        private String email,name,phone,password;
        RegisterUser(String email,String name,String phone, String password){
            this.email = email;
            this.name = name;
            this.phone = phone;
            this.password = password;

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("name", name);
            params.put("phone", phone);
            params.put("password", password);

            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_REGISTER, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("SignUp","sfdsds : "+s);
            //hiding the progressbar after completion
            progressBar.setVisibility(View.GONE);
            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    //getting the user from the response
                    JSONObject userJson = obj.getJSONObject("user");
                    //creating a new user object
                    User user = new User(
                            userJson.getString("email"),
                            userJson.getString("name"),
                            userJson.getString("phone")
                    );
                    //storing the user in shared preferences
                    PrefManager.getInstance(getApplicationContext()).setUserLogin(user);
                    //starting the profile activity
                    finish();
                    startActivity(new Intent(getApplicationContext(), CustomerHistoryActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}