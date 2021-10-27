package com.list.app.time

import android.app.Dialog
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.navigation.findNavController


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
        db?.execSQL("CREATE TABLE IF NOT EXISTS `$userEntry`(ID INTEGER PRIMARY KEY AUTOINCREMENT, RNAME TEXT UNIQUE)")
        db?.execSQL("INSERT OR IGNORE into Recipe (RNAME) VALUES ('$userEntry')")
    }
    fun deleteRowData(selectedObject: String, db: SQLiteDatabase?, arrayAdapter: ArrayAdapter<String>, recipeList: MutableList<String>,dbName:String){
        db?.execSQL("DELETE FROM $dbName WHERE RNAME = '$selectedObject'")
        if(dbName == selectedObject){
            deleteTableData(db, selectedObject)
        }
        val selectedObject =  selectedObject.replace("''","'")
        arrayAdapter.remove(selectedObject)
        recipeList.remove(selectedObject)
        arrayAdapter.notifyDataSetChanged()
    }
    private fun deleteTableData(db: SQLiteDatabase?, selectedObject: String){
        db?.execSQL("DROP TABLE `$selectedObject`")
    }
    fun insertIngredientdata(userEntry2: String, entry:String, editT:EditText, db: SQLiteDatabase?){
        val userEntry2 = userEntry2.replace("'", "''")//replaces ' with '' due to sql constraints
        db?.execSQL("INSERT OR IGNORE into `$entry` (RNAME) VALUES('$userEntry2')")
        editT.setText("")
    }

    fun ingredientpopup(db: SQLiteDatabase?, userEntry: String, editT: EditText, dialog: Dialog, view: View) {
        insertData(db, userEntry)
        editT.setText("")
        dialog.setContentView(R.layout.addingredientspopup)
        val editR: EditText = dialog.findViewById(R.id.Redit2)
        val addB: Button = dialog.findViewById(R.id.AddButton)
        val savB: Button = dialog.findViewById(R.id.SaveButton)
        savB.isEnabled = false
        addB.setOnClickListener {
            var userEntry2 = editR.text.toString()
            insertIngredientdata(userEntry2, userEntry, editR, db)
            savB.isEnabled = true
        }
        savB.setOnClickListener {
            dialog.dismiss()
            view?.findNavController()
                ?.navigate(R.id.action_navigation_Ingredients_to_navigation_Ingredients)
        }
    }
    fun printData(cursor: Cursor, recipeList: MutableList<String>, mListView: ListView, arrayAdapter: ArrayAdapter<*>){
        cursor.moveToFirst()
        if(!cursor.isAfterLast){//only run loop if the table has contents
            do {
                val data: String = cursor.getString(1)
                recipeList.add(data)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
            mListView.adapter = arrayAdapter
        }
    }

}