package com.ebe.employeemonitorapp.presentation.employees

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.ebe.employeemonitorapp.R
import com.ebe.employeemonitorapp.databinding.FragmentEmployeesListBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EmployeesListFragment : Fragment() {


    lateinit var binding: FragmentEmployeesListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEmployeesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.inflateMenu(R.menu.search_menu)

        val searchItem: MenuItem = binding.toolbar.menu.findItem(R.id.search)
        val searchView: SearchView = searchItem.actionView as SearchView

    }


}