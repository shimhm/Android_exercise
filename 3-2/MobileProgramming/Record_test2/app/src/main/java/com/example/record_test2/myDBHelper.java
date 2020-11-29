package com.example.record_test2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class myDBHelper extends SQLiteOpenHelper {
    public myDBHelper(Context context) {
        super(context, "userSleep", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE userSleep ( start_date text, end_date text );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS userSleep");
        onCreate(db);
    }

    public Boolean insertData(String start, String end) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        sqlDB.execSQL("insert into userSleep values (datetime('"+start+"'), datetime('"+end+"'));");
        sqlDB.close();
        return true;
    }
}
