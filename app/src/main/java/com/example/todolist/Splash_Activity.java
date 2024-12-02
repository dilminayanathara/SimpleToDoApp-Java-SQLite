package com.example.todolist;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class Splash_Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

         final Intent i = new Intent(Splash_Activity.this,MainActivity.class);
         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {
                 startActivity(i);
                 finish();
             }
         },3000);

        ImageView dot1 = findViewById(R.id.dot1);
        ImageView dot2 = findViewById(R.id.dot2);
        ImageView dot3 = findViewById(R.id.dot3);



        Handler handler = new Handler();
        handler.postDelayed(() -> {
            ((AnimationDrawable) dot1.getDrawable()).start();
        }, 0);

        handler.postDelayed(() -> {
            ((AnimationDrawable) dot2.getDrawable()).start();
        }, 400);

        handler.postDelayed(() -> {
            ((AnimationDrawable) dot3.getDrawable()).start();
        }, 800);

    }
}