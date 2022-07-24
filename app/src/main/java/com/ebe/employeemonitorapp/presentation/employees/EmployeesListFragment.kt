package com.ebe.employeemonitorapp.presentation.employees


import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.util.Pair
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ebe.employeemonitorapp.R
import com.ebe.employeemonitorapp.databinding.DialogLayoutBinding
import com.ebe.employeemonitorapp.databinding.FragmentEmployeesListBinding
import com.ebe.employeemonitorapp.domain.models.Employee
import com.ebe.employeemonitorapp.utils.NetworkObserver
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class EmployeesListFragment : Fragment(), EmployeesAdapter.OnEmployeeClick {


    private val viewModel: EmployeesViewModel by viewModels()


    private lateinit var binding: FragmentEmployeesListBinding
    private var employeesAdapter: EmployeesAdapter? = null
    private var employees: MutableList<Employee> = mutableListOf()
    private lateinit var materialAlertDialog: AlertDialog
    private var selectedDates: Pair<String, String>? = null
    private lateinit var networkObserver: NetworkObserver

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
        val connectivityManager =
            requireActivity().getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        addingSearchView()
        networkObserver = NetworkObserver(connectivityManager)
        addObservers()
        employeesAdapter = EmployeesAdapter()
        binding.employeesRv.adapter = employeesAdapter
        employeesAdapter!!.employeeClick = this
//        if (isNetworkConnected(requireContext())) {
//            viewModel.getAllEmployees()
//        } else {
//            if (employees.isEmpty())
//                Toast.makeText(
//                    requireContext(),
//                    getString(R.string.connect_internet),
//                    Toast.LENGTH_LONG
//                ).show()
//            binding.employeesProgress.visibility = View.INVISIBLE
//        }

        addFabListener()

    }


    private fun addingSearchView() {
        binding.toolbar.inflateMenu(R.menu.search_menu)

        val searchItem: MenuItem = binding.toolbar.menu.findItem(R.id.search)
        val searchView: SearchView = searchItem.actionView as SearchView
        val id = searchView.context
            .resources
            .getIdentifier("android:id/search_src_text", null, null)

        val searchEditText =
            searchView.findViewById<View>(id) as EditText

        searchEditText.backgroundTintMode = PorterDuff.Mode.SRC_IN
        searchEditText.hint = "Search By Name"




        var job: Job? = null
        searchEditText.addTextChangedListener {
            job?.cancel()
            employeesAdapter?.submitList(listOf())
            job = viewLifecycleOwner.lifecycleScope.launch {
                binding.employeesProgress.visibility = View.VISIBLE
                delay(1000)
                filterList(it.toString())
            }

        }

    }

    private fun filterList(newText: String?) {


        val currentList = employees
        val filteredList: MutableList<Employee> = mutableListOf()

        if (newText?.isNotEmpty()!! && newText.isNotBlank()) {
            currentList.forEach {

                if (it.name.contains(newText as CharSequence, true)) {
                    filteredList.add(it)
                }
            }

            employeesAdapter?.submitList(filteredList)
        } else {
            employeesAdapter?.submitList(listOf())
            employeesAdapter?.submitList(employees.toList())
        }
        binding.employeesProgress.visibility = View.INVISIBLE


    }

    fun addFabListener() {
        binding.fab.setOnClickListener {
            showDateRangePickerForEmployees()
        }
    }


    fun addObservers() {
        viewModel.loadingState.observe(viewLifecycleOwner)
        {
            if (it)
                binding.employeesProgress.visibility = View.VISIBLE
            else
                binding.employeesProgress.visibility = View.INVISIBLE


        }


        viewModel.employees.observe(viewLifecycleOwner)
        {
            if (!it.isNullOrEmpty()) {
                employeesAdapter?.submitList(it)
                employees = it.toMutableList()
            }


        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                viewModel.errorEventFlow.collect() {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                }
            }
        }



        viewModel.permState.observe(viewLifecycleOwner) {

            if (it.isNotBlank() && it.isNotEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }


        }


        viewLifecycleOwner.lifecycleScope.launch {
            networkObserver.networkState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest {

                    if (it && employees.isEmpty()) {
                        viewModel.getAllEmployees()
                    } else if (!it && employees.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            requireActivity().getString(R.string.connect_internet),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

        }


    }

    override fun OnClicK(employee: Employee) {
        val dialog = DialogLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        dialog.givePerm.setOnClickListener {
            viewModel.givePermission(employee)
            materialAlertDialog.dismiss()
        }
        dialog.selectDates.setOnClickListener {
            showDateRangePickerForEmployee(employee)
            materialAlertDialog.dismiss()

        }

        materialAlertDialog =
            MaterialAlertDialogBuilder(requireContext()).setView(dialog.root)
                .setCancelable(false).create()
        materialAlertDialog.show()
    }


    private fun showDateRangePickerForEmployee(employee: Employee) {


        val dateRangePicker = buildDatePicker()
        dateRangePicker.addOnPositiveButtonClickListener {

            selectedDates = getSelectedDates(it)

            navigateToEmployeeDetails(employee.phone, selectedDates!!.first, selectedDates!!.second)
            Log.e(
                "selectedDates",
                "showDateRangePicker: ${selectedDates?.first}  ${selectedDates?.second} "
            )
        }

        dateRangePicker.show(requireActivity().supportFragmentManager, "tag")

    }

    private fun showDateRangePickerForEmployees() {
        val dateRangePicker = buildDatePicker()
        dateRangePicker.addOnPositiveButtonClickListener {

            selectedDates = getSelectedDates(it)

            navigateToEmployeesVisits(selectedDates!!.first, selectedDates!!.second)
            Log.e(
                "selectedDates",
                "showDateRangePicker: ${selectedDates?.first}  ${selectedDates?.second} "
            )
        }

        dateRangePicker.show(requireActivity().supportFragmentManager, "tag")
    }


    private fun navigateToEmployeeDetails(phone: String, firstDate: String, secondDate: String) {
        findNavController().navigate(
            EmployeesListFragmentDirections.actionEmployeesListFragmentToEmployeeDetailsFragment(
                phone,
                firstDate,
                secondDate
            )
        )
    }


    private fun navigateToEmployeesVisits(firstDate: String, secondDate: String) {
        findNavController().navigate(
            EmployeesListFragmentDirections.actionEmployeesListFragmentToVisitsFragment(
                firstDate,
                secondDate
            )
        )
    }


    private fun buildDatePicker(): MaterialDatePicker<Pair<Long, Long>> {

        return MaterialDatePicker.Builder.dateRangePicker()
            .setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .setTitleText("Select dates")
            .build()
    }


    private fun getSelectedDates(dates: Pair<Long, Long>): Pair<String, String>? {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val netDate1 = Date(dates.first)
        val netDate2 = Date(dates.second)
        val firstDate = sdf.format(netDate1)
        val secondDate = sdf.format(netDate2)
        return Pair(firstDate, secondDate)
    }


}