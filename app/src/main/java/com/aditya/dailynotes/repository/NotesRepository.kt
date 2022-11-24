package com.aditya.dailynotes.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.aditya.dailynotes.activity.MainActivity
import com.aditya.dailynotes.database.*

class NotesRepository(
    private val notesDatabase: NotesDatabase) {

    fun getNotes() : LiveData<List<Notes>> {
        return notesDatabase.notesDao().getNotes()
    }
    fun getNote(ID: String): LiveData<Notes>{
        return notesDatabase.notesDao().getNote(ID)
    }

    suspend fun insertNote(note: Notes) {
        notesDatabase.notesDao().insertNote(note)
    }

    suspend fun deleteNote(note: Notes) {
        notesDatabase.notesDao().deleteNote(note)
    }

    suspend fun updateNote(notes: Notes) {
        notesDatabase.notesDao().updateNote(notes)
    }

}