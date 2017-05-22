package com.safeandsound.app.safeandsound.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.safeandsound.app.safeandsound.AppConfig;
import com.safeandsound.app.safeandsound.ConnectionFeed;
import com.safeandsound.app.safeandsound.ExceptionHandler;
import com.safeandsound.app.safeandsound.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Dominic on 19.05.2017.
 */

public class LibraryActivity extends FragmentActivity{

    private int countMotionItems = 0;
    private int[] helperArr;
    List<Map<String,String>> motionList = new ArrayList<Map<String, String>>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.library);

        String result = "[]";

        //Get Request, um alle Zeitpunkte zu erhalten, in denen Bewegung erkannt wurde
        try {
            URL urlObj = new URL(AppConfig.URL_MOTION);
            result = new ConnectionFeed().execute(urlObj.toString()).get();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }

        /*
            Wertet den erhaltenen String vom Backend aus

         */
        initList(result);

        final ListView listView = (ListView)findViewById(R.id.listViewLibrary);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, motionList, android.R.layout.simple_expandable_list_item_1, new String[] {"id"}, new int[] {android.R.id.text1} );

        /*
        *   ClickListener: Navigiert zur MotionPictureActivity, die das zum einem Zeitpunkt gespeicherte Bild anzeigt
        * */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LibraryActivity.this, MotionPictureActivity.class);
                intent.putExtra("id", countMotionItems-i );
                startActivity(intent);
            }
        });
        listView.setAdapter(simpleAdapter);
    }

    private void initList(String re){

        try{
            //JSONObject jsonResponse = new JSONObject(re);
            JSONArray jsonMainNode = new JSONArray(re);
            for(int i = 0; i < jsonMainNode.length(); i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String id = jsonChildNode.optString("ID");
                countMotionItems = i +1;
                String timestamp = jsonChildNode.optString("time_stamp");
                String outPut = id+ "\t\t\t Time:\t" + timestamp;
                motionList.add(createMotionItems("id", outPut));
            }
        } catch (JSONException e){
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private HashMap<String, String> createMotionItems(String name, String number){
        HashMap<String, String> motionNr = new HashMap<String, String>();
        motionNr.put(name, number);
        return motionNr;
    }


}
