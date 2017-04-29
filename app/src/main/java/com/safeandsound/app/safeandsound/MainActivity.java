package com.safeandsound.app.safeandsound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Android interne Datenbank
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logout();
        }
    }

    public void openAirQualityActivity(View view) {
        Intent i = new Intent(MainActivity.this, AirQuality.class);
        startActivity(i);
    }

    public void openWindowActivity(View view) {
        Intent i = new Intent(MainActivity.this, Window.class);
        startActivity(i);
    }

    public void openSettingsActivity(View view) {
        Intent i = new Intent(MainActivity.this, Settings.class);
        startActivity(i);
    }

    public void openLogInActivity(View view) {
        Intent i = new Intent(MainActivity.this, LogIn.class);
        startActivity(i);
    }

    public void openRoomActivity(View view) {
        Intent i = new Intent(MainActivity.this, Room.class);
        startActivity(i);
    }

    //Meldet den aktuellen Nutzer ab
    private void logout() {
        session.setLogin(false);
        HashMap<String, String> user = db.getUserDetails();
        db.logOutUser(user.get("user_id"));
        // Zur LogIn Activity zurück springen
        Intent intent = new Intent(MainActivity.this, LogIn.class);
        startActivity(intent);
    }
}