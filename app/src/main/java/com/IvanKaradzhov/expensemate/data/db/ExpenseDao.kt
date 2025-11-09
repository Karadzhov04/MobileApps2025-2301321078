package com.IvanKaradzhov.expensemate.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.IvanKaradzhov.expensemate.data.model.Expense

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAll(): LiveData<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense): Long

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)
}
