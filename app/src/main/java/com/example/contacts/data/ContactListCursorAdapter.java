package com.example.contacts.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.contacts.R;

public class ContactListCursorAdapter extends CursorAdapter {
    public ContactListCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView firstNameTextView = view.findViewById(R.id.firstNameTextView);
        TextView lastNameTextView = view.findViewById(R.id.lastNameTextView);
        TextView phoneNumberTextView = view.findViewById(R.id.phoneNumberTextView);

        String firstName = cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.TableName.FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.TableName.LAST_NAME));
        String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.TableName.PHONE_NUMBER));

        firstNameTextView.setText(firstName);
        lastNameTextView.setText(lastName);
        phoneNumberTextView.setText(phoneNumber);
    }
}
