package com.list.app.time

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.ArrayAdapter


class Dbhelper(context: Context) : SQLiteOpenHelper(context, "RECIPES",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Recipe(ID INTEGER PRIMARY KEY AUTOINCREMENT, RNAME TEXT)")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Recipe")
        onCreate(db)
    }
    fun insertData(db: SQLiteDatabase?, userEntry: String){
        userEntry.replace("'", "''")//replaces ' with '' due to sql constraints
        db?.execSQL("CREATE TABLE IF NOT EXISTS `" + userEntry + "`(ID INTEGER PRIMARY KEY AUTOINCREMENT, RNAME TEXT)")
        db?.execSQL("INSERT into Recipe (RNAME) VALUES ('" + userEntry + "')")
    }
    fun deleteData(selectedObject: String, db: SQLiteDatabase?, arrayAdapter: ArrayAdapter<String>, recipeList: MutableList<String>){
        db?.execSQL("DELETE FROM Recipe WHERE RNAME = '" + selectedObject + "'")
        db?.execSQL("DROP TABLE `" + selectedObject +"`")
        selectedObject.replace("''","'")
        arrayAdapter.remove(selectedObject)
        recipeList.remove(selectedObject)
        arrayAdapter.notifyDataSetChanged()
    }

}