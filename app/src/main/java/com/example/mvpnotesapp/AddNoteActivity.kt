package com.example.mvpnotesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mvpnotesapp.databinding.ActivityAddNote2Binding

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddNote2Binding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNote2Binding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}