package com.safeandsound.app.safeandsound.view;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.safeandsound.app.safeandsound.AppConfig;
import com.safeandsound.app.safeandsound.ConnectionFeed;
import com.safeandsound.app.safeandsound.R;
import com.safeandsound.app.safeandsound.controller.database.SQLiteHandler;
import com.safeandsound.app.safeandsound.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by louisapabst on 16.04.17.
 */

public class AirQualityActivity extends FragmentActivity {

    private SQLiteHandler db;
    private SessionManager session;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.air_quality);

        // session manager
        session = new SessionManager(getApplicationContext());

        //Android interne Datenbank
        db = new SQLiteHandler(getApplicationContext());

        if (!session.isLoggedIn()) {
            logout();
        }

        String result = null;
        InputStream is = null;
        StringBuilder sb=null;

        //http post
        try {
            URL urlObj = new URL(AppConfig.URL_TEMP);
            result = new ConnectionFeed().execute(urlObj.toString()).get();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }


        //paring data
        String fd_temp = "";
        String fd_humidity = "";
        try{
            result = result.replace('"','\'');
            JSONObject json_data= new JSONObject(result);
            fd_humidity = json_data.getString("humidity");
            fd_temp = json_data.getString("temp");
        }catch(JSONException e1){
            Toast.makeText(getBaseContext(), "No Data Found", Toast.LENGTH_LONG).show();
        }catch (ParseException e1){
            e1.printStackTrace();
        }

        //print temperature
        TextView text_temperature = (TextView)findViewById(R.id.temperatureOutputText);
        text_temperature.setText(fd_temp);

        //print Humidity
        TextView text_humidity = (TextView)findViewById(R.id.humidityOutputText);
        text_humidity.setText(fd_humidity);
    }

    //Meldet den aktuellen Nutzer ab
    private void logout() {
        session.setLogin(false);
        HashMap<String, String> user = db.getUserDetails();
        db.logOutUser(user.get("user_id"));
        // Zur LogInActivity Activity zurück springen
        Intent intent = new Intent(AirQualityActivity.this, LogInActivity.class);
        startActivity(intent);
    }

}