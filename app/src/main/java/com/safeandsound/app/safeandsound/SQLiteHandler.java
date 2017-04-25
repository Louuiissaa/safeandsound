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

        public SQLiteHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Tabelle USER wird erstellt
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_NAME + " TEXT NOT NULL,"
                    + KEY_PASSWORD + " TEXT," + KEY_EMAIL + " TEXT NOT NULL UNIQUE,"
                    + KEY_IPADDRESSRP + " TEXT" + ")";
            db.execSQL(CREATE_LOGIN_TABLE);

            Log.d(TAG, "Database tables created");
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
        public void addUser(int userID, String name, String password, String email, String ipAddressRP) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ID, userID);
            values.put(KEY_NAME, name); // Name
            values.put(KEY_PASSWORD, password); // Password
            values.put(KEY_EMAIL, email); // Email
            values.put(KEY_IPADDRESSRP, ipAddressRP); // IP Address RP

            // Werte werden hinzugefügt
            long id = db.insert(TABLE_USER, null, values);
            db.close();

            Log.d(TAG, "New user inserted into sqlite: " + id);
        }

        /**
         * Getting user data from database
         * */
        public HashMap<String, String> getUserDetails() {
            HashMap<String, String> user = new HashMap<String, String>();
            String selectQuery = "SELECT  * FROM " + TABLE_USER;

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
            }
            cursor.close();
            db.close();
            // return user
            Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

            return user;
        }

        /**
         * Datenbank wird gelöscht und wieder neu erstellt
         * */
        public void deleteUsers() {
            SQLiteDatabase db = this.getWritableDatabase();
            // Delete All Rows
            db.delete(TABLE_USER, null, null);
            db.close();

            Log.d(TAG, "Deleted all user info from sqlite");
        }

    }
