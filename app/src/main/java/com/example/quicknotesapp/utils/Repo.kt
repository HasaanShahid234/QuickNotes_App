package com.example.quicknotesapp.utils

import androidx.lifecycle.LiveData
import com.example.quicknotesapp.database.NoteDao
import com.example.quicknotesapp.model.Note

class Repo(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()
    suspend fun insert(note: Note) = noteDao.insert(note)
    suspend fun delete(note: Note) = noteDao.delete(note)

    suspend fun update(note: Note) {
        noteDao.update(note)
    }
}