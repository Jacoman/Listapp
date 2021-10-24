package com.list.app.time

import android.annotation.SuppressLint
import android.app.Dialog
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.list.app.time.databinding.FragmentIngredientsBinding


class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("WrongConstant")
    val helper = activity?.let { Dbhelper(this.requireContext()) }
    val db = helper?.readableDatabase
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val helper = activity?.let { Dbhelper(this.requireContext()) }
        val db = helper?.readableDatabase
         _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val mListView: ListView = binding.recipeListView
        val recipeList: MutableList<String> = ArrayList()
        val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, recipeList)
        binding.addRecipeButton.setOnClickListener() {
            val dialog = Dialog(requireActivity())
            dialog.setContentView(R.layout.pop_up)
            val submit: Button = dialog.findViewById(R.id.RecipeButton1) as Button
            val editT: EditText = dialog.findViewById(R.id.Redit1)
            dialog.show()
            dialog.getWindow()?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
            )
            submit.setOnClickListener(View.OnClickListener {
                var userEntry = editT.text.toString()
                if(recipeList.size >= 1){
                        if(recipeList.contains(userEntry)){
                            Toast.makeText(activity, "Duplicate Entry, Try Again!", Toast.LENGTH_LONG).show()
                            System.out.println("duplicate")
                            System.out.println(recipeList.size - 1)
                            dialog.dismiss()
                        }
                        else
                        {
                            helper!!.insertData(db,userEntry)
                            //getActivity()?.getIntent()?.putExtra("key", userEntry)

                            editT.setText("")
                            Toast.makeText(activity, "Recipe Added", Toast.LENGTH_LONG).show()
                            dialog.setContentView(R.layout.addingredientspopup)
                            val editR: EditText = dialog.findViewById(R.id.Redit2)
                            val addB: Button = dialog.findViewById(R.id.AddButton)
                            val savB: Button = dialog.findViewById(R.id.SaveButton)
                            addB.setOnClickListener(){
                                var userEntry2 = editR.text.toString()
                                userEntry = userEntry.replace("'","''")
                                userEntry2 = userEntry2.replace("'","''")//replaces ' with '' due to sql constraints
                                db?.execSQL("INSERT OR IGNORE INTO `" +userEntry+"` (RNAME) VALUES('"+userEntry2+ "')")
                                editR.setText("")
                            }
                            savB.setOnClickListener(){
                                dialog.dismiss()
                                view?.findNavController()?.navigate(R.id.action_navigation_Ingredients_to_navigation_Ingredients)
                            }

                        }
                    //view?.findNavController()
                  //      ?.navigate(R.id.action_navigation_Ingredients_to_navigation_AddIngredients)
                    // Close dialog
                    //dialog.dismiss()
                    }

                else {
                    helper!!.insertData(db, userEntry)
                    //getActivity()?.getIntent()?.putExtra("key", userEntry)

                    editT.setText("")
                    Toast.makeText(activity, "Recipe Added", Toast.LENGTH_LONG).show()
                    dialog.setContentView(R.layout.addingredientspopup)
                    val editR: EditText = dialog.findViewById(R.id.Redit2)
                    val addB: Button = dialog.findViewById(R.id.AddButton)
                    val savB: Button = dialog.findViewById(R.id.SaveButton)
                    addB.setOnClickListener() {
                        var userEntry2 = editR.text.toString()
                        userEntry2 = userEntry2.replace("'", "''")//replaces ' with '' due to sql constraints
                        db?.execSQL("INSERT OR IGNORE into `" + userEntry + "` (RNAME) VALUES('" + userEntry2 + "')")
                        editR.setText("")
                    }
                    savB.setOnClickListener() {
                        dialog.dismiss()
                        view?.findNavController()
                            ?.navigate(R.id.action_navigation_Ingredients_to_navigation_Ingredients)
                    }
                }

                })
        }
        val cursor = db?.rawQuery("SELECT * FROM  Recipe", null)
        printData(cursor!!,recipeList,mListView,arrayAdapter)
        mListView.setOnItemLongClickListener { parent, view, position, id ->
            var selectedObject = mListView.getItemAtPosition(position).toString()
            selectedObject = selectedObject.replace("'","''")

            val dialog = Dialog(requireActivity())
            dialog.setContentView(R.layout.list_layout)
            val submit: Button = dialog.findViewById(R.id.RecipeButton1) as Button
            dialog.show()
            dialog.getWindow()?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
            )
            submit.setOnClickListener(View.OnClickListener {
                Toast.makeText(activity, "Recipe Deleted", Toast.LENGTH_LONG).show()

                helper?.deleteData(selectedObject, db, arrayAdapter,recipeList)
                dialog.dismiss()
            })

            return@setOnItemLongClickListener true
        }
        cursor?.close()
        return root
    }

    fun printData(cursor: Cursor, recipeList: MutableList<String>, mListView: ListView, arrayAdapter: ArrayAdapter<*>){
        cursor?.moveToFirst()
        if(cursor?.isAfterLast() == false){//only run loop if the table has contents
            do {
                val data: String = cursor!!.getString(1)
                recipeList.add(data)
                cursor.moveToNext()

            } while (!cursor?.isAfterLast()!!)
            //val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, recipeList)
            mListView.adapter = arrayAdapter
            mListView.setOnItemClickListener { parent, view, position, id ->
                val selectedObject = mListView.getItemAtPosition(position).toString()
                getActivity()?.getIntent()?.putExtra("key2", selectedObject)
                view?.findNavController()?.navigate(R.id.action_navigation_Ingredients_to_navigation_DisplayIngredients)
            }
        }
    }



}




