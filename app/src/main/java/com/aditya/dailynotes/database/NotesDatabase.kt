package com.aditya.dailynotes.database

import android.content.Context
import androidx.room.*


@Database(entities = [Notes::class], version = 1)
public abstract class NotesDatabase : RoomDatabase(){

    abstract fun notesDao() : NotesDao

    companion object {
        @Volatile
        private var databaseInstance : NotesDatabase? = null
        fun getDatabase(context: Context) : NotesDatabase {
            if(databaseInstance == null) {

                synchronized(this) {
                    databaseInstance = Room.databaseBuilder(context.applicationContext,
                                    NotesDatabase::class.java,
                                    "notes").build()
                }
            }
            return databaseInstance!!
        }
    }

}