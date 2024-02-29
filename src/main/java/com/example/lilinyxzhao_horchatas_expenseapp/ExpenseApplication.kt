package com.example.lilinyxzhao_horchatas_expenseapp

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ExpenseApplication : Application(){

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { ExpenseDatabaseImpl.getDatabase(this,applicationScope) }
    val repository by lazy { ExpenseRepository(database.expenseDao()) }

}