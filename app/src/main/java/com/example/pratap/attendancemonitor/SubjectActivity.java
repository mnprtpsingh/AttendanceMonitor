package com.example.pratap.attendancemonitor;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.pratap.attendancemonitor.data.SubjectContract.SubjectEntry;

public class SubjectActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the subject data loader */
    private static final int EXISTING_SUBJECT_LOADER = 0;

    /** Content URI for the existing subject (null if it's a new subject) */
    private Uri mCurrentSubjectUri;

    /** EditText field to enter the subject's name */
    private EditText mNameEditText;

    private EditText mMondayEditText;
    private EditText mTuesdayEditText;
    private EditText mWednesdayEditText;
    private EditText mThursdayEditText;
    private EditText mFridayEditText;

    /** Boolean flag that keeps track of whether the subject has been edited (true) or not (false) */
    private boolean mSubjectHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the msubjectHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mSubjectHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new pet or editing an existing one.
        Intent intent = getIntent();
        mCurrentSubjectUri = intent.getData();

        // If the intent DOES NOT contain a subject content URI, then we know that we are
        // creating a new subject.
        if (mCurrentSubjectUri == null) {
            // This is a new subject, so change the app bar to say "Add a Subject"
            setTitle(getString(R.string.subject_activity_title_new_subject));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a subject that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing subject, so change app bar to say "Edit Subject"
            setTitle(getString(R.string.subject_activity_title_edit_subject));

            // Initialize a loader to read the subject data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_SUBJECT_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.tv_name);
        mMondayEditText = (EditText) findViewById(R.id.tv_monday);
        mTuesdayEditText = (EditText) findViewById(R.id.tv_tuesday);
        mWednesdayEditText = (EditText) findViewById(R.id.tv_wednesday);
        mThursdayEditText = (EditText) findViewById(R.id.tv_thursday);
        mFridayEditText = (EditText) findViewById(R.id.tv_friday);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mMondayEditText.setOnTouchListener(mTouchListener);
        mTuesdayEditText.setOnTouchListener(mTouchListener);
        mWednesdayEditText.setOnTouchListener(mTouchListener);
        mThursdayEditText.setOnTouchListener(mTouchListener);
        mFridayEditText.setOnTouchListener(mTouchListener);
    }

    /**
     * Get user input from editor and save pet into database.
     */
    private void saveSubject() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String name = mNameEditText.getText().toString().trim();
        int monday = Integer.parseInt(mMondayEditText.getText().toString().trim());
        int tuesday = Integer.parseInt(mTuesdayEditText.getText().toString().trim());
        int wednesday = Integer.parseInt(mWednesdayEditText.getText().toString().trim());
        int thursday = Integer.parseInt(mThursdayEditText.getText().toString().trim());
        int friday = Integer.parseInt(mFridayEditText.getText().toString().trim());

        // Check if this is supposed to be a new subject
        // and check if all the fields in the editor are blank
        if (mCurrentSubjectUri == null &&
                TextUtils.isEmpty(name)) {
            // Since no fields were modified, we can return early without creating a new subject.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and subject attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(SubjectEntry.COLUMN_SUBJECT_NAME, name);
        values.put(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_MONDAY, monday);
        values.put(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_TUESDAY, tuesday);
        values.put(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_WEDNESDAY, wednesday);
        values.put(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_THURSDAY, thursday);
        values.put(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_FRIDAY, friday);

        // Determine if this is a new or existing pet by checking if mCurrentPetUri is null or not
        if (mCurrentSubjectUri == null) {
            // This is a NEW pet, so insert a new subject into the provider,
            // returning the content URI for the new subject.
            Uri newUri = getContentResolver().insert(SubjectEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.insert_subject_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.insert_subject_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING subject, so update the subject with content URI: mCurrentSubjectUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentSubjectUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentSubjectUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.update_subject_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.update_subject_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_subject.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_subject, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                saveSubject();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mSubjectHasChanged) {
                    NavUtils.navigateUpFromSameTask(SubjectActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(SubjectActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the subject hasn't changed, continue with handling back button press
        if (!mSubjectHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the subject table
        String[] projection = {
                SubjectEntry._ID,
                SubjectEntry.COLUMN_SUBJECT_NAME,
                SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_MONDAY,
                SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_TUESDAY,
                SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_WEDNESDAY,
                SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_THURSDAY,
                SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_FRIDAY };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentSubjectUri,         // Query the content URI for the current subject
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of subject attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_SUBJECT_NAME);
            int mondayColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_MONDAY);
            int tuesdayColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_TUESDAY);
            int wednesdayColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_WEDNESDAY);
            int thursdayColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_THURSDAY);
            int fridayColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_NUMBER_OF_PERIODS_ON_FRIDAY);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int monday = cursor.getInt(mondayColumnIndex);
            int tuesday = cursor.getInt(tuesdayColumnIndex);
            int wednesday = cursor.getInt(wednesdayColumnIndex);
            int thursday = cursor.getInt(thursdayColumnIndex);
            int friday = cursor.getInt(fridayColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mMondayEditText.setText(Integer.toString(monday));
            mTuesdayEditText.setText(Integer.toString(tuesday));
            mWednesdayEditText.setText(Integer.toString(wednesday));
            mThursdayEditText.setText(Integer.toString(thursday));
            mFridayEditText.setText(Integer.toString(friday));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mMondayEditText.setText("");
        mTuesdayEditText.setText("");
        mWednesdayEditText.setText("");
        mThursdayEditText.setText("");
        mFridayEditText.setText("");
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the subject.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this subject.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the subject.
                deleteSubject();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the subject.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteSubject() {
        // Only perform the delete if this is an existing subject.
        if (mCurrentSubjectUri != null) {
            // Call the ContentResolver to delete the subject at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentSubjectUri
            // content URI already identifies the subject that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentSubjectUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.delete_subject_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.delete_subject_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}
