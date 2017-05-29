package com.safeandsound.app.safeandsound.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.safeandsound.app.safeandsound.AppConfig;
import com.safeandsound.app.safeandsound.ConnectionPOST;
import com.safeandsound.app.safeandsound.PostParams;
import com.safeandsound.app.safeandsound.R;
import com.safeandsound.app.safeandsound.model.database.SQLiteHandler;

import java.util.concurrent.ExecutionException;

/**
 * Created by louisapabst on 22.04.17.
 */

public class SignUpActivity extends FragmentActivity {

    private String username;
    private String password;
    private String email;
    private String ipAddressRP;

    private SQLiteHandler db;

    private ProgressDialog pDialog;



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Android interne SQLite Datenbank wird erstellt
        db = new SQLiteHandler(getApplicationContext());

    }

    public Boolean addUserToDB(String username, String password, String email, String ipAddressRP) {
        Boolean result = null;

        //http post
        try {
            PostParams params = new PostParams(username, password, email, ipAddressRP, AppConfig.URL_SIGNUP);
            result = new ConnectionPOST().execute(params).get();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        return result;
    }

    public void signup(View view) {
        final EditText username_ET = (EditText) findViewById(R.id.username_input);
        final EditText password_ET = (EditText) findViewById(R.id.password_input);
        final EditText email_ET = (EditText) findViewById(R.id.email_input);
        final EditText ipAddressRP_ET = (EditText) findViewById(R.id.ipAddress_input);

        username = username_ET.getText().toString();
        password = password_ET.getText().toString();
        email = email_ET.getText().toString();
        ipAddressRP = ipAddressRP_ET.getText().toString();

        if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !ipAddressRP.isEmpty()) {

            Boolean result = addUserToDB(username, password, email, ipAddressRP);

            pDialog.setMessage("Sign up " + username);
            showDialog();

            showMessage(result);

            // User wird in interne Android DB gespeichert
            //db.addUser(null, username, password, email, ipAddressRP);

            //Nutzer auf LogInActivity Bildschirm führen
            Intent intent = new Intent(
                    SignUpActivity.this,
                    LogInActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Fordert den User alle Felder auszufüllen
            Toast.makeText(getApplicationContext(),
                    "Please enter the credentials!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void showMessage(Boolean b){
        String text = "Thank you for Signing Up!";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        hideDialog();

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}