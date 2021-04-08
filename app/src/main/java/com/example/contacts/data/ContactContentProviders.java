package com.example.contacts.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.contacts.MainActivity;
import com.example.contacts.data.ContactContract.*;

public class ContactContentProviders extends ContentProvider {

    private ContactsDatabase contactsDatabase;
    public static final int CONTACTS = 1;
    public static final int CONTACT_ID = 2;

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        uriMatcher.addURI(ContactContract.AUTHORITY,
                ContactContract.PATH_MEMBERS, CONTACTS);
        uriMatcher.addURI(ContactContract.AUTHORITY,
                ContactContract.PATH_MEMBERS + "/#", CONTACT_ID);
    }

    @Override
    public boolean onCreate() {

        contactsDatabase = Room.databaseBuilder(MainActivity.MainContext, ContactsDatabase.class, ContactContract.DATABASE_NAME) // костыль разобраться
                .allowMainThreadQueries().build();
        return true;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        contactsDatabase.getTableContactsDAO().getAllContacts();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case CONTACTS:
                cursor = contactsDatabase.query(TableContacts.TABLE_NAME, projection, selection,
                        selectionArgs,null, null, sortOrder);
                break;
            case CONTACT_ID:
                selection = TableContacts.ID + "=?";
                selectionArgs = new String []{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(TableContacts.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Incorrect URI: " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);  // Когда данные по URI изменяются нужно обновить cursor
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        String firstName = values.getAsString(TableContacts.FIRST_NAME);
        if (firstName == null){
            throw new IllegalArgumentException("You have to input first name");
        }
        String lastName = values.getAsString(TableContacts.LAST_NAME);
        if (lastName == null){
            throw new IllegalArgumentException("You have to input last name");
        }
        String phoneNumber = values.getAsString(TableContacts.PHONE_NUMBER);
        if (phoneNumber == null){
            throw new IllegalArgumentException("You have to input phone number");
        }
        

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        switch (match) {
            case CONTACTS:
               long id = db.insert(TableContacts.TABLE_NAME,
                       null, values);
               if (id==-1) {
                   Log.e("insertMethod",
                           "Insertion of data in the table failed for "
                                   + uri);
                   return null;
               }

               getContext().getContentResolver().notifyChange(uri, null);
               return ContentUris.withAppendedId(uri, id);


            default:
                throw new IllegalArgumentException("Can't insert URI: " + uri);

        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case CONTACTS:
                rowsDeleted = db.delete(TableContacts.TABLE_NAME, selection, selectionArgs);
                break;
            case CONTACT_ID:
                selection = TableContacts.ID + "=?";
                selectionArgs = new String []{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete (TableContacts.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Can't delete URI: " + uri);

        }

        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if(values.containsKey(TableContacts.FIRST_NAME)){
        String firstName = values.getAsString(TableContacts.FIRST_NAME);
        if (firstName == null){
            throw new IllegalArgumentException("You have to input first name");
        }}
        if(values.containsKey(TableContacts.LAST_NAME)){
        String lastName = values.getAsString(TableContacts.LAST_NAME);
        if (lastName == null){
            throw new IllegalArgumentException("You have to input last name");
        }}
        if(values.containsKey(TableContacts.PHONE_NUMBER)){
        String phoneNumber = values.getAsString(TableContacts.PHONE_NUMBER);
        if (phoneNumber == null){
            throw new IllegalArgumentException("You have to input phone number");
        }}

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case CONTACTS:
                rowsUpdated = db.update(TableContacts.TABLE_NAME, values, selection, selectionArgs);
                if (rowsUpdated != 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsUpdated;
            case CONTACT_ID:
                selection = TableContacts.ID + "=?";
                selectionArgs = new String []{String.valueOf(ContentUris.parseId(uri))};

                rowsUpdated = db.update(TableContacts.TABLE_NAME, values, selection, selectionArgs);
                if (rowsUpdated != 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsUpdated;

                default:
                throw new IllegalArgumentException("Can't update URI: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case CONTACTS:
                return TableContacts.CONTENT_MULTIPLE_ITEMS;
            case CONTACT_ID:
                return TableContacts.CONTENT_SINGLE_ITEM;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
