package com.safeandsound.app.safeandsound;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlContext;
import java.util.concurrent.ExecutionException;

import static java.security.AccessController.getContext;

/**
 * Created by louisapabst on 13.04.17.
 */

public class AirQuality extends FragmentActivity {
        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.air_quality);
            String result = null;
            InputStream is = null;
            StringBuilder sb=null;

            //http post
            try {
                String url = "http://192.168.10.53:1880/temp";
                URL urlObj = new URL(url);
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
                //result = "{\"temp\":\"22.90\",\"humidity\":\"36.70\",\"sensorid\":\"dht22\",\"date\":149}";
                //JSONArray jArray = new JSONArray(result);
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

}
