package com.rodgerskips.care_it.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rodgerskips.care_it.R;
import com.rodgerskips.care_it.utils.Utils;

public class InitialActivity extends AppCompatActivity {
    Button customer_button,technician_button;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        Utils.SetStatusBar(this);




    }


    public void Register(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }

    public void Login(View view) {
        startActivity(new Intent(this,LogInActivity.class));
    }
}