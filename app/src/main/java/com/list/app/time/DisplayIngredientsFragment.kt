package com.list.app.time

import android.R
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.list.app.time.databinding.FragmentDisplayIngredientsBinding


class displayIngredientsFragment : Fragment() {
    private var _binding: FragmentDisplayIngredientsBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDisplayIngredientsBinding.inflate(inflater, container, false)
        val helper = activity?.let { Dbhelper(this.requireContext()) }
        val db = helper?.readableDatabase
        val root: View = binding.root
        val mListView: ListView = binding.IngredientListView
        val dialog = Dialog(requireActivity())
        dialog.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        )
        var entry = activity?.intent?.extras?.getString("key2")
        entry = entry?.replace("'","''")
        var recipeList: MutableList<String> = ArrayList()
        val cursor = db?.rawQuery("SELECT * FROM `$entry`", null)
        val arrayAdapter = ArrayAdapter(requireActivity(), R.layout.simple_list_item_1, recipeList)
        Dbhelper(this.requireContext()).printData(cursor!!,recipeList,mListView,arrayAdapter)

            binding.addRecipeButton2.setOnClickListener {
                println("button clicked")
                dialog.setContentView(com.list.app.time.R.layout.addingredientspopup)
                val add: Button = dialog.findViewById(com.list.app.time.R.id.AddButton)
                val save: Button = dialog.findViewById(com.list.app.time.R.id.SaveButton)
                val editT: EditText = dialog.findViewById(com.list.app.time.R.id.Redit2)
                save.isEnabled = false
                dialog.show()
                add.setOnClickListener {
                    val userEntry2 = editT.text.toString()
                    if (recipeList.size >= 1) {//if array has elements in it
                        if (recipeList.contains(userEntry2)) {//checks for duplicate entry

                        } else {//if not duplicate
                            save.isEnabled = true
                            Dbhelper(this.requireContext()).insertIngredientdata(userEntry2, entry.toString(), editT,db)
                            Toast.makeText(activity, "Ingredient Added!", Toast.LENGTH_LONG).show()
                        }
                    } else {//if no items in array
                        save.isEnabled = true
                        Dbhelper(this.requireContext()).insertIngredientdata(userEntry2, entry.toString(), editT,db)
                        Toast.makeText(activity, "Ingredient Added!", Toast.LENGTH_LONG).show()
                    }
                    save.setOnClickListener {//close and refresh
                        dialog.dismiss()
                        view?.findNavController()
                            ?.navigate(com.list.app.time.R.id.action_display_ingredients_to_display_ingredients)
                    }

                }
            }

            mListView.setOnItemLongClickListener { parent, view, position, id ->
                var selectedObject = mListView.getItemAtPosition(position).toString()
                selectedObject = selectedObject.replace("'","''")
                println(selectedObject)
                dialog.setContentView(com.list.app.time.R.layout.list_layout)
                val submit: Button = dialog.findViewById(com.list.app.time.R.id.RecipeButton1) as Button
                dialog.show()
                submit.setOnClickListener {
                    helper!!.deleteRowData(selectedObject, db, arrayAdapter, recipeList, entry.toString())
                    Toast.makeText(activity, "Ingredient Deleted", Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }

                return@setOnItemLongClickListener true
            }
        cursor!!.close()
        return root
    }

}