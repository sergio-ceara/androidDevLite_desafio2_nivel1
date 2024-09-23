package com.example.desafio2_nivel1_teste1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class NoteAdapter(context: Context, notes: List<NoteInfo>) : ArrayAdapter<NoteInfo>(context, 0, notes) {

    private val notesList: MutableList<NoteInfo> = notes.toMutableList()

    fun updateNotes(newNotes: List<NoteInfo>) {
        //notesList.clear()
        notesList.addAll(newNotes)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return notesList.size
    }

    override fun getItem(position: Int): NoteInfo? {
        return notesList[position]
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val noteInfo = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_note, parent, false)

        val text1 = view.findViewById<TextView>(R.id.text1)

        // Define o texto da nota
        text1.text = "${noteInfo?.studentName} - turma: ${noteInfo?.className} - nota: ${noteInfo?.note} "

        // Altera a cor de fundo com base na posição (par ou ímpar)
        if (position % 2 == 0) {
            view.setBackgroundColor(context.getColor(android.R.color.white)) // Branco para posições pares
        } else {
            view.setBackgroundColor(context.getColor(android.R.color.darker_gray)) // Cinza para posições ímpares
        }

        return view
    }

}
