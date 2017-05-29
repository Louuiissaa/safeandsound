package com.safeandsound.app.safeandsound.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.safeandsound.app.safeandsound.model.ruleengine.IfStatement;
import com.safeandsound.app.safeandsound.model.ruleengine.Rule;
import com.safeandsound.app.safeandsound.model.ruleengine.ThenStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by louisapabst on 25.04.17.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // Datenbank Version
    private static final int DATABASE_VERSION = 1;

    // Datenbank Name
    private static final String DATABASE_NAME = "SAFEANDSOUND";

    // Datenbank Tabellen
    private static final String TABLE_USER = "USER";
    private static final String TABLE_RULE = "RULE";
    private static final String TABLE_IFSTATEMENT = "IFSTATEMENT";
    private static final String TABLE_THENSTATEMENT = "THENSTATEMENT";
    private static final String TABLE_RULE_IF = "RULE_IF";
    private static final String TABLE_RULE_THEN = "RULE_THEN";

    //Allgemeine Spalten
    private static final String KEY_ID = "ID";
    private static final String KEY_RULE = "RULE";
    private static final String KEY_CONJUNCTION = "Conjunction";
    private static final String KEY_IDRULE = "IDRule";

    // Spalten der USER Tabelle
    private static final String KEY_NAME = "Name";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_IPADDRESSRP = "IPAddressRP";
    private static final String KEY_ISLOGGEDIN = "isLoggedIn";

    // Spalten der RULE Tabelle
    private static final String KEY_USERID = "User_ID";

    // Spalten der IFSTATEMENT Tabelle
    private static final String KEY_DATATYPE = "DataType";
    private static final String KEY_COMPARISON_TYPE = "ComparisonType";
    private static final String KEY_COMPARISON_DATA = "ComparisonData";

    // Spalten der THENSTATEMENT Tabelle
    private static final String KEY_THENTEXT = "ThenText";
    private static final String KEY_THENTYPE = "ThenType";


    //Spalten der RULE_IF Tabelle
    private static final String KEY_IDIFSTATEMENT = "IDIfStatement";

    //Spalten der RULE_THEN Tabelle
    private static final String KEY_IDTHENSTATEMENT = "IDThenStatement";

    //Create Tables Strings
    String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_NAME + " TEXT NOT NULL,"
            + KEY_PASSWORD + " TEXT," + KEY_EMAIL + " TEXT NOT NULL UNIQUE,"
            + KEY_IPADDRESSRP + " TEXT," + KEY_ISLOGGEDIN + " INTEGER)";

    String CREATE_RULE_TABLE = "CREATE TABLE " + TABLE_RULE + "(" + KEY_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_USERID + " INTEGER NOT NULL," +
            KEY_RULE + " TEXT NOT NULL," +
            "FOREIGN KEY (" + KEY_USERID + ") REFERENCES " + TABLE_USER + " ("+ KEY_ID + "))";

    String CREATE_IFSTATEMENT_TABLE = "CREATE TABLE " + TABLE_IFSTATEMENT + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_DATATYPE + " TEXT NOT NULL,"
            + KEY_COMPARISON_TYPE + " TEXT NOT NULL," + KEY_COMPARISON_DATA + " TEXT NOT NULL,"
            + KEY_CONJUNCTION + " INTEGER)";

    String CREATE_THENSTATEMENT_TABLE = "CREATE TABLE " + TABLE_THENSTATEMENT + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_THENTEXT + " TEXT NOT NULL,"
            + KEY_THENTYPE + " TEXT," + KEY_CONJUNCTION + " INTEGER)";

    String CREATE_RULE_IF_TABLE = "CREATE TABLE " + TABLE_RULE_IF + "(" + KEY_IDRULE +
            " INTEGER NOT NULL," + KEY_IDIFSTATEMENT + " INTEGER NOT NULL," +
            "FOREIGN KEY (" + KEY_IDRULE + ") REFERENCES " + TABLE_RULE + " ("+ KEY_ID + "), " +
            "FOREIGN KEY (" + KEY_IDIFSTATEMENT + ") REFERENCES " + TABLE_IFSTATEMENT + " ("+ KEY_ID + "))";

    String CREATE_RULE_THEN_TABLE = "CREATE TABLE " + TABLE_RULE_THEN + "(" + KEY_IDRULE +
            " INTEGER NOT NULL," + KEY_IDTHENSTATEMENT + " INTEGER NOT NULL," +
            "FOREIGN KEY (" + KEY_IDRULE + ") REFERENCES " + TABLE_RULE + " ("+ KEY_ID + "), " +
            "FOREIGN KEY (" + KEY_IDTHENSTATEMENT + ") REFERENCES " + TABLE_THENSTATEMENT + " ("+ KEY_ID + "))";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tabellen werden erstellt
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_RULE_TABLE);
        db.execSQL(CREATE_IFSTATEMENT_TABLE);
        db.execSQL(CREATE_THENSTATEMENT_TABLE);
        db.execSQL(CREATE_RULE_IF_TABLE);
        db.execSQL(CREATE_RULE_THEN_TABLE);
    }

    // Alte Tabelle wird gelöscht und neue erstellt
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RULE_THEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RULE_IF);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THENSTATEMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IFSTATEMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);
    }

    /**
    * User Informationen werden in DB gespeichert
    * */
    public void addUser(int userID, String name, String password, String email, String ipAddressRP, int isLoogedIn) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, userID);
        values.put(KEY_NAME, name); // Name
        values.put(KEY_PASSWORD, password); // Password
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_IPADDRESSRP, ipAddressRP); // IP Address RP
        values.put(KEY_ISLOGGEDIN, isLoogedIn); // true if User is currently logged in

        // Werte werden hinzugefügt
        long id = db.insert(TABLE_USER, null, values);
        db.close();

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Eingeloggter User wird zurückgegeben
     */
        public HashMap<String, String> getUserDetails() {
            HashMap<String, String> user = new HashMap<String, String>();
            String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE isLoggedIn = 1";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // Move to first row
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                user.put("user_id", cursor.getString(0));
                user.put("name", cursor.getString(1));
                user.put("password", cursor.getString(2));
                user.put("email", cursor.getString(3));
                user.put("ipAddressRP", cursor.getString(4));
                user.put("isLoggedIn", cursor.getString(5));
            }
            cursor.close();
            db.close();
            // return user
            Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

            return user;
        }

    /**
     * Eingeloggter User wird zurückgegeben
     */
    public boolean checkUserExists(String email) {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE email = '" + email + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            return true;
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return false;
    }

    /**
     * UserID des eingeloggten Users wird zurückgegeben
     */
    public String getUsersIPAddressRP(String email) {
        HashMap<String, String> user = new HashMap<String, String>();
        String ipAddressRP = "";
        String selectQuery = "SELECT  ipAddressRP FROM " + TABLE_USER + " WHERE email = '" + email + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor != null) {
            // Move to first row
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                ipAddressRP = cursor.getString(0);
            }
            cursor.close();
            db.close();
        }
        // return user
        Log.d(TAG, "Fetching ip Address from Sqlite: " + ipAddressRP);

        return ipAddressRP;
    }

    /**
     * UserID des eingeloggten Users wird zurückgegeben
     */
    public String getloggedInUser() {
        HashMap<String, String> user = new HashMap<String, String>();
        String userID = "";
        String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_USER + " WHERE " + KEY_ISLOGGEDIN + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if(cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                userID = cursor.getString(0);
            }
            cursor.close();
            db.close();
        }
        // return user
        Log.d(TAG, "Fetching IP Address from Sqlite: " + userID);

        return userID;
    }

        /**
         * User wird in Datenbank als ausgeloggt markiert
         * */
        public void logOutUser(String currentUserID) {
            SQLiteDatabase db = this.getWritableDatabase();
            // Delete All Rows
            String updateQuery = "UPDATE "+ TABLE_USER + " SET " + KEY_ISLOGGEDIN +" = 0 WHERE " + KEY_ID + " = " + currentUserID;
            db.execSQL(updateQuery);

            Log.d(TAG, "Logged current user out");
        }

        /**
        * Datenbank wird gelöscht und wieder neu erstellt
        * */
        public void logInUser(String currentUserID) {
            SQLiteDatabase db = this.getWritableDatabase();
            // Delete All Rows
            String updateQuery = "UPDATE "+ TABLE_USER + " SET " + KEY_ISLOGGEDIN +" = 1 WHERE " + KEY_ID + " = " + currentUserID;
            db.execSQL(updateQuery);

            Log.d(TAG, "Logged current user in");
        }

    /**
     * IF-Statement wird in DB gespeichert
     * */
    public long addIfStatement(String dataType, String comparisonType, String comparisonData, String conjunction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATATYPE, dataType); //DataType
        values.put(KEY_COMPARISON_TYPE, comparisonType); // ComparisonType
        values.put(KEY_COMPARISON_DATA, comparisonData); // ComparisonData
        values.put(KEY_CONJUNCTION, conjunction); // Conjunction

        // Werte werden hinzugefügt
        long id = db.insert(TABLE_IFSTATEMENT, null, values);
        db.close();

        Log.d(TAG, "New if statement inserted into sqlite.");
        return id;
    }


    /**
     * THEN-Statement wird in DB gespeichert
     * */
    public long addThenStatement(String thenText, String thenType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_THENTEXT, thenText); // ThenText
        values.put(KEY_THENTYPE, thenType); // ComparisonType

        // Werte werden hinzugefügt
        long id = db.insert(TABLE_THENSTATEMENT, null, values);
        db.close();

        Log.d(TAG, "New then statement inserted into sqlite.");
        return id;
    }

    /**
     * RULE-IF-Verknüpfung wird in DB gespeichert
     * */
    public void addRuleIf(long idRule, long idIf) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDRULE, idRule); // ThenText
        values.put(KEY_IDIFSTATEMENT, idIf); // ComparisonType

        // Werte werden hinzugefügt
        db.insert(TABLE_RULE_IF, null, values);
        db.close();

        Log.d(TAG, "New then statement inserted into sqlite.");
    }



    /**
     * RULE-THEN-Verknüpfung wird in DB gespeichert
     * */
    public void addRuleThen(long idRule, long idThen) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IDRULE, idRule); // ThenText
        values.put(KEY_IDTHENSTATEMENT, idThen); // ComparisonType

        // Werte werden hinzugefügt
        db.insert(TABLE_RULE_THEN, null, values);
        db.close();

        Log.d(TAG, "New then statement inserted into sqlite.");
    }

    /**
     * THEN-Statement wird in DB gespeichert
     * */
    public void addRule(String userid, List<IfStatement> ifStatements, List<ThenStatement> thenStatements) {
        SQLiteDatabase db = this.getWritableDatabase();

        String s_rule = getRuleString(ifStatements, thenStatements);
        ContentValues valuesRule = new ContentValues();
        valuesRule.put(KEY_USERID, userid);
        valuesRule.put(KEY_RULE, s_rule);

        // Werte werden hinzugefügt
        long idRule = db.insert(TABLE_RULE, null, valuesRule);
        for(int i = 0; i < ifStatements.size(); i++){
            IfStatement ifStatement = ifStatements.get(i);
            long idIf = addIfStatement(ifStatement.getDataType(), ifStatement.getComparisonType(), ifStatement.getComparisonData(), ifStatement.getConjunction());
            addRuleIf(idRule, idIf);
        }
        for(int t = 0; t < thenStatements.size(); t++) {
            ThenStatement thenStatement = thenStatements.get(t);
            long idThen = addThenStatement(thenStatement.getThenText(), thenStatement.getThenType());
            addRuleThen(idRule, idThen);
        }
        db.close();

        Log.d(TAG, "New then statement inserted into sqlite.");
    }

    public String getRuleString(List<IfStatement> ifStatements, List<ThenStatement> thenStatements){
        String s_if = getIfString(ifStatements);
        String s_then = getThenString(thenStatements);
        String s_rule = "if (" + s_if + ") { '" + s_then + "'; }";
        return s_rule;
    }

    public String getIfString(List<IfStatement> ifStatements){
        String s_if = "";
        for(int i = 0; i < ifStatements.size(); i++){
            IfStatement ifStatement = ifStatements.get(i);
            String datatype = ifStatement.getDataType();
            String comparisontype = ifStatement.getComparisonType();
            String comparisondata = ifStatement.getComparisonData();
            String conjunction = ifStatement.getConjunction();
            if(comparisondata.contains(";")){
                String[] comparisontypes = comparisontype.split(";");
                String[] comparisondatas = comparisondata.split(";");
                s_if += when(datatype, comparisontypes[0], comparisontypes[1], comparisondatas[0], comparisondatas[1], conjunction);
            }else{
                s_if += when(datatype, comparisontype, comparisondata, conjunction);
            }
        }
        return s_if;
    }

    public String when(String datatype, String comparisontype, String comparisondata, String conjunction){
        String template = "({datatype} {comparisontype} {comparisondata}) {conjunction}";
        template = template.replace("{datatype}", datatype).replace("{comparisontype}", comparisontype)
                .replace("{comparisondata}", comparisondata).replace("{conjunction}", conjunction);
        return template;
    }

    public String when(String datatype, String first_comparisontype, String second_comparisontype, String first_comparisondata, String second_comparisondata, String conjunction){
        String template = "({datatype} {first_comparisontype} {first_comparisondata} && {datatype} {second_comparisontype} {second_comparisondata}) {conjunction}";
        template = template.replace("{datatype}", datatype).replace("{first_comparisontype}", first_comparisontype)
                .replace("{second_comparisontype}", second_comparisontype).replace("{first_comparisondata}", first_comparisondata)
                .replace("{second_comparisondata}", second_comparisondata).replace("{conjunction}", conjunction);
        return template;
    }

    public String getThenString(List<ThenStatement> thenStatements){
        String s_then = "";
        for(int i = 0; i < thenStatements.size(); i++){
            ThenStatement thenStatement = thenStatements.get(i);
            String thentext = thenStatement.getThenText();
            s_then += thentext;
        }
        return s_then;
    }


    /**
     *
     * @param userid
     * @return
     */
    public List<String> getRuleStrings(String userid) {
        List<String> result = new ArrayList<String>();
        //HashMap<String, String> rules = new HashMap<String, String>();
        String selectRuleQuery = "SELECT " + KEY_RULE + " FROM " + TABLE_RULE + " WHERE " + KEY_USERID + " = " + userid;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectRuleQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                result.add(cursor.getString(0));
                cursor.moveToNext();
            }

        }
        return result;
    }

    /**
     *
     * @param userid
     * @return
     */
    public List<Rule> getRules(String userid) {
        List<Rule> result = new ArrayList<Rule>();
        //HashMap<String, String> rules = new HashMap<String, String>();
        String selectRuleQuery = "SELECT  * FROM " + TABLE_RULE + " WHERE " + KEY_USERID + " = " + userid;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectRuleQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                List<IfStatement> ifStatements = new ArrayList<IfStatement>();
                List<ThenStatement> thenStatements = new ArrayList<ThenStatement>();
                Rule rule = null;
                ThenStatement thenStatement = null;
                // Alle If Statements zur Regel erhalten
                String selectRuleIfQuery = "SELECT * FROM " + TABLE_RULE_IF + " WHERE " + KEY_IDRULE + " = " + cursor.getString(0);
                Cursor cursorRuleIf = db.rawQuery(selectRuleIfQuery, null);
                cursorRuleIf.moveToFirst();
                if (cursorRuleIf.getCount() > 0) {
                    for (int ri = 0; ri < cursorRuleIf.getCount(); ri++) {
                        String selectIfQuery = "SELECT  * FROM " + TABLE_IFSTATEMENT + " WHERE " + KEY_ID + " = " + cursorRuleIf.getString(1);
                        Cursor cursorIf = db.rawQuery(selectIfQuery, null);
                        cursorIf.moveToFirst();
                        if (cursorIf.getCount() > 0) {
                            IfStatement ifStatement = null;
                            ifStatement = new IfStatement(cursorIf.getString(1), cursorIf.getString(2), cursorIf.getString(3), cursorIf.getString(4));
                            ifStatements.add(ifStatement);
                        }
                        cursorIf.close();
                        cursorRuleIf.moveToNext();
                    }
                }
                // Alle Then Statements zur Regel erhalten
                String selectRuleThenQuery = "SELECT * FROM " + TABLE_RULE_THEN + " WHERE " + KEY_IDRULE + " = " + cursor.getString(0);
                Cursor cursorRuleThen = db.rawQuery(selectRuleThenQuery, null);
                cursorRuleThen.moveToFirst();
                if (cursorRuleThen.getCount() > 0) {
                    String selectThenQuery = "SELECT  * FROM " + TABLE_THENSTATEMENT + " WHERE " + KEY_ID + " = " + cursorRuleThen.getString(1);
                    Cursor cursorThen = db.rawQuery(selectThenQuery, null);
                    cursorThen.moveToFirst();
                    if (cursorThen.getCount() > 0) {
                        thenStatement = new ThenStatement(cursorThen.getString(1), cursorThen.getString(2), cursorThen.getString(3));
                        thenStatements.add(thenStatement);
                    }
                    cursorThen.close();
                }
                if (ifStatements.size() != 0 && thenStatements.size()!= 0) {
                    rule = new Rule(ifStatements, thenStatements);
                    result.add(rule);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching rules from Sqlite: " + result.toString());

        return result;
    }

}
