package com.example.pratap.attendancemonitor.data;

/**
 * Created by pratap on 1/4/17.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.pratap.attendancemonitor.data.SubjectContract.SubjectEntry;

/**
 * {@link ContentProvider} for Attendance Monitor app.
 */
public class SubjectProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = SubjectProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the subjects table */
    private static final int SUBJECTS = 100;

    /** URI matcher code for the content URI for a single subject in the subjects table */
    private static final int SUBJECT_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.pratap.attendancemonitor/attendancemonitor"
        // will map to the integer code {@link #SUBJECTS}. This URI is used to provide access to MULTIPLE rows
        // of the subjects table.
        sUriMatcher.addURI(SubjectContract.CONTENT_AUTHORITY, SubjectContract.PATH_SUBJECTS, SUBJECTS);

        // The content URI of the form "content://com.example.pratap.attendancemonitor/attendancemonitor/#"
        // will map to the integer code {@link #SUBJECT_ID}. This URI is used to provide access to ONE single row
        // of the subjects table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.pratap.attendancemonitor/attendancemonitor/3" matches, but
        // "content://com.example.pratap.attendancemonitor/attendancemonitor" (without a number at the end)
        // doesn't match.
        sUriMatcher.addURI(SubjectContract.CONTENT_AUTHORITY, SubjectContract.PATH_SUBJECTS + "/#", SUBJECT_ID);
    }

    /** Database helper object */
    private SubjectDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new SubjectDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case SUBJECTS:
                // For the SUBJECTS code, query the subjects table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the subjects table.
                cursor = database.query(SubjectEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case SUBJECT_ID:
                // For the SUBJECT_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.pratap.attendancemonitor/attendancemonitor/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = SubjectEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(SubjectEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SUBJECTS:
                return insertSubject(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a subject into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertSubject(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(SubjectEntry.COLUMN_SUBJECT_NAME);
        if (name == null  || name == "") {
            throw new IllegalArgumentException("Subject requires a name");
        }

        // Check that the number of periods on monday is valid
        Integer monday = values.getAsInteger(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_MONDAY);
        if (!(monday >= 0 || monday <= 9)) {
            throw new IllegalArgumentException("Subject requires valid number of periods");
        }

        // Check that the number of periods on tuesday is valid
        Integer tuesday = values.getAsInteger(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_TUESDAY);
        if (tuesday < 0 || tuesday > 9) {
            throw new IllegalArgumentException("Subject requires valid number of periods");
        }

        // Check that the number of periods on wednesday is valid
        Integer wednesday = values.getAsInteger(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_WEDNESDAY);
        if (wednesday < 0 || wednesday > 9) {
            throw new IllegalArgumentException("Subject requires valid number of periods");
        }

        // Check that the number of periods on thursday is valid
        Integer thursday = values.getAsInteger(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_THURSDAY);
        if (thursday < 0 || thursday > 9) {
            throw new IllegalArgumentException("Subject requires valid number of periods");
        }

        // Check that the number of periods on friday is valid
        Integer friday = values.getAsInteger(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_FRIDAY);
        if (friday < 0 || friday > 9) {
            throw new IllegalArgumentException("Subject requires valid number of periods");
        }

        // No need to check the present, absent, any value is valid (including null).

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new subject with the given values
        long id = database.insert(SubjectEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the subject content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SUBJECTS:
                return updateSubject(uri, contentValues, selection, selectionArgs);
            case SUBJECT_ID:
                // For the SUBJECT_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = SubjectEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateSubject(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update subjects in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more subjects).
     * Return the number of rows that were successfully updated.
     */
    private int updateSubject(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link SubjectEntry#COLUMN_SUBJECT_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(SubjectEntry.COLUMN_SUBJECT_NAME)) {
            String name = values.getAsString(SubjectEntry.COLUMN_SUBJECT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Subject requires a name");
            }
        }

        // If the {@link SubjectEntry#COLUMN_NUMBER_OF_PERIODS_ON_MONDAY} key is present,
        // check that the number of period value is valid.
        if (values.containsKey(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_MONDAY)) {
            Integer monday = values.getAsInteger(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_MONDAY);
            if (monday < 0 || monday > 9) {
                throw new IllegalArgumentException("Subject requires valid number of periods");
            }
        }

        // If the {@link SubjectEntry#COLUMN_NUMBER_OF_PERIODS_ON_TUESDAY} key is present,
        // check that the number of period value is valid.
        if (values.containsKey(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_TUESDAY)) {
            Integer tuesday = values.getAsInteger(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_TUESDAY);
            if (tuesday < 0 || tuesday > 9) {
                throw new IllegalArgumentException("Subject requires valid number of periods");
            }
        }

        // If the {@link SubjectEntry#COLUMN_NUMBER_OF_PERIODS_ON_WEDNESDAY} key is present,
        // check that the number of period value is valid.
        if (values.containsKey(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_WEDNESDAY)) {
            Integer wednesday = values.getAsInteger(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_WEDNESDAY);
            if (wednesday < 0 || wednesday > 9) {
                throw new IllegalArgumentException("Subject requires valid number of periods");
            }
        }

        // If the {@link SubjectEntry#COLUMN_NUMBER_OF_PERIODS_ON_THURSDAY} key is present,
        // check that the number of period value is valid.
        if (values.containsKey(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_THURSDAY)) {
            Integer thursday = values.getAsInteger(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_THURSDAY);
            if (thursday < 0 || thursday > 9) {
                throw new IllegalArgumentException("Subject requires valid number of periods");
            }
        }

        // If the {@link SubjectEntry#COLUMN_NUMBER_OF_PERIODS_ON_FRIDAY} key is present,
        // check that the number of period value is valid.
        if (values.containsKey(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_FRIDAY)) {
            Integer friday = values.getAsInteger(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_FRIDAY);
            if (friday < 0 || friday > 9) {
                throw new IllegalArgumentException("Subject requires valid number of periods");
            }
        }

        // No need to check the present, absent, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(SubjectEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SUBJECTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(SubjectEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SUBJECT_ID:
                // Delete a single row given by the ID in the URI
                selection = SubjectEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(SubjectEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SUBJECTS:
                return SubjectEntry.CONTENT_LIST_TYPE;
            case SUBJECT_ID:
                return SubjectEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
