package com.vidya.contact_vidya_c0778642_android;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vidya.contact_vidya_c0778642_android.Data.ConCursorAdapter;
import com.vidya.contact_vidya_c0778642_android.Data.ContactContract.ContactEntry;
import com.vidya.contact_vidya_c0778642_android.Data.ContactDatabaseHelper;

public
class ContactListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PET_LOADER = 0;
    ConCursorAdapter mCursorAdapter;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(ContactListActivity.this, AddEditActivity.class);
                startActivity(intent);
            }
        });

        ListView conListView = findViewById(R.id.list);

        mCursorAdapter = new ConCursorAdapter(this, null);
        conListView.setAdapter(mCursorAdapter);

        //Setup item Click listener
        conListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public
            void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Create new Intent to go to  {@link EditorActivity}
                Intent intent = new Intent(ContactListActivity.this, AddEditActivity.class);

                //From the content URI that represents the specific pet that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                //{@link PetEntry#CONTENT_URI}.
                //For example, the URI would be "content://com.ibsanju.pets/pets/2"
                //if the pet with ID 2 was clicked on.
                Uri currentPetUri = ContentUris.withAppendedId(ContactEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentPetUri);

                //Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });
        //Kick off the loader
        LoaderManager.getInstance(this).initLoader(PET_LOADER, null, this);
    }

    private
    void displayDatabaseInfo() {

        String[] projection = {
                ContactEntry._ID,
                ContactEntry.COLUMN_FirstNAME,
                ContactEntry.COLUMN_LastNAME,
                ContactEntry.COLUMN_PHONE};

        // Perform a query on the Notes table
        Cursor cursor = getContentResolver().query(
                ContactEntry.CONTENT_URI,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null);                   // The sort order


        try {
//            displayView.setText(ContactEntry._ID + " - " +
//                                        ContactEntry.COLUMN_FirstNAME + " - " +
//                                        ContactEntry.COLUMN_LastNAME + " - " +
//                                        ContactEntry.COLUMN_PHONE + "\n");

            // Figure out the index of each column
            int idColumnIndex   = cursor.getColumnIndex(ContactEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_FirstNAME);
            int DescColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_LastNAME);
            int DateColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_PHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word at the current row the cursor is on.
                int    currentID   = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentDesc = cursor.getString(DescColumnIndex);
                String currentDate = cursor.getString(DateColumnIndex);

            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
        // Find the ListView which will be populated with the pet data
        ListView petListView = (ListView) findViewById(R.id.list);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        ConCursorAdapter adapter = new ConCursorAdapter(this, cursor);

        // Attach the adapter to the ListView.
        petListView.setAdapter(adapter);
    }

    private
    void insertContact() {
        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_FirstNAME, "Bharath");
        values.put(ContactEntry.COLUMN_LastNAME, "Kumar");
        values.put(ContactEntry.COLUMN_PHONE, "+919035202073");
        values.put(ContactEntry.COLUMN_EMAIL, "7");

        Uri newUri = getContentResolver().insert(ContactEntry.CONTENT_URI, values);
    }

    @Override
    protected
    void onStart() {
        super.onStart();
//        displayDatabaseInfo();
    }


    public
    void insert(View view) {
        insertContact();
//        displayDatabaseInfo();
    }

    private
    void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(ContactEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
        Toast.makeText(getApplicationContext(), rowsDeleted + " rows deleted from pet database",
                       Toast.LENGTH_SHORT).show();
    }

    @Override
    public
    boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public
    boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertContact();
//                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public
    Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                ContactEntry._ID,
                ContactEntry.COLUMN_FirstNAME,
                ContactEntry.COLUMN_PHONE};

        return new CursorLoader(this,   // Parent activity Context
                                ContactEntry.CONTENT_URI,           // The content URI of the words table
                                projection,                     // The columns to return for each row
                                null,                   // Either null, or the word the user entered
                                null,               // Either empty, or the string the user entered
                                null);                  // The sort order for the returned rows
    }


    @Override
    public
    void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // Update {@link petCursorAdapter with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public
    void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);

    }

}