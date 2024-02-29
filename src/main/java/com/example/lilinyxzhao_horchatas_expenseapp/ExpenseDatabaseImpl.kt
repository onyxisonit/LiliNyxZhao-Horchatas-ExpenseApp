package com.example.lilinyxzhao_horchatas_expenseapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = [Expense::class], version = 1, exportSchema = false)
abstract class ExpenseDatabaseImpl : ExpenseRoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabaseImpl? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ExpenseDatabaseImpl {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabaseImpl::class.java,
                    "expense_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(ExpenseDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class ExpenseDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    // Call a suspend function here if you want to populate database on creation
                }
            }
        }
    }
}
