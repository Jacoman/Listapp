package com.list.app.time.ui.dashboard

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.list.app.time.Dbhelper
import com.list.app.time.R
import com.list.app.time.databinding.FragmentIngredientsBinding













class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val helper = activity?.let { Dbhelper(it.applicationContext) }
        val db = helper?.readableDatabase
         _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        val root: View = binding.root
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
                userEntry = userEntry.replace("'","''")//replaces ' with '' due to sql constraints
                db?.execSQL("CREATE TABLE IF NOT EXISTS `" + userEntry + "`(ID INTEGER PRIMARY KEY AUTOINCREMENT, RNAME TEXT)")

                db?.execSQL("INSERT into Recipe (RNAME) VALUES ('"+userEntry+ "')")
                getActivity()?.getIntent()?.putExtra("key", userEntry)

                editT.setText("")
                Toast.makeText(activity, "Recipe Added", Toast.LENGTH_LONG).show()
                view?.findNavController()?.navigate(R.id.action_navigation_Ingredients_to_navigation_AddIngredients)
                // Close dialog
                dialog.dismiss()
            })
        }
        val mListView: ListView = binding.recipeListView

        val recipeList: MutableList<String> = ArrayList()
        val cursor = db?.rawQuery("SELECT * FROM  Recipe", null)
        cursor?.moveToFirst()
        if(cursor?.isAfterLast() == false){//only run loop if the table has contents
            do {
                val data: String = cursor!!.getString(1)
                recipeList.add(data)
                cursor.moveToNext()

            } while (!cursor?.isAfterLast()!!)
            val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, recipeList)
            mListView.adapter = arrayAdapter

            mListView.setOnItemClickListener { parent, view, position, id ->
                val selectedObject = mListView.getItemAtPosition(position).toString()
                getActivity()?.getIntent()?.putExtra("key2", selectedObject)
                view?.findNavController()?.navigate(R.id.action_navigation_Ingredients_to_navigation_DisplayIngredients)
            }
        }

        cursor?.close()
        return root

    }


}




