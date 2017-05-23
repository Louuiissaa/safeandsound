package com.safeandsound.app.safeandsound.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.safeandsound.app.safeandsound.controller.ruleengine.IfStatement;
import com.safeandsound.app.safeandsound.controller.ruleengine.Rule;
import com.safeandsound.app.safeandsound.controller.ruleengine.ThenStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private static final String TABLE_RULE_IF_THEN = "RULE_IF_THEN";

    //Allgemeine Spalten
    private static final String KEY_ID = "ID";
    private static final String KEY_RULE = "RULE";

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


    //Spalten der RULE_IF_THEN Tabelle
    private static final String KEY_IDRULE = "IDRule";
    private static final String KEY_IDIFSTATEMENT = "IDIfStatement";
    private static final String KEY_IDTHENSTATEMENT = "IDThenStatement";

    //Create Tables Strings
    String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_NAME + " TEXT NOT NULL,"
            + KEY_PASSWORD + " TEXT," + KEY_EMAIL + " TEXT NOT NULL UNIQUE,"
            + KEY_IPADDRESSRP + " TEXT," + KEY_ISLOGGEDIN + " INTEGER)";

    String CREATE_RULE_TABLE = "CREATE TABLE " + TABLE_RULE + "(" + KEY_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_USERID + " INTEGER NOT NULL," +
            "FOREIGN KEY (" + KEY_USERID + ") REFERENCES " + TABLE_USER + " ("+ KEY_ID + "))";

    String CREATE_IFSTATEMENT_TABLE = "CREATE TABLE " + TABLE_IFSTATEMENT + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_DATATYPE + " TEXT NOT NULL,"
            + KEY_COMPARISON_TYPE + " TEXT NOT NULL," + KEY_COMPARISON_DATA + " TEXT NOT NULL,"
            + KEY_RULE + " INTEGER NOT NULL)";

    String CREATE_THENSTATEMENT_TABLE = "CREATE TABLE " + TABLE_THENSTATEMENT + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_THENTEXT + " TEXT NOT NULL,"
            + KEY_THENTYPE + " TEXT NOT NULL," + KEY_RULE + " INTEGER NOT NULL)";

    String CREATE_RULE_IF_THEN_TABLE = "CREATE TABLE " + TABLE_RULE + "(" + KEY_IDRULE +
            " INTEGER NOT NULL," + KEY_IDIFSTATEMENT + " INTEGER NOT NULL,"
            + KEY_IDTHENSTATEMENT + " INTEGER NOT NULL," +
            "FOREIGN KEY (" + KEY_IDRULE + ") REFERENCES " + TABLE_RULE + " ("+ KEY_ID + "), " +
            "FOREIGN KEY (" + KEY_IDIFSTATEMENT + ") REFERENCES " + TABLE_IFSTATEMENT + " ("+ KEY_ID + "), " +
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
        db.execSQL(CREATE_RULE_IF_THEN_TABLE);
    }

    // Alte Tabelle wird gelöscht und neue erstellt
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RULE_IF_THEN);
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
        Log.d(TAG, "Fetching user from Sqlite: " + ipAddressRP);

        return ipAddressRP;
    }

    /**
     * UserID des eingeloggten Users wird zurückgegeben
     */
    public String getloggedInUser() {
        HashMap<String, String> user = new HashMap<String, String>();
        String ipAddressRP = "";
        String selectQuery = "SELECT ipAddressRP FROM " + TABLE_USER + " WHERE " + KEY_ISLOGGEDIN + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if(cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                ipAddressRP = cursor.getString(0);
            }
            cursor.close();
            db.close();
        }
        // return user
        Log.d(TAG, "Fetching IP Address from Sqlite: " + ipAddressRP);

        return ipAddressRP;
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
    public void addIfStatement(int id, String dataType, String comparisonType, String comparisonData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_DATATYPE, dataType); //DataType
        values.put(KEY_COMPARISON_TYPE, comparisonType); // ComparisonType
        values.put(KEY_COMPARISON_DATA, comparisonData); // ComparisonData

        // Werte werden hinzugefügt
        db.insert(TABLE_IFSTATEMENT, null, values);
        db.close();

        Log.d(TAG, "New if statement inserted into sqlite.");
    }

    /**
     * THEN-Statement wird in DB gespeichert
     * */
    public void addThenStatement(int id, String thenText, String thenType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_THENTEXT, thenText); // ThenText
        values.put(KEY_THENTYPE, thenType); // ComparisonType

        // Werte werden hinzugefügt
        db.insert(TABLE_THENSTATEMENT, null, values);
        db.close();

        Log.d(TAG, "New then statement inserted into sqlite.");
    }

    /**
     * THEN-Statement wird in DB gespeichert
     * */
    public void addRule(int id, int userid, int idIfStatement, int idThenStatement) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_USERID, userid);
        values.put(KEY_IDIFSTATEMENT, idIfStatement); // ID IfStatement
        values.put(KEY_IDTHENSTATEMENT, idThenStatement); // ID ThenStatement

        // Werte werden hinzugefügt
        db.insert(TABLE_RULE, null, values);
        db.close();

        Log.d(TAG, "New then statement inserted into sqlite.");
    }

    /**
     * Eingeloggter User wird zurückgegeben
     */
    public List<Rule> getRules(String userid) {
        List<Rule> result = new ArrayList<Rule>();
        HashMap<String, String> rules = new HashMap<String, String>();
        String selectRuleQuery = "SELECT  * FROM " + TABLE_RULE + " WHERE " + KEY_USERID + " = " + userid;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectRuleQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            IfStatement ifStatement = null;
            String selectIfQuery = "SELECT  * FROM " + TABLE_IFSTATEMENT + " WHERE " + KEY_ID + " = " + cursor.getString(2);
            Cursor cursorIf = db.rawQuery(selectIfQuery, null);
            if(cursorIf.getCount() > 0){
                ifStatement = new IfStatement(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            }
            ThenStatement thenStatement = null;
            String selectThenQuery = "SELECT  * FROM " + TABLE_THENSTATEMENT + " WHERE " + KEY_ID + " = " + cursor.getString(3);
            Cursor cursorThen = db.rawQuery(selectThenQuery, null);
            if(cursorThen.getCount() > 0){
                thenStatement = new ThenStatement(cursor.getString(1), cursor.getString(2));
            }
            cursorIf.close();
            cursorThen.close();
            result.add(new Rule(ifStatement, thenStatement));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + rules.toString());

        return result;
    }

}
