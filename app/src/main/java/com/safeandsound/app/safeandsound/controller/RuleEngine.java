package com.safeandsound.app.safeandsound.controller;

import android.renderscript.RenderScript;
import android.renderscript.Script;

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


/**
 * Created by louisapabst on 24.05.17.
 */

public class RuleEngine {
    private SQLiteHandler db;

    public void checkRules(){
        String string = "{ [datatype] [comparisontype] [comparisondata]";

    }

    /*public void checkRules(String humidity, String temperature, String co2, String co){
        db = new SQLiteHandler(AppController.getInstance());
        List<Rule> rules = db.getRules(db.getloggedInUser());
        for(int i = 0; i < rules.size(); i++){
            Rule currentRule = rules.get(i);
            HashMap<IfStatement, Integer> ifStatements = currentRule.getIfStatements();
            Boolean b = checkIfs(ifStatements, humidity, temperature, co2, co);
            if(b){
                fireRule();
            }
        }
    }

    private Boolean checkIfs(HashMap<IfStatement, Integer> ifStatements){
        Iterator it = ifStatements.entrySet().iterator();
        String s_IfStatement = "";
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            IfStatement ifCurrentStatement =(IfStatement) entry.getKey();
            String dataType = ifCurrentStatement.getDataType();
            String comparisonType = ifCurrentStatement.getComparisonType();
            String comparisonData = ifCurrentStatement.getComparisonData();
            String comparisonData2 = "";
            String conjunction = ifCurrentStatement.getConjunction();
            if(comparisonType.equals("between")){
                String[] strings = comparisonData.split(";");
                comparisonData = strings[0];
                comparisonData2 = strings[1];
                s_IfStatement += "(" + dataType + " > " + comparisonData + " && " + dataType + " < " + comparisonData2 + ") " + conjunction + " ";
            } else {
                s_IfStatement += "(" + dataType + " " + comparisonType + " " + comparisonData + ") " + conjunction + " ";
            }
        }
        if(s_IfStatement){
            return true;
        }
        return false;
    }

    private void fireRule(){

    }*/



}
