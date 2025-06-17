package com.thesisapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Swimmer::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun swimmerDao(): SwimmerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "swimmer_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}