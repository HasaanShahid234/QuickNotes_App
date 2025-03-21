package com.example.quicknotesapp.ui

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicknotesapp.R
import com.example.quicknotesapp.adapter.NoteAdapter
import com.example.quicknotesapp.databinding.ActivityMainBinding
import com.example.quicknotesapp.model.Note
import com.example.quicknotesapp.utils.NoteViewModel
import yuku.ambilwarna.AmbilWarnaDialog

class MainActivity : AppCompatActivity(), NoteAdapter.ClickHandler {
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var notesList: List<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        notesList = ArrayList()

        adapter = NoteAdapter(notesList,this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.allNotes.observe(this) { notes ->
            adapter.setNotes(notes)
        }

        binding.addNotesBtn.setOnClickListener {
            showAddNotesDialog()
        }
        binding.voiceBtn.setOnClickListener {

        }
    }
    private fun showAddNotesDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_notes)

        val etTitle = dialog.findViewById<EditText>(R.id.etTitle)
        val etContent = dialog.findViewById<EditText>(R.id.etContent)
        val addBtn = dialog.findViewById<Button>(R.id.btnAddNotes)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            (Resources.getSystem().displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addBtn.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val content = etContent.text.toString().trim()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val note = Note(title = title, content = content, color = Color.YELLOW, isPinned = false)
                viewModel.insert(note)
                Toast.makeText(this, "Expense added successfully!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                etTitle.error = "Required"
                etContent.error = "Required"
            }
        }
        dialog.show()
    }

    override fun onColorClick(note: Note) {
        val colorPicker = AmbilWarnaDialog(this, note.color, object : AmbilWarnaDialog.OnAmbilWarnaListener {
            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                val updatedNote = note.copy(color = color)
                viewModel.update(updatedNote)
            }
            override fun onCancel(dialog: AmbilWarnaDialog?) {
            }
        })
        colorPicker.show()
    }

    override fun onPinClick(note: Note) {
        viewModel.update(note)
    }
}