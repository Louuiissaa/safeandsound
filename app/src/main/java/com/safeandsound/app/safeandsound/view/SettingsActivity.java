package com.safeandsound.app.safeandsound.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.safeandsound.app.safeandsound.R;
import com.safeandsound.app.safeandsound.controller.database.SQLiteHandler;
import com.safeandsound.app.safeandsound.SensorOverview;
import com.safeandsound.app.safeandsound.SessionManager;
import com.safeandsound.app.safeandsound.model.User;

import java.util.HashMap;

/**
 * Created by louisapabst on 14.04.17.
 */

public class SettingsActivity extends FragmentActivity {
    private String ipAddressRP = "";
    private SessionManager session;
    private SQLiteHandler db;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        TextView ipAddressRP_Text = (TextView)findViewById(R.id.ipAddressOutput);

        // session manager
        session = new SessionManager(getApplicationContext());

        //Android interne Datenbank
        db = new SQLiteHandler(getApplicationContext());

        ipAddressRP = db.getloggedInUser();
        ipAddressRP_Text.setText(ipAddressRP);

        if (!session.isLoggedIn()) {
            logout();
        }

        // Logout Button
        Button btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public void changeIPRP(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter the IP Address of your RaspberryPi you want to connect:");

        // Setzt das Feld fürs Input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Setzt den Button für positive und negative Eingabe
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ipAddressRP = input.getText().toString();
                TextView ipAddressRP_Text = (TextView)findViewById(R.id.ipAddressOutput);
                ipAddressRP_Text.setText(ipAddressRP);
                //AppConfig.setIpAddressRP(ipAddressRP);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    //Meldet den aktuellen Nutzer ab
    private void logout() {
        session.setLogin(false);
        User.reset();
        HashMap<String, String> user = db.getUserDetails();
        db.logOutUser(user.get("user_id"));
        // Zur LogInActivity Activity zurück springen
        Intent intent = new Intent(SettingsActivity.this, LogInActivity.class);
        startActivity(intent);
    }

    public void openSensorOverviewActivity(View view) {
        Intent i = new Intent(SettingsActivity.this, SensorOverview.class);
        startActivity(i);
    }
}