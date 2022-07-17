package com.ebe.employeemonitorapp.presentation.visitsview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ebe.employeemonitorapp.databinding.FragmentVisitsBinding


class VisitsFragment : Fragment() {


    private lateinit var binding: FragmentVisitsBinding

    private val viewModel: VisitsViewModel by viewModels()

    private val args: VisitsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVisitsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getVisitsByDate(args.from!!, args.to!!)
        addObservers()


    }


    fun addObservers() {
        viewModel.employeesDetails.observe(viewLifecycleOwner) {

            if (it.isNullOrEmpty()) {
                binding.noData.visibility = View.VISIBLE
            } else {
                val adapter = VisitsAdapter(it)
                binding.visitsRecycler.adapter = adapter
                binding.noData.visibility = View.INVISIBLE
            }
        }


        viewModel.errorEvent.observe(viewLifecycleOwner)
        {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG)
        }


        viewModel.loadingState.observe(viewLifecycleOwner)
        {
            if (it) {
                binding.visitsProgress.visibility = View.VISIBLE
            } else {
                binding.visitsProgress.visibility = View.VISIBLE
            }
        }
    }


}