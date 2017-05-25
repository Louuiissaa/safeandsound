package com.safeandsound.app.safeandsound.model.ruleengine;

/**
 * Created by louisapabst on 16.05.17.
 */

public class ThenStatement {
    int ID;
    String thenText;
    String thenType;

    public ThenStatement(){
    }

    public ThenStatement(String thenText, String thenType){
        this.thenText = thenText;
        this.thenType = thenType;
    }

    public ThenStatement(int ID, String thenText, String thenType){
        this.ID = ID;
        this.thenText = thenText;
        this.thenType = thenType;
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
}
