package com.thesisapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "SwimmersDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE swimmers(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE, age INTEGER, category TEXT)")
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS swimmers")
        onCreate(db)
    }
    fun addSwimmer(name: String, age: Int, category: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("name", name)
        values.put("age", age)
        values.put("category", category)

        return db.insert("swimmers", null, values) != -1L
    }
    fun getAllSwimmers(): List<String> {
        val swimmers = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name FROM swimmers", null)

        if (cursor.moveToFirst()) {
            do {
                swimmers.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return swimmers
    }
    fun swimmerExists(name: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM swimmers WHERE name = ?", arrayOf(name))
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }
}