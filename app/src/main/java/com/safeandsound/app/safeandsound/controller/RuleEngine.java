package com.safeandsound.app.safeandsound.controller;

import android.renderscript.RenderScript;
import android.renderscript.Script;

import com.eclipsesource.v8.V8;
import com.safeandsound.app.safeandsound.AppController;
import com.safeandsound.app.safeandsound.controller.database.SQLiteHandler;
import com.safeandsound.app.safeandsound.model.ruleengine.IfStatement;
import com.safeandsound.app.safeandsound.model.ruleengine.Rule;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import android.renderscript.ScriptC;
import android.util.Log;

import static android.content.ContentValues.TAG;


/**
 * Created by louisapabst on 24.05.17.
 */

public class RuleEngine {
    private SQLiteHandler db;

    public void run(HashMap<String, String> values){
        try {
            PushMessage push = new PushMessage();
            push.sendNotification("test");
            String variables = "";
            V8 runtime = V8.createV8Runtime();
            db = new SQLiteHandler(AppController.getInstance());
            List<String> rules = db.getRuleStrings(db.getloggedInUser());
            for (int r = 0; r < rules.size(); r++) {
                String rule = rules.get(r);
                Iterator it = values.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    variables += "var " + entry.getKey().toString() + " = " + entry.getValue() + ";";
                }
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

    // if(5 < 8){
    //      return "Open window";
    // }
}
