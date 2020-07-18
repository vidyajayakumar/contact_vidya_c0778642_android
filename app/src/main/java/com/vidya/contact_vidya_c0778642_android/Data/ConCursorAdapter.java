package com.vidya.contact_vidya_c0778642_android.Data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.vidya.contact_vidya_c0778642_android.R;
import com.vidya.contact_vidya_c0778642_android.Data.ContactContract.ContactEntry;


public
class ConCursorAdapter extends CursorAdapter {

    public
    ConCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public
    View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public
    void bindView(View view, Context context, Cursor cursor) {
        TextView         nameTextView    = view.findViewById(R.id.tv_fullname);
        TextView phoneTextView = view.findViewById(R.id.tv_phone);

        int nameColumnIndex  = cursor.getColumnIndex(ContactEntry.COLUMN_FirstNAME);
        int phoneColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_PHONE);

        String conName  = cursor.getString(nameColumnIndex);
        String conPhone = cursor.getString(phoneColumnIndex);

        nameTextView.setText(conName);
        phoneTextView.setText(conPhone);
    }
}
