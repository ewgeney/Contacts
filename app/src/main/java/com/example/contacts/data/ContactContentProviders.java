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

import com.example.contacts.data.ContactContract.*;

public class ContactContentProviders extends ContentProvider {

    ContactListDbOpenHelper dbOpenHelper;

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
        dbOpenHelper = new ContactListDbOpenHelper(getContext());
        return true;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case CONTACTS:
                cursor = db.query(TableName.TABLE_NAME, projection, selection,
                        selectionArgs,null, null, sortOrder);
                break;
            case CONTACT_ID:
                selection = TableName.ID + "=?";
                selectionArgs = new String []{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(TableName.TABLE_NAME, projection, selection, selectionArgs, null,
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

        String firstName = values.getAsString(TableName.FIRST_NAME);
        if (firstName == null){
            throw new IllegalArgumentException("You have to input first name");
        }
        String lastName = values.getAsString(TableName.LAST_NAME);
        if (lastName == null){
            throw new IllegalArgumentException("You have to input last name");
        }
        String phoneNumber = values.getAsString(TableName.PHONE_NUMBER);
        if (phoneNumber == null){
            throw new IllegalArgumentException("You have to input phone number");
        }
        

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        switch (match) {
            case CONTACTS:
               long id = db.insert(TableName.TABLE_NAME,
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
                rowsDeleted = db.delete(TableName.TABLE_NAME, selection, selectionArgs);
                break;
            case CONTACT_ID:
                selection = TableName.ID + "=?";
                selectionArgs = new String []{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete (TableName.TABLE_NAME, selection, selectionArgs);
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

        if(values.containsKey(TableName.FIRST_NAME)){
        String firstName = values.getAsString(TableName.FIRST_NAME);
        if (firstName == null){
            throw new IllegalArgumentException("You have to input first name");
        }}
        if(values.containsKey(TableName.LAST_NAME)){
        String lastName = values.getAsString(TableName.LAST_NAME);
        if (lastName == null){
            throw new IllegalArgumentException("You have to input last name");
        }}
        if(values.containsKey(TableName.PHONE_NUMBER)){
        String phoneNumber = values.getAsString(TableName.PHONE_NUMBER);
        if (phoneNumber == null){
            throw new IllegalArgumentException("You have to input phone number");
        }}

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case CONTACTS:
                rowsUpdated = db.update(TableName.TABLE_NAME, values, selection, selectionArgs);
                if (rowsUpdated != 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsUpdated;
            case CONTACT_ID:
                selection = TableName.ID + "=?";
                selectionArgs = new String []{String.valueOf(ContentUris.parseId(uri))};

                rowsUpdated = db.update(TableName.TABLE_NAME, values, selection, selectionArgs);
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
                return TableName.CONTENT_MULTIPLE_ITEMS;
            case CONTACT_ID:
                return TableName.CONTENT_SINGLE_ITEM;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
