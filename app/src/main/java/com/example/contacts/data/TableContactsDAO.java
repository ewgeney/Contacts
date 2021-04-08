package com.example.contacts.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TableContactsDAO {
    @Insert
    public long add_contact (ContactContract.TableContacts tableContacts);

    @Update
    public void update_contact (ContactContract.TableContacts tableContacts);

    @Delete
    public void delete_contact (ContactContract.TableContacts tableContacts);

    @Query("select * from contactList")
    public List<ContactContract.TableContacts> getAllContacts();

    @Query("select * from contactList where contact_id==:contactId")
    public ContactContract.TableContacts getContact (long contactId);
}
