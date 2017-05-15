package com.safeandsound.app.safeandsound;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.safeandsound.app.safeandsound.controller.database.SQLiteHandler;
import com.safeandsound.app.safeandsound.view.LogInActivity;

import java.util.HashMap;

/**
 * Created by louisapabst on 24.04.17.
 */

public class SensorOverview extends Activity {

    private SQLiteHandler db;
    private SessionManager session;

    TextView intervall;
    int intervall_Value;
    int intervall_Time;

    TextView range;
    int range_Min;
    int range_Max;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensoroverview);

        // session manager
        session = new SessionManager(getApplicationContext());

        //Android interne Datenbank
        db = new SQLiteHandler(getApplicationContext());

        if (!session.isLoggedIn()) {
            logout();
        }
    }

    public void setIntervall(View view){
        String sensorTyp = view.getTag().toString();
        final String textViewName = sensorTyp + "IntervallOutput";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select the intervall:");

        //Setzt ein Linear Layout
        final LinearLayout LL = new LinearLayout(this);
        LL.setOrientation(LinearLayout.HORIZONTAL);

        final String[] stringArray = { "seconds", "minutes","hours","years"};

        //Setzt die Number Picker
        final NumberPicker valueNumberPicker = new NumberPicker(this);
        valueNumberPicker.setMaxValue(50);
        valueNumberPicker.setMinValue(1);
        valueNumberPicker.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        final NumberPicker timeNumberPicker = new NumberPicker(this);
        timeNumberPicker.setMaxValue(4);
        timeNumberPicker.setMinValue(1);
        timeNumberPicker.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        timeNumberPicker.setDisplayedValues(stringArray);
        LL.addView(valueNumberPicker);
        LL.addView(timeNumberPicker);

        builder.setView(LL);

        // Setzt den Button für positive und negative Eingabe
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intervall_Time = timeNumberPicker.getValue();
                String str = stringArray[intervall_Time -1];
                intervall_Value = valueNumberPicker.getValue();
                intervall = (TextView)findViewById(getResources().getIdentifier(textViewName, "id", getPackageName()));
                if (intervall_Value == 1) {
                    intervall.setText(intervall_Value + " " + str.substring(0, str.length()-1));
                } else {
                    intervall.setText(intervall_Value + " " + str);
                }
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

    public void setRange(View view){
        String sensorTyp = view.getTag().toString();
        final String textViewName = sensorTyp + "RangeOutput";

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select the desired range:");

        //Setzt ein Linear Layout
        final LinearLayout LL = new LinearLayout(this);
        LL.setOrientation(LinearLayout.HORIZONTAL);

        //Setzt die Number Picker
        final NumberPicker minNumberPicker = new NumberPicker(this);
        minNumberPicker.setMaxValue(50);
        minNumberPicker.setMinValue(1);
        minNumberPicker.setLayoutParams(params);
        final NumberPicker maxNumberPicker = new NumberPicker(this);
        maxNumberPicker.setMaxValue(50);
        maxNumberPicker.setMinValue(1);
        maxNumberPicker.setLayoutParams(params);

        String unit_sc;

        switch (sensorTyp){
            case "temperature":
                unit_sc = "°C";
                break;
            case "humidity":
                unit_sc = "%";
                break;
            case "carbonmonoxid":
                unit_sc = "?";
                break;
            case "carbondioxid":
                unit_sc = "?";
                break;
            case "oxygen":
                unit_sc = "?";
                break;
            default:
                unit_sc = "";
        }

        final String unit = unit_sc;

        TextView tv = new TextView(this);
        tv.setLayoutParams(params);
        tv.setText(unit);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);

        LL.addView(minNumberPicker);
        LL.addView(maxNumberPicker);
        LL.addView(tv);

        builder.setView(LL);

        // Setzt den Button für positive und negative Eingabe
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                range_Min = minNumberPicker.getValue();
                range_Max = maxNumberPicker.getValue();
                if(range_Min > range_Max){
                    int tmp = range_Min;
                    range_Min = range_Max;
                    range_Max = tmp;
                }
                range = (TextView)findViewById(getResources().getIdentifier(textViewName, "id", getPackageName()));
                range.setText(range_Min + unit + " - " + range_Max + unit);
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

    //Meldet den aktuellen Nutzer ab
    private void logout() {
        session.setLogin(false);
        HashMap<String, String> user = db.getUserDetails();
        db.logOutUser(user.get("user_id"));
        // Zur LogInActivity Activity zurück springen
        Intent intent = new Intent(SensorOverview.this, LogInActivity.class);
        startActivity(intent);
    }
}
