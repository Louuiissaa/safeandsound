package com.safeandsound.app.safeandsound.model.ruleengine;

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
    List<IfStatement> ifStatements;
    String ifStatement;
    //IfStatement ifStatements;
    //List<ThenStatement> thenStatements;
    ThenStatement thenStatements;

    public Rule(List<IfStatement> ifStatements, ThenStatement thenStatements){
        this.ifStatements = ifStatements;
        this.thenStatements = thenStatements;
    }

    public Rule(String ifStatement, ThenStatement thenStatements){
        this.ifStatement = ifStatement;
        this.thenStatements = thenStatements;
    }

    public void setIfParameters(List<IfStatement> ifStatements) {
        this.ifStatements = ifStatements;
    }


    //Getter & Setter
    public List<IfStatement> getIfStatements() {
        return ifStatements;
    }

    public void setIfStatements(List<IfStatement> ifStatements) {
        this.ifStatements = ifStatements;
    }

    public ThenStatement getThenStatements() {
        return thenStatements;
    }

    public void setThenStatements(ThenStatement thenStatements) {
        this.thenStatements = thenStatements;
    }

    public String getIfStatement() {
        return ifStatement;
    }

    public void setIfStatement(String ifStatement) {
        this.ifStatement = ifStatement;
    }
}
