package com.safeandsound.app.safeandsound.controller.ruleengine;

import java.util.HashMap;
import java.util.List;

/**
 * Created by louisapabst on 15.05.17.
 */

public class Rule {
    /*
    String: declares which sensor input
    Integer: declares by what the sensor is followed in the statement (0 = statement is finished, 1 = AND, 2 = OR)
     */
    //HashMap<IfStatement, Integer> ifStatements;
    IfStatement ifStatements;
    //List<ThenStatement> thenStatements;
    ThenStatement thenStatements;

    public Rule(IfStatement ifStatements, ThenStatement thenStatements){
        this.ifStatements = ifStatements;
        this.thenStatements = thenStatements;
    }

    /*public void setIfParameters(HashMap<IfStatement, Integer> ifStatements) {
        this.ifStatements = ifStatements;
    }*/

    public String combineIfStatement(HashMap<IfStatement, Integer> map){
        String ifStatement = "";
        String endOfIf = "";
        for (HashMap.Entry<IfStatement, Integer> entry : map.entrySet())
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
