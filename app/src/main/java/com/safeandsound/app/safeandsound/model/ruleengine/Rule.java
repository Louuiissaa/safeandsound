package com.safeandsound.app.safeandsound.model.ruleengine;

import java.util.List;

/**
 * Created by louisapabst on 15.05.17.
 */

public class Rule {
    private List<IfStatement> ifStatements;
    private String ruleScript;
    private List<ThenStatement> thenStatements;

    public Rule(List<IfStatement> ifStatements, List<ThenStatement> thenStatements){
        this.ifStatements = ifStatements;
        this.thenStatements = thenStatements;
    }

    public Rule(String ruleScript, List<ThenStatement> thenStatements){
        this.ruleScript = ruleScript;
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

    public List<ThenStatement> getThenStatements() {
        return thenStatements;
    }

    public void setThenStatements(List<ThenStatement> thenStatements) {
        this.thenStatements = thenStatements;
    }

    public String getRuleScript() {
        return ruleScript;
    }

    public void setRuleScript(String ruleScript) {
        this.ruleScript = ruleScript;
    }
}
