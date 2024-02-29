package com.example.lilinyxzhao_horchatas_expenseapp
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM $table_name")
    fun getAllExpenses(): LiveData<List<Expense>>

    @Insert
    suspend fun insert(expense: Expense)

    @Query("DELETE FROM $table_name")
    suspend fun deleteAll()

    @Update
    suspend fun update(expense: Expense)

    @Query("SELECT * FROM $table_name WHERE id = :id")
    fun getExpenseById(id: Int): LiveData<Expense>

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT * FROM $table_name WHERE category = :category")
    fun filterByCategory(category: String): LiveData<List<Expense>>
}