package com.ebe.employeemonitorapp.presentation.employees


import android.graphics.PorterDuff
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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ebe.employeemonitorapp.R
import com.ebe.employeemonitorapp.databinding.DialogLayoutBinding
import com.ebe.employeemonitorapp.databinding.FragmentEmployeesListBinding
import com.ebe.employeemonitorapp.domain.models.Employee
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
        addingSearchView()
        addObservers()
        employeesAdapter = EmployeesAdapter()
        binding.employeesRv.adapter = employeesAdapter
        employeesAdapter!!.employeeClick = this
        viewModel.getAllEmployees()

    }


    fun addingSearchView() {
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


//        searchEditText.backgroundTintList = ColorStateList.valueOf(R.color.white)
//        searchEditText.backgroundTintBlendMode = BlendMode.SRC_OUT


//        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//               filterList(newText)
//
//                return false
//            }
//
//        })

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


    }

    override fun OnClicK(employee: Employee) {
        val dialog = DialogLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        dialog.givePerm.setOnClickListener {
            viewModel.givePermission(employee)
            materialAlertDialog.dismiss()
        }
        dialog.selectDates.setOnClickListener {
            showDateRangePicker(employee)
            materialAlertDialog.dismiss()

        }

        materialAlertDialog =
            MaterialAlertDialogBuilder(requireContext()).setView(dialog.root)
                .setCancelable(false).create()
        materialAlertDialog.show()
    }


    fun showDateRangePicker(employee: Employee) {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .setTitleText("Select dates")
                .build()

        dateRangePicker.addOnPositiveButtonClickListener {


            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val netDate1 = Date(it.first)
            val netDate2 = Date(it.second)
            val firstDate = sdf.format(netDate1)
            val secondDate = sdf.format(netDate2)
            selectedDates = Pair(firstDate, secondDate)



            findNavController().navigate(
                EmployeesListFragmentDirections.actionEmployeesListFragmentToEmployeeDetailsFragment(
                    employee.phone,
                    firstDate,
                    secondDate
                )
            )
            Log.e("selectedDates", "showDateRangePicker: $firstDate  $secondDate ")
        }

        dateRangePicker.show(requireActivity().supportFragmentManager, "tag")

    }


}