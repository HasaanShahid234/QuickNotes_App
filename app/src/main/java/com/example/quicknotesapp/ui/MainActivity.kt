package com.example.quicknotesapp.ui

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicknotesapp.R
import com.example.quicknotesapp.adapter.NoteAdapter
import com.example.quicknotesapp.databinding.ActivityMainBinding
import com.example.quicknotesapp.model.Note
import com.example.quicknotesapp.utils.NoteViewModel
import yuku.ambilwarna.AmbilWarnaDialog
import java.util.Locale
import kotlin.jvm.java

class MainActivity : AppCompatActivity(), NoteAdapter.ClickHandler {
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var notesList: List<Note>
    private var noteTitle: String? = null
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        notesList = ArrayList()

        adapter = NoteAdapter(notesList, this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.allNotes.observe(this) { notes ->
            adapter.setNotes(notes)
        }

        binding.addNotesBtn.setOnClickListener {
            showAddNotesDialog()
        }

        binding.voiceBtn.setOnClickListener {
            noteTitle = null
            showAddNotesDialog()
            startVoiceInput("title")
        }
    }

    private fun showAddNotesDialog() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_notes)

        etTitle = dialog.findViewById(R.id.etTitle)
        etContent = dialog.findViewById(R.id.etContent)
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
                val note = Note(title = title, content = content, color = Color.CYAN, isPinned = false)
                viewModel.insert(note)
                Toast.makeText(this, "Note added successfully!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                etTitle.error = "Required"
                etContent.error = "Required"
            }
        }

        dialog.show()
    }

    private fun startVoiceInput(type: String) {
        val prompt = if (type == "title") "Speak the title of the note" else "Now speak the note content"
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, prompt)
        }
        if (type == "title") {
            titleVoiceInput.launch(intent)
        } else {
            contentVoiceInput.launch(intent)
        }
    }

    private val titleVoiceInput = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val matches = result.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                noteTitle = matches[0]
                etTitle.setText(noteTitle)
                startVoiceInput("content")
            }
        } else {
            Toast.makeText(this, "Failed to recognize speech", Toast.LENGTH_SHORT).show()
        }
    }

    private val contentVoiceInput = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val matches = result.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                etContent.setText(matches[0])
            }
        } else {
            Toast.makeText(this, "Failed to recognize speech", Toast.LENGTH_SHORT).show()
        }
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

    override fun onItemClicked(note: Note) {
        val intent = Intent(this, NoteDetailsActivity::class.java)
        intent.putExtra("notes", note)
        startActivity(intent)
    }
}
