package com.example.care_it;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InitialActivity extends AppCompatActivity {
    Button customer_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        customer_button=findViewById(R.id.customer_button);

        customer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
             startActivity(new Intent(InitialActivity.this,LogInActivity.class));
            }
        });
    }
}