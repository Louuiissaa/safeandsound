package com.safeandsound.app.safeandsound.view;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.safeandsound.app.safeandsound.AppConfig;
import com.safeandsound.app.safeandsound.ConnectionFeed;
import com.safeandsound.app.safeandsound.ExceptionHandler;
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
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
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
        Double fd_co2 = 0.0;
        Double fd_co = 0.0;
        Double fd_nh4 = 0.0;
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

        //Auslesen von Gas sensor werten
        try{
            //result = result.replace('"','\'');
            JSONArray resultArray = new JSONArray(result);
            JSONObject json_data= (JSONObject)resultArray.getJSONObject(0);
            fd_co2 =json_data.getDouble("CO2");
            fd_co = json_data.getDouble("CO");
            fd_nh4 = json_data.getDouble("NH4");
        }catch(JSONException e1){
            Toast.makeText(getBaseContext(), "No Data Found" + e1.toString(), Toast.LENGTH_LONG).show();
        }catch (ParseException e1){
            e1.printStackTrace();
        }

        //print temperature
        TextView text_temperature = (TextView)findViewById(R.id.temperatureOutputText);
        text_temperature.setText(fd_temp+ "°C");

        //print Humidity
        TextView text_humidity = (TextView)findViewById(R.id.humidityOutputText);
        text_humidity.setText(fd_humidity +"%");

        //print CO2
        TextView text_co2 = (TextView)findViewById(R.id.carbondioxidOutputText);
        text_co2.setText(String.format("%.2f", fd_co2) + " ppm");

        //print CO
        TextView text_co = (TextView)findViewById(R.id.carbonmonoxidOutputText);
        text_co.setText(String.format("%.2f", fd_co) + " ppm");

        //print NH4 carbonmonoxidOutputText
        TextView text_nh = (TextView)findViewById(R.id.oxygenOutputText);
        text_nh.setText(String.format("0.2f", fd_nh4) + " ppm");


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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

    //button
    public void onClickNavDiagramm(View view){
        Intent i = new Intent(AirQualityActivity.this, AirQualityDiagrammActivity.class);
        //helpURL wird benutz, um zu untersheiden welcher Service (GAS, oder Luftfeuchte&Temp) geladen werden soll
        int helpURL;
        switch (view.getId()){
            case R.id.tempBtn:
                //id für Temperatur verlauf = 0
                helpURL = 0;
                i.putExtra("ID", helpURL);
                break;
            case R.id.humBtn:
                //id für Luftfeuchtigkeit verlauf = 1
                helpURL = 0;
                i.putExtra("ID", helpURL);
            break;
            case R.id.co2Btn:
                //id für Kohlenstoffdioxid verlauf = 2
                helpURL = 1;
                i.putExtra("ID", helpURL);
            break;
            case R.id.coBtn:
                //id für Kohlenstoffmonoxid verlauf = 3
                helpURL = 1;
                i.putExtra("ID", helpURL);
            break;
            case R.id.nh4Btn:
                //id für nh4 verlauf = 4
                helpURL = 1;
                i.putExtra("ID", helpURL);
            break;
        }
        startActivity(i);
    }



}