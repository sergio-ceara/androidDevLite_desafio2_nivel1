package com.example.desafio2_nivel1_teste1

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio2_nivel1_teste1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val students = mutableListOf<Student>()
    private final lateinit var binding: ActivityMainBinding
    private lateinit var studentNameStr:EditText
    private lateinit var studentClassStr:EditText
    private lateinit var studentNoteStr:EditText
    private lateinit var studentAddNote:Button
    private lateinit var studentConsult:Button
    private lateinit var studentMessage:TextView
    private lateinit var listViewNotes: ListView
    private lateinit var noteAdapter: NoteAdapter
    private var currentStudent: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentNameStr   = binding.editName
        studentClassStr  = binding.editClass
        studentNoteStr   = binding.editNote
        studentAddNote   = binding.buttonAddNote
        studentConsult   = binding.buttonConsult
        studentMessage   = binding.txtMessage
        listViewNotes    = binding.listViewNotes

        noteAdapter = NoteAdapter(this, emptyList())
        listViewNotes.adapter = noteAdapter

        studentAddNote.setOnClickListener { addNote() }
        studentConsult.setOnClickListener { consultStudent() }

        listViewNotes.setOnItemClickListener { _, _, position, _ ->
            // Obtém a nota clicada
            val selectedNoteInfo = noteAdapter.getItem(position)

            selectedNoteInfo?.let { noteInfo ->
                // Atualiza os campos de texto com os dados da nota clicada
                studentNameStr.setText(noteInfo.studentName)
                studentClassStr.setText(noteInfo.className)
                studentNoteStr.setText(noteInfo.note.toString())

                // Chama a função consultStudent usando os dados da linha clicada
                studentNameStr.setText(noteInfo.studentName)
                studentClassStr.setText(noteInfo.className)
                consultStudent() // Chama a função de consulta com os dados preenchidos
                Log.d("listViewNotes", "Consultando aluno: ${noteInfo.studentName}, Turma: ${noteInfo.className}, Nota: ${noteInfo.note}")
            }
        }

    }

    private fun addNote() {
        var nameStr = studentNameStr.text.toString()
        var classStr = studentClassStr.text.toString()
        var noteStr = studentNoteStr.text.toString()

        if (nameStr.isEmpty() || classStr.isEmpty() || noteStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        val studentNoteDouble = noteStr.toDoubleOrNull()
        if (studentNoteDouble == null || studentNoteDouble < 0 || studentNoteDouble > 10) {
            Toast.makeText(this, "Nota inválida!", Toast.LENGTH_SHORT).show()
            return
        }

        val student = students.find {
            it.name.lowercase() == nameStr.lowercase() &&
                    it.className.lowercase() == classStr.lowercase()
        } ?: Student(nameStr, classStr).also { students.add(it) }

        student.notes.add(studentNoteDouble)

        currentStudent = student
        updateNotesList()

        Toast.makeText(this, "Nota adicionada com sucesso!", Toast.LENGTH_SHORT).show()
        studentNoteStr.text.clear()
    }

    private fun consultStudent() {
        val nameStr = studentNameStr.text.toString().lowercase()
        val classStr = studentClassStr.text.toString().lowercase()

        if (nameStr.isEmpty() || classStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        val aluno = students.find { it.name.lowercase() == nameStr && it.className.lowercase() == classStr }
        if (aluno == null) {
            studentMessage.text = "Aluno não encontrado!"
        } else {
            val average = aluno.calculateAverage()
            val status = if (aluno.isApproved()) "Aprovado" else "Reprovado"
            val amount = aluno.notes.size
            studentMessage.text = "Média: %.2f - Notas lançadas: ${amount} Situação: $status".format(average)
        }
    }
    private fun updateNotesList() {
        currentStudent?.let { student ->
            // Cria a lista de NoteInfo apenas com a última nota adicionada
            val lastNoteIndex = student.notes.size - 1
            if (lastNoteIndex >= 0) {
                val lastNote = student.notes[lastNoteIndex]
                val noteInfo = NoteInfo(student.name, student.className, lastNote, lastNoteIndex)
                noteAdapter.updateNotes(listOf(noteInfo)) // Atualiza com apenas a última nota
            }
        } ?: run {
            Log.d("UpdateNotesList", "Nenhum aluno selecionado.")
            noteAdapter.updateNotes(emptyList())
        }
    }

}
