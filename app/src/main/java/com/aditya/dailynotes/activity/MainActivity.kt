package com.aditya.dailynotes.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    private lateinit var filteredNameList: List<Notes>
    private lateinit var notes: List<Notes>

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = NotesRepository(NotesDatabase.getDatabase(applicationContext))
        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(repository))[MainViewModel::class.java]

        mainViewModel.getNotes().observe(this, Observer {
            binding.notesRecyclerView.adapter = NotesAdapter(it)
            binding.notesRecyclerView.layoutManager= StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            filteredNameList = it
            notes = it
        })

        binding.newNote.setOnClickListener {
            val intent = Intent(this@MainActivity, InsertNoteActivity::class.java)
            startActivity(intent)
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

    private fun notesFilter(p0: String?) {
        val filteredNames  = arrayListOf<Notes>()
        for (note in filteredNameList) {
            if(note.title.contains(p0!!) or note.description.contains(p0)) {
                filteredNames.add(note)
            }

            if(filteredNames.isEmpty()) {
            } else {
                NotesAdapter(notes).filteredList(filteredNames)
            }

        }

        binding.notesRecyclerView.adapter = NotesAdapter(filteredNames)
         binding.notesRecyclerView.layoutManager= StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
    }
}