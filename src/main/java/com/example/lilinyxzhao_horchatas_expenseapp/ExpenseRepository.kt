package com.example.lilinyxzhao_horchatas_expenseapp

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class ExpenseRepository(private val expenseDAO: ExpenseDao) {

    val allExpenses: LiveData<List<Expense>> = expenseDAO.getAllExpenses()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(expense: Expense) {
        expenseDAO.insert(expense)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(expense: Expense) {
        expenseDAO.delete(expense)
    }
    fun getExpenseById(id: Int): LiveData<Expense> {
        return expenseDAO.getExpenseById(id)
    }
    suspend fun update(expense: Expense) {
        expenseDAO.update(expense)
    }
    fun filterByCategory(category: String): LiveData<List<Expense>>{
        return expenseDAO.filterByCategory(category)
    }

}
