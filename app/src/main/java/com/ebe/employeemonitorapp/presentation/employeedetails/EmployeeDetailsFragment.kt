package com.ebe.employeemonitorapp.presentation.employeedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ebe.employeemonitorapp.databinding.FragmentEmployeeDetailsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EmployeeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEmployeeDetailsBinding

    val args: EmployeeDetailsFragmentArgs by navArgs()

    private lateinit var adapter: EmployeeDetailsAdapter

    private val viewModel: DetailsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentEmployeeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.phone != null && args.firstDate != null && args.secondDate != null) {
            viewModel.getEmployeeDetails(args.phone!!, args.firstDate!!, args.secondDate!!)
        }

        adapter = EmployeeDetailsAdapter(listOf())
        binding.detailsRecycler.adapter = adapter
        addObservers()


    }


    fun addObservers() {
        viewModel.employeeDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }

        viewModel.loadingState.observe(viewLifecycleOwner)
        {
            if (it) {
                binding.detailsProgress.visibility = View.VISIBLE
            } else {
                binding.detailsProgress.visibility = View.INVISIBLE
            }
        }

        viewModel.errorEvent.observe(viewLifecycleOwner)
        {
            if (it != null && it.isNotEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }


}