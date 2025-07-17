package com.aditya.dailynotes.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.lifecycle.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.aditya.dailynotes.R
import com.aditya.dailynotes.adapter.NotesAdapter
import com.aditya.dailynotes.database.Notes
import com.aditya.dailynotes.database.NotesDatabase
import com.aditya.dailynotes.databinding.ActivityMainBinding
import com.aditya.dailynotes.repository.NotesRepository
import com.aditya.dailynotes.viewModel.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var notes: List<Notes>

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Setup toolbar
        setSupportActionBar(binding.toolbar)

        val repository = NotesRepository(NotesDatabase.getDatabase(applicationContext))
        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(repository))[MainViewModel::class.java]

        mainViewModel.getNotes().observe(this, Observer {
            setupRecyclerView(it)
            notes = it
            updateEmptyState(it.isEmpty())
        })

        binding.newNote.setOnClickListener {
            val intent = Intent(this@MainActivity, InsertNoteActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
    
    private fun setupRecyclerView(notesList: List<Notes>) {
        binding.notesRecyclerView.adapter = NotesAdapter(notesList)
        binding.notesRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }
    
    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.emptyStateLayout.visibility = android.view.View.VISIBLE
            binding.notesRecyclerView.visibility = android.view.View.GONE
        } else {
            binding.emptyStateLayout.visibility = android.view.View.GONE
            binding.notesRecyclerView.visibility = android.view.View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_note,menu)
        val menuItem = menu?.findItem(R.id.searchNote)
        val searchView: SearchView = menuItem?.actionView as SearchView
        searchView.queryHint = "Search Notes Here..."
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                notesFilter(p0)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun notesFilter(query: String?) {
        val filteredNotes = arrayListOf<Notes>()
        
        if (query.isNullOrBlank()) {
            // If search is empty, show all notes
            filteredNotes.addAll(notes)
        } else {
            // Filter notes based on title or description containing the query (case insensitive)
            for (note in notes) {
                if (note.title.contains(query, ignoreCase = true) || 
                    note.description.contains(query, ignoreCase = true)) {
                    filteredNotes.add(note)
                }
            }
        }
        
        // Update adapter with filtered results
        setupRecyclerView(filteredNotes)
        updateEmptyState(filteredNotes.isEmpty())
    }
}