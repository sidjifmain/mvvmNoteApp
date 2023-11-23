    package com.example.mvpnotesapp

    import android.app.Activity
    import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.widget.Toast
    import com.example.mvpnotesapp.Models.Note
    import com.example.mvpnotesapp.databinding.ActivityAddNote2Binding
    import java.text.SimpleDateFormat
    import java.util.Date

    @Suppress("DEPRECATION")
    class AddNoteActivity : AppCompatActivity() {

        private lateinit var binding: ActivityAddNote2Binding

        private lateinit var note: Note
        private lateinit var old_note: Note
        private var isUpdate = false

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityAddNote2Binding.inflate(layoutInflater)
            setContentView(binding.root)

            try {
                old_note = intent.getSerializableExtra("current_note") as? Note ?: Note(
                    null,
                    "",
                    "",
                    ""
                )

                binding.titleInput.setText(old_note.title)
                binding.noteInput.setText(old_note.note)
                isUpdate = true

            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding.doneBtn.setOnClickListener {
                val title = binding.titleInput.text.toString()
                val noteDesc = binding.noteInput.text.toString()

                if (title.isNotEmpty() || noteDesc.isNotEmpty()) {
                    val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

                    note = if (isUpdate) {
                        Note(
                            old_note.id, title, noteDesc, formatter.format(Date())
                        )
                    } else {
                        Note(
                            null, title, noteDesc, formatter.format(Date())
                        )
                    }

                    val resultIntent = Intent()
                    resultIntent.putExtra("note", note)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()

                } else {
                    Toast.makeText(this@AddNoteActivity, "Please enter note data", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
