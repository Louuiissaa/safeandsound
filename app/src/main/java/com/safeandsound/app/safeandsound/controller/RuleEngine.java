package com.safeandsound.app.safeandsound.controller;

import com.eclipsesource.v8.V8;
import com.safeandsound.app.safeandsound.model.database.SQLiteHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.util.Log;

import static android.content.ContentValues.TAG;


/**
 * Created by louisapabst on 24.05.17.
 */

public class RuleEngine {
    private SQLiteHandler db;

    /**
     * Setzt zu dem JavaScript der Regeln die Variablen hinzu und interpretiert dieses in einer V8 Umgebung (JavaScript Umgebung)
     * @param values
     */
    public void run(HashMap<String, String> values){
        try {
            PushNotification push = new PushNotification();
            String variables = "var hour = new Date().getHours();";
            variables += "var day = new Date().getDate();";
            variables += "var month = new Date().getMonth()+1;";
            variables += "var weekday = new Date().getDay();";
            V8 runtime = V8.createV8Runtime();
            db = new SQLiteHandler(AppController.getInstance());
            List<String> rules = db.getRuleStrings(db.getloggedInUser());
            Iterator it = values.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                variables += "var " + entry.getKey().toString() + " = " + entry.getValue() + ";";
            }
            for (int r = 0; r < rules.size(); r++) {
                String rule = rules.get(r);
                String notificationText = runtime.executeStringScript(variables + rule);
                runtime.release();
                if(!notificationText.isEmpty()) {
                    push.sendNotification(notificationText);
                }
            }
        }catch (IllegalStateException e ){
            Log.d(TAG, "RuleEngine Exception: " + e);
        }
    }
}
