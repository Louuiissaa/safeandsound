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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by louisapabst, Dominic on 16.04.17.
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

        //http get
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
        String fd_co2 = "";
        String fd_co = "";
        String fd_nh4 = "";
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

        result = null;
        is = null;
        sb=null;

        /*
        *    Get-Request um die aktuellen daten des Mq-135 sensors auszulesen ********
        *
        */

        try {
            URL urlObj = new URL(AppConfig.URL_MQGas);
            result = new ConnectionFeed().execute(urlObj.toString()).get();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }


        try{
            result = result.replace('"','\'');
            JSONArray resultArray = new JSONArray( result);
            JSONObject json_data= (JSONObject)resultArray.getJSONObject(0);
            fd_co2 = json_data.getString("CO2");
            fd_co = json_data.getString("CO");
            fd_nh4 = json_data.getString("NH4");
        }catch(JSONException e1){
            Toast.makeText(getBaseContext(), "No Data Found", Toast.LENGTH_LONG).show();
        }catch (ParseException e1){
            e1.printStackTrace();
        }


        //CO2, CO, NH4 Werte runden
        fd_co2 = String.format(Locale.GERMAN, "%.3f", Double.parseDouble(fd_co2));
        fd_co = String.format(Locale.GERMAN, "%.3f", Double.parseDouble(fd_co));
        fd_nh4 = String.format(Locale.GERMAN, "%.3f", Double.parseDouble(fd_nh4));


        //print temperature
        TextView text_temperature = (TextView)findViewById(R.id.temperatureOutputText);
        text_temperature.setText(fd_temp+ "°C");

        //print Humidity
        TextView text_humidity = (TextView)findViewById(R.id.humidityOutputText);
        text_humidity.setText(fd_humidity +"%");

        //print CO2
        TextView text_co2 = (TextView)findViewById(R.id.carbondioxidOutputText);
        text_co2.setText(fd_co2 + " ppm");

        //print CO
        TextView text_co = (TextView)findViewById(R.id.carbonmonoxidOutputText);
        text_co.setText(fd_co + " ppm");

        //print NH4 carbonmonoxidOutputText
        TextView text_nh = (TextView)findViewById(R.id.oxygenOutputText);
        text_nh.setText(fd_nh4 + " ppm");
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