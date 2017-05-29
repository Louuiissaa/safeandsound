package com.safeandsound.app.safeandsound.model.ruleengine;

/**
 * Created by louisapabst on 16.05.17.
 */

public class ThenStatement {
    int ID;
    String thenText;
    String thenType;
    String conjunction;

    public ThenStatement(){
    }

    public ThenStatement(String thenText, String thenType, String conjunction){
        this.thenText = thenText;
        this.thenType = thenType;
        this.conjunction = conjunction;
    }

    public ThenStatement(int ID, String thenText, String thenType, String conjunction){
        this.ID = ID;
        this.thenText = thenText;
        this.thenType = thenType;
        this.conjunction = conjunction;
    }

    //Getter & Setter

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getThenText() {
        return thenText;
    }

    public void setThenText(String thenText) {
        this.thenText = thenText;
    }

    public String getThenType() {
        return thenType;
    }

    public void setThenType(String thenType) {
        this.thenType = thenType;
    }

    public String getConjunction() {
        return conjunction;
    }

    public void setConjunction(String conjunction) {
        this.conjunction = conjunction;
    }
}
