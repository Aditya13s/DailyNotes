package com.aditya.dailynotes.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aditya.dailynotes.R
import com.aditya.dailynotes.database.Notes
import com.aditya.dailynotes.database.NotesDatabase
import com.aditya.dailynotes.databinding.ActivityViewNoteBinding
import com.aditya.dailynotes.repository.NotesRepository
import com.aditya.dailynotes.viewModel.*

class ViewNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewNoteBinding
    private lateinit var mainViewModel: MainViewModel

    var canLoad = true

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Setup navigation
        binding.toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val ID: Int = intent.getIntExtra("id", 0)

        val repository = NotesRepository(NotesDatabase.getDatabase(applicationContext))
        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository)
        )[MainViewModel::class.java]

        var id = "";
        var title: String = ""
        var description: String = ""
        mainViewModel.getNote(ID.toString()).observe(this, Observer {

            if (canLoad) {
                binding.noteTextView.text = it.description
                binding.titleTextView.text = it.title
                binding.dateTextView.text = it.date
                id = it.id.toString()
                title = it.title
                description = it.description
            }

        })

        binding.deleteNote.setOnClickListener {
            canLoad = false
            var note: Notes =
                deleteNote(id, title, description, binding.dateTextView.text.toString());
            mainViewModel.deleteNote(note)
            finish()
            Toast.makeText(this, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show()
        }

        binding.editNote.setOnClickListener {
            val intent = Intent(this@ViewNoteActivity, InsertNoteActivity::class.java)
            intent.putExtra("id", ID)
            intent.putExtra("state", "update")
            intent.putStringArrayListExtra(
                "notes",
                arrayListOf(
                    id,
                    binding.titleTextView.text.toString(),
                    binding.noteTextView.text.toString(),
                    binding.dateTextView.text.toString()
                )
            )
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }


    }

    private fun deleteNote(
        id: String,
        title: String,
        description: String,
        toString: String
    ): Notes {
        return Notes(Integer.parseInt(id), title, description, toString)
    }
}