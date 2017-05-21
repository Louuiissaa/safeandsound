package com.safeandsound.app.safeandsound.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.icu.math.BigDecimal;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.safeandsound.app.safeandsound.AppConfig;
import com.safeandsound.app.safeandsound.ConnectionFeed;
import com.safeandsound.app.safeandsound.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by Dominic on 20.05.2017.
 */

public class AirQualityDiagrammActivity extends Activity{

    //Titel der Aktivität
    private String title;
    private String url, requestURL;
    private static final String TAG = "AirQualityDiagrammActivity";

    //Timestamp zum verlgeiche, dass Startdatum nicht vor Enddatum
    private Date startDateTimestamp = null;
    private Date endDateTimestamp = null;
    //String zum Anzeigen des Datus auf den Date Picker Buttons
    private String startdate, endDate;

    //variablen zum generieren der URL
    private Integer startYear, startMonth, startDay, endYear, endMonth, endDay, actYear, actMonth, actDay;

    private Button mDisplayDateStart;
    private Button mDisplayDateEnd;
    private DatePickerDialog.OnDateSetListener mDateSetListenerStart;
    private DatePickerDialog.OnDateSetListener mDateSetListenerEnd;
    private String result = "[]";
    private Boolean tempHumData, gasData;
    private ArrayList<String> arrList = new ArrayList<>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        Integer getShipper = 0;

        title = getResources().getString(R.string.dataProfile);

        if(bundle != null){
            getShipper = bundle.getInt("ID");
        }

        if(getShipper != null){
            if(getShipper == 0){
                title = getResources().getString(R.string.tempHumProfile);
                url = AppConfig.URL_TEMPINT;
                tempHumData = true;
                gasData =false;
            }
            if(getShipper == 1){
                title = getResources().getString(R.string.gasProfile);
                url = AppConfig.URL_MQGasInt;
                tempHumData = false;
                gasData = true;
            }
        }

        setContentView(R.layout.air_quality_diagramm);
        //Titel in Aktivität anzeigen
        TextView heading = (TextView)findViewById(R.id.diagramTextView);
        heading.setText(title);

        //chart set default settings
        final LineChart chart = (LineChart) findViewById(R.id.chart);
        chart.setNoDataText("To display data please select dates");
        Paint p = chart.getPaint(Chart.PAINT_INFO);
        p.setColor(Color.BLACK);


        final Calendar cal = Calendar.getInstance();
        actYear = cal.get(Calendar.YEAR);
        actMonth = cal.get(Calendar.MONTH);
        actDay = cal.get(Calendar.DAY_OF_MONTH);

        //Button zum festlegen des Startdatum
        mDisplayDateStart = (Button)findViewById(R.id.btnStart);
        mDisplayDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(
                        AirQualityDiagrammActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerStart,
                        actYear,actMonth,actDay);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        /*
        *   Sobald auf den Button geklick wird öffnet sich ein DatePicker
        *   Startdatum kann dadaurch festgelegt werden
        * */

