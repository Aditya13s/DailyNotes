package com.aditya.dailynotes.viewModel

import androidx.lifecycle.*
import com.aditya.dailynotes.database.Notes
import com.aditya.dailynotes.repository.NotesRepository
import kotlinx.coroutines.*

class MainViewModel(private val repository: NotesRepository) : ViewModel() {

    fun getNotes() : LiveData<List<Notes>> {
        return repository.getNotes()
    }

    fun getNote(ID : String) : LiveData<Notes> {
        return repository.getNote(ID)
    }

     fun insertNote(note: Notes) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNote(note)
        }
    }

     fun deleteNote(note: Notes) {
         viewModelScope.launch(Dispatchers.IO) {
             repository.deleteNote(note)
         }
     }

     fun updateNote(notes: Notes) {
         viewModelScope.launch(Dispatchers.IO) {
             repository.updateNote(notes)
         }
     }


}