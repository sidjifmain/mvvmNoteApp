package com.example.mvpnotesapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvpnotesapp.Models.Note
import com.example.mvpnotesapp.R
import kotlin.random.Random


class NoteAdapter(private val context : Context , val listener : NotesClickListener) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){

    private val NotesList = ArrayList<Note>()
    private val fulllist  = ArrayList<Note>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.note_card , parent , false)
        )
    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    fun updateList(newList: List<Note>) {
        fulllist.clear()
        fulllist.addAll(newList)

        NotesList.clear()
        NotesList.addAll(fulllist.toList())

        notifyDataSetChanged()
    }


    fun filterList(search: String?) {
        NotesList.clear()

        for (item in fulllist) {
            val title = item.title?.lowercase()
            val note = item.note?.lowercase()

            if ((title?.contains(search?.lowercase() ?: "") == true) ||
                (note?.contains(search?.lowercase() ?: "") == true)
            ) {
                NotesList.add(item)
            }
        }
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NotesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true

        holder.note.text = currentNote.note

        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.noteColor.setBackgroundColor(holder.itemView.resources.getColor(randomColor() , null))

        holder.notes_layout.setOnClickListener{

            listener.onItemClicked(NotesList[holder.adapterPosition])

        }

        holder.notes_layout.setOnLongClickListener{

            listener.onLongItemClicked(NotesList[holder.adapterPosition] , holder.notes_layout)
            true

        }



    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val notes_layout = itemView.findViewById<CardView>(R.id.cardView)
        val noteColor = itemView.findViewById<LinearLayout>(R.id.noteBack)
        val title = itemView.findViewById<TextView>(R.id.title)
        val note = itemView.findViewById<TextView>(R.id.note)
        val date = itemView.findViewById<TextView>(R.id.date)
    }


    fun randomColor() : Int{
        val list = ArrayList<Int>()
        list.add(R.color.custom_color_1)
        list.add(R.color.custom_color_2)
        list.add(R.color.custom_color_3)
        list.add(R.color.custom_color_4)
        list.add(R.color.custom_color_5)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]

    }

    interface NotesClickListener{
        fun onItemClicked(note : Note)

        fun onLongItemClicked(note : Note , cardView : CardView)
    }
}