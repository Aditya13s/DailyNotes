package com.aditya.dailynotes.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aditya.dailynotes.database.Notes
import com.aditya.dailynotes.database.NotesDatabase
import com.aditya.dailynotes.databinding.ActivityViewNoteBinding
import com.aditya.dailynotes.repository.NotesRepository
import com.aditya.dailynotes.viewModel.*

class ViewNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewNoteBinding
    private lateinit var mainViewModel: MainViewModel

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val position: Int = intent.getIntExtra("position",0)

        val repository = NotesRepository(NotesDatabase.getDatabase(applicationContext))
        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(repository))[MainViewModel::class.java]

        var id = "";
        var title : String = ""
        var description: String = ""
        mainViewModel.getNotes().observe(this, Observer {
            if(it.isNotEmpty()) {
                binding.noteTextView.text = it[position].description
                binding.titleTextView.text = it[position].title
                binding.dateTextView.text = it[position].date
                id = it[position].id.toString()
                title = it[position].title
                description = it[position].description
            }
        })


        binding.deleteNote.setOnClickListener {
            var note: Notes = deleteNote(id,title,description,binding.dateTextView.text.toString() );
            mainViewModel.deleteNote(note)
            finish()
            Toast.makeText(this,"Note Successfully Deleted", Toast.LENGTH_SHORT).show()
        }
        binding.insertActivityBackButton.setOnClickListener {
            finish()
        }

        binding.editNote.setOnClickListener {
            val intent = Intent(this@ViewNoteActivity, InsertNoteActivity::class.java)
            intent.putExtra("position", position)
            intent.putExtra("state", "update")
            intent.putStringArrayListExtra("notes", arrayListOf(id,binding.titleTextView.text.toString(),binding.noteTextView.text.toString(),binding.dateTextView.text.toString()))
            startActivity(intent)
        }


    }

    private fun deleteNote(id: String, title: String, description: String, toString: String): Notes {
        return Notes(Integer.parseInt(id),title,description, toString)
    }
}