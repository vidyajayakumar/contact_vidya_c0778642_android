//package com.vidya.contact_vidya_c0778642_android.Data;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.vidya.contact_vidya_c0778642_android.R;
//import com.vidya.contact_vidya_c0778642_android.Data.ContactContract.ContactEntry;
//
//import java.util.ArrayList;
//
//public
//class ContactAdpater extends ArrayAdapter<ContactData> {
//    public ContactAdpater(Context context, Cursor cursor) {
//        super(context,0);
//    }
//    @Override
//    public
//    View newView(Context context, Cursor cursor, ViewGroup parent) {
//        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
//    }
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        // Find fields to populate in inflated template
//        TextView tvBody = (TextView) view.findViewById(R.id.tv_fullname);
//        TextView tvPriority = (TextView) view.findViewById(R.id.tv_phone);
//        // Extract properties from cursor
//        String body = cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_FirstNAME ));
//        String priority = cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.COLUMN_PHONE));
//        // Populate fields with extracted properties
//        tvBody.setText(body);
//        tvPriority.setText(String.valueOf(priority));
//    }
//
//
//}
