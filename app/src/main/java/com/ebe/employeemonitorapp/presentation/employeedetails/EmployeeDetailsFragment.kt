package com.ebe.employeemonitorapp.presentation.employeedetails

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
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
class EmployeeDetailsFragment : Fragment(), EmployeeDetailsAdapter.GetMapLocation {

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
        val geocoder = Geocoder(requireContext())
        adapter.geocoder = geocoder
        adapter.context = requireContext()
        adapter.getMapLocation = this
        binding.detailsRecycler.adapter = adapter
        addObservers()


    }


    fun addObservers() {
        viewModel.employeeDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.isEmpty()) {
                    binding.noVisits.visibility = View.VISIBLE
                } else {
                    binding.noVisits.visibility = View.INVISIBLE
                }
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

    override fun getMap(lat: Double, long: Double, address: String) {
        val gmmIntentUri = Uri.parse("geo:$lat,$long?q=" + Uri.encode(address))
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(requireActivity().packageManager)?.let {
            startActivity(mapIntent)
        }


    }
}