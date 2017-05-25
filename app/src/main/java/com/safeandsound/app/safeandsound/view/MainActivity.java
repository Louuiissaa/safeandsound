package com.safeandsound.app.safeandsound.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.safeandsound.app.safeandsound.R;
import com.safeandsound.app.safeandsound.controller.database.SQLiteHandler;
import com.safeandsound.app.safeandsound.SessionManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //handle no service available
        Bundle bundle = getIntent().getExtras();
        String getShipper = "";

        if(bundle != null){
            getShipper = bundle.getString("error");
            Toast.makeText(getApplicationContext(), getShipper, Toast.LENGTH_SHORT);
        }

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
        Intent i = new Intent(MainActivity.this, AirQualityActivity.class);
        startActivity(i);
    }

    public void openWindowActivity(View view) {
        Intent i = new Intent(MainActivity.this, WindowActivity.class);
        startActivity(i);
    }

    public void openSettingsActivity(View view) {
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
    }

    public void openLogInActivity(View view) {
        Intent i = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(i);
    }

    public void openRuleActivity(View view) {
        Intent i = new Intent(MainActivity.this, RuleActivity.class);
        startActivity(i);
    }
    
    public void openLiveStreamActivity(View view) {
        Intent i = new Intent(MainActivity.this, LiveStreamActivity.class);
        startActivity(i);
    }

    public void openLibraryActivity(View view) {
        Intent i = new Intent(MainActivity.this, LibraryActivity.class);
        startActivity(i);
    }

    //Meldet den aktuellen Nutzer ab
    private void logout() {
        session.setLogin(false);
        HashMap<String, String> user = db.getUserDetails();
        db.logOutUser(user.get("user_id"));
        // Zur LogInActivity Activity zurück springen
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(intent);
    }


    public void onResume(){
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        String getShipper = "";

        if(bundle != null){
            getShipper = bundle.getString("error");
            Toast.makeText(getApplicationContext(), "Service not available - Check if Raspberry is running...", Toast.LENGTH_SHORT).show();
        }

    }


}