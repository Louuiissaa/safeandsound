package com.safeandsound.app.safeandsound.controller.firebase;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.safeandsound.app.safeandsound.controller.RuleEngine;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by louisapabst on 30.04.17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseInstanceService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
            HashMap<String, String > values = new HashMap<String, String>();
            ObjectMapper mapper = new ObjectMapper();
            MapType type = mapper.getTypeFactory().constructMapType(
                    Map.class, String.class, String.class
            );
            JSONObject jObj = new JSONObject(remoteMessage.getData());
            try{
                values = mapper.readValue(jObj.toString(), type);
            }catch(IOException e){
                Log.d(TAG, e.toString());
            }
            RuleEngine ruleEngine = new RuleEngine();
            ruleEngine.run(values);

        Log.d(TAG, "From: " + remoteMessage.getFrom());
    }
}
