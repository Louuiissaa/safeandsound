package com.safeandsound.app.safeandsound;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.safeandsound.app.safeandsound.controller.database.SQLiteHandler;
import com.safeandsound.app.safeandsound.model.User;
import com.safeandsound.app.safeandsound.view.LogInActivity;
import com.safeandsound.app.safeandsound.view.SignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by louisapabst on 16.04.17.
 */

public class Room extends FragmentActivity {

    private String areaname;
    private String userid;
    //private HashMap<String, String> rooms;

    private SQLiteHandler db;
    private SessionManager session;
    private ProgressDialog pDialog;
    private static final String TAG = SignUpActivity.class.getSimpleName();


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
        getUserRooms(userid);
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
                            String roomID = jObj.getString("roomid");
                            HashMap<String, String> rooms = User.getInstance().getRooms();
                            rooms.put(roomID, areaname);
                            User.getInstance().setRooms(rooms);
                            finish();
                            startActivity(getIntent());
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
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                // Setzt die POST Parameter für den LogInActivity
                Map<String, String> params = new HashMap<String, String>();
                params.put("areaname", areaname);
                params.put("user_id", userid);

                return params;
            }
        };

        // Hinzufügen des Requests zu der Request Queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void getUserRooms(String userid){
        //Boolean b = getRoomsFromDB();
        try {
            HashMap<String, String> rooms = User.getInstance().getRooms();
            Iterator it = rooms.entrySet().iterator();


        //Umrechnung von gewünschten dip zu pixels des Handys
        int dip70 = 70;
        int dip2 = 2;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final float scale = metrics.density;
        int pixels70 = (int) (dip70 * scale + 0.5f);
        int pixels2 = (int) (dip2 * scale + 0.5f);

            while (it.hasNext()) {
                Map.Entry r = (Map.Entry)it.next();
            //Initialisierung des LinearLayouts des neuen Windows
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setBackgroundColor(Color.RED);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pixels70);
            LLParams.setMargins(pixels2, pixels2, pixels2, pixels2);
            linearLayout.setLayoutParams(LLParams);

            //Initialisierung des ImageView mit WindowActivity-Icon des neuen Windows
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundColor(Color.WHITE);
            imageView.setImageResource(R.drawable.ic_house);
            LinearLayout.LayoutParams IVParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            imageView.setLayoutParams(IVParams);

            //Initialisierung des TextView mit Area/Sensor Name des neuen Windows
            TextView textView_WindowName = new TextView(this);
            textView_WindowName.setBackgroundColor(Color.WHITE);
            textView_WindowName.setTextSize(16);
            textView_WindowName.setText(r.getValue().toString());
            textView_WindowName.setGravity(Gravity.CENTER | Gravity.LEFT);
            LinearLayout.LayoutParams TVParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 6f);
            textView_WindowName.setLayoutParams(TVParams);

            //Initialisierung des ImageView mit NextPage-Icon des neuen Windows
            ImageView imageView_Arrow = new ImageView(this);
            imageView_Arrow.setBackgroundColor(Color.WHITE);
            imageView_Arrow.setImageResource(R.drawable.ic_next_page);
            imageView_Arrow.setLayoutParams(IVParams);

            //Hinzufügen des LinearLayouts, den zwei TextViews und den zwei ImageViews zu dem default
            // erstellten LinearLayouts windowButtonLayout
            LinearLayout ll = (LinearLayout) findViewById(R.id.windowButtonLayout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(linearLayout);
            linearLayout.addView(imageView);
            linearLayout.addView(textView_WindowName);
            linearLayout.addView(imageView_Arrow);
        }
        }catch (Exception e){
            // Ausgabe des Errors der von dem PHP weitergegeben wurde
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
        HashMap<String, String> user = db.getUserDetails();
        db.logOutUser(user.get("user_id"));
        // Zur LogInActivity Activity zurück springen
        Intent intent = new Intent(Room.this, LogInActivity.class);
        startActivity(intent);
    }
}
