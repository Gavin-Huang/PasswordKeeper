package com.example.huanghailiang.passwordkeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 12;

    // Database Name
    private static final String DATABASE_NAME = "HHLcrud.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_STUDENT = "CREATE TABLE " + MyNote.TABLE  + "("
                + MyNote.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + MyNote.KEY_date + " TEXT, "
                + MyNote.KEY_content + " TEXT,"
                + MyNote.KEY_title + " TEXT )";
        db.execSQL(CREATE_TABLE_STUDENT);

        String CREATE_TABLE_Account = "CREATE TABLE " + MyAccount.TABLE  + "("
                + MyAccount.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + MyAccount.KEY_Password + " TEXT) ";
        db.execSQL(CREATE_TABLE_Account);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + MyNote.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MyAccount.TABLE);
        // Create tables again
        onCreate(db);
    }

}