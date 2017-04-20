package com.safeandsound.app.safeandsound;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by louisapabst on 14.04.17.
 */

public class Settings extends FragmentActivity {
    private String ipAddressRP = "";
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        TextView ipAddressRP_Text = (TextView)findViewById(R.id.ipAddressOutput);
        ipAddressRP_Text.setText(ipAddressRP);
    }

    public void changeIPRP(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter the IP Address of your RaspberryPi you want to connect:");

        // Setzt das Feld fürs Input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
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
}
