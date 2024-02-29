package com.example.lilinyxzhao_horchatas_expenseapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class EditExpenseActivity : AppCompatActivity() {

    private lateinit var amountEditText: TextView
    private lateinit var dateEditText: TextView
    private lateinit var categorySpinner: Spinner

    private lateinit var expenseViewModel: ExpenseViewModel
    private var expenseId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_expense)

        amountEditText = findViewById(R.id.edit_amount_input)
        dateEditText = findViewById(R.id.edit_date_input)
        categorySpinner = findViewById(R.id.edit_category_spinner)

        val repository = (application as ExpenseApplication).repository
        val viewModelFactory = ExpenseViewModelFactory(repository)
        expenseViewModel = ViewModelProvider(this, viewModelFactory).get(ExpenseViewModel::class.java)
        if (intent.hasExtra(EXTRA_EXPENSE_ID)) {
            expenseId = intent.getIntExtra(EXTRA_EXPENSE_ID, 0)
            loadExpense(expenseId)
        }

        val saveButton: Button = findViewById(R.id.edit_save_button)
        saveButton.setOnClickListener {
            updateExpense()
        }

        val closeButton: Button = findViewById(R.id.edit_close_button)
        closeButton.setOnClickListener {
            finish()
        }
        ArrayAdapter.createFromResource(
            this,
            R.array.category_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected if needed
            }
        }
    }

    private fun loadExpense(id: Int) {
        expenseViewModel.getExpenseById(id).observe(this, { expense ->
            if (expense != null) {
                amountEditText.setText(expense.amount.toString())
                dateEditText.setText(expense.date)
                val categoryPosition = getCategoryPosition(expense.category)
                categorySpinner.setSelection(categoryPosition)
            }
        })
    }

    private fun updateExpense() {
        val amount = amountEditText.text.toString().toDouble()
        val date = dateEditText.text.toString()
        val category = categorySpinner.selectedItem.toString()

        if (amount != null && date.isNotEmpty() && category.isNotEmpty()) {
            val updatedExpense = Expense(expenseId, date, amount, category)
            expenseViewModel.update(updatedExpense)
            finish()
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCategoryPosition(category: String): Int {
        val categories = resources.getStringArray(R.array.category_options)
        return categories.indexOf(category)
    }

    companion object {
        const val EXTRA_EXPENSE_ID = "extra_expense_id"

        fun newIntent(context: Context, expenseId: Int): Intent {
            val intent = Intent(context, EditExpenseActivity::class.java)
            intent.putExtra(EXTRA_EXPENSE_ID, expenseId)
            return intent
        }
    }
}
