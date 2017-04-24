package com.safeandsound.app.safeandsound;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by louisapabst on 24.04.17.
 */

public class SensorOverview extends Activity {

    TextView intervall;
    int temperatureIntervall_Value;
    int temperatureIntervall_Time;

    TextView range;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensoroverview);
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
                temperatureIntervall_Time = timeNumberPicker.getValue();
                String str = stringArray[temperatureIntervall_Time-1];
                temperatureIntervall_Value = valueNumberPicker.getValue();
                intervall = (TextView)findViewById(getResources().getIdentifier(textViewName, "id", getPackageName()));
                if (temperatureIntervall_Value == 1) {
                    intervall.setText(temperatureIntervall_Value + " " + str.substring(0, str.length()-1));
                } else {
                    intervall.setText(temperatureIntervall_Value + " " + str);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select the range:");

        //Setzt ein Linear Layout
        final LinearLayout LL = new LinearLayout(this);
        LL.setOrientation(LinearLayout.HORIZONTAL);

        //Setzt die Number Picker
        final DatePicker fromDatePicker = new DatePicker(this);
        fromDatePicker.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        final DatePicker tillDatePicker = new DatePicker(this);
        tillDatePicker.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        LL.addView(fromDatePicker);
        LL.addView(tillDatePicker);

        builder.setView(LL);

        // Setzt den Button für positive und negative Eingabe
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                range = (TextView) findViewById(R.id.rangeTempOutput);
                if((fromDatePicker.getMonth() == tillDatePicker.getMonth() && (fromDatePicker.getYear() == tillDatePicker.getYear()))) {
                    range.setText(fromDatePicker.getDayOfMonth() +". - " +tillDatePicker.getDayOfMonth() + "." + tillDatePicker.getMonth() + "." + tillDatePicker.getYear());
                }else if(fromDatePicker.getYear() == tillDatePicker.getYear()) {
                    range.setText(fromDatePicker.getDayOfMonth() +"." +fromDatePicker.getMonth()+". - " +tillDatePicker.getDayOfMonth() + "." + tillDatePicker.getMonth() + "." + tillDatePicker.getYear());
                }else{
                    range.setText(fromDatePicker.getDayOfMonth() +"." +fromDatePicker.getMonth()+ "." + fromDatePicker.getYear() + " - " +tillDatePicker.getDayOfMonth() + "." + tillDatePicker.getMonth() + "." + tillDatePicker.getYear());
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
}
