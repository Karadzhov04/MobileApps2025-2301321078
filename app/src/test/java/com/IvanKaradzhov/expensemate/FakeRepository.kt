package com.IvanKaradzhov.expensemate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.IvanKaradzhov.expensemate.data.model.Expense

/**
 * Фалшив репозиторий за тестове – държи данните в паметта.
 * Не наследява ExpenseRepository, за да няма проблем с final методи.
 */
class FakeRepository {
    private val expenses = mutableListOf<Expense>()
    private val liveData = MutableLiveData<List<Expense>>(expenses)

    fun getAll(): LiveData<List<Expense>> = liveData

    suspend fun insert(expense: Expense): Long {
        expenses.add(expense)
        liveData.value = expenses
        return expense.id
    }

    suspend fun update(expense: Expense) {
        val index = expenses.indexOfFirst { it.id == expense.id }
        if (index != -1) {
            expenses[index] = expense
            liveData.value = expenses
        }
    }

    suspend fun delete(expense: Expense) {
        expenses.remove(expense)
        liveData.value = expenses
    }
}
