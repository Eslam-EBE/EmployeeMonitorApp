package com.ebe.employeemonitorapp.presentation.visitsview

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
import com.ebe.employeemonitorapp.R
import com.ebe.employeemonitorapp.databinding.FragmentVisitsBinding
import com.ebe.employeemonitorapp.utils.isNetworkConnected
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VisitsFragment : Fragment(), VisitsAdapter.VisitsGetMapLocation {


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

        if (isNetworkConnected(requireContext())) {
            viewModel.getVisitsByDate(args.from!!, args.to!!)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.connect_internet),
                Toast.LENGTH_LONG
            )
                .show()
            binding.visitsProgress.visibility = View.INVISIBLE
        }

        addObservers()


    }


    fun addObservers() {
        viewModel.employeesDetails.observe(viewLifecycleOwner) {

            if (it.isNullOrEmpty()) {
                binding.noData.visibility = View.VISIBLE
            } else {
                val adapter = VisitsAdapter(it)
                adapter.context = requireContext()
                val geocoder = Geocoder(requireContext())
                adapter.geocoder = geocoder
                adapter.getMapLocation = this
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
                binding.visitsProgress.visibility = View.INVISIBLE
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