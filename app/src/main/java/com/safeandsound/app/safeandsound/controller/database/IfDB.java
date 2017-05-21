package com.safeandsound.app.safeandsound.controller.database;

/**
 * Created by louisapabst on 20.05.17.
 */

public class IfDB {
    int ID;
    String dataType;
    String comparisonType;
    String comparisonData;

    public IfDB(){
    }

    public IfDB(String dataType, String comparisonType, String comparisonData){
        this.dataType = dataType;
        this.comparisonType = comparisonType;
        this.comparisonData = comparisonData;
    }

    public IfDB(int ID, String dataType, String comparisonType, String comparisonData){
        this.ID = ID;
        this.dataType = dataType;
        this.comparisonType = comparisonType;
        this.comparisonData = comparisonData;
    }

    public String getDataType() {
        return dataType;
    }

    //Getter & Setter

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(String comparisonType) {
        this.comparisonType = comparisonType;
    }

    public String getComparisonData() {
        return comparisonData;
    }

    public void setComparisonData(String comparisonData) {
        this.comparisonData = comparisonData;
    }
}
