package com.example.contacts.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ContactContract {
    private ContactContract() {
    }
    public static final String DATABASE_NAME = "contactListDb";
    public static final int DATABASE_VERSION = 1;

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.contacts";
    public static final String PATH_MEMBERS = "contactList";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME+AUTHORITY);


    public static final class TableName implements BaseColumns {
        public static final String TABLE_NAME = "contactList";

        public static final String ID = BaseColumns._ID;
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String PHONE_NUMBER = "phoneNumber";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMBERS);

        public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;
        public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;

    }
}
