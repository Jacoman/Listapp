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

/***********************************************************************************
The Dbhelper class sets up the data base that users will create tables and insert
data into. This class holds, printing, insertion, and deletion functions that
ineract with persistence data in some form.
 ***********************************************************************************/
class Dbhelper(context: Context) : SQLiteOpenHelper(context, "RECIPES", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Recipe(ID INTEGER PRIMARY KEY AUTOINCREMENT, RNAME TEXT UNIQUE)")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Recipe")
        onCreate(db)
    }

    /***********************************************************************************
     * The insertData function creates a table based on user entry and sets up an ID
     * with auto increment as the primary key for organization. Rname is set as UNIQUE
     * to avoid duplicates. This function also interts the user data into th recipe
     * table to display on the homepage.
     ***********************************************************************************/
    private fun insertData(db: SQLiteDatabase?, userEntry: String) {
        val userEntry = userEntry.replace("'", "''")//replaces ' with '' due to sql constraints
        db?.execSQL("CREATE TABLE IF NOT EXISTS `$userEntry`(ID INTEGER PRIMARY KEY AUTOINCREMENT, RNAME TEXT UNIQUE)")
        db?.execSQL("INSERT OR IGNORE into Recipe (RNAME) VALUES ('$userEntry')")
    }

    /***********************************************************************************
     * The deleteRowData function calls a DELETE from db statement based on user entry.
     * The entry that is deleted is the recipe that appears on the home screen.
     ***********************************************************************************/
    fun deleteRowData(selectedObject: String, db: SQLiteDatabase?, arrayAdapter: ArrayAdapter<String>, recipeList: MutableList<String>, dbName: String) {
        db?.execSQL("DELETE FROM $dbName WHERE RNAME = '$selectedObject'")
        if (dbName == "Recipe") {//if in the homepage delete the table as well.
            deleteTableData(db, selectedObject)
            print(selectedObject)
        }
        val selectedObject = selectedObject.replace("''", "'")//formatting back for array
        arrayAdapter.remove(selectedObject)
        recipeList.remove(selectedObject)
        arrayAdapter.notifyDataSetChanged()
    }

    /***********************************************************************************
     * The deleteTableData function calls a DROP table SQLITE statement based on user
     * entry to delete the entry that shows each ingredient in DisplayIngredientsFragment
     ***********************************************************************************/
    private fun deleteTableData(db: SQLiteDatabase?, selectedObject: String) {
        db?.execSQL("DROP TABLE `$selectedObject`")
    }

    /***********************************************************************************
     * The insertIngredient data function calls an insert or ignore SQLITE statement.
     * The users entries get passed in accordingly to the function and are entered into
     * the database.
     ***********************************************************************************/
    fun insertIngredientdata(userEntry2: String, entry: String, editT: EditText, db: SQLiteDatabase?) {
        val userEntry2 = userEntry2.replace("'", "''")//replaces ' with '' due to sql constraints
        db?.execSQL("INSERT OR IGNORE into `$entry` (RNAME) VALUES('$userEntry2')")//insert or ignore protects against duplicates.
        editT.setText("")//resets user entry text field
    }

    /***********************************************************************************
     * The ingredientpopup function calls in insertData, and insertIngredient data
     * while creating a pop up window with the addingredientpopup XML layout. The save
     * button is disabled until the user add their first ingredient. When the user
     * clicks save, the pop up closes and the page refreshes.
     ***********************************************************************************/
    fun ingredientpopup(db: SQLiteDatabase?, userEntry: String, editT: EditText, dialog: Dialog, view: View) {
        insertData(db, userEntry)
        editT.setText("")
        dialog.setContentView(R.layout.addingredientspopup)
        val editR: EditText = dialog.findViewById(R.id.Redit2)
        val addB: Button = dialog.findViewById(R.id.AddButton)
        val savB: Button = dialog.findViewById(R.id.SaveButton)
        savB.isEnabled = false
        addB.setOnClickListener {
            val userEntry2 = editR.text.toString()
            insertIngredientdata(userEntry2, userEntry, editR, db)
            savB.isEnabled = true
        }
        savB.setOnClickListener {
            dialog.dismiss()
            view.findNavController().navigate(R.id.action_navigation_Ingredients_to_navigation_Ingredients)
        }

    }

    /***********************************************************************************
     * The printData function steps through the entries that are read in through the
     * data base by using a cursor, each pass through the loop converts the database entries
     * into strings where the cursor is at, the string is then added to the arraylist
     * and set to the list view to be outputted.
     ***********************************************************************************/
    fun printData(
        cursor: Cursor,
        recipeList: MutableList<String>,
        mListView: ListView,
        arrayAdapter: ArrayAdapter<*>
    ) {
        cursor.moveToFirst()
        if (!cursor.isAfterLast) {//only run loop if the table has contents
            do {
                val data: String = cursor.getString(1)
                recipeList.add(data)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
            mListView.adapter = arrayAdapter
        }
    }

}