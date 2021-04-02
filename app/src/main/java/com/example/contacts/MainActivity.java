package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.contacts.data.ContactContract.*;

import com.example.contacts.data.ContactListCursorAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CONTACT_LOADER = 3;
    ContactListCursorAdapter contactListCursorAdapter;

    ListView contactList;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactList = findViewById(R.id.contactList);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , addContact.class);
                startActivity(intent);
            }
        });

        contactListCursorAdapter = new ContactListCursorAdapter(this, null, false);
        contactList.setAdapter(contactListCursorAdapter);

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this , addContact.class);
                Uri currentContactUri = ContentUris.withAppendedId(TableName.CONTENT_URI, id);
                intent.setData(currentContactUri);
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(CONTACT_LOADER, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String [] projection = {
                TableName.ID,
                TableName.FIRST_NAME,
                TableName.LAST_NAME,
                TableName.PHONE_NUMBER };
        CursorLoader cursorLoader = new CursorLoader(this, TableName.CONTENT_URI, projection,
                null, null, null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        contactListCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        contactListCursorAdapter.swapCursor(null);

    }
}