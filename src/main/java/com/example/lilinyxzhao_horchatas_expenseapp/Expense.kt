package com.example.lilinyxzhao_horchatas_expenseapp
import androidx.room.*
@Entity(tableName = table_name)
data class Expense(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val date: String,
    val amount: Double,
    val category: String
)