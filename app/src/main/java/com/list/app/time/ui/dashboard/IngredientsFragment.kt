package com.list.app.time.ui.dashboard
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val helper = activity?.let { Dbhelper(it.applicationContext) }
        var db = helper?.readableDatabase
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.addRecipeButton.setOnClickListener() {
            view?.findNavController()
                ?.navigate(R.id.action_navigation_Ingredients_to_navigation_AddIngredients)
        }
        var mListView: ListView = binding.recipeListView

        val recipeList: MutableList<String> = ArrayList()
        var cursor = db?.rawQuery("SELECT * FROM  Ingredient", null)
        cursor?.moveToFirst()
        if(cursor?.isAfterLast() == false){//only run loop if the table has contents
            do {
                val data: String = cursor!!.getString(0 )
                recipeList.add(data)
                cursor.moveToNext()

            } while (!cursor!!.isAfterLast())

            val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, recipeList)
            mListView.adapter = arrayAdapter
        }

        cursor?.close()
        return root
    }

}




