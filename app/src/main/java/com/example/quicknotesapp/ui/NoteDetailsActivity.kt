package com.example.quicknotesapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quicknotesapp.R
import com.example.quicknotesapp.databinding.ActivityNoteDetailsBinding
import com.example.quicknotesapp.model.Note

class NoteDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNoteDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val note = intent.getSerializableExtra("notes") as? Note
        note?.let {
            binding.label.text = it.title
            binding.body.text = it.content
        }
    }
}