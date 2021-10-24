package com.list.app.time

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView


class Dbhelper(context: Context) : SQLiteOpenHelper(context, "RECIPES",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Recipe(ID INTEGER PRIMARY KEY AUTOINCREMENT, RNAME TEXT UNIQUE)")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Recipe")
        onCreate(db)
    }
    fun insertData(db: SQLiteDatabase?, userEntry: String){
        val userEntry = userEntry.replace("'", "''")//replaces ' with '' due to sql constraints
        db?.execSQL("CREATE TABLE IF NOT EXISTS `" + userEntry + "`(ID INTEGER PRIMARY KEY AUTOINCREMENT, RNAME TEXT UNIQUE)")
        db?.execSQL("INSERT OR IGNORE into Recipe (RNAME) VALUES ('" + userEntry + "')")
    }
    fun deleteRowData(selectedObject: String, db: SQLiteDatabase?, arrayAdapter: ArrayAdapter<String>, recipeList: MutableList<String>,dbName:String){
        db?.execSQL("DELETE FROM "+ dbName +" WHERE RNAME = '" + selectedObject + "'")
        if(dbName == selectedObject){
            deleteTableData(db, selectedObject)
        }
        val selectedObject =  selectedObject.replace("''","'")
        arrayAdapter.remove(selectedObject)
        recipeList.remove(selectedObject)
        arrayAdapter.notifyDataSetChanged()
    }
    fun deleteTableData(db: SQLiteDatabase?, selectedObject: String){
        db?.execSQL("DROP TABLE `" + selectedObject +"`")
    }
    fun insertIngredientdata(userEntry2: String, entry:String, editT:EditText, db: SQLiteDatabase?){
        val userEntry2 = userEntry2.replace("'", "''")//replaces ' with '' due to sql constraints
        db?.execSQL("INSERT OR IGNORE into `" + entry + "` (RNAME) VALUES('" + userEntry2 + "')")
        editT.setText("")
    }
    fun printData(cursor: Cursor, recipeList: MutableList<String>, mListView: ListView, arrayAdapter: ArrayAdapter<*>){
        cursor?.moveToFirst()
        if(cursor?.isAfterLast() == false){//only run loop if the table has contents
            do {
                val data: String = cursor!!.getString(1)
                recipeList.add(data)
                cursor.moveToNext()

            } while (!cursor?.isAfterLast()!!)
            mListView.adapter = arrayAdapter
        }
    }

}