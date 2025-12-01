package com.IvanKaradzhov.expensemate.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.IvanKaradzhov.expensemate.data.db.AppDatabase
import com.IvanKaradzhov.expensemate.data.model.Expense
import com.IvanKaradzhov.expensemate.data.repository.ExpenseRepository
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    // repo е var, за да може да се презапише в тестовия конструктор
    private var repo: ExpenseRepository

    // Конструктор за тестове – позволява подаване на FakeRepository
    constructor(application: Application, testRepo: ExpenseRepository) : this(application) {
        repo = testRepo
    }

    init {
        val dao = AppDatabase.getDatabase(application).expenseDao()
        repo = ExpenseRepository(dao)
    }

    val expenses: LiveData<List<Expense>> = repo.getAll()

    fun addExpense(expense: Expense) = viewModelScope.launch {
        repo.insert(expense)
    }

    fun updateExpense(expense: Expense) = viewModelScope.launch {
        repo.update(expense)
    }

    fun deleteExpense(expense: Expense) = viewModelScope.launch {
        repo.delete(expense)
    }
}
