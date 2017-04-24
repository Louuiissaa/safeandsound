package com.safeandsound.app.safeandsound;

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

/**
 * Created by louisapabst on 14.04.17.
 */

public class Settings extends FragmentActivity {
    private String ipAddressRP = "";
    SessionManager session;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        TextView ipAddressRP_Text = (TextView)findViewById(R.id.ipAddressOutput);
        ipAddressRP_Text.setText(ipAddressRP);

        // session manager
        session = new SessionManager(getApplicationContext());

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
        // Zur LogIn Activity zurück springen
        Intent intent = new Intent(Settings.this, LogIn.class);
        startActivity(intent);
    }

    public void openSensorOverviewActivity(View view) {
        Intent i = new Intent(Settings.this, SensorOverview.class);
        startActivity(i);
    }
}
