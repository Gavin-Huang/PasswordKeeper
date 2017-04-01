package com.example.huanghailiang.passwordkeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by huanghailiang on 2016/8/10.
 */
public class MyAccountRepo {
    private DBHelper dbHelper;

    public MyAccountRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(MyAccount myAccount) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyAccount.KEY_Password, myAccount.password);

        // Inserting Row
        long MyAccountID = db.insert(MyAccount.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) MyAccountID;
    }

    private void delete(int myAccountID) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(MyAccount.TABLE, MyAccount.KEY_ID + "= ?", new String[]{String.valueOf(myAccountID)});
        db.close(); // Closing database connection
    }
    public   void ResetPassword()
    {
        ArrayList<HashMap<String, String>> NoteList =  getMyAccountList();
        boolean result=false;
        String id="Unkown";
        for (int i =0; i< NoteList.size();i++)
        {
            id =  NoteList.get(i).get("id");
            delete(Integer.parseInt(id));
        }
    }
    public void update(MyAccount myAccount) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MyAccount.KEY_Password, myAccount.password);


        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(MyAccount.TABLE, values, MyAccount.KEY_ID + "= ?", new String[]{String.valueOf(myAccount.Note_ID)});
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>> getMyAccountList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                MyAccount.KEY_ID + "," +
                MyAccount.KEY_Password + "" +
                " FROM " + MyAccount.TABLE;

        //Student student = new Student();
        ArrayList<HashMap<String, String>> accountList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> myAccount = new HashMap<String, String>();
                myAccount.put("id", cursor.getString(cursor.getColumnIndex(MyAccount.KEY_ID)));
                myAccount.put("password", cursor.getString(cursor.getColumnIndex(MyAccount.KEY_Password)));
                accountList.add(myAccount);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return accountList;

    }

    public MyAccount getMyAccountById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                MyAccount.KEY_ID + "," +
                MyAccount.KEY_Password+ "" +
                " FROM " + MyAccount.TABLE
                + " WHERE " +
                MyAccount.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        MyAccount myAccount = new MyAccount();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                myAccount.Note_ID =cursor.getInt(cursor.getColumnIndex(MyAccount.KEY_ID));
                myAccount.password =cursor.getString(cursor.getColumnIndex(MyAccount.KEY_Password));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return myAccount;
    }
}
