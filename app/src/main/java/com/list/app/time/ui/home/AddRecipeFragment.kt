package com.list.app.time.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.list.app.time.Dbhelper
import com.list.app.time.databinding.FragmentAddrecipeBinding


class AddRecipeFragment : Fragment() {


    private var _binding: FragmentAddrecipeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("Recycle")
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddrecipeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val helper = activity?.let { Dbhelper(it.applicationContext) }
        var db = helper?.readableDatabase
        binding.addRecipeButton.setOnClickListener(){
            var userEntry = binding.editRecipeName.text.toString()
            userEntry = userEntry.replace("'","''")//replaces ' with '' due to sql constraints
            db?.execSQL("CREATE TABLE `" + userEntry + "`( RNAME TEXT PRIMARY KEY )")
            db?.execSQL("INSERT into `"+userEntry+ "` VALUES('"+userEntry+ "')")
            binding.editRecipeName.setText("")
            binding.editRecipeName.requestFocus()
            Toast.makeText(activity, "Recipe Added", Toast.LENGTH_LONG).show()
        }
        return root
    }

}


