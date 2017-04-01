package com.example.huanghailiang.passwordkeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.HashMap;

public class MyNoteRepo {
    private DBHelper dbHelper;

    public MyNoteRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(MyNote student) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyNote.KEY_content, student.content);
        values.put(MyNote.KEY_date,student.date);
        values.put(MyNote.KEY_title, student.title);

        // Inserting Row
        long student_Id = db.insert(MyNote.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) student_Id;
    }

    public boolean delete(int Note_ID) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
       int i= db.delete(MyNote.TABLE, MyNote.KEY_ID + "= ?", new String[] { String.valueOf(Note_ID) });
        db.close(); // Closing database connection
        return  i>0;
    }

    public void update(MyNote myNote) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MyNote.KEY_content, myNote.content);
        values.put(MyNote.KEY_title,myNote.title);
        values.put(MyNote.KEY_date, myNote.date);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(MyNote.TABLE, values, MyNote.KEY_ID + "= ?", new String[] { String.valueOf(myNote.Note_ID) });
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>>  getNotesList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                MyNote.KEY_ID + "," +
                MyNote.KEY_date + "," +
                MyNote.KEY_title + "," +
                MyNote.KEY_content +
                " FROM " + MyNote.TABLE;

        //Student student = new Student();
        ArrayList<HashMap<String, String>> notelist = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> myNotes = new HashMap<String, String>();
                myNotes.put("id", cursor.getString(cursor.getColumnIndex(MyNote.KEY_ID)));
                myNotes.put("title", cursor.getString(cursor.getColumnIndex(MyNote.KEY_title)));
                notelist.add(myNotes);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notelist;

    }

    public MyNote getNotesById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                MyNote.KEY_ID + "," +
                MyNote.KEY_date + "," +
                MyNote.KEY_title + "," +
                MyNote.KEY_content +
                " FROM " + MyNote.TABLE
                + " WHERE " +
                MyNote.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        MyNote myNote = new MyNote();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                myNote.Note_ID =cursor.getInt(cursor.getColumnIndex(MyNote.KEY_ID));
                myNote.date =cursor.getString(cursor.getColumnIndex(MyNote.KEY_date));
                myNote.title  =cursor.getString(cursor.getColumnIndex(MyNote.KEY_title));
                myNote.content =cursor.getString(cursor.getColumnIndex(MyNote.KEY_content));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return myNote;
    }

}