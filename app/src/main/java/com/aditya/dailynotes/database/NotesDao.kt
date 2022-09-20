package com.aditya.dailynotes.database

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes")
    fun getNotes() : LiveData<List<Notes>>

    @Insert
    suspend fun insertNote(note: Notes)

    @Update
    suspend fun updateNote(notes: Notes)

    @Delete
    suspend fun deleteNote(note: Notes)
}