package com.safeandsound.app.safeandsound;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by louisapabst on 22.04.17.
 */



public class ConnectionPOST extends AsyncTask<PostParams, Void, Boolean> {
    private Exception exception;

    protected Boolean doInBackground(PostParams... params) {
        InputStream is = null;
        StringBuilder sb = null;
        String result = null;
        OutputStreamWriter request;
        String response = null;

        String name = params[0].name;
        String password = params[0].password;
        String email = params[0].email;
        String ipAddressRP = params[0].ipAddressRP;
        String urlString = params[0].url;

        String parameters = "username="+name+"&password="+password+"&email="+email+"&ipaddressrp="+ipAddressRP;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sbt = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            // Response from server after login process will be stored in response variable.
            response = sbt.toString();
            // You can perform UI operations here
            //Toast.makeText(this,"Message from Server: \n"+ response, Toast.LENGTH_SHORT).show();
            isr.close();
            reader.close();
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection"+e.toString());
        }

        //convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String line="0";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result=sb.toString();
        }catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
        }
        return true;
    }

    protected void onPostExecute(String result) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
