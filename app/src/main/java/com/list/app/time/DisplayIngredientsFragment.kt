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
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisplayIngredientsBinding.inflate(inflater, container, false)
        val helper = activity?.let { Dbhelper(this.requireContext()) }
        val db = helper?.readableDatabase
        val root: View = binding.root
        val mListView: ListView = binding.IngredientListView
        var entry = getActivity()?.getIntent()?.getExtras()?.getString("key2")
        entry = entry?.replace("'","''")
        val recipeList: MutableList<String> = ArrayList()
        val cursor = db?.rawQuery("SELECT * FROM `" + entry+"`", null)
        cursor?.moveToFirst()
        if(cursor?.isAfterLast() == false) {//only run loop if the table has contents
            do {
                val data: String = cursor!!.getString(1)
                recipeList.add(data)
                cursor.moveToNext()

            } while (!cursor?.isAfterLast()!!)
        }
            binding.addRecipeButton2.setOnClickListener() {
                System.out.println("button clicked")
                val dialog = Dialog(requireActivity())
                dialog.setContentView(com.list.app.time.R.layout.addingredientspopup)
                dialog.getWindow()?.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                )
                val add: Button = dialog.findViewById(com.list.app.time.R.id.AddButton)
                val save: Button = dialog.findViewById(com.list.app.time.R.id.SaveButton)
                val editT: EditText = dialog.findViewById(com.list.app.time.R.id.Redit2)

                dialog.show()
                add.setOnClickListener() {
                    var userEntry2 = editT.text.toString()
                    if (recipeList.size >= 1) {
                        if (recipeList.contains(userEntry2)) {
                            Toast.makeText(activity, "Duplicate Entry, Try Again!", Toast.LENGTH_LONG).show()
                            System.out.println("duplicate")
                            System.out.println(recipeList.size - 1)
                            dialog.dismiss()
                        } else {
                            userEntry2 = userEntry2.replace("'", "''")//replaces ' with '' due to sql constraints
                            db?.execSQL("INSERT OR IGNORE into `" + entry + "` (RNAME) VALUES('" + userEntry2 + "')")
                            editT.setText("")
                        }
                    } else {
                        userEntry2 = userEntry2.replace("'", "''")//replaces ' with '' due to sql constraints
                        db?.execSQL("INSERT OR IGNORE into `" + entry + "` (RNAME) VALUES('" + userEntry2 + "')")
                        editT.setText("")
                    }
                    save.setOnClickListener() {
                        dialog.dismiss()
                        view?.findNavController()
                            ?.navigate(com.list.app.time.R.id.action_display_ingredients_to_display_ingredients)
                    }

                }
            }

            val arrayAdapter = ArrayAdapter(requireActivity(), R.layout.simple_list_item_1, recipeList)
            mListView.adapter = arrayAdapter

            mListView.setOnItemLongClickListener { parent, view, position, id ->
                var selectedObject = mListView.getItemAtPosition(position).toString()
                selectedObject = selectedObject.replace("'","''")
                System.out.println(selectedObject)
                val dialog = Dialog(requireActivity())
                dialog.setContentView(com.list.app.time.R.layout.list_layout)
                val submit: Button = dialog.findViewById(com.list.app.time.R.id.RecipeButton1) as Button
                dialog.show()
                dialog.getWindow()?.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                )
                submit.setOnClickListener(View.OnClickListener {
                    db?.execSQL("DELETE FROM `" + entry +"` WHERE RNAME = '" + selectedObject + "'")


                    Toast.makeText(activity, "Recipe Deleted", Toast.LENGTH_LONG).show()
                    selectedObject = selectedObject.replace("''","'")//changing back for array adapter
                    arrayAdapter.remove(selectedObject)
                    recipeList.remove(selectedObject)
                    System.out.println(arrayAdapter)
                    arrayAdapter.notifyDataSetChanged()
                    // Close dialog
                    dialog.dismiss()
                })

                return@setOnItemLongClickListener true
            }




        cursor?.close()

        return root
    }

}