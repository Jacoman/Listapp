package com.list.app.time

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class Dbhelper(context: Context) : SQLiteOpenHelper(context, "RECIPES",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Recipe(ID INTEGER PRIMARY KEY AUTOINCREMENT, RNAME TEXT)")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Recipe")
        onCreate(db)
    }


}