package com.fypvpreventor.VpreventorFYP.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Contacts::class], version = 1 , exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun contactsDao() : ContactDao

    companion object{
        @Volatile
        private var INSTANCE : AppDatabase ?= null

        fun getDatabase(context: Context) : AppDatabase{
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }

}