package edu.cmu.ece.jsphdev.foodie.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author  Yaoxiu Hu
 * Class for handling local database
 */
public class MyDataBaseHelper extends SQLiteOpenHelper{

    public static final String CREATE_MESSAGE_RECORD = "create table records ("
            + "id integer primary key autoincrement, "
            + "username text, "
            + "message text)";

    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_MESSAGE_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
