package com.example.quicknotesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quicknotesapp.R
import com.example.quicknotesapp.databinding.ItemNoteBinding
import com.example.quicknotesapp.model.Note

class NoteAdapter(private var notesList: List<Note>, private val clickHandler: ClickHandler) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.binding.noteTitle.text = note.title
        holder.binding.noteContent.text = note.content
        holder.binding.noteCard.setCardBackgroundColor(note.color)

        holder.binding.btnPin.setImageResource(
            if (note.isPinned) R.drawable.baseline_push_pin_24 else R.drawable.ic_pin_outline
        )

        holder.binding.colorPicker.setOnClickListener {
            clickHandler.onColorClick(note)
        }

        holder.binding.btnPin.setOnClickListener {
            clickHandler.onPinClick(note.copy(isPinned = !note.isPinned))
        }
    }

    override fun getItemCount() = notesList.size

    fun setNotes(newNotes: List<Note>) {
        notesList = newNotes
        notifyDataSetChanged()
    }

    interface ClickHandler {
        fun onColorClick(note: Note)
        fun onPinClick(note: Note)
    }
}
