package com.example.pratap.attendancemonitor;

/**
 * Created by pratap on 1/4/17.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.pratap.attendancemonitor.data.SubjectContract.SubjectEntry;

/**
 * {@link MonitorCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of subject data as its data source. This adapter knows
 * how to create list items for each row of subject data in the {@link Cursor}.
 */
public class MonitorCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link MonitorCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public MonitorCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
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
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.tv_name_lable);
        TextView presentTextView = (TextView) view.findViewById(R.id.tv_present);
        TextView absentTextView = (TextView) view.findViewById(R.id.tv_absent);
        TextView percentTextView = (TextView) view.findViewById(R.id.tv_percent_attendance);

        // Find the columns of subject attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_SUBJECT_NAME);
        int presentColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_NUMBER_OF_DAYS_PRESENT);
        int absentColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_NUMBER_OF_DAYS_ABSENT);

        // Read the subject attributes from the Cursor for the current subject
        String Name = cursor.getString(nameColumnIndex);
        int present = cursor.getInt(presentColumnIndex);
        int absent = cursor.getInt(absentColumnIndex);

        // Update the TextViews with the attributes for the current subject
        nameTextView.setText(Name);
        presentTextView.setText(String.valueOf(present));
        absentTextView.setText(String.valueOf(absent));
        String percentAttendence = " % Attendance";
        percentTextView.setText(percentAttendence);
    }
}
