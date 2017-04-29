package com.safeandsound.app.safeandsound;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by louisapabst on 21.04.17.
 */

public class User {
    //Singleton
    private static User user;

    private ProgressDialog pDialog;
    private static final String TAG = SignUp.class.getSimpleName();

    private long user_id;
    private String username;
    private String password;
    private String email;
    private String iPAddressRP;
    private HashMap<String, String> rooms;
    private HashMap<String, String> windows;

    private User(long user_id, String username, String password, String email, String iPAddressRP, Context context){
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.iPAddressRP = iPAddressRP;
        this.rooms = new HashMap<String, String>();
        pDialog = new ProgressDialog(context);
        fetchRooms(context);
        this.windows = new HashMap<String, String>();
        //fetchWindows(context);
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIPAddressRP() {
        return iPAddressRP;
    }

    public void setIPAddressRP(String iPAddressRP) {
        this.iPAddressRP = iPAddressRP;
    }

    public HashMap<String, String> getRooms() {
        return rooms;
    }

    public void setRooms(HashMap<String, String> rooms) {
        this.rooms = rooms;
    }

    public HashMap<String, String> getWindows() {
        return windows;
    }

    public void setWindows(HashMap<String, String> windows) {
        this.windows = windows;
    }


    public static void init(long user_id, String username, String password, String email, String iPAddressRP, Context context){
        if(user == null){
            user = new User(user_id, username, password, email, iPAddressRP, context);
        }
    }

    public static User getInstance() throws Exception {
        if(user != null) {
            return user;
        }else{
            throw new Exception("User Object has not been created");
        }
    }

    private void fetchRooms(final Context context){
        String tag_string_req = "req_getrooms";

        //Anfrage an den ApacheServer mit POST Method und Antwort mit onResponse-Methode entgegennehmen
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_ROOMS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    response = response.replace('"', '\'');
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Überprüfung ob bei der PHP Ausführung ein Fehler passiert ist
                    if (!error) {
                        Iterator<String> iter = jObj.keys();
                        while (iter.hasNext()) {
                            String areaId = iter.next();
                            if(!areaId.equals("error")){
                                String areaName = jObj.getString(areaId);
                                rooms.put(areaId, areaName);
                            }
                        }
                        User.getInstance().setRooms(rooms);
                    } else {
                        // Ausgabe des Errors der während des LogIns aufgetreten ist
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // Ausgabe des Errors der von dem PHP weitergegeben wurde
                    e.printStackTrace();
                    Toast.makeText(context, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    // Ausgabe des Errors der von dem PHP weitergegeben wurde
                    e.printStackTrace();
                    Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String err_Msg = error.getMessage();
                if(err_Msg != null ) {
                    Toast.makeText(context,
                            err_Msg, Toast.LENGTH_LONG).show();
                    hideDialog();
                }else{
                    err_Msg = "Something went wrong!";
                    Toast.makeText(context, err_Msg, Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Setzt die POST Parameter für den LogIn
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put("userid", String.valueOf(User.getInstance().getUser_id()));
                }catch (Exception e){
                    String err_Msg = "Something went wrong!";
                    Toast.makeText(context, err_Msg, Toast.LENGTH_LONG).show();
                    hideDialog();
                }

                return params;
            }
        };

        // Hinzufügen des Requests zu der Request Queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void reset() {
        user = null;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", iPAddressRP='" + iPAddressRP + '\'' +
                '}';
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
