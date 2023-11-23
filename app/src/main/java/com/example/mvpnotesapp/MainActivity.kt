package com.example.mvpnotesapp

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mvpnotesapp.Adapters.NoteAdapter
import com.example.mvpnotesapp.Database.MyDatabase
import com.example.mvpnotesapp.Models.Note
import com.example.mvpnotesapp.Models.NoteViewModel
import com.example.mvpnotesapp.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() , NoteAdapter.NotesClickListener , PopupMenu.OnMenuItemClickListener{

    private lateinit var binding : ActivityMainBinding
    private lateinit var database : MyDatabase
    lateinit var viewModel : NoteViewModel
    lateinit var adapter : NoteAdapter
    lateinit var selectedNote : Note


    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

        if (result.resultCode == Activity.RESULT_OK){

            val note = result.data?.getSerializableExtra("note") as Note
            if (note != null){
                viewModel.updateNote(note)
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(this) {list ->

            list?.let {
                adapter.updateList(list)
            }

        }

        database = MyDatabase.getDataBase(this)

    }

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(this,this)
        binding.recyclerView.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if (result.resultCode == Activity.RESULT_OK){
                val note = result.data?.getSerializableExtra("note") as? Note
                if ( note != null){

                    viewModel.insertNote(note)

                }
            }
        }


        binding.addNoteBtn.setOnClickListener{

            val intent = Intent(this , AddNoteActivity::class.java)
            getContent.launch(intent)

        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText != null){
                    adapter.filterList(newText)
                }
                return true
            }

        })


    }

    override fun onItemClicked(note: Note) {
        val intent = Intent(this , AddNoteActivity::class.java)
        intent.putExtra("current_note",note)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView){
        val popup = PopupMenu(this , cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.menu1)
        popup.show()

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.delete){

            viewModel.deleteNote(selectedNote)
            return true
        }

        return false
    }
}

