package com.safeandsound.app.safeandsound;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louisapabst on 20.04.17.
 */

public class LogIn extends FragmentActivity{

    private static final String TAG = SignUp.class.getSimpleName();
    private EditText inputEmail;
    private EditText inputPassword;
    private String ipAddressRP;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Android interne SQLite Datenbank wird instanziiert
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Überprüfung ob User eingeloggt ist
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LogIn.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void login(View view){
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        //String ipAddressRP;

        // Kontrolle ob Email und Passwort eingegeben wurden
        if (!email.isEmpty() && !password.isEmpty()) {
            ipAddressRP = db.getUsersIPAddressRP(email);
                if(ipAddressRP == null){
                    getUsersIP(email);
                }
                checkLogin(email, password);
        } else {
            // Fordert den User auf Email UND Passwort einzugeben
            Toast.makeText(getApplicationContext(),
                    "Please enter the credentials!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void getUsersIP(String email){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Please fill in the IP Address of your Raspberry Pi:");
        builder.setMessage("Message");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ipAddressRP = input.getText().toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        builder.show();
    }


    public void openSignUpActivity(View view){
        Intent i = new Intent(LogIn.this, SignUp.class);
        startActivity(i);
        finish();
    }

    /**
     * Überprüfung der LogIn User Eingabe mit den Datensätzen der Datenbank
     * */
    private void checkLogin(final String email, final String password) {
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        //Anfrage an den ApacheServer mit POST Method und Antwort mit onResponse-Methode entgegennehmen
        //StringRequest strReq = new StringRequest(Request.Method.POST,
        //        "http://" + ipAddressRP + "/db_login.php", new Response.Listener<String>() {
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String >(){
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    response = response.replace('"','\'');
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Überprüfung ob bei der PHP Ausführung ein Fehler passiert ist
                    if (!error) {
                        // Session des Users wird wahr
                        session.setLogin(true);

                        int uid = Integer.parseInt(jObj.getString("uid"));
                        JSONObject userJSON = jObj.getJSONObject("user");
                        String name = userJSON.getString("name");
                        String email = userJSON.getString("email");
                        String ipAddressRP = userJSON.getString("ipaddressrp");

                        User.init(uid, name, null, email, ipAddressRP, getApplicationContext());

                        // User wird in interne Android DB gespeichert
                        if(!db.checkUserExists(email)) {
                            db.addUser(uid, name, null, email, ipAddressRP, 1);
                        }
                        db.logInUser(jObj.getString("uid"));
                        // User wird zum Hauptmenü weitergeleitet
                        Intent intent = new Intent(LogIn.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else{
                        // Ausgabe des Errors der während des LogIns aufgetreten ist
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // Ausgabe des Errors der von dem PHP weitergegeben wurde
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }catch (Exception e) {
                    // Ausgabe des Errors der von dem PHP weitergegeben wurde
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String err_Msg = error.getMessage();
                if(err_Msg != null ) {
                    Log.e(TAG, "Login Error: " + err_Msg);
                    Toast.makeText(getApplicationContext(),
                            err_Msg, Toast.LENGTH_LONG).show();
                    hideDialog();
                }else{
                    err_Msg = "Pleas sign up first to log in!";
                    Toast.makeText(getApplicationContext(), err_Msg, Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Setzt die POST Parameter für den LogIn
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        // Hinzufügen des Requests zu der Request Queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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