        mDateSetListenerStart = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                cal.set(year, month, day, 0, 0 , 0 );
                startYear = year;
                startMonth = month+1;
                startDay = day;
                startDateTimestamp = cal.getTime();
                //String zum Anzeigen des Datums auf dem Button
                startdate = getResources().getString(R.string.startDate)+":\t"+ day + "."+ (month+1) + "." +year;
                mDisplayDateStart.setText(startdate);
            }
        };


        //Button zum Festlegen des Enddatums
        mDisplayDateEnd = (Button)findViewById(R.id.btnEnd);
        mDisplayDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(
                        AirQualityDiagrammActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerEnd,
                        actYear,actMonth,actDay);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        /*
        *   Enddatum kann dadaurch festgelegt werden
        *   Wird benötigt um Daten innerhalb eines Zeitraums zu erhalten
        * */
        mDateSetListenerEnd = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                cal.set(year, month, day, 0, 0 , 0 );
                endYear = year;
                endMonth = month+1; //monat beginnt bei 0
                endDay = day;
                endDateTimestamp = cal.getTime();
                //String zum Anzeigen des Datums auf dem Button
                endDate = getResources().getString(R.string.endDate)+":\t"+ day + "."+ (month +1) + "." +year;
                mDisplayDateEnd.setText(endDate);
            }
        };

        /*
        * Lade Daten OnClick listener
        * Lädt die Daten des Gas und Temperatur Sensors vom Backend
        * Angegebener Zeitraum wird dabei berücksichtigt
        * */
        final Button loadDataBtn = (Button)findViewById(R.id.getDataDia);
        loadDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startDateTimestamp == null || endDateTimestamp == null){
                    Toast.makeText(AirQualityDiagrammActivity.this,"Please select dates", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    int i = startDateTimestamp.compareTo(endDateTimestamp);

                    //compareto liefert 0 wenn beide daten gleich, 1 wenn enddate vor startdate und -1 wenn startdate vor enddate
                    if (i > 0){
                        //Toast wird angezeigt bei falscher Eingabe (Startdatum > Enddatum)
                        Toast.makeText(AirQualityDiagrammActivity.this, "End date must be after or equal to start date", Toast.LENGTH_SHORT).show();
                    }else {
                        /*
                        *   RequestURL an den Server
                        *   Start und Enddatu werden dabei berücksichtigt
                        *   Bsp: <ip-rasp>:1880/tempInt?from=20.05.2017&to=21.05.2017
                        * */
                        requestURL = url + startDay + "."+startMonth +"."+ startYear + "&to="+ endDay + "."+endMonth + "."+endYear;

                        //do HTTP GET Request an den Server
                        try {
                            URL urlObj = new URL(requestURL);
                            result = new ConnectionFeed().execute(urlObj.toString()).get();
                        }catch (MalformedURLException e){
                            e.printStackTrace();
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }catch (ExecutionException e){
                            e.printStackTrace();
                        }


                        /*
                        *   Falls keine Daten gefunden wurden, wird ein leere JSON Array als String zurückgeliefert
                        *   Toast wird angezeigt, dass zu diesem Zeitraum keine Daten vorhanden sind
                        * */
                        if(result.equals("[]\n")){
                            Toast.makeText(AirQualityDiagrammActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }else{
                            /*
                            *   Falls result- String daten enthält zeige diese über showDiagramm an
                            * */
                            showDiagramm(chart, result);
                        }
                    }
                }
            }
        });
    }




    /*
    *   show Response Data in Diagramm
    *   distinguish between Temperature & Humidity Data and Gas Sensor (MQ 135) Data
    *
    * */
    public void showDiagramm(LineChart chart, String result){
        LineData data;

        if(tempHumData){
            arrList = getTimeStamp(result);
            data = getTemHumDataForDiagramm(result);

        }else if(gasData){
            arrList = getTimeStamp(result);
            data = getGasDataForDiagramm(result);

        }else{
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            return;
        }

        /*
        *   in arrList all Timestamps to a each value of the resoponse are stored
         *   timestamps are show below the x-axis as a label
        * */
        arrList = getTimeStamp(result);
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //return testArr[(int) value];
                return arrList.get((int)value);
            }
        };

        //SETTINGS TO THE DIAGRAMM
        chart.setTouchEnabled(true);
        chart.setDoubleTapToZoomEnabled(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        //Legend SETTINGS
        Legend legend = chart.getLegend();
        legend.setTextColor(Color.BLACK);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //SETTINGS YAxis
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setGranularityEnabled(true);
        //SETTINGS XAxis
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(Color.LTGRAY);
        //SETTINGS to display the Data
        chart.setData(data);
        chart.setVisibleXRangeMaximum(5);
        chart.setNoDataText("Please select dates");
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    /*
    *   Method to extract the timestamp
    *   timestamps are show below x-axis
    *   Timestamp to show a data profile (e.g. temperature / humidity profile)
    * */
    public ArrayList getTimeStamp(String result){

        ArrayList<String> timeArr = new ArrayList<>();

        try{
            //JSONObject jsonResponse = new JSONObject(re);
            JSONArray jsonMainNode = new JSONArray(result);
            for(int i = 0; i < jsonMainNode.length(); i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String timestamp = jsonChildNode.optString("timestamp");
                long test = Long.parseLong(timestamp);
                DateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
                Date netDate = (new Date(test));
                String test20 = sdf.format(netDate).toString();
                timeArr.add(test20);
            }
        } catch (JSONException e){
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        return timeArr;
    }


    /*
     * Prepare the Response data to display temperature, humidity profile it in Diagramm based on user inputs
     * */
    public LineData getTemHumDataForDiagramm(String s){

        List<Entry> tempList = new ArrayList<Entry>();
        List<Entry> humidityList = new ArrayList<Entry>();

        try{
            //JSONObject jsonResponse = new JSONObject(re);
            JSONArray jsonMainNode = new JSONArray(s);
            for(int i = 0; i < jsonMainNode.length(); i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                //Double temperature = jsonChildNode.optDouble("temperature");
                Float temperature = BigDecimal.valueOf(jsonChildNode.getDouble("temperature")).floatValue();
                tempList.add(new Entry((float)i, temperature));
                Float humidity = BigDecimal.valueOf(jsonChildNode.getDouble("humidity")).floatValue();
                humidityList.add(new Entry((float)i, humidity));
            }
        } catch (JSONException e){
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        LineDataSet dataSetHum = new LineDataSet(humidityList, "Humidity (%)");
        dataSetHum.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetHum.setValueTextSize(8);
        dataSetHum.setColor(Color.BLUE);
        dataSetHum.setCircleColor(Color.BLUE);

        LineDataSet dataSetTemp = new LineDataSet(tempList, "Temperature (°C)");
        dataSetTemp.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetTemp.setValueTextSize(8);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSetTemp);
        dataSets.add(dataSetHum);

        LineData data = new LineData(dataSets);

        return data;

    }

    /*
    *   Prepare the Response data of the Gas Sensor to display it in Diagramm
    * */
    public LineData getGasDataForDiagramm(String s){

        List<Entry> co2List = new ArrayList<Entry>();
        List<Entry> coList = new ArrayList<Entry>();
        List<Entry> nhList = new ArrayList<Entry>();

        try{
            //JSONObject jsonResponse = new JSONObject(re);
            JSONArray jsonMainNode = new JSONArray(s);
            for(int i = 0; i < jsonMainNode.length(); i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                //Double temperature = jsonChildNode.optDouble("temperature");
                Float co2 = BigDecimal.valueOf(jsonChildNode.getDouble("CO2")).floatValue();
                co2List.add(new Entry((float)i, co2));
                Float co = BigDecimal.valueOf(jsonChildNode.getDouble("CO")).floatValue();
                coList.add(new Entry((float)i, co));
                Float nh = BigDecimal.valueOf(jsonChildNode.getDouble("NH4")).floatValue();
                coList.add(new Entry((float)i, nh));
            }
        } catch (JSONException e){
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        LineDataSet dataSetCo2 = new LineDataSet(co2List, "CO2 (ppm)");
        dataSetCo2.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetCo2.setValueTextSize(8);
        dataSetCo2.setColor(Color.BLUE);
        dataSetCo2.setCircleColor(Color.BLUE);

        LineDataSet dataSetCo = new LineDataSet(coList, "CO (ppm)");
        dataSetCo.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetCo.setValueTextSize(8);

        LineDataSet dataSetNh = new LineDataSet(co2List, "NH4 (ppm)");
        dataSetNh.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetNh.setValueTextSize(8);
        dataSetNh.setColor(Color.BLUE);
        dataSetNh.setCircleColor(Color.BLUE);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSetCo2);
        dataSets.add(dataSetCo);
        dataSets.add(dataSetNh);

        LineData data = new LineData(dataSets);

        return data;
    }




}
