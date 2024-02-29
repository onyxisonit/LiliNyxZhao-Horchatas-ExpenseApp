package com.example.lilinyxzhao_horchatas_expenseapp

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter : ListAdapter<Expense, RecyclerViewAdapter.ExpenseViewHolder>(
    EXPENSE_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        return ExpenseViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val amount: TextView = itemView.findViewById(R.id.amount_type)
        private val date: TextView = itemView.findViewById(R.id.date)
        private val category: TextView = itemView.findViewById(R.id.category)
        private val delete: Button = itemView.findViewById(R.id.delete)
        private val edit: Button = itemView.findViewById(R.id.edit)
        private val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)

        fun bind(expense: Expense?) {
            val amountExpense = "${expense?.amount}"
            val categoryChosen = "${expense?.category}"
            amount.text = amountExpense
            date.text = expense?.date
            category.text = categoryChosen
            val store: Expense = expense as Expense
            edit.setOnClickListener {
                val context = itemView.context
                val intent = EditExpenseActivity.newIntent(context, store.id)
                context.startActivity(intent)
            }
            delete.setOnClickListener {
                val dialog = AlertDialog.Builder(itemView.context)
                    .setTitle("Delete Expense?")
                    .setMessage("Are you sure you want to delete the expense?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        val expenseViewModel = ExpenseViewModel(ExpenseApplication().repository)
                        expenseViewModel.delete(store)
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.cancel()
                    }
                    .create()
                dialog.show()
            }
        }

        companion object {
            fun create(parent: ViewGroup): ExpenseViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.expense_item, parent, false)
                return ExpenseViewHolder(view)
            }
        }
    }

    companion object {
        private val EXPENSE_COMPARATOR = object : DiffUtil.ItemCallback<Expense>() {
            override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}
