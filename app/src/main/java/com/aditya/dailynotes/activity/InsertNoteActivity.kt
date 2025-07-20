package com.aditya.dailynotes.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.lifecycle.ViewModelProvider
import com.aditya.dailynotes.R
import com.aditya.dailynotes.database.Notes
import com.aditya.dailynotes.database.NotesDatabase
import com.aditya.dailynotes.databinding.ActivityInsertNoteBinding
import com.aditya.dailynotes.repository.NotesRepository
import com.aditya.dailynotes.viewModel.MainViewModel
import com.aditya.dailynotes.viewModel.MainViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InsertNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertNoteBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var state: String
    private var position: Int = 0

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Setup navigation
        binding.toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        state = intent.getStringExtra("state").toString()
        position = intent.getIntExtra("id", 0)

        val notesDatabase = NotesDatabase.getDatabase(applicationContext)
        val repository = NotesRepository(notesDatabase)

        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(repository))[MainViewModel::class.java]

        if(state == "update") {
            binding.toolbar.title = getString(R.string.edit_note)
            val title = intent.getStringArrayListExtra("notes")?.get(1).toString()
            val note = intent.getStringArrayListExtra("notes")?.get(2).toString()

            binding.insertActivityNote.setText(if (note != "") note else "")
            binding.insertActivityTitle.setText(if (title != "") title else "")
        }

        binding.insertActivityTitle.doOnLayout {
            binding.insertActivityTitle.requestFocus()
            binding.insertActivityTitle.setSelection(binding.insertActivityTitle.text?.length ?: 0)

            Handler(Looper.getMainLooper()).postDelayed({
                val imm = binding.insertActivityTitle.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.insertActivityTitle, InputMethodManager.SHOW_IMPLICIT)
            }, 200)

        }

        binding.insertActivityNote.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val editText = v as EditText
                editText.setSelection(editText.text.length) // Move cursor to end
            }
        }

        binding.insertActivitySaveButton.setOnClickListener {
            if(state == "update") {
                //This give us a note data class which we have to update
                val note = updateNote()
                mainViewModel.updateNote(note)
                Toast.makeText(this, getString(R.string.note_updated), Toast.LENGTH_SHORT).show()
                finish()

            } else {
                //This give us a note data class which we have to create
                val addNote = createNote()
                if(addNote.title == "" && addNote.description == "") {//This check that the title and description of note is empty or not
                    finish()
                } else {
                    mainViewModel.insertNote(addNote)
                    Toast.makeText(this@InsertNoteActivity, getString(R.string.note_saved), Toast.LENGTH_SHORT).show()
                    finish()
                }

            }
        }
    }

    private fun updateNote(): Notes {
        val id : Int = Integer.parseInt(intent.getStringArrayListExtra("notes")?.get(0).toString())
        val title = binding.insertActivityTitle.text.toString()
        val note = binding.insertActivityNote.text.toString()
        val date = intent.getStringArrayListExtra("notes")?.get(3).toString()

        return Notes(id,title,note,date)

    }

    private fun createNote(): Notes {
        val title = binding.insertActivityTitle.text.toString()
        val note = binding.insertActivityNote.text.toString()
        val local= LocalDateTime.now()
        val dateTime = local.format(DateTimeFormatter.ofPattern("MMM dd, yyyy \nhh:mm a"))

        return Notes(0, title, note, dateTime)

    }
}