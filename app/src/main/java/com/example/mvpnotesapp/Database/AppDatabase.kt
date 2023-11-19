package com.example.mvpnotesapp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mvpnotesapp.DATABASE_NAME
import com.example.mvpnotesapp.Models.Note




@Database(entities = [Note :: class] , version = 1 , exportSchema = false)
abstract class MyDatabase() : RoomDatabase(){

    abstract fun getDao() : NoteDao


    companion object{

        @Volatile
        private var INSTANCE : MyDatabase? = null

        fun getDataBase(context: Context) : MyDatabase{

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context = context,
                    klass = MyDatabase::class.java,
                    name = DATABASE_NAME
                ).build()

                INSTANCE = instance

                instance
            }
        }

    }


}
