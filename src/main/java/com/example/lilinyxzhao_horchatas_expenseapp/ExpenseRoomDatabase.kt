package com.example.lilinyxzhao_horchatas_expenseapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Expense::class], version = 1, exportSchema = false)
abstract class ExpenseRoomDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}
