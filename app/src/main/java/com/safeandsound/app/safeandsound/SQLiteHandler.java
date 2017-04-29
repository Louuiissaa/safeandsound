package com.safeandsound.app.safeandsound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by louisapabst on 25.04.17.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

        private static final String TAG = SQLiteHandler.class.getSimpleName();

        // Datenbank Version
        private static final int DATABASE_VERSION = 1;

        // Datenbank Name
        private static final String DATABASE_NAME = "android_api";

        // Datenbank Tabelle zum LogIn
        private static final String TABLE_USER = "USER";

        // Spalten der USER Tabelle
        private static final String KEY_ID = "User_ID";
        private static final String KEY_NAME = "Name";
        private static final String KEY_PASSWORD = "Password";
        private static final String KEY_EMAIL = "Email";
        private static final String KEY_IPADDRESSRP = "IPAddressRP";
        private static final String KEY_ISLOGGEDIN = "isLoggedIn";

        public SQLiteHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Tabelle USER wird erstellt
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_LOGIN_TABLES = "CREATE TABLE " + TABLE_USER + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_NAME + " TEXT NOT NULL,"
                    + KEY_PASSWORD + " TEXT," + KEY_EMAIL + " TEXT NOT NULL UNIQUE,"
                    + KEY_IPADDRESSRP + " TEXT," + KEY_ISLOGGEDIN + " INTEGER)";
            db.execSQL(CREATE_LOGIN_TABLES);

            Log.d(TAG, "Database tables created: " + CREATE_LOGIN_TABLES);
        }

        // Alte Tabelle wird gelöscht und neue erstellt
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
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
         * Datenbank wird gelöscht und wieder neu erstellt
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

}
