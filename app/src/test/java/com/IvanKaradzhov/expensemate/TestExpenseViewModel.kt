package com.IvanKaradzhov.expensemate

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.IvanKaradzhov.expensemate.data.model.Expense
import kotlinx.coroutines.launch

class TestExpenseViewModel(private val fakeRepo: FakeRepository) : ViewModel() {
    val expenses: LiveData<List<Expense>> = fakeRepo.getAll()

    fun addExpense(expense: Expense) = viewModelScope.launch {
        fakeRepo.insert(expense)
    }

    fun updateExpense(expense: Expense) = viewModelScope.launch {
        fakeRepo.update(expense)
    }

    fun deleteExpense(expense: Expense) = viewModelScope.launch {
        fakeRepo.delete(expense)
    }
}
