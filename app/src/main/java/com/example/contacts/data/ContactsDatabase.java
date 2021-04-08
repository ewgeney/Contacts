package com.example.contacts.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ContactContract.TableContacts.class}, version = ContactContract.DATABASE_VERSION)
public abstract class ContactsDatabase extends RoomDatabase {
    public abstract TableContactsDAO getTableContactsDAO ();
}
