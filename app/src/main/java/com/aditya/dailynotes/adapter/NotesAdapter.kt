package com.aditya.dailynotes.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aditya.dailynotes.R
import com.aditya.dailynotes.activity.ViewNoteActivity
import com.aditya.dailynotes.database.Notes

class NotesAdapter(var notes : List<Notes>) : RecyclerView.Adapter<NotesAdapter.MyViewHolder>() {
    lateinit var context: Context
    private lateinit var allNotesItems : List<Notes>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        allNotesItems = notes as ArrayList<Notes>
        return MyViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_notes,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = notes[position].title
        holder.date.text = notes[position].date
        holder.description.text = notes[position].description

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ViewNoteActivity::class.java)
            intent.putExtra("id",notes[position].id)
            context.startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filteredList(note : List<Notes>) {
        NotesAdapter(note).notifyDataSetChanged()

    }
    override fun getItemCount(): Int {
        return notes.size
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById<TextView>(R.id.notesTitle)
        var description = itemView.findViewById<TextView>(R.id.notesDescription)
        var date = itemView.findViewById<TextView>(R.id.notesDate)


    }





}