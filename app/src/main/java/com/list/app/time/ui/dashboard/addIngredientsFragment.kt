package com.list.app.time.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.list.app.time.Dbhelper
import com.list.app.time.databinding.FragmentAddIngredientsBinding


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

        val entry = getActivity()?.getIntent()?.getExtras()?.getString("key")
        System.out.println(entry)
        val helper = activity?.let { Dbhelper(it.applicationContext) }
        val db = helper?.readableDatabase
        binding.SaveButton.setOnClickListener {
            var userEntry = binding.ingedientEditText.text.toString()
            userEntry = userEntry.replace("'","''")//replaces ' with '' due to sql constraints
            db?.execSQL("INSERT into " +entry+" (RNAME) VALUES('"+userEntry+ "')")
            binding.ingedientEditText.setText("")
            binding.ingedientEditText.requestFocus()
            Toast.makeText(activity, "Recipe Added", Toast.LENGTH_LONG).show()
        }

        return root
    }

}