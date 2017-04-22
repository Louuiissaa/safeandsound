package com.safeandsound.app.safeandsound;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by louisapabst on 20.04.17.
 */

public class LogIn extends FragmentActivity{

    private String username;
    private String password;
    private String email;
    private String ipAddressRP;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        EditText password = (EditText) findViewById(R.id.password);
        password.setTypeface(Typeface.DEFAULT);

    }

    public void postData(String username, String password, String email, String ipAddressRP) {
        HttpURLConnection connection;
        OutputStreamWriter request = null;


        Boolean result = null;
        InputStream is = null;
        StringBuilder sb=null;

        //http post
        try {
            String urlString = "http://192.168.10.53/db_register.php";
            PostParams params = new PostParams(username, password, email, ipAddressRP, urlString);
            result = new ConnectionPOST().execute(params).get();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
    }

    public void signup(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Up");

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TableLayout.LayoutParams input_params = new TableLayout.LayoutParams();
        input_params.setMargins(75, 20, 75, 5);
        TableLayout.LayoutParams username_params = new TableLayout.LayoutParams();
        username_params.setMargins(75, 80, 75, 5);


        // Setzt das Feld für Username Input
        final EditText username_input = new EditText(this);
        username_input.setInputType(InputType.TYPE_CLASS_TEXT);
        username_input.setHint("Username");
        username_input.setLayoutParams(username_params);
        linearLayout.addView(username_input);

        // Setzt das Feld für Email Input
        final EditText email_input = new EditText(this);
        email_input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        email_input.setHint("Email");
        email_input.setLayoutParams(input_params);
        linearLayout.addView(email_input);

        // Setzt das Feld für Password Input
        final EditText password_input = new EditText(this);
        password_input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password_input.setHint("Password");
        password_input.setLayoutParams(input_params);
        linearLayout.addView(password_input);

        // Setzt das Feld für RaspberryPi IP Address Input
        final EditText iPRP_input = new EditText(this);
        iPRP_input.setInputType(InputType.TYPE_CLASS_TEXT);
        iPRP_input.setHint("IP Address of Raspberry Pi");
        iPRP_input.setLayoutParams(input_params);
        linearLayout.addView(iPRP_input);

        builder.setView(linearLayout);

        // Setzt den Button für positive und negative Eingabe
        builder.setPositiveButton("Sign Up", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                username = username_input.getText().toString();
                password = password_input.getText().toString();
                email = email_input.getText().toString();
                ipAddressRP = iPRP_input.getText().toString();
                postData(username, password, email, ipAddressRP);
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
