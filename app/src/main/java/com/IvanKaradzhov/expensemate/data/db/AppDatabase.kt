package com.IvanKaradzhov.expensemate.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.IvanKaradzhov.expensemate.data.model.Expense

@Database(entities = [Expense::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expense_db"
                ).build()
                INSTANCE = inst
                inst
            }
        }
    }
}
