package com.vidya.contact_vidya_c0778642_android.Data;

import android.provider.BaseColumns;

public
class ContactContract {
    private ContactContract(){}

    public static final class ContactEntry implements BaseColumns {

        public final static String TABLE_NAME = "contacts";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_FirstNAME ="conName";
        public final static String COLUMN_LastNAME ="conName";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_STREET = "street";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_ZIP = "zip";

    }
}
