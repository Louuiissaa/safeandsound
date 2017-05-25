package com.safeandsound.app.safeandsound.controller.ruleengine;

/**
 * Created by louisapabst on 16.05.17.
 */

public class ThenStatement {
    int ID;
    String text;
    String thenType;

    public ThenStatement(){
    }

    public ThenStatement(String text, String thenType){
        this.text = text;
        this.thenType = thenType;
    }

    public ThenStatement(int ID, String text, String thenType){
        this.ID = ID;
        this.text = text;
        this.thenType = thenType;
    }

    //Getter & Setter

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getThenType() {
        return thenType;
    }

    public void setThenType(String thenType) {
        this.thenType = thenType;
    }
}
