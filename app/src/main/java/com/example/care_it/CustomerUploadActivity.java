package com.example.care_it;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

public class CustomerUploadActivity extends AppCompatActivity {
    EditText description,add_location;
    ImageView uploadImage;
    TextView choseImage;
    Button uploadButton;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_upload);
        uploadImage=findViewById(R.id.post_image);
        choseImage =findViewById(R.id.choose_image_text);
        description=findViewById(R.id.description);
        add_location=findViewById(R.id.location);
        uploadButton=findViewById(R.id.upload_button);

        choseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 101);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101 && resultCode==RESULT_OK)
        {
            if(data!=null)
            {
                imageUri= data.getData();
                Glide.with(getApplicationContext()).load(imageUri).into(uploadImage);
            }
        }
    }
}

