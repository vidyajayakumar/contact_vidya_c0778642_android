package com.vidya.contact_vidya_c0778642_android;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import com.vidya.contact_vidya_c0778642_android.Data.ContactContract.ContactEntry;

public
class ContactListActivity extends AppCompatActivity {

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
    }

    private
    void displayDatabaseInfo() {
        // Create and/or open a database to read from it

        SQLiteDatabase db = db.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ContactEntry._ID,
                ContactEntry.COLUMN_FirstNAME,
                ContactEntry.COLUMN_LastNAME,
                ContactEntry.COLUMN_PHONE};

        // Perform a query on the Notes table
        Cursor cursor = db.query(
                ContactEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        TextView displayView = (TextView) findViewById(R.id.tv);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The Notes table contains <number of rows in Cursor> Notes.
            // _id - name - Desc - Date - weight
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.append(ContactEntry._ID + " - " +
                                       ContactEntry.COLUMN_FirstNAME + " - " +
                                       ContactEntry.COLUMN_LastNAME + " - " +
                                       ContactEntry.COLUMN_PHONE + "\n");

            // Figure out the index of each column
            int idColumnIndex     = cursor.getColumnIndex(ContactEntry._ID);
            int nameColumnIndex   = cursor.getColumnIndex(ContactEntry.COLUMN_FirstNAME);
            int DescColumnIndex  = cursor.getColumnIndex(ContactEntry.COLUMN_LastNAME);
            int DateColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_PHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int    currentID     = cursor.getInt(idColumnIndex);
                String currentName   = cursor.getString(nameColumnIndex);
                String currentDesc  = cursor.getString(DescColumnIndex);
                String  currentDate = cursor.getString(DateColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentDesc + " - " +
                        currentDate ));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }
}