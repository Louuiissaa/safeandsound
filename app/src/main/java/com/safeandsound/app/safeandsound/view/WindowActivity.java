package com.safeandsound.app.safeandsound.view;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.safeandsound.app.safeandsound.AppConfig;
import com.safeandsound.app.safeandsound.ConnectionFeed;
import com.safeandsound.app.safeandsound.ExceptionHandler;
import com.safeandsound.app.safeandsound.R;
import com.safeandsound.app.safeandsound.model.database.SQLiteHandler;
import com.safeandsound.app.safeandsound.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


/**
 * Created by louisapabst, Dominic on 16.04.17.
 */

public class WindowActivity extends FragmentActivity {

    private SQLiteHandler db;
    private SessionManager session;
    private String result;
    private int value;
    private String room;
    private long timestamp;
    private static int oldValue = 10;
    private static String oldDate;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.window);

        // session manager
        session = new SessionManager(getApplicationContext());

        //Android interne Datenbank
        db = new SQLiteHandler(getApplicationContext());

        if (!session.isLoggedIn()) {
            logout();
        }


        showWindowData();



    }

    public void showWindowData(){


        try {
            URL urlObj = new URL(AppConfig.URL_WINDOW);
            result = new ConnectionFeed().execute(urlObj.toString()).get();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }

        try{
            //JSONObject jsonResponse = new JSONObject(re);
            JSONArray jsonMainNode = new JSONArray(result);
            for(int i = 0; i < jsonMainNode.length(); i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                value = jsonChildNode.optInt("value");
                room = jsonChildNode.optString("room");
                timestamp = jsonChildNode.optLong("timestamp");
            }
        } catch (JSONException e){
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        TextView roomStatus = (TextView)findViewById(R.id.windowStatusOutput);
        TextView roomName = (TextView)findViewById(R.id.windowText);
        roomName.setText(room);
        if(value == 1){
            roomStatus.setText(R.string.windowClosed);
            roomStatus.setTextColor(Color.RED);
        }else if(value == 0){
            roomStatus.setText(R.string.windowOpen);
            roomStatus.setTextColor(Color.GREEN);
        }else{
            roomStatus.setText("");
        }


        if(oldValue != value){
            showLastStatusChanged(timestamp);
            oldValue = value;
        }

        TextView lastStatusChange = (TextView)findViewById(R.id.lastChangeOutput);
        lastStatusChange.setText(oldDate);




    }

    public void showLastStatusChanged(Long timestamp){
        DateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
        Date netDate = (new Date(timestamp));
        String dateFormatted = sdf.format(netDate).toString();
        oldDate = dateFormatted.toString();


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }



    /*

    public void addWindow(View view){

        //Damit die hinzugefügten Elemente persistent werden
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Set<String> imageViewSet = prefs.getStringSet("saved_imageViews",null);
        Set<String> textViewSet = prefs.getStringSet("saved_textViews",null);
        Set<String> layoutSet = prefs.getStringSet("saved_layouts",null);
        if(imageViewSet == null){
            imageViewSet = new HashSet<String>();
        }
        if(textViewSet == null){
            textViewSet = new HashSet<String>();
        }
        if(layoutSet == null){
            layoutSet = new HashSet<String>();
        }

        //Umrechnung von gewünschten dip zu pixels des Handys
        int dip70 = 70;
        int dip2 = 2;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final float scale = metrics.density;
        int pixels70 = (int) (dip70 * scale + 0.5f);
        int pixels2 = (int) (dip2 * scale + 0.5f);

        //Initialisierung des LinearLayouts des neuen Windows
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(Color.RED);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,pixels70);
        LLParams.setMargins(pixels2, pixels2, pixels2, pixels2);
        linearLayout.setLayoutParams(LLParams);
        layoutSet.add("LinearLayout");

        //Initialisierung des ImageView mit WindowActivity-Icon des neuen Windows
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(Color.WHITE);
        imageView.setImageResource(R.drawable.ic_window);
        LinearLayout.LayoutParams IVParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);        imageView.setLayoutParams(IVParams);
        imageViewSet.add("ImageView_WindowIcon");

        //Initialisierung des TextView mit Area/Sensor Name des neuen Windows
        TextView textView_WindowName = new TextView(this);
        textView_WindowName.setBackgroundColor(Color.WHITE);
        textView_WindowName.setTextSize(16);
        textView_WindowName.setThenText("Elternschlafzimmer");
        textView_WindowName.setGravity(Gravity.CENTER | Gravity.LEFT);
        LinearLayout.LayoutParams TVParams = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT, 3f);
        textView_WindowName.setLayoutParams(TVParams);
        textViewSet.add("TextView_SensorName");
        // id="@+id/oxygenText", gravity="center_vertical|left", textAlignment="gravity" />

        //Initialisierung des TextView mit Sensor Status des neuen Windows
        TextView textView_WindowStatus = new TextView(this);
        textView_WindowStatus.setBackgroundColor(Color.WHITE);
        textView_WindowStatus.setTextSize(16);
        textView_WindowStatus.setThenText("open");
        textView_WindowStatus.setGravity(Gravity.CENTER | Gravity.RIGHT);
        textView_WindowStatus.setLayoutParams(TVParams);
        textViewSet.add("TextView_SensorStatus");
        //layout_width="0dip", layout_height="match_parent", layout_weight="3",
        // id="@+id/oxygenOutputText"

        //Initialisierung des ImageView mit NextPage-Icon des neuen Windows
        ImageView imageView_Arrow = new ImageView(this);
        imageView_Arrow.setBackgroundColor(Color.WHITE);
        imageView_Arrow.setImageResource(R.drawable.ic_next_page);
        imageView_Arrow.setLayoutParams(IVParams);
        imageViewSet.add("ImageView_NextPageIcon");
        //layout_weight="1"

        //Hinzufügen des LinearLayouts, den zwei TextViews und den zwei ImageViews zu dem default
        // erstellten LinearLayouts windowButtonLayout
        LinearLayout ll = (LinearLayout)findViewById(R.id.windowButtonLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(linearLayout);
        linearLayout.addView(imageView);
        linearLayout.addView(textView_WindowName);
        linearLayout.addView(textView_WindowStatus);
        linearLayout.addView(imageView_Arrow);

        prefs.edit().putStringSet("saved_imageViews", imageViewSet).commit();
        prefs.edit().putStringSet("saved_textViews", textViewSet).commit();
        prefs.edit().putStringSet("saved_layouts", layoutSet).commit();
    }*/

    //Meldet den aktuellen Nutzer ab
    private void logout() {
        session.setLogin(false);
        HashMap<String, String> user = db.getUserDetails();
        db.logOutUser(user.get("user_id"));
        // Zur LogInActivity Activity zurück springen
        Intent intent = new Intent(WindowActivity.this, LogInActivity.class);
        startActivity(intent);
    }

}
