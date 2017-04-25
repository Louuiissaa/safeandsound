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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by louisapabst on 16.04.17.
 */

public class Room extends FragmentActivity {

    private String areaname;
    private String userid;

    private SQLiteHandler db;
    private SessionManager session;
    private ProgressDialog pDialog;
    private static final String TAG = SignUp.class.getSimpleName();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //Datenbank
        db = new SQLiteHandler(getApplicationContext());

        //SessionManager
        session = new SessionManager(getApplicationContext());

        if(!session.isLoggedIn()){
            logout();
        }

        HashMap<String, String> user = db.getUserDetails();
        userid = user.get("user_id");
    }

    public void addRoom(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter the name of the room:");

        final EditText input = new EditText(this);

        builder.setView(input);

        // Setzt den Button für positive und negative Eingabe
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                areaname = input.getText().toString();
                addRoomToDB(areaname);

                pDialog.setMessage("Added room " + areaname);
                showDialog();
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

    public void addRoomToDB(final String areaname){
        String tag_string_req = "req_addroom";

        //Anfrage an den ApacheServer mit POST Method und Antwort mit onResponse-Methode entgegennehmen
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_ROOM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {

                        Log.d(TAG, "Login Response: " + response.toString() + userid);
                        response = response.replace('"', '\'');
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        // Überprüfung ob bei der PHP Ausführung ein Fehler passiert ist
                        if (!error) {


                        } else {
                            // Ausgabe des Errors der während des LogIns aufgetreten ist
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                } catch (JSONException e) {
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
                params.put("areaname", areaname);
                params.put("user_id", userid);

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

    //Meldet den aktuellen Nutzer ab
    private void logout() {
        session.setLogin(false);
        db.deleteUsers();
        // Zur LogIn Activity zurück springen
        Intent intent = new Intent(Room.this, LogIn.class);
        startActivity(intent);
    }
}
