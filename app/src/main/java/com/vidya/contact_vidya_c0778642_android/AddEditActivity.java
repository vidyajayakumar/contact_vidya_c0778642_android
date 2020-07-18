package com.vidya.contact_vidya_c0778642_android;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.vidya.contact_vidya_c0778642_android.Data.ContactContract.ContactEntry;
import com.vidya.contact_vidya_c0778642_android.Data.ContactData;

public
class AddEditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_PET_LOADER = 0;

    ContactData cd;
    private Uri mCurrentPetUri;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mPhoneEditText;
    private EditText mEmailEditText;
    private EditText mStreetEditText;
    private EditText mCityEditText;
    private EditText mStateEditText;
    private EditText mZipEditText;

    private boolean mPetHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public
        boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;
            return false;
        }
    };

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        Intent intent = getIntent();
        mCurrentPetUri = intent.getData();

        cd                 = new ContactData();
        mFirstNameEditText = findViewById(R.id.et_FN);
        mLastNameEditText  = findViewById(R.id.et_LN);
        mPhoneEditText     = findViewById(R.id.et_phone);
        mEmailEditText     = findViewById(R.id.et_email);
        mStreetEditText    = findViewById(R.id.et_street);
        mCityEditText      = findViewById(R.id.et_city);
        mStateEditText     = findViewById(R.id.et_state);
        mZipEditText       = findViewById(R.id.et_zip);

        mFirstNameEditText.setOnTouchListener(mTouchListener);
        mLastNameEditText.setOnTouchListener(mTouchListener);
        mPhoneEditText.setOnTouchListener(mTouchListener);
        mEmailEditText.setOnTouchListener(mTouchListener);
        mStreetEditText.setOnTouchListener(mTouchListener);
        mCityEditText.setOnTouchListener(mTouchListener);
        mStateEditText.setOnTouchListener(mTouchListener);
        mZipEditText.setOnTouchListener(mTouchListener);

        if (mCurrentPetUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_contact));
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_contact));

            invalidateOptionsMenu();

            LoaderManager.getInstance(this).initLoader(EXISTING_PET_LOADER, null, this);
        }

    }

    /**
     * Get user input from editor and save new pet into database.
     */
    private
    void saveContact() {
        String nameString  = mFirstNameEditText.getText().toString().trim();
        String phoneString = mPhoneEditText.getText().toString().trim();
        if (mCurrentPetUri == null && TextUtils.isEmpty(nameString)
                && TextUtils.isEmpty(phoneString)) {
            return;
        }


        cd.contactData(mFirstNameEditText.getText().toString().trim(),
                       mLastNameEditText.getText().toString().trim(),
                       mPhoneEditText.getText().toString().trim(),
                       mEmailEditText.getText().toString().trim(),
                       mStreetEditText.getText().toString().trim(),
                       mCityEditText.getText().toString().trim(),
                       mStateEditText.getText().toString().trim(),
                       mZipEditText.getText().toString().trim());


        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_FirstNAME, cd.getConFirstName());
        values.put(ContactEntry.COLUMN_LastNAME, cd.getConLastName());
        values.put(ContactEntry.COLUMN_PHONE, cd.getConPhone());
        values.put(ContactEntry.COLUMN_EMAIL, cd.getConEmail());
        values.put(ContactEntry.COLUMN_STREET, cd.getConStreet());
        values.put(ContactEntry.COLUMN_CITY, cd.getConCity());
        values.put(ContactEntry.COLUMN_STATE, cd.getConState());
        values.put(ContactEntry.COLUMN_ZIP, cd.getConZip());

        if (mCurrentPetUri == null) {
            /*This is a NEW pet, so insert a new pet into the provider,
             returning the content URI for the new pet. */
            Uri newUri = getContentResolver().insert(ContactEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                               Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                               Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentPetUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_pet_failed),
                               Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_pet_successful),
                               Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public
    boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public
    boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentPetUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public
    boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                saveContact();
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
                // Navigate back to parent activity (CatalogActivity)
//                NavUtils.navigateUpFromSameTask(this);
//                finish();
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mPetHasChanged) {
                    NavUtils.navigateUpFromSameTask(AddEditActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public
                            void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(AddEditActivity.this);
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
    public
    void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mPetHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public
                    void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private
    void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("R.string.unsaved_changes_dialog_msg");
        builder.setPositiveButton("R.string.discard", discardButtonClickListener);
        builder.setNegativeButton("R.string.keep_editing", new DialogInterface.OnClickListener() {
            public
            void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private
    void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public
            void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
//                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public
            void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @NonNull
    @Override
    public
    Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                ContactEntry._ID,
                ContactEntry.COLUMN_FirstNAME,
                ContactEntry.COLUMN_LastNAME,
                ContactEntry.COLUMN_PHONE,
                ContactEntry.COLUMN_EMAIL,
                ContactEntry.COLUMN_STREET,
                ContactEntry.COLUMN_CITY,
                ContactEntry.COLUMN_STATE,
                ContactEntry.COLUMN_ZIP
        };


        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                                mCurrentPetUri,         // Query the content URI for the current pet
                                projection,             // Columns to include in the resulting Cursor
                                null,                   // No selection clause
                                null,                   // No selection arguments
                                null);                  // Default sort order
    }


    @Override
    public
    void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int fnameColumnIndex  = cursor.getColumnIndex(ContactEntry.COLUMN_FirstNAME);
            int lnameColumnIndex  = cursor.getColumnIndex(ContactEntry.COLUMN_LastNAME);
            int phoneColumnIndex  = cursor.getColumnIndex(ContactEntry.COLUMN_PHONE);
            int emailColumnIndex  = cursor.getColumnIndex(ContactEntry.COLUMN_EMAIL);
            int streetColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_STREET);
            int cityColumnIndex   = cursor.getColumnIndex(ContactEntry.COLUMN_CITY);
            int stateColumnIndex  = cursor.getColumnIndex(ContactEntry.COLUMN_STATE);
            int zipColumnIndex    = cursor.getColumnIndex(ContactEntry.COLUMN_ZIP);

            // Extract out the value from the Cursor for the given column index

            String fname  = cursor.getString(fnameColumnIndex);
            String lname  = cursor.getString(lnameColumnIndex);
            String phone  = cursor.getString(phoneColumnIndex);
            String email  = cursor.getString(emailColumnIndex);
            String street = cursor.getString(streetColumnIndex);
            String city   = cursor.getString(cityColumnIndex);
            String state  = cursor.getString(stateColumnIndex);
            String zip    = cursor.getString(zipColumnIndex);


            // Update the views on the screen with the values from the database

            mFirstNameEditText.setText(fname);
            mLastNameEditText.setText(lname);
            mPhoneEditText.setText(phone);
            mEmailEditText.setText(email);
            mStreetEditText.setText(street);
            mCityEditText.setText(city);
            mStateEditText.setText(state);
            mZipEditText.setText(zip);
            // Gender is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 is Unknown, 1 is Male, 2 is Female).
            // Then call setSelection() so that option is displayed on screen as the current selection.

        }
    }

    @Override
    public
    void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mFirstNameEditText.setText("");
        mLastNameEditText.setText("");
        mPhoneEditText.setText("");
        mEmailEditText.setText("");
        mStateEditText.setText("");
        mCityEditText.setText("");
        mZipEditText.setText("");
        mStreetEditText.setText("");

    }


}