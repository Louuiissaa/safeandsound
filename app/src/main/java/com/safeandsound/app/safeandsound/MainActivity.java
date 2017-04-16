package com.safeandsound.app.safeandsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openAirQuality(View view) {
        Intent i = new Intent(MainActivity.this, AirQuality.class);
        startActivity(i);
    }
}
