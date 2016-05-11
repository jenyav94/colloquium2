package ru.csc.vikulov.todolist.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "reader.db";

    private static final String SQL_CREATE_ENTRIES_TABLE =
            "CREATE TABLE " + FeedsTable.TABLE_NAME
                    + "("
                    + FeedsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + FeedsTable.COLUMN_TITLE + " TEXT, "
                    + FeedsTable.COLUMN_DESCRIPTION + " TEXT, "
                    + FeedsTable.COLUMN_DATE + " DATETIME, "
                    + FeedsTable.COLUMN_DONE + " BOOLEAN, "
                    + FeedsTable.COLUMN_PRIOR + " BOOLEAN"
                    + ")";

    private static final String SQL_CREATE_ENTRIES_TABLE2 =
            "CREATE TABLE " + TagsTable.TABLE_NAME
                    + "("
                    + TagsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TagsTable.COLUMN_TASK + " TEXT, "
                    + TagsTable.COLUMN_TASK_ID + " TEXT "
                    + ")";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedsTable.TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES2 = "DROP TABLE IF EXISTS " + TagsTable.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_TABLE);
        db.execSQL(SQL_CREATE_ENTRIES_TABLE2);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES2);
        onCreate(db);
    }

}
