package com.example.pratap.attendancemonitor.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pratap.attendancemonitor.data.SubjectContract.SubjectEntry;

/**
 * Created by pratap on 1/4/17.
 */

/**
 * Database helper for Pets app. Manages database creation and version management.
 */
public class SubjectDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = SubjectDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "monitor.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link SubjectDbHelper}.
     *
     * @param context of the app
     */
    public SubjectDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the subjects table
        String SQL_CREATE_SUBJECTS_TABLE =  "CREATE TABLE " + SubjectEntry.TABLE_NAME + " ("
                + SubjectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SubjectEntry.COLUMN_SUBJECT_NAME + " TEXT NOT NULL, "
                + SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_MONDAY + " INTEGER NOT NULL DEFAULT 0, "
                + SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_TUESDAY + " INTEGER NOT NULL DEFAULT 0, "
                + SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_WEDNESDAY + " INTEGER NOT NULL DEFAULT 0, "
                + SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_THURSDAY + " INTEGER NOT NULL DEFAULT 0, "
                + SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_FRIDAY + " INTEGER NOT NULL DEFAULT 0, "
                + SubjectEntry.COLUMN_NUMBER_OF_DAYS_PRESENT + " INTEGER DEFAULT 0, "
                + SubjectEntry.COLUMN_NUMBER_OF_DAYS_ABSENT + " INTEGER DEFAULT 0, "
                + SubjectEntry.COLUMN_LAST_DATE + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_SUBJECTS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
