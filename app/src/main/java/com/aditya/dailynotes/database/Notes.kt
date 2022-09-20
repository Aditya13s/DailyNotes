package com.aditya.dailynotes.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Notes(

    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val description:String,
    val date:String
)
