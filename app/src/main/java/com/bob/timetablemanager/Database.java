package com.bob.timetablemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "Database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void checkTable(String date) {
        String create = "CREATE TABLE IF NOT EXISTS `"+date+
                "` (`ID` integer, `TASK` text, `From` text, `To` text, `Color` text );";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(create);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addTask(Task t, String date) {
        checkTable(date);
        SQLiteDatabase db = this.getWritableDatabase();
        String insert = "INSERT INTO `"+date+"` (`ID`, `Task`, `From`, `To`, `Color`) VALUES " +
                "( '"+t.getID()+"', '"+t.getTask()+"', '"+t.getFromToString()+"', '"+t.getToToString()+
                "', '"+t.getColor()+"' );";
        db.execSQL(insert);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Task> getAllTasks(String date) {
        checkTable(date);
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Task> tasks = new ArrayList<>();
        String select = "SELECT * FROM `"+date+"`;";
        Cursor cursor = db.rawQuery(select, null);
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                Task t = new Task();
                t.setID(cursor.getInt(0));
                t.setTask(cursor.getString(1));
                t.setFrom(cursor.getString(2));
                t.setTo(cursor.getString(3));
                t.setColor(cursor.getString(4));
                tasks.add(t);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getNextID(String date) {
        ArrayList<Task> tasks = getAllTasks(date);
        int id = 0;
        int size = tasks.size();
        if (size != 0) {
            int lastIndex = tasks.size()-1;
            id = tasks.get(lastIndex).getID()+1;
        }
        return id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateTask(Task t, String date) {
        String update = "UPDATE `"+date+"` SET `TASK` = '"+t.getTask()+"', `From` = '"+
                t.getFromToString()+"', `To` = '"+t.getToToString()+"', `Color` = '"+t.getColor()
                +"' WHERE `ID` = "+t.getID()+";";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(update);
    }

    public void deleteTask(int id, String date) {
        String delete = "DELETE FROM `"+date+"` WHERE `ID` = '"+id+"';";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(delete);
    }

}
