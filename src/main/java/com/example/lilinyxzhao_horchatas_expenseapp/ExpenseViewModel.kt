package com.example.lilinyxzhao_horchatas_expenseapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    fun insertExpense(date: String, amount: Double, category: String) = viewModelScope.launch {
        val expense = Expense(0, date, amount, category)
        repository.insert(expense)
    }
    fun delete(expense: Expense) {
        viewModelScope.launch {
            repository.delete(expense)
        }
    }
    fun getExpenseById(id: Int): LiveData<Expense> {
        return repository.getExpenseById(id)
    }

    fun update(expense: Expense) {
        viewModelScope.launch {
            repository.update(expense)
        }
    }

    fun filterByCategory(category: String): LiveData<List<Expense>> {
        return repository.filterByCategory(category)
    }

    val allExpenses: LiveData<List<Expense>> = repository.allExpenses
}

class ExpenseViewModelFactory(private val repository: ExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
