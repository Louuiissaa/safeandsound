package com.safeandsound.app.safeandsound;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by louisapabst, Dominic on 16.04.17.
 */

public class ConnectionFeed extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            InputStream is = null;
            StringBuilder sb = null;
            String result = null;
            try {
                URL url = new URL(urls[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                is = urlConnection.getInputStream();
                String test = is.toString();
                //System.out.print("InputSTream: " + is);
                int status = urlConnection.getResponseCode();
                System.out.println("Status: " + status);
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

                return result;

        }

        protected void onPostExecute(String result) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
}
