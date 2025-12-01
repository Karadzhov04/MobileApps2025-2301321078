package com.IvanKaradzhov.expensemate.data.repository

import androidx.lifecycle.LiveData
import com.IvanKaradzhov.expensemate.data.db.ExpenseDao
import com.IvanKaradzhov.expensemate.data.model.Expense

open class ExpenseRepository(private val dao: ExpenseDao) {

    fun getAll(): LiveData<List<Expense>> = dao.getAll()
    suspend fun insert(expense: Expense) = dao.insert(expense)
    suspend fun update(expense: Expense) = dao.update(expense)
    suspend fun delete(expense: Expense) = dao.delete(expense)
}
