package com.example.pratap.attendancemonitor.data;

/**
 * Created by pratap on 1/4/17.
 */

import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;

/**
 * API Contract for the Attendance Monitor app.
 */
public final class SubjectContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private SubjectContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.pratap.attendancemonitor";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.pratap.attendancemonitor/attendancemonitor/ is a valid path for
     * looking at subject data. content://com.example.pratap.attendancemonitor/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_SUBJECTS = "subjects";

    /**
     * Inner class that defines constant values for the attendancemonitor database table.
     * Each entry in the table represents a single subject.
     */
    public static final class SubjectEntry implements BaseColumns {

        /** The content URI to access the subject data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SUBJECTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of subjects.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUBJECTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single subject.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUBJECTS;

        /** Name of database table for subjects */
        public final static String TABLE_NAME = "subjects";

        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_SUBJECT_NAME ="name";
        public final static String COLUMN_NUMBER_OF_PERIODS_ON_MONDAY ="monday";
        public final static String COLUMN_NUMBER_OF_PERIODS_ON_TUESDAY ="tuesday";
        public final static String COLUMN_NUMBER_OF_PERIODS_ON_WEDNESDAY ="wednesday";
        public final static String COLUMN_NUMBER_OF_PERIODS_ON_THURSDAY ="thursday";
        public final static String COLUMN_NUMBER_OF_PERIODS_ON_FRIDAY ="friday";
        public final static String COLUMN_NUMBER_OF_DAYS_PRESENT ="present";
        public final static String COLUMN_NUMBER_OF_DAYS_ABSENT ="absent";
        public final static String COLUMN_LAST_DATE ="date";
    }



}
