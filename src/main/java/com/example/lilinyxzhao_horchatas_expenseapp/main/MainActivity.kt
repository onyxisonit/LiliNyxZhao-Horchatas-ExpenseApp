package com.example.lilinyxzhao_horchatas_expenseapp.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lilinyxzhao_horchatas_expenseapp.Expense
import com.example.lilinyxzhao_horchatas_expenseapp.ExpenseApplication
import com.example.lilinyxzhao_horchatas_expenseapp.ExpenseViewModel
import com.example.lilinyxzhao_horchatas_expenseapp.ExpenseViewModelFactory
import com.example.lilinyxzhao_horchatas_expenseapp.NewExpenseActivity
import com.example.lilinyxzhao_horchatas_expenseapp.R
import com.example.lilinyxzhao_horchatas_expenseapp.RecyclerViewAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.time.ExperimentalTime

class MainActivity : AppCompatActivity() {

    private val newExpenseActivityRequestCode = 1
    private lateinit var filterSpinner: Spinner
    lateinit var overview: TextView

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        val expenseViewModel: ExpenseViewModel by viewModels {
            ExpenseViewModelFactory((application as ExpenseApplication).repository)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        filterSpinner = findViewById(R.id.filter_spinner)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        var expenses: List<Expense> = listOf()
        expenseViewModel.allExpenses.observe(this, Observer { t ->
            t?.let {
                expenses = it
                adapter.submitList(expenses.asReversed())
            }
        })
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, NewExpenseActivity::class.java)
            startActivityForResult(intent, newExpenseActivityRequestCode)
        }
        ArrayAdapter.createFromResource(
            this,
            R.array.category_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            filterSpinner.adapter = adapter
        }
        filterSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = parent?.getItemAtPosition(position) as String
                expenseViewModel.filterByCategory(selectedCategory).observe(this@MainActivity, { expenses ->
                    expenses?.let {
                        adapter.submitList(expenses)
                    }
                })
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        })


        val cal = Calendar.getInstance().time
        cal.hours = 0
        cal.minutes = 0
        cal.seconds = 0
    }

    private fun combineValues(total: Int?, today: Int?) {
        if (total != null && today != null) {
            val s = "Total Spent: $total\nToday's Spent: $today"
            overview.text = s
        }
    }
    private fun filterByCategory(category: String) {
        val expenseViewModel: ExpenseViewModel by viewModels {
            ExpenseViewModelFactory((application as ExpenseApplication).repository)
        }
        expenseViewModel.filterByCategory(category)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val expenseViewModel: ExpenseViewModel by viewModels {
            ExpenseViewModelFactory((application as ExpenseApplication).repository)
        }
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newExpenseActivityRequestCode && resultCode == RESULT_OK) {
            var a: Double?
            var d: String?
            var c: String?
            a = 0.0
            data?.getDoubleExtra("Amount", 0.0)?.let {
                a = it
            }

            data?.getStringExtra("Date").let {
                d = it
            }
            data?.getStringExtra("Category").let {
                c = it
            }

            if (a != null && d != null && c != null) {
                val expense = Expense(date = d!!, amount = a!!, category = c!!)
                expenseViewModel.insertExpense(expense.date, expense.amount, expense.category)
            } else {
                Toast.makeText(this, "Expense NULL", Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(this, "Expense Failed", Toast.LENGTH_LONG).show()
        }
    }



}
