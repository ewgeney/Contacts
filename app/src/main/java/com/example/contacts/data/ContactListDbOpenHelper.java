package com.example.contacts.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class ContactListDbOpenHelper extends SQLiteOpenHelper {
    public ContactListDbOpenHelper(Context context) {
        super(context, ContactContract.DATABASE_NAME, null, ContactContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE "+ContactContract.TableName.TABLE_NAME+ "("
                +ContactContract.TableName.ID + " INTEGER PRIMARY KEY,"
                +ContactContract.TableName.FIRST_NAME + " TEXT,"
                +ContactContract.TableName.LAST_NAME + " TEXT,"
                +ContactContract.TableName.PHONE_NUMBER + " INTEGER NOT NULL" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ ContactContract.DATABASE_NAME);
        onCreate(db);

    }
}
