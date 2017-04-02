package com.example.pratap.attendancemonitor;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;

import com.example.pratap.attendancemonitor.data.SubjectContract.SubjectEntry;

/**
 * Created by pratap on 2/4/17.
 */

public class AttendanceCursorAdapter extends CursorAdapter {

    private Context mContext;

    public AttendanceCursorAdapter(Context context, Cursor c) {

        super(context, c, 0 /* flags */);
        mContext = context;
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item_attendance, parent, false);
    }

    /**
     * This method binds the subject data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current subject can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        Button presentButton = (Button) view.findViewById(R.id.button_present);
        Button absentButton = (Button) view.findViewById(R.id.button_absent);
        Button holidayButton = (Button) view.findViewById(R.id.button_holiday);


        // Find the columns of subject attributes that we're interested in
        final int idColumnIndex = cursor.getColumnIndex(SubjectEntry._ID);
        int presentColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_NUMBER_OF_DAYS_PRESENT);
        int absentColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_NUMBER_OF_DAYS_ABSENT);

        // Read the subject attributes from the Cursor for the current subject
        final long id = cursor.getLong(idColumnIndex);
        int present = cursor.getInt(presentColumnIndex);
        int absent = cursor.getInt(absentColumnIndex);

        // Update the TextViews with the attributes for the current subject
        String percentAttendence = " % Attendance";
    }
}
