package com.safeandsound.app.safeandsound.controller.ruleengine;

import java.util.HashMap;

/**
 * Created by louisapabst on 15.05.17.
 */

public class Rule {
    /*
    String: declares which sensor input
    Integer: declares by what the sensor is followed in the statement (0 = statement is finished, 1 = AND, 2 = OR)
     */
    HashMap<String, Integer> ifParameters;

    public void setIfParameters(HashMap<String, Integer> ifParameters) {
        this.ifParameters = ifParameters;
    }

    public String combineIfStatement(HashMap<String, Integer> map){
        String ifStatement = "";
        String endOfIf = "";
        for (HashMap.Entry<String, Integer> entry : map.entrySet())
        {
            if(entry.getValue() == 0){
                ifStatement = ifStatement + entry.getKey();
                return ifStatement;
            }else if(entry.getValue() == 1){
                ifStatement = ifStatement + entry.getKey() + " AND ";
            }else if(entry.getValue() == 2){
                ifStatement = ifStatement + entry.getKey() + " OR ";
            }
        }
        return ifStatement;
    }

    public void combineThenStatement(){

    }
}
