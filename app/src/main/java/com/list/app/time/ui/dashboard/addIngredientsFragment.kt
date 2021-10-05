package com.list.app.time.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.list.app.time.Dbhelper
import com.list.app.time.R
import com.list.app.time.databinding.FragmentAddIngredientsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [addIngredientsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class addIngredientsFragment : Fragment() {
    private var _binding: FragmentAddIngredientsBinding? = null
    val helper = activity?.let { Dbhelper(it.applicationContext) }
    var db = helper?.readableDatabase
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("Recycle")
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddIngredientsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val helper = activity?.let { Dbhelper(it.applicationContext) }
        var db = helper?.readableDatabase
        binding.SaveButton.setOnClickListener {
            var userEntry = binding.ingedientEditText.text.toString()
            userEntry = userEntry.replace("'","''")//replaces ' with '' due to sql constraints
            db?.execSQL("INSERT into Ingredient VALUES('"+userEntry+ "')")
            binding.ingedientEditText.setText("")
            binding.ingedientEditText.requestFocus()
            Toast.makeText(activity, "Recipe Added", Toast.LENGTH_LONG).show()
            view?.findNavController()
                ?.navigate(R.id.action_navigation_AddIngredients_to_navigation_Ingredients)


        }

        return root
    }

}