package com.safeandsound.app.safeandsound.controller.database;

/**
 * Created by louisapabst on 20.05.17.
 */

public class ThenDB {
    int ID;
    String text;
    String thenType;

    public ThenDB(){
    }

    public ThenDB(String text, String thenType){
        this.text = text;
        this.thenType = thenType;
    }

    public ThenDB(int ID, String text, String thenType){
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
