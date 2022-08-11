package com.wegooooo.ndk;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.sample_text).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MnnActivity.class);
            MainActivity.this.startActivity(intent);
        });
    }
}