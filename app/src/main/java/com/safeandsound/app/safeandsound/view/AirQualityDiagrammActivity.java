package com.safeandsound.app.safeandsound.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.safeandsound.app.safeandsound.R;

import org.w3c.dom.Text;

import java.util.Date;

import static com.safeandsound.app.safeandsound.R.string.tempDiagramm;

/**
 * Created by Dominic on 20.05.2017.
 */

public class AirQualityDiagrammActivity extends Activity{

    //Titel der Aktivität
    private String title = "";
    String url;



    // _______________________________

    private static final String TAG = "AirQualityDiagrammActivity";

    private Date startDateTimestamp = null;
    private Date endDateTimestamp = null;
    private String startdate;
    private String endDate;



    private Button mDisplayDateStart;
    private Button mDisplayDateEnd;
    private DatePickerDialog.OnDateSetListener mDateSetListenerStart;
    private DatePickerDialog.OnDateSetListener mDateSetListenerEnd;
    //_________________________________

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Calendar cal = Calendar.getInstance();
        Bundle bundle = getIntent().getExtras();
        String bundleName = "";
        Integer getShipper = 0;
        if(bundle != null){
            bundleName = bundle.getString("ID");
            getShipper = bundle.getInt("ship1");
        }

        if(getShipper != null){
            if(getShipper == 0){
                title = bundleName;
                url = "temp";
            }
            if(getShipper == 1){
                title = bundleName;
                url = "hum";
            }
            if(getShipper == 2){
                title = bundleName;
                url = "co2";
            }
            if(getShipper == 3){
                title = bundleName;
                url = "co";
            }
            if(getShipper == 4){
                title = bundleName;
                url = "nh4";
            }

        }
        //setzte richtigen Titel
        /*if(bundleName == getResources().getString(R.string.tempDiagramm)){
            title = getResources().getString(R.string.tempDiagramm);
        }
        if(bundleName == getResources().getString(R.string.luftfeutigkeit)){
            title = getResources().getString(R.string.humDiagramm);
        }
        if(bundleName == getResources().getString(R.string.kohlenstoffdioxid)){
            title = getResources().getString(R.string.co2Diagramm);
        }
        if(bundleName == getResources().getString(R.string.kohlenstoffmonoxid)){
            title = getResources().getString(R.string.coDiagramm);
        }
        if(bundleName == getResources().getString(R.string.sauerstoff)){
            title = getResources().getString(R.string.nhDiagramm);
        }*/



        setContentView(R.layout.air_quality_diagramm);
        //Titel in Aktivity anzeigen
        TextView heading = (TextView)findViewById(R.id.diagramTextView);
        heading.setText(title);

        //TextView zum festlegen des Startdatum
        mDisplayDateStart = (Button)findViewById(R.id.btnStart);
        mDisplayDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AirQualityDiagrammActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerStart,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerStart = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                cal.set(year, month, day, 0, 0 , 0 );
                startDateTimestamp = cal.getTime();
                startdate = getResources().getString(R.string.startDate)+":\t"+ day + "."+ (month+1) + "." +year;
                mDisplayDateStart.setText(startdate);
            }
        };


        //TextView zum festlegen des Enddatum
        mDisplayDateEnd = (Button)findViewById(R.id.btnEnd);
        mDisplayDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AirQualityDiagrammActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerEnd,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerEnd = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                cal.set(year, month, day, 0, 0 , 0 );
                endDateTimestamp = cal.getTime();
                endDate = getResources().getString(R.string.endDate)+":\t"+ day + "."+ (month +1) + "." +year;
                mDisplayDateEnd.setText(endDate);
            }
        };

        //Lade Daten OnClick listener
        final Button loadDataBtn = (Button)findViewById(R.id.getDataDia);
        loadDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startDateTimestamp == null || endDateTimestamp == null){
                    Toast.makeText(AirQualityDiagrammActivity.this,"Bitte Datum auswählen", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    int i = startDateTimestamp.compareTo(endDateTimestamp);

                    //compareto liefert 0 wenn beide daten gleich, 1 wenn enddate vor startdate und -1 wenn startdate vor enddate
                    if (i > 0){
                        Toast.makeText(AirQualityDiagrammActivity.this, "Startdatum liegt vor Enddatum" + Integer.toString(i), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(AirQualityDiagrammActivity.this, url, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

}
