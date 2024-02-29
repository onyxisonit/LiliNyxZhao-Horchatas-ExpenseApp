package com.example.lilinyxzhao_horchatas_expenseapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class NewExpenseActivity : AppCompatActivity() {

    private lateinit var amount: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var categorySpinner: Spinner
    private lateinit var add: Button

    var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_expense)

        initialize()

        dateInput.setOnClickListener {
            showDatePickerDialog()
        }

        add.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(amount.text)) {
                setResult(RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra("Amount", amount.text.toString().toDouble())
                replyIntent.putExtra("Date", dateInput.text.toString())
                replyIntent.putExtra("Category", type)
                setResult(RESULT_OK, replyIntent)
            }
            finish()
        }

        var closeButton: Button = findViewById(R.id.back_button)
        closeButton.setOnClickListener {
            finish()
        }

    }

    private fun initialize() {
        amount = findViewById(R.id.amount_input)
        dateInput = findViewById(R.id.date_input)
        add = findViewById(R.id.add_button)
        categorySpinner = findViewById(R.id.category_spinner)
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
                type = parent?.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, monthOfYear, dayOfMonth ->
                val selectedDate = "$selectedYear-${monthOfYear + 1}-$dayOfMonth"
                dateInput.setText(selectedDate)
            },
            year, month, day
        )
        datePicker.show()
    }
}
