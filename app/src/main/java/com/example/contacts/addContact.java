package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contacts.data.ContactContract.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class addContact extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneNumberEditText;
    private static final int EDIT_CONTACT_LOADER=4;
    Uri currentContactUri;
    FloatingActionButton callContactFloatingActionButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        callContactFloatingActionButton = findViewById(R.id.CallContactFloatingActionButton);
        callContactFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                if(!TextUtils.isEmpty(phoneNumber)) {
                    String dial = "tel:" + phoneNumber;
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                }else {
                    Toast.makeText(addContact.this, "Enter a phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = getIntent();
        currentContactUri = intent.getData();

        if(currentContactUri==null){
            setTitle("Add contact");
            invalidateOptionsMenu();
        }
        else {
            setTitle("Edit contact");
            getSupportLoaderManager().initLoader(EDIT_CONTACT_LOADER, null, this);
        }

        firstNameEditText = findViewById(R.id.editTextFirstName);
        lastNameEditText = findViewById(R.id.editTextLastName);
        phoneNumberEditText = findViewById(R.id.editTextPhone);

    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(currentContactUri==null){
            MenuItem menuItem = menu.findItem(R.id.deleteContact);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.saveContact:
                saveContact();
                return true;
            case R.id.deleteContact:
                showDeleteDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void saveContact() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString();

        if (TextUtils.isEmpty(firstName)){
            Toast.makeText(this, "Input first name",
                    Toast.LENGTH_LONG).show();
            return;
        }
        else if (TextUtils.isEmpty(lastName)){
            Toast.makeText(this, "Input last name",
                    Toast.LENGTH_LONG).show();
            return;
        }
        else if (TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this, "Input phone number",
                    Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(TableName.FIRST_NAME, firstName);
        contentValues.put(TableName.LAST_NAME, lastName);
        contentValues.put(TableName.PHONE_NUMBER, phoneNumber);

        if (currentContactUri == null) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(TableName.CONTENT_URI, contentValues);

            if (uri == null) {
                Toast.makeText(this, "insertion data fault",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Contact saved", Toast.LENGTH_LONG).show();
            }
        }
        else {
           int rowsChanged = getContentResolver().update(currentContactUri, contentValues, null,null);
           if (rowsChanged==0){
               Toast.makeText(this, "Saving data fault",
                       Toast.LENGTH_LONG).show();
           }
           else {
               Toast.makeText(this, "Contact updated", Toast.LENGTH_LONG).show();
           }
        }
        Intent intent = new Intent(addContact.this , MainActivity.class);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String [] projection = {
                TableName.ID,
                TableName.FIRST_NAME,
                TableName.LAST_NAME,
                TableName.PHONE_NUMBER };
        CursorLoader cursorLoader = new CursorLoader(this, currentContactUri, projection,
                null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()){
            int firstNameColumnIndex = data.getColumnIndex(TableName.FIRST_NAME);
            int lastNameColumnIndex = data.getColumnIndex(TableName.LAST_NAME);
            int phoneNumberColumnIndex = data.getColumnIndex(TableName.PHONE_NUMBER); //получаем индекс строки

            String firstName = data.getString(firstNameColumnIndex);
            String lastName = data.getString(lastNameColumnIndex);
            String phoneNumber = data.getString(phoneNumberColumnIndex); //получаем текст по индексу строки

            firstNameEditText.setText(firstName);
            lastNameEditText.setText(lastName);
            phoneNumberEditText.setText(phoneNumber); //передаем текст в поле textView
        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
    private void showDeleteDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Do you want to delete contact?");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteContact();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void deleteContact() {
        if (currentContactUri != null){
            int rowsDeleted = getContentResolver().delete(currentContactUri, null,null);
            if(rowsDeleted==0){
                Toast.makeText(this, "Deleted Failed", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Contact deleted", Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }
}
