package com.example.contacts.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public final class ContactContract {
    private ContactContract() {
    }
    public static final String DATABASE_NAME = "contactListDb";
    public static final int DATABASE_VERSION = 1;

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.contacts";
    public static final String PATH_MEMBERS = "contactList";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME+AUTHORITY);


    @Entity (tableName = "contactList" )
    public class TableContacts {

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo (name = "contact_id")
        private long ID;

        @ColumnInfo (name = "firstName")
        private String FIRST_NAME;

        @ColumnInfo (name = "lastName")
        private String LAST_NAME;

        @ColumnInfo (name = "phoneNumber")
        private String PHONE_NUMBER;

        public TableContacts (long ID, String FIRST_NAME, String LAST_NAME, String PHONE_NUMBER){
            this.ID=ID;
            this.FIRST_NAME=FIRST_NAME;
            this.LAST_NAME=LAST_NAME;
            this.PHONE_NUMBER=PHONE_NUMBER;
        }


        public final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMBERS);

        public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;
        public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;

    }
}